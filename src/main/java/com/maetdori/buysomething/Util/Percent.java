package com.maetdori.buysomething.Util;

public class Percent {
    public static int discountPercent(int amount, int percent) {
        return amount / 100 * (100-percent);
    }
}
