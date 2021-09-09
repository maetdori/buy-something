package com.maetdori.buysomething.domain.Point;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.Business.InvalidValue.PointExpiredException;
import com.maetdori.buysomething.exception.Business.InvalidValue.PointInvalidAmountException;
import com.maetdori.buysomething.exception.Business.UserMatching.UserPointNotMatchingException;
import lombok.Builder;
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

	private LocalDate expiryDate; //만료일

	@Builder
	public Point(User user, int amount, LocalDate expiryDate) {
		this.user = user;
		this.amount = amount;
		this.expiryDate = expiryDate;
	}

	//포인트 사용
	public void usePoint(int pointToUse) {
		verifyExpiration(); //만료일 확인
		verifyAmountToUse(pointToUse); //사용금액 유효성 확인
		this.amount -= pointToUse;
	}

	//포인트 환불처리
	public void resetPoint(int amountToReset) {
		this.amount += amountToReset;
	}

	//포인트 만료여부 검사
	private void verifyExpiration() {
		if(LocalDate.now().isAfter(this.expiryDate))
			throw new PointExpiredException();
	}

	//포인트 사용금액 유효성 검사
	private void verifyAmountToUse(int pointToUse) {
		if(pointToUse < 0 || pointToUse > this.amount)
			throw new PointInvalidAmountException();
	}

	//포인트 주인 검사
	public void verifyUser(int userId) {
		if(this.user.getId() != userId)
			throw new UserPointNotMatchingException();
	}
}
