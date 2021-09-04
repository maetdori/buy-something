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

	//extends InvalidValueException
	COUPON_ALREADY_USED(BAD_REQUEST, "이미 사용한 쿠폰입니다"),
	COUPON_DOESNT_MEET_MIN_AMOUNT(BAD_REQUEST, "주문금액이 쿠폰 사용 기준금액에 미치지 못합니다"),
	POINT_EXPIRED(BAD_REQUEST, "기한이 만료된 포인트입니다"),
	POINT_INVALID_AMOUNT(BAD_REQUEST, "가용 포인트 범위를 벗어납니다"),
	SAVINGS_INVALID_AMOUNT(BAD_REQUEST, "가용 적립금 범위를 벗어납니다"),
	DISCOUNT_INVALID_AMOUNT(BAD_REQUEST, "할인금액이 주문금액을 초과합니다"),
	PAYMENT_ALREADY_REFUNDED(BAD_REQUEST, "이미 환불된 내역입니다"),
	ZERO_CART_AMOUNT(BAD_REQUEST, "주문 금액이 입력되지 않았습니다"),

	//extends UserMatchingException
	USER_SAVINGS_NOT_MATCHING(BAD_REQUEST, "사용자정보와 적립금정보가 일치하지 않습니다"),
	USER_COUPON_NOT_MATCHING(BAD_REQUEST, "사용자정보와 쿠폰정보가 일치하지 않습니다"),
	USER_POINT_NOT_MATCHING(BAD_REQUEST, "사용자정보와 포인트정보가 일치하지 않습니다"),
	USER_PAYMENT_NOT_MATCHING(BAD_REQUEST, "사용자정보와 결제내역이 일치하지 않습니다"),


	/* 404 NOT_FOUND : 리소스를 찾을 수 없음 */

	//extends EntityNotFoundException
	USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
	SAVINGS_NOT_FOUND(NOT_FOUND, "해당 적립금 정보를 찾을 수 없습니다"),
	COUPON_NOT_FOUND(NOT_FOUND, "해당 쿠폰 정보를 찾을 수 없습니다"),
	POINT_NOT_FOUND(NOT_FOUND, "해당 포인트 정보를 찾을 수 없습니다"),
	PAYMENT_NOT_FOUND(NOT_FOUND, "해당 결제 내역을 찾을 수 없습니다")

	;

	private final HttpStatus httpStatus;
	private final String detail;
}
