package com.maetdori.buysomething.service.MakeRefundService;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.RefundDto;
import com.maetdori.buysomething.web.dto.SavingsDto;

public interface MakeRefundService {
	RefundDto makeRefund(Integer paymentId);
	void resetSavings(Payment payment);
	void resetCoupon(Payment payment);
	void resetPoints(Payment payment, RefundDto refundDto);
}
