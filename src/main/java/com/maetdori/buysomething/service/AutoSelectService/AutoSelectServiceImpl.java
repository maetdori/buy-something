package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.maetdori.buysomething.Util.Percent.discountPercent;

@RequiredArgsConstructor
@Service
public class AutoSelectServiceImpl implements AutoSelectService {
	private int cartAmount; //사용자가 장바구니에 담은 주문금액
	private int payAmount; //할인이 적용된 최종 결제금액을 저장할 변수

	@Override
	@Transactional
	public Selection getSelection(UserInfo userInfo) {
		cartAmount = userInfo.getCartAmount();
		payAmount = cartAmount; //주문금액으로 초기화

		//자동할인 적용순서: 쿠폰 -> 포인트 -> 적립금

		//1. 쿠폰 1개 이상 있을 경우 쿠폰 자동선택
		CouponDto couponToUse =
				userInfo.hasCoupons() ? selectCoupon(userInfo.getCoupons()) : null;

		//2. 포인트 1개 이상 있을 경우 포인트 자동선택
		List<PointDto> pointsToUse =
				userInfo.hasPoints() ? selectPoints(userInfo.getPoints()) : new ArrayList<>();

		//3. 적립금 있을 경우 적립금 자동선택
		SavingsDto savingsToUse =
				userInfo.hasSavings() ? selectSavings(userInfo.getSavings()) : null;

		return Selection.builder()
				.userId(userInfo.getUserId())
				.payAmount(payAmount)
				.savings(savingsToUse)
				.coupon(couponToUse)
				.points(pointsToUse)
				.build();
	}

	private CouponDto selectCoupon(List<CouponDto> coupons) {
		//최종 결제금액이 0이 된 경우 리턴
		if(payAmount==0) return null;

		//할인율 기준 내림차순 정렬
		Collections.sort(coupons, (c1,c2)-> c2.getDiscountRate()-c1.getDiscountRate());

		CouponDto couponToUse = null;
		for(CouponDto coupon: coupons) { //최소주문금액을 만족하는 가장 큰 할인율 쿠폰을 찾는다.
			if(coupon.getMinAmount() > cartAmount) continue;
			payAmount = discountPercent(payAmount, coupon.getDiscountRate());
			couponToUse = coupon;
			break;
		}
		return couponToUse;
	}

	private List<PointDto> selectPoints(List<PointDto> points) {
		List<PointDto> selected = new ArrayList<>();

		//최종 결제금액이 0이 된 경우 리턴
		if(payAmount==0) return selected;

		Collections.sort(points, Comparator.comparing(PointDto::getExpiryDate));

		for(PointDto pointDto: points) {
			int point = pointDto.getAmount();
			if(payAmount > point) {
				payAmount -= point;
				selected.add(PointDto.builder()
						.id(pointDto.getId())
						.amount(point)
						.build());
				continue;
			}
			selected.add(PointDto.builder()
					.id(pointDto.getId())
					.amount(payAmount)
					.build());
			payAmount = 0;
			break;
		}
		return selected;
	}

	private SavingsDto selectSavings(SavingsDto savings) {
		//최종 결제금액이 0이 된 경우 리턴
		if(payAmount==0) return null;

		int savingsAmount = savings.getAmount();

		int savingsToUse;

		if(payAmount > savingsAmount) {
			savingsToUse = savingsAmount;
			payAmount -= savingsToUse;
		}
		else {
			savingsToUse = payAmount;
			payAmount = 0;
		}

		return SavingsDto.builder()
				.id(savings.getId())
				.amount(savingsToUse)
				.build();
	}
}
