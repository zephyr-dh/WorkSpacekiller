package io.oacy.education.xunwu.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HouseSubscribeDTO implements Serializable {
    private Long id;

    private Long houseId;

    private Long userId;

    private Long adminId;

    // 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
    private int status;

    private Date createTime;

    private Date lastUpdateTime;

    private Date orderTime;

    private String telephone;

    private String desc;
}
