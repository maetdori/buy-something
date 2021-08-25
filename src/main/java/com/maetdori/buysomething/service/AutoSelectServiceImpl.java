package com.maetdori.buysomething.service;

import com.maetdori.buysomething.exception.NoSuchUserException;
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

import static com.maetdori.Util.calcPercentage.calcPercentage;

@RequiredArgsConstructor
@Service
public class AutoSelectServiceImpl implements AutoSelectService {
	private final UserInfoService userInfoService;
	private int payAmount;

	@Override
	@Transactional
	public UserDto.AutoSelect getSelection(UserDto.Request userRequest) throws NoSuchUserException {
		UserDto.Info user = userInfoService.getUserInfo(userRequest);
		payAmount = userRequest.getAmount();

		CouponDto couponToUse = selectCoupon(user.getCoupons());
		List<PointDto.Selected> pointsToUse = selectPoints(user.getPoints());
		int savingsToUse = selectSavings(user.getSavings());

		return new UserDto.AutoSelect(payAmount, savingsToUse,couponToUse, pointsToUse);
	}

	private CouponDto selectCoupon(List<CouponDto> coupons) {
		if(coupons.isEmpty()) return null;

		Collections.sort(coupons, (c1,c2)-> c2.getDiscountRate()-c1.getDiscountRate());
		payAmount = calcPercentage(payAmount, coupons.get(0).getDiscountRate());
		return coupons.get(0);
	}

	private List<PointDto.Selected> selectPoints(List<PointDto> points) {
		if(points.isEmpty()) return null;

		Collections.sort(points, Comparator.comparing(PointDto::getExpiryDate));
		List<PointDto.Selected> selected = new ArrayList<>();
		for(PointDto pointDto: points) {
			int point = pointDto.getAmount();
			if(payAmount > point) {
				payAmount -= point;
				selected.add(new PointDto.Selected(pointDto.getId(), point));
				continue;
			}
			selected.add(new PointDto.Selected(pointDto.getId(), payAmount));
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
