package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Savings.Savings;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SavingsDto {
	private Integer id;
	private int amount;

	public SavingsDto(Savings entity) {
		this.id = entity.getId();
		this.amount = entity.getAmount();
	}

	@Builder
	public SavingsDto(Integer id, int amount) {
		this.id = id;
		this.amount = amount;
	}
}
