package com.maetdori.buysomething.service.MakePaymentService;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.web.dto.*;

public interface MakePaymentService {
    int makePayment(SelectionDto selection);
    Payment savePayment(Integer userId, int cartAmount);
    void useSavings(SavingsDto savings, Payment payment);
    void useCoupon(CouponDto coupon, Payment payment);
    void usePoint(PointDto point, Payment payment);
}