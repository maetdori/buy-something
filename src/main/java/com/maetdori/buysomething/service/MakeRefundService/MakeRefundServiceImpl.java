package com.maetdori.buysomething.service.MakeRefundService;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.Point.PointRepository;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.Savings.SavingsRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.web.dto.RefundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class MakeRefundServiceImpl implements MakeRefundService {
	private final PaymentRepository paymentRepo;
	private final SavingsRepository savingsRepo;
	private final SavingsUsedRepository savingsUsedRepo;
	private final PointRepository pointRepo;
	private final PointUsedRepository pointUsedRepo;
	private final CouponRepository couponRepo;


	@Override
	@Transactional
	public RefundDto makeRefund(Integer paymentId) {
		Payment targetPayment = paymentRepo.findById(paymentId).get(); //validation required
		targetPayment.refundPayment();

		RefundDto refundDto = new RefundDto(targetPayment.getPayAmount());

		resetSavings(targetPayment);
		resetCoupon(targetPayment);
		resetPoints(targetPayment, refundDto);

		return refundDto;
	}

	@Override
	public void resetSavings(Payment payment) {
		SavingsUsed savingsUsed = savingsUsedRepo.findByPaymentId(payment.getId());
		if(savingsUsed == null) return;

		int amount = savingsUsed.getAmount();
		savingsRepo.findByUserId(payment.getUser().getId()).resetSavings(amount);
	}

	@Override
	public void resetCoupon(Payment payment) {
		Coupon couponUsed = couponRepo.findByPaymentId(payment.getId());
		if(couponUsed == null) return;

		couponUsed.resetCoupon();
	}

	@Override
	public void resetPoints(Payment payment, RefundDto refundDto) {
		for(PointUsed pointUsed: pointUsedRepo.findAllByPaymentId(payment.getId())) {
			Point point = pointUsed.getPoint();

			if(point.getExpiryDate().isBefore(LocalDate.now())) {
				refundDto.minusExpiredPoint(pointUsed.getAmount());
			}
		}
	}
}
