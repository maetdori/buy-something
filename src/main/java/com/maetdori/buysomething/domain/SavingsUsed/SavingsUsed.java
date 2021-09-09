package com.maetdori.buysomething.domain.SavingsUsed;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Savings.Savings;
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

	@OneToOne
	@JoinColumn(name = "savings_id")
	private Savings savings;

	private int amount; //적립금 사용 금액

	@Builder
	public SavingsUsed(Payment payment, Savings savings, int amount) {
		this.payment = payment;
		this.savings = savings;
		this.amount = amount;
	}
}
