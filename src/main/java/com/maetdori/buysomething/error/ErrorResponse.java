package com.maetdori.buysomething.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
	private final LocalDateTime timestamp = LocalDateTime.now();
	private final String error;
	private final String message;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
				.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.error(errorCode.getHttpStatus().name())
						.message(errorCode.getDetail())
						.build());
	}
}
