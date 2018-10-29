package com.sanjiang.provider.constrants;

/**
 * @author kimiyu
 * @date 2018/4/26 11:43
 */
public enum ShelfType {

    LAMINATE("CB"),

    BASKET("WL"),

    POTHOOK("GG"),

    PROMOTION_POSITION("CX"),

    COLD_WIND("LF"),



    ICE_TANK("BG");

    private final String value;

    ShelfType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
