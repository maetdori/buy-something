package com.maetdori.buysomething.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/* 400 BAD_REQUEST : 잘못된 요청 */
	COUPON_ALREADY_USED(BAD_REQUEST, "이미 사용한 쿠폰입니다"),
	COUPON_DOESNT_MEET_MIN_AMOUNT(BAD_REQUEST, "주문금액이 쿠폰 사용 기준금액에 미치지 못합니다"),
	POINT_EXPIRED(BAD_REQUEST, "기한이 만료된 포인트입니다"),
	POINT_INVALID_AMOUNT(BAD_REQUEST, "가용 포인트 범위를 벗어납니다"),
	SAVINGS_INVALID_AMOUNT(BAD_REQUEST, "가용 적립금 범위를 벗어납니다"),

	/* 404 NOT_FOUND : 리소스를 찾을 수 없음 */
	USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
	COUPON_NOT_FOUND(NOT_FOUND, "해당 쿠폰 정보를 찾을 수 없습니다"),
	POINT_NOT_FOUND(NOT_FOUND, "해당 포인트 정보를 찾을 수 없습니다"),

	;

	private final HttpStatus httpStatus;
	private final String detail;
}
