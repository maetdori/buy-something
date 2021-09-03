package com.maetdori.buysomething.domain.Coupon;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.CouponAlreadyUsedException;
import com.maetdori.buysomething.exception.CouponDoesntMeetMinAmountException;
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

	private boolean expired;

	@Convert(converter = CouponTypeInverter.class)
	private CouponType couponType;

	public void useCoupon(Payment payment) {
		verifyExpiration();
		verifyMinAmount(payment.getCartAmount());
		this.payment = payment;
		this.expired = true;
	}

	public void resetCoupon() {
		this.payment = null;
		this.expired = false;
	}

	private void verifyExpiration() {
		if(this.expired)
			throw new CouponAlreadyUsedException();
	}

	private void verifyMinAmount(int cartAmount) {
		if(cartAmount < this.couponType.getMinAmount())
			throw new CouponDoesntMeetMinAmountException();
	}
}
