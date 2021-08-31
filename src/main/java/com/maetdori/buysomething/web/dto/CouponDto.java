package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponDto {
	private Integer id;
	private String name;
	private int discountRate;
	private int minAmount;

	public CouponDto(Coupon entity) {
		this.id = entity.getId();
		this.name = entity.getCouponType().getName();
		this.discountRate = entity.getCouponType().getDiscountRate();
		this.minAmount = entity.getCouponType().getMinAmount();
	}
}
