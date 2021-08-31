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

    private LocalDateTime purchaseDate;

    private boolean refunded;

    @Builder
    public Payment(User user) {
        this.user = user;
        this.purchaseDate = LocalDateTime.now();
        this.refunded = false;
    }
}