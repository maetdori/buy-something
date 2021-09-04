package com.maetdori.buysomething.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDetailDto {
    private int cartAmount;
    private int payAmount;
    private CouponDto usedCoupon;
    private List<PointDto> usedPoints;
    private SavingsDto usedSavings;
}
