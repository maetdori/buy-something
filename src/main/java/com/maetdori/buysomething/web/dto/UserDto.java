package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class UserDto {
	@Getter
	@AllArgsConstructor
	public static class Request {
		private Long userId;
		private int amount;
	}

	@Getter
	@AllArgsConstructor
	public static class Response {
		private Info userInfo;
		private AutoSelect selection;
	}

	@Getter
	@AllArgsConstructor
	public static class Info {
		private Long userId;
		private int savings;
		private List<PointDto> points;
		private List<CouponDto> coupons;
	}

	@Getter
	@AllArgsConstructor
	public static class AutoSelect {
		private int payAmount;
		private int savingsToUse;
		private CouponDto couponToUse;
		private List<PointDto.Selected> pointsToUse;
	}
}
