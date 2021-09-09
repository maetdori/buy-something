package com.maetdori.buysomething.service.AutoSelectService;

import com.maetdori.buysomething.exception.Business.InvalidValue.ZeroCartAmountException;
import com.maetdori.buysomething.util.Percent;
import com.maetdori.buysomething.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AutoSelectServiceImpl implements AutoSelectService {

	@Override
	@Transactional(readOnly = true)
	public SelectionDto getSelection(UserPayInfoDto payInfo, int cartAmount) {
		if(cartAmount == 0) throw new ZeroCartAmountException();

		SelectionDto selection = new SelectionDto();
		selection.setUserId(payInfo.getUserId());
		selection.setCartAmount(cartAmount);
		selection.setPayAmount(cartAmount);

		//자동할인 적용순서: 쿠폰 -> 포인트 -> 적립금
		selectCoupon(payInfo, selection);
		selectPoints(payInfo, selection);
		selectSavings(payInfo, selection);

		return selection;
	}

	private void selectCoupon(UserPayInfoDto userInfo, SelectionDto selection) {
		List<CouponDto> coupons = userInfo.getCoupons(); //유저가 가진 쿠폰 리스트

		//쿠폰이 없을 경우
		if(coupons.isEmpty()) return;

		//할인율 기준 내림차순 정렬
		Collections.sort(coupons, (c1,c2)-> c2.getDiscountRate()-c1.getDiscountRate());

		//최소주문금액을 만족하는 가장 큰 할인율 쿠폰을 찾는다.
		for(CouponDto coupon: coupons) {
			if(coupon.getMinAmount() > selection.getCartAmount()) continue;

			int discountAmount = Percent.discountAmount(selection.getCartAmount(), coupon.getDiscountRate());

			selection.setPayAmount(selection.getPayAmount() - discountAmount);
			selection.setCouponToUse(coupon);
			return;
		}
	}

	private void selectPoints(UserPayInfoDto userInfo, SelectionDto selection) {
		List<PointDto> points = userInfo.getPoints(); //유저가 가진 포인트 리스트
		List<PointDto> selected = new ArrayList<>(); //선택한 포인트를 저장할 리스트

		//포인트가 없을 경우
		if(userInfo.getPoints().isEmpty()) {
			selection.setPointsToUse(selected);
			return;
		}

		//유효기간 짧은 순서대로 정렬
		Collections.sort(points, Comparator.comparing(PointDto::getExpiryDate));

		//가용 포인트 모두 사용
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

	private void selectSavings(UserPayInfoDto userInfo, SelectionDto selection) {
		SavingsDto savings = userInfo.getSavings(); //유저가 가진 적립금

		int savingsAmount = savings.getAmount(); //유저의 적립금 금액
		int payAmount = selection.getPayAmount(); //결제예정금액

		//적립금이 없거나 최종 결제금액이 0이 된 경우
		if(savingsAmount==0 || payAmount==0) return;

		int savingsToUse; //사용할 적립금

		//결제예정금액이 보유 적립금보다 큰 경우
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
