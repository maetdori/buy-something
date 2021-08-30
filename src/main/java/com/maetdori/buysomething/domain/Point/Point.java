package com.maetdori.buysomething.domain.Point;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
public class Point {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private int amount;

	private LocalDate expiryDate;

	public void usePoint(int pointToUse) {
		this.amount -= pointToUse;
	}
}
