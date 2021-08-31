package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Selection {
	private Integer userId;
	private int cartAmount;
	private int payAmount;
	private SavingsDto savingsToUse;
	private CouponDto couponToUse;
	private List<PointDto> pointsToUse;

	@Builder
	public Selection(Integer userId, int payAmount, SavingsDto savings, CouponDto coupon, List<PointDto> points) {
		this.userId = userId;
		this.payAmount = payAmount;
		this.savingsToUse = savings;
		this.couponToUse = coupon;
		this.pointsToUse = points;
	}

	public boolean containsSavings() {
		return savingsToUse != null;
	}

	public boolean containsCoupon() {
		return couponToUse != null;
	}

	public boolean containsPoints() {
		return !pointsToUse.isEmpty();
	}
}