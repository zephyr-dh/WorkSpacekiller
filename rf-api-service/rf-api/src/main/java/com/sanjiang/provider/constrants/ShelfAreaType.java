package com.sanjiang.provider.constrants;

/**
 * 货架区域编码
 *
 * @author kimiyu
 * @date 2018/4/26 11:38
 */
public enum ShelfAreaType {

    SHELF("HJ"),

    PILE_POSITION("DW"),

    END_FRAME("DJ"),

    STORAGE_ONE("CK1"),

    STORAGE_TWO("CK2");

    private final String value;

    ShelfAreaType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }


}
