package com.sanjiang.provider.domain.goodsoutoftime;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by byinbo on 2018/5/11.
 */
@Data
public class PreWarnShelfLife implements Serializable {

    /**
     * 生成预警单日期
     */
    private LocalDateTime jcrq;

    /**
     * 生成日期
     */
    private LocalDateTime scrq;

    /**
     * 处理日期
     */
    private LocalDateTime clrq;

    /**
     * 品种数
     */
    private Integer cnt;

    /**
     * 已检查数
     */
    private Integer yjccnt;

    /**
     * 分组范围
     */
    private String fzm;

    /**
     * 分组名称
     */
    private String fzmc;

    /**
     * 分组类型
     */
    private String fzlx;

    /**
     * 部门等级id
     */
    private Integer deptlevelid;

    /**
     * 已检查组别
     */
    private String yjczb;

    private String spmc;

    private String hjbh;

    private String spbh;

    private String jzhyj;

    private String kcsl;

    private String glbh;

    private String bzqts;

}
