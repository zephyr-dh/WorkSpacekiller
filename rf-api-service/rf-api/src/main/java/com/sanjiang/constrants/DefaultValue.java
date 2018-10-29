package com.sanjiang.constrants;

/**
 * 默认值
 *
 * @author kimiyu
 * @date 2018/5/2 17:23
 */
public enum DefaultValue {

    DEFAULT_SHOPID("00023"),

    DEFAULT_ERROR_PARAM("参数校验失败"),

    DEFAULT_EXEC_PROC_ERROR("存储过程执行异常"),

    DEFAULT_CLOSE_DATASOURCE("关闭数据源");

    private final String value;

    DefaultValue(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
