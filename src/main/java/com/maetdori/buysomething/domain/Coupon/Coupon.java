package com.maetdori.buysomething.domain.Coupon;

import com.maetdori.buysomething.domain.Enum.CouponType;
import com.maetdori.buysomething.domain.Enum.CouponTypeInverter;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.User.User;
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

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	private boolean expired;

	@Convert(converter = CouponTypeInverter.class)
	private CouponType couponType;

	public void UseCoupon(Payment payment) {
		//this.payment = payment;
		this.expired = true;
	}
}
