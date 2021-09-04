package com.maetdori.buysomething.service.MakeRefundService;

import com.maetdori.buysomething.domain.Coupon.Coupon;
import com.maetdori.buysomething.domain.Coupon.CouponRepository;
import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Payment.PaymentRepository;
import com.maetdori.buysomething.domain.Point.Point;
import com.maetdori.buysomething.domain.PointUsed.PointUsed;
import com.maetdori.buysomething.domain.PointUsed.PointUsedRepository;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsed;
import com.maetdori.buysomething.domain.SavingsUsed.SavingsUsedRepository;
import com.maetdori.buysomething.exception.Business.EntityNotFound.PaymentNotFoundException;
import com.maetdori.buysomething.web.dto.RefundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MakeRefundServiceImpl implements MakeRefundService {
	private final PaymentRepository paymentRepo;
	private final SavingsUsedRepository savingsUsedRepo;
	private final PointUsedRepository pointUsedRepo;
	private final CouponRepository couponRepo;

	@Override
	@Transactional
	public RefundDto makeRefund(Integer userId, Integer paymentId, LocalDateTime refundDate) {
		Payment targetPayment = paymentRepo.findById(paymentId)
						.orElseThrow(() -> new PaymentNotFoundException());

		targetPayment.verifyUser(userId); //유저의 결제내역이 맞는지 확인

		targetPayment.refundPayment(refundDate);

		RefundDto refundDto = new RefundDto(targetPayment.getPayAmount());

		resetSavings(targetPayment);
		resetCoupon(targetPayment);
		resetPoints(targetPayment, refundDto, refundDate.toLocalDate());

		return refundDto;
	}

	@Override
	public void resetSavings(Payment payment) {
		SavingsUsed savingsUsed = savingsUsedRepo.findByPaymentId(payment.getId()).orElse(null);
		if(savingsUsed == null) return;

		int amount = savingsUsed.getAmount();
		savingsUsed.getSavings().resetSavings(amount);
	}

	@Override
	public void resetCoupon(Payment payment) {
		Coupon couponUsed = couponRepo.findByPaymentId(payment.getId()).orElse(null);
		if(couponUsed == null) return;

		couponUsed.resetCoupon();
	}

	@Override
	public void resetPoints(Payment payment, RefundDto refundDto, LocalDate refundDate) {
		for(PointUsed pointUsed: pointUsedRepo.findAllByPaymentId(payment.getId())) {
			Point point = pointUsed.getPoint();

			if(refundDate.isAfter(point.getExpiryDate()))
				refundDto.cantRefundExpiredPoint(pointUsed.getAmount());

			point.resetPoint(pointUsed.getAmount());
		}
	}
}
