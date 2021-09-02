package com.maetdori.buysomething.service.MakePaymentService;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.PointDto;
import com.maetdori.buysomething.web.dto.SavingsDto;
import com.maetdori.buysomething.web.dto.SelectionDto;

public interface MakePaymentService {
    Payment makePayment(SelectionDto selection);
    Payment savePayment(Integer userId, int cartAmount);
    void useSavings(SavingsDto savings, Payment payment);
    void useCoupon(CouponDto coupon, Payment payment);
    void usePoint(PointDto point, Payment payment);
}