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

	//적립금 사용처리
	public void useSavings(int savingsToUse) {
		verifyAmountToUse(savingsToUse);
		this.amount -= savingsToUse;
	}

	//적립금 환불처리
	public void resetSavings(int savingsToReset) {
		this.amount += savingsToReset;
	}

	//적립금 사용금액 유효성 검사
	private void verifyAmountToUse(int savingsToUse) {
		if(savingsToUse < 0 || savingsToUse > this.amount)
			throw new SavingsInvalidAmountException();
	}

	//적립금 주인 검사
	public void verifyUser(int userId) {
		if(this.user.getId() != userId)
			throw new UserSavingsNotMatchingException();
	}
}
