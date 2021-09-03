package com.maetdori.buysomething.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private int cartAmount;
    private int payAmount;
    private CouponDto usedCoupon;
    private List<PointDto> usedPoints;
    private SavingsDto usedSavings;

    @Builder
    public PaymentDto(int cartAmount, int payAmount, CouponDto coupon, List<PointDto> points, SavingsDto savings) {
        this.cartAmount = cartAmount;
        this.payAmount = payAmount;
        this.usedCoupon = coupon;
        this.usedPoints = points;
        this.usedSavings = savings;
    }
}
