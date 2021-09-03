package com.maetdori.buysomething.util;

public class Percent {
    public static int discountAmount(int amount, int percent) {
        return amount / 100 * percent;
    }
}
