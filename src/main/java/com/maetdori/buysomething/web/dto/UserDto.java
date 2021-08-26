package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserDto {
	private Request request;
	private Info info;
	private Selection selection;

	public UserDto(Request request) {
		this.request = request;
	}

	public UserDto(Info info) {
		this.info = info;
	}

	public UserDto(Selection selection) {
		this.selection = selection;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {
		private String userName;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Info {
		private Long userId;
		private int cartAmount;
		private int savings;
		private List<PointDto> points;
		private List<CouponDto> coupons;

		public void setCartAmount(int amount) {
			this.cartAmount = amount;
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Selection {
		private Long userId;
		private int cartAmount;
		private int payAmount;
		private int savingsToUse;
		private CouponDto couponToUse;
		private List<PointDto.Selected> pointsToUse;
	}
}
