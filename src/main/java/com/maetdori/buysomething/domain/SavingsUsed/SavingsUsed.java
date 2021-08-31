package com.maetdori.buysomething.domain.SavingsUsed;

import com.maetdori.buysomething.domain.Payment.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SavingsUsed {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	private int amount;

	@Builder
	public SavingsUsed(Payment payment, int amount) {
		this.payment = payment;
		this.amount = amount;
	}
}
