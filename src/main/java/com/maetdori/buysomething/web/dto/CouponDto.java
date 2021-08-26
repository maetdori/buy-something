package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
	private Long id;
	private String name;
	private int discountRate;
	private int minAmount;

	public CouponDto(Coupon entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.discountRate = entity.getDiscountRate();
		this.minAmount = entity.getMinAmount();
	}
}
