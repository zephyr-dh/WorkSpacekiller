package com.sanjiang.provider.constrants;

/**
 * SQL执行参数常量
 *
 * @author kimiyu
 * @date 2018/5/2 17:31
 */
public enum SqlParam {

    AS_HJBH("as_hjbh"),

    AS_CH("as_ch"),

    AS_WZ("as_wz"),

    AS_GLBH("as_glbh"),

    AS_SPBH("as_spbh"),

    AS_JZHYJ("jzhyj"),

    AS_SCBH("as_scbh"),

    AS_UUID("as_uuid"),

    AS_BMBH("as_bmbh"),

    AS_CCH("as_cch");

    private final String value;

    SqlParam(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
