package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class User {

	@Getter
	public static class Request {
		private Long userId;
	}

	@Getter
	@AllArgsConstructor
	public static class Response {
		private Long userId;
		private int savings;
		List<Point> points;
		List<Coupon> coupons;
	}
}
