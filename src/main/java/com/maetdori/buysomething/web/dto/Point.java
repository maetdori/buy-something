package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Point {
	private Long id;
	private int amount;
	private LocalDate expiryDate;

	@Getter
	@AllArgsConstructor
	public static class Selected {
		private Long id;
		private int amount;
	}
}
