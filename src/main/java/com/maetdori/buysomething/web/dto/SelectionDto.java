package com.maetdori.buysomething.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SelectionDto {
	private Integer userId;
	private int cartAmount;
	private int payAmount;
	private SavingsDto savingsToUse;
	private CouponDto couponToUse;
	private List<PointDto> pointsToUse;

	@Builder
	public SelectionDto(Integer userId, int cartAmount, int payAmount, SavingsDto savings, CouponDto coupon, List<PointDto> points) {
		this.userId = userId;
		this.cartAmount = cartAmount;
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