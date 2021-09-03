package com.maetdori.buysomething.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundDto {
	private int repayAmount;

	public void minusExpiredPoint(int amount) {
		this.repayAmount -= amount;
	}
}
