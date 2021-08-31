package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	private Integer userId;
	private int cartAmount;
	private SavingsDto savings;
	private List<PointDto> points;
	private List<CouponDto> coupons;

	@Builder
	public UserInfo(Integer userId, SavingsDto savings, List<PointDto> points, List<CouponDto> coupons) {
		this.userId = userId;
		this.savings = savings;
		this.points = points;
		this.coupons = coupons;
	}

	public void setCartAmount(int amount) {
		this.cartAmount = amount;
	}

	public boolean hasSavings() {
		return this.savings != null;
	}

	public boolean hasPoints() {
		return !this.points.isEmpty();
	}

	public boolean hasCoupons() {
		return !this.coupons.isEmpty();
	}
}
