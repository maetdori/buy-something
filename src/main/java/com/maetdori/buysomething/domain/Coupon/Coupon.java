package com.maetdori.buysomething.domain.Coupon;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.Business.InvalidValue.CouponAlreadyUsedException;
import com.maetdori.buysomething.exception.Business.InvalidValue.CouponDoesntMeetMinAmountException;
import com.maetdori.buysomething.exception.Business.UserMatching.UserCouponNotMatchingException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Coupon {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private Payment payment;

	private boolean used;

	@Convert(converter = CouponTypeInverter.class)
	private CouponType couponType;

	@Builder
	public Coupon(User user, CouponType couponType) {
		this.user = user;
		this.used = false;
		this.couponType = couponType;
	}

	//쿠폰 사용처리
	public void useCoupon(Payment payment) {
		verifyUsedCoupon();
		verifyMinAmount(payment.getCartAmount());
		this.payment = payment;
		this.used = true;
	}

	//쿠폰 환불처리
	public void resetCoupon() {
		this.payment = null;
		this.used = false;
	}

	//쿠폰 사용여부 검사
	private void verifyUsedCoupon() {
		if(this.used)
			throw new CouponAlreadyUsedException();
	}

	//쿠폰 최소주문금액 검사
	private void verifyMinAmount(int cartAmount) {
		if(cartAmount < this.couponType.getMinAmount())
			throw new CouponDoesntMeetMinAmountException();
	}

	//쿠폰 주인 검사
	public void verifyUser(int userId) {
		if(this.user.getId() != userId)
			throw new UserCouponNotMatchingException();
	}
}
