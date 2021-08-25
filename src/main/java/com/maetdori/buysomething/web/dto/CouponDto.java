package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponDto {
	private Long id;
	private String name;
	private int discountRate;
	private int minAmount;

	public CouponDto(Coupon coupon) {
		this.id = coupon.getId();
		this.name = coupon.getName();
		this.discountRate = coupon.getDiscountRate();
		this.minAmount = coupon.getMinAmount();
	}
}
