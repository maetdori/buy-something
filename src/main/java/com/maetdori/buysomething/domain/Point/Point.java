package com.maetdori.buysomething.domain.Point;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.PointExpiredException;
import com.maetdori.buysomething.exception.PointInvalidAmountException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Point {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private int amount;

	private LocalDate expiryDate;

	public void usePoint(int pointToUse) {
		verifyExpiration();
		verifyAmountToUse(pointToUse);
		this.amount -= pointToUse;
	}

	private void verifyExpiration() {
		if(LocalDate.now().isAfter(this.expiryDate))
			throw new PointExpiredException();
	}

	private void verifyAmountToUse(int pointToUse) {
		if(pointToUse < 0 || pointToUse > this.amount)
			throw new PointInvalidAmountException();
	}
}
