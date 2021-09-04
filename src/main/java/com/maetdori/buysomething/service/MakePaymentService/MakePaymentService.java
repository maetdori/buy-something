package com.maetdori.buysomething.service.MakePaymentService;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.web.dto.*;

import java.time.LocalDateTime;

public interface MakePaymentService {
    Integer makePayment(SelectionDto selection, LocalDateTime purchaseDate);
    Payment createPayment(Integer userId, int cartAmount, LocalDateTime purchaseDate);
    void useSavings(SelectionDto selection, Payment payment);
    void useCoupon(SelectionDto selection, Payment payment);
    void usePoint(PointDto point, Payment payment);
}