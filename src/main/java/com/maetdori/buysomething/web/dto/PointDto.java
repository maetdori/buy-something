package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

	public PointDto(PointUsed entity) {
		this.amount = entity.getAmount();
		this.expiryDate = entity.getPoint().getExpiryDate();
	}
}
