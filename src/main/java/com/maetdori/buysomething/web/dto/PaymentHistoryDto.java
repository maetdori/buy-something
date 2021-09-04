package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Payment.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentHistoryDto {
	private Integer id;
	private LocalDateTime purchaseDate;

	public PaymentHistoryDto(Payment entity) {
		this.id = entity.getId();
		this.purchaseDate = entity.getPurchaseDate();
	}
}
