package com.maetdori.buysomething.domain.Savings;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.Business.InvalidValue.SavingsInvalidAmountException;
import com.maetdori.buysomething.exception.Business.UserMatching.UserSavingsNotMatchingException;
import lombok.Builder;
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

	@Builder
	public Savings(User user, int amount) {
		this.user = user;
		this.amount = amount;
	}

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

	public void verifyUser(int userId) {
		if(this.user.getId() != userId)
			throw new UserSavingsNotMatchingException();
	}
}
