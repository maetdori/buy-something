package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Point.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PointDto {
	private Long id;
	private int amount;
	private LocalDate expiryDate;

	public PointDto(Point point) {
		this.id = point.getId();
		this.amount = point.getAmount();
		this.expiryDate = point.getExpiryDate();
	}

	@Getter
	@AllArgsConstructor
	public static class Selected {
		private Long id;
		private int amount;
	}
}
