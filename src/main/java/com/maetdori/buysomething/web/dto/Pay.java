package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class Pay {

	@Getter
	public static class Request {
		private Long userId;
		private int savings;
		private Long couponId;
		private List<PointDto> points;
	}

	@Getter
	@AllArgsConstructor
	public static class Response {
		private Long userId;
		private Long paymentId;
	}
}
