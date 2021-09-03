package com.maetdori.buysomething.domain.Coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponType {
	SUMMER_EVENT(1, "무더위 극복 쿠폰", 15, 30000),
	NEWBIE_COUPON(2, "신규회원 쿠폰", 10, 10000),
	APP_DOWNLOAD_COUPON(3, "앱 다운로드 쿠폰", 5, 15000);

	private final int code;
	private final String name;
	private final int discountRate;
	private final int minAmount;

	public static CouponType ofCode(int code) {
		return Arrays.stream(CouponType.values())
				.filter(c -> c.getCode() == code)
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
		//추후 예외 및 메시지 처리 수정
	}
}
