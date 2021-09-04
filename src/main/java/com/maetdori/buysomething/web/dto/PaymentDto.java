package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Payment.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {
    private int cartAmount;
    private int payAmount;
    private CouponDto usedCoupon;
    private List<PointDto> usedPoints;
    private SavingsDto usedSavings;

    public PaymentDto(Payment entity) {
        this.cartAmount = entity.getCartAmount();
        this.payAmount = entity.getPayAmount();
    }
}
