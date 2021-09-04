package com.maetdori.buysomething.service.MakeRefundService;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.web.dto.RefundDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MakeRefundService {
	RefundDto makeRefund(Integer userId, Integer paymentId, LocalDateTime refundDate);
	void resetSavings(Payment payment);
	void resetCoupon(Payment payment);
	void resetPoints(Payment payment, RefundDto refundDto, LocalDate refundDate);
}
