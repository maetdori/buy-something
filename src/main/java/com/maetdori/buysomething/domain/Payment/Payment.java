package com.maetdori.buysomething.domain.Payment;

import com.maetdori.buysomething.domain.User.User;
import com.maetdori.buysomething.exception.Business.InvalidValue.DiscountInvalidAmountException;
import com.maetdori.buysomething.exception.Business.InvalidValue.PaymentAlreadyRefundedException;
import com.maetdori.buysomething.exception.Business.UserMatching.UserPaymentNotMatchingException;
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

    private int cartAmount; //주문금액

    private int payAmount; //결제금액

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

    //결제내역 환불처리
    public void refundPayment(LocalDateTime dateTime) {
        verifyRefundedPayment(); //이미 환불된 결제내역인지 확인
        this.refundDate = dateTime;
    }

    //결제건 환불여부 검사
    private void verifyRefundedPayment() {
        if(refundDate != null)
            throw new PaymentAlreadyRefundedException();
    }

    //결제내역 주인 검사
    public void verifyUser(Integer userId) {
        if(this.user.getId() != userId)
            throw new UserPaymentNotMatchingException();
    }
}