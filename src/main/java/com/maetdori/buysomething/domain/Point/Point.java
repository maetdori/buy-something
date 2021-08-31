package com.maetdori.buysomething.domain.Point;

import com.maetdori.buysomething.domain.User.User;
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
		this.amount -= pointToUse;
	}
}
