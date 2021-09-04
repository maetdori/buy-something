package com.maetdori.buysomething.domain.Payment;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.DiscountInvalidAmountException;
import com.maetdori.buysomething.exception.PaymentAlreadyRefundedException;
import com.maetdori.buysomething.exception.UserPaymentNotMatchingException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int cartAmount;

    private int payAmount;

    private LocalDateTime purchaseDate;

    private LocalDateTime refundDate;

    @Builder
    public Payment(User user, int cartAmount, int payAmount, LocalDateTime purchaseDate) {
        this.user = user;
        this.cartAmount = cartAmount;
        this.payAmount = payAmount;
        this.purchaseDate = purchaseDate;
    }

    public void discount(int amount) {
        if(amount > this.payAmount)
            throw new DiscountInvalidAmountException();
        this.payAmount -= amount;
    }

    public void refundPayment(LocalDateTime dateTime) {
        verifyRefundedPayment(); //이미 환불된 결제내역인지 확인
        this.refundDate = dateTime;
    }

    private void verifyRefundedPayment() {
        if(refundDate != null)
            throw new PaymentAlreadyRefundedException();
    }

    public void verifyUser(Integer userId) {
        if(this.user.getId() != userId)
            throw new UserPaymentNotMatchingException();
    }
}