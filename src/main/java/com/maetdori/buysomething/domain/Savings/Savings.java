package com.maetdori.buysomething.domain.Savings;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.SavingsInvalidAmountException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Savings {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "savings_id")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private int amount;

	public void useSavings(int savingsToUse) {
		verifyAmountToUse(savingsToUse);
		this.amount -= savingsToUse;
	}

	public void resetSavings(int savingsToReset) {
		this.amount += savingsToReset;
	}

	private void verifyAmountToUse(int savingsToUse) {
		if(savingsToUse < 0 || savingsToUse > this.amount)
			throw new SavingsInvalidAmountException();
	}
}
