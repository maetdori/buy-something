package com.maetdori.buysomething.web.dto;

import com.maetdori.buysomething.domain.Payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private List<History> history;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class History {
        private Long id;
        private int savingsUsed;
        private LocalDateTime purchaseDate;
        private LocalDateTime refundDate;

        public History(Payment entity) {
            this.id = entity.getId();
            this.savingsUsed = entity.getSavingsUsed();
            this.purchaseDate = entity.getPurchaseDate();
            this.refundDate = entity.getRefundDate();
        }
    }
}
