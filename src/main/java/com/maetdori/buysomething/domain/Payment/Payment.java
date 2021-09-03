package com.maetdori.buysomething.domain.Payment;

import com.maetdori.buysomething.domain.User.User;
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
    public Payment(User user, int cartAmount, int payAmount) {
        this.user = user;
        this.cartAmount = cartAmount;
        this.payAmount = payAmount;
        this.purchaseDate = LocalDateTime.now();
    }

    public void discount(int amount) {
        this.payAmount -= amount;
    }

    public void refundPayment() {
        this.refundDate = LocalDateTime.now();
    }
}