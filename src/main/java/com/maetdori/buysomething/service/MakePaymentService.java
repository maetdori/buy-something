package com.maetdori.buysomething.service;

import com.maetdori.buysomething.web.dto.CouponDto;
import com.maetdori.buysomething.web.dto.UserDto;

public interface MakePaymentService {
    Long makePayment(UserDto.Selection selection);
    Long savePayment(Long userId, int savingsUsed);
    void useSavings(Long userId, int savingsToUse);
    void useCoupon(Long userId, Long paymentId, CouponDto coupon);
    void usePoint(Long userId, Long paymentId, Long pointId, int pointToUse);
}