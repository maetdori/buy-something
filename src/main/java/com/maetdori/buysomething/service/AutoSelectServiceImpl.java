package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.maetdori.buysomething.Util.calcPercentage.calcPercentage;

@RequiredArgsConstructor
@Service
public class AutoSelectServiceImpl implements AutoSelectService {
	private int cartAmount;
	private int payAmount;

	@Override
	@Transactional
	public UserDto.Selection getSelection(UserDto.Info autoSelectRequest) {
		cartAmount = autoSelectRequest.getCartAmount();
		payAmount = cartAmount;

		CouponDto couponToUse = selectCoupon(autoSelectRequest.getCoupons());
		List<PointDto.Selected> pointsToUse = selectPoints(autoSelectRequest.getPoints());
		int savingsToUse = selectSavings(autoSelectRequest.getSavings());

		return new UserDto.Selection(autoSelectRequest.getUserId(), cartAmount, payAmount, savingsToUse, couponToUse, pointsToUse);
	}

	private CouponDto selectCoupon(List<CouponDto> coupons) {
		if(payAmount==0 || coupons.isEmpty()) return null;

		Collections.sort(coupons, (c1,c2)-> c2.getDiscountRate()-c1.getDiscountRate());

		CouponDto couponToUse = null;
		for(CouponDto coupon: coupons) {
			if(coupon.getMinAmount() > cartAmount) continue;
			payAmount = calcPercentage(payAmount, coupon.getDiscountRate());
			couponToUse = coupon;
			break;
		}
		return couponToUse;
	}

	private List<PointDto.Selected> selectPoints(List<PointDto> points) {
		if(payAmount==0 || points.isEmpty()) return null;

		Collections.sort(points, Comparator.comparing(PointDto::getExpiryDate));
		List<PointDto.Selected> selected = new ArrayList<>();
		for(PointDto pointDto: points) {
			int point = pointDto.getAmount();
			if(payAmount > point) {
				payAmount -= point;
				selected.add(new PointDto.Selected(pointDto.getId(), point));
				continue;
			}
			selected.add(new PointDto.Selected(pointDto.getId(),payAmount));
			payAmount = 0;
			break;
		}
		return selected;
	}

	private int selectSavings(int savings) {
		payAmount = payAmount < savings ? 0 : payAmount-savings;
		return Math.min(payAmount, savings);
	}
}
