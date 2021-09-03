package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.util.Percent;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AutoSelectServiceImpl implements AutoSelectService {

	@Override
	@Transactional
	public SelectionDto getSelection(UserInfoDto userInfo) {
		SelectionDto selection = new SelectionDto();
		selection.setUserId(userInfo.getUserId());
		selection.setCartAmount(userInfo.getCartAmount());
		selection.setPayAmount(userInfo.getCartAmount());

		//자동할인 적용순서: 쿠폰 -> 포인트 -> 적립금

		selectCoupon(userInfo, selection);
		selectPoints(userInfo, selection);
		selectSavings(userInfo, selection);

		return selection;
	}

	private void selectCoupon(UserInfoDto userInfo, SelectionDto selection) {
		List<CouponDto> coupons = userInfo.getCoupons();

		//쿠폰이 없거나 최종 결제금액이 0이 된 경우
		if(coupons.isEmpty() || selection.getPayAmount()==0) return;

		//할인율 기준 내림차순 정렬
		Collections.sort(coupons, (c1,c2)-> c2.getDiscountRate()-c1.getDiscountRate());

		for(CouponDto coupon: coupons) { //최소주문금액을 만족하는 가장 큰 할인율 쿠폰을 찾는다.
			if(coupon.getMinAmount() > selection.getCartAmount()) continue;
			selection.setPayAmount(Percent.discountPercent(selection.getPayAmount(), coupon.getDiscountRate()));
			selection.setCouponToUse(coupon);
			return;
		}
	}

	private void selectPoints(UserInfoDto userInfo, SelectionDto selection) {
		List<PointDto> points = userInfo.getPoints();
		List<PointDto> selected = new ArrayList<>();

		//포인트가 없거나 최종 결제금액이 0이 된 경우
		if(userInfo.getPoints().isEmpty() || selection.getPayAmount()==0) {
			selection.setPointsToUse(selected);
			return;
		}

		Collections.sort(points, Comparator.comparing(PointDto::getExpiryDate));

		for(PointDto pointDto: points) {
			int point = pointDto.getAmount();
			int payAmount = selection.getPayAmount();

			if(payAmount > point) {
				selection.setPayAmount(payAmount - point);
				selected.add(pointDto);
				continue;
			}

			selection.setPayAmount(0);
			pointDto.setAmount(payAmount);
			selected.add(pointDto);
			break;
		}

		selection.setPointsToUse(selected);
		return;
	}

	private void selectSavings(UserInfoDto userInfo, SelectionDto selection) {
		SavingsDto savings = userInfo.getSavings();

		//적립금이 없거나 최종 결제금액이 0이 된 경우
		if(savings==null || selection.getPayAmount()==0) return;

		int savingsAmount = savings.getAmount();
		int payAmount = selection.getPayAmount();

		int savingsToUse;

		if(payAmount > savingsAmount) {
			savingsToUse = savingsAmount;
			payAmount -= savingsToUse;
		}
		else {
			savingsToUse = payAmount;
			payAmount = 0;
		}

		savings.setAmount(savingsToUse);
		selection.setPayAmount(payAmount);
		selection.setSavingsToUse(savings);
		return;
	}
}
