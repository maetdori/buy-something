package com.maetdori.buysomething.web;

import com.maetdori.buysomething.error.ErrorCode;
import com.maetdori.buysomething.error.ErrorResponse;
import com.maetdori.buysomething.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/* Invalid Value Exception */
	
	@ExceptionHandler(value = {CouponAlreadyUsedException.class})
	protected ResponseEntity<ErrorResponse> handleCouponAlreadyUsedException() {
		return ErrorResponse.toResponseEntity(ErrorCode.COUPON_ALREADY_USED);
	}

	@ExceptionHandler(value = {CouponDoesntMeetMinAmountException.class})
	protected ResponseEntity<ErrorResponse> handleCouponDoesntMeetMinAmountException() {
		return ErrorResponse.toResponseEntity(ErrorCode.COUPON_DOESNT_MEET_MIN_AMOUNT);
	}

	@ExceptionHandler(value = {PointExpiredException.class})
	protected ResponseEntity<ErrorResponse> handlePointExpiredException() {
		return ErrorResponse.toResponseEntity(ErrorCode.POINT_EXPIRED);
	}

	@ExceptionHandler(value = {PointInvalidAmountException.class})
	protected ResponseEntity<ErrorResponse> handlePointInvalidAmountException() {
		return ErrorResponse.toResponseEntity(ErrorCode.POINT_INVALID_AMOUNT);
	}

	@ExceptionHandler(value = {SavingsInvalidAmountException.class})
	protected ResponseEntity<ErrorResponse> handleSavingsInvalidAmountException() {
		return ErrorResponse.toResponseEntity(ErrorCode.SAVINGS_INVALID_AMOUNT);
	}


	/* Entity Not Found Exception */

	@ExceptionHandler(value = {UserNotFoundException.class})
	protected ResponseEntity<ErrorResponse> handleUserNotFoundException() {
		return ErrorResponse.toResponseEntity(ErrorCode.USER_NOT_FOUND);
	}

	@ExceptionHandler(value = {CouponNotFoundException.class})
	protected ResponseEntity<ErrorResponse> handleCouponNotFoundException() {
		return ErrorResponse.toResponseEntity(ErrorCode.COUPON_NOT_FOUND);
	}

	@ExceptionHandler(value = {PointNotFoundException.class})
	protected ResponseEntity<ErrorResponse> handlePointNotFoundException() {
		return ErrorResponse.toResponseEntity(ErrorCode.POINT_NOT_FOUND);
	}
}