package com.maetdori.buysomething.domain.PointUsed;

import com.maetdori.buysomething.domain.Payment.Payment;
import com.maetdori.buysomething.domain.Point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PointUsed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "point_id")
    private Point point;

    private int amount; //사용한 포인트 금액

    @Builder
    public PointUsed(Payment payment, Point point, int amount) {
        this.payment = payment;
        this.point = point;
        this.amount = amount;
    }
}