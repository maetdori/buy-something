package com.maetdori.buysomething.domain.Savings;

import com.maetdori.buysomething.domain.User.User;
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
		this.amount -= savingsToUse;
	}
}
