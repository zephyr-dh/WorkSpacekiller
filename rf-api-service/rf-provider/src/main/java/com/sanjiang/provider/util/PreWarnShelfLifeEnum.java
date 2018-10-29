package com.sanjiang.provider.util;

/**
 * Created by byinbo on 2018/5/14.
 */
public enum PreWarnShelfLifeEnum {

    HZCX("hzcx"),

    YJDSCFZM("yjdsc_fzm"),

    YJDJCFZM("yjd_jc_fzm"),

    YJDJCSP("yjd_jc_sp"),

    YJDSPHJBH("yjd_sp_hjbh"),

    YJDSPSCRQ("yjd_sp_scrq"),

    YJDDPJC("yjd_dpjc");

    private String report;

    PreWarnShelfLifeEnum(String report) {
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    @Override
    public String toString() {
        return this.name() + "[" + report + "]";
    }
}
