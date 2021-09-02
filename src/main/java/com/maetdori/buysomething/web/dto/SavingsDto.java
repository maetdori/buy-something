package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Savings.Savings;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SavingsDto {
	private Integer id;
	private int amount;

	public SavingsDto(Savings entity) {
		this.id = entity.getId();
		this.amount = entity.getAmount();
	}

	public SavingsDto(SavingsUsed entity) {
		this.amount = entity.getAmount();
	}
}
