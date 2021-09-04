package com.maetdori.buysomething.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserPayInfoDto {
	private Integer userId;
	private SavingsDto savings;
	private List<PointDto> points;
	private List<CouponDto> coupons;

	@Builder
	public UserPayInfoDto(Integer userId, SavingsDto savings, List<PointDto> points, List<CouponDto> coupons) {
		this.userId = userId;
		this.savings = savings;
		this.points = points;
		this.coupons = coupons;
	}
}
