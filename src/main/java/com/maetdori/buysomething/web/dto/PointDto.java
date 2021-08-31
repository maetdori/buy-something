package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Point.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {
	private Integer id;
	private int amount;
	private LocalDate expiryDate;

	public PointDto(Point entity) {
		this.id = entity.getId();
		this.amount = entity.getAmount();
		this.expiryDate = entity.getExpiryDate();
	}

	@Builder
	public PointDto(Integer id, int amount) {
		this.id = id;
		this.amount = amount;
	}
}
