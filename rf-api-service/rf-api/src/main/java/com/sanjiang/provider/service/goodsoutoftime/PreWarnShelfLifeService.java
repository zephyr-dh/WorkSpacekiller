package com.sanjiang.provider.service.goodsoutoftime;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.goodsoutoftime.PreWarnShelfLife;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by byinbo on 2018/5/11.
 * 保质期预警
 */
public interface PreWarnShelfLifeService {

    /**
     * 保质期预警查询
     * @param report
     * @param shopId
     * @return
     */
    ResponseMessage goodsOutOfTimeCx(String report,
                                                             String shopId,
                                                             String jcrq,
                                                             String fzm,
                                                             String spbh);

    /**
     * 生成预警单
     * @param shopId
     */
    ResponseMessage goodsOutOfTimeCreat(String shopId, String[] fzm);

    /**
     * 预警检查
     * @return
     */
    ResponseMessage goodsOutOfTimeCz(String czlx, String shopId, String glbh, String scrq, String hjbh, String clry, String jcrq);
}
