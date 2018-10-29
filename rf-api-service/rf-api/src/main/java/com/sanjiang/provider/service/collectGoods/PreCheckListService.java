package com.sanjiang.provider.service.collectGoods;

import com.sanjiang.core.ResponseMessage;
import com.sanjiang.provider.domain.collectGoods.PreCheckList;
import com.sanjiang.provider.model.CollectGoodsModel;
import com.sanjiang.provider.model.PreCheckModel;

import java.util.List;

/**
 * Created by byinbo on 2018/5/21.
 * 预检
 */
public interface PreCheckListService {

    /**
     * 显示15天内未生成预检单
     * @param scbh
     * @return
     */
    ResponseMessage<Object> getReportPreCheckList(String scbh);


//    /**
//     * 预检单检测
//     * @param scbh
//     * @param bmbh
//     * @param cch
//     * @return
//     */
//    ResponseMessage checkPreCheckList(String scbh, String bmbh, String cch);

    /**
     * 查询预检单
     * @param scbh
     * @param bmbh
     * @param cch
     * @return
     */
    ResponseMessage<Object> queryPreCheckList(String scbh, String bmbh, String cch);

    /**
     * 生成预检单
     * @param scbh
     * @param uuid
     * @return
     */
    ResponseMessage createPreCheckList(String scbh, String uuid);

    /**
     * 验货明细列表（差异列表）
     * @param scbh
     * @param djdh
     * @return
     */
    ResponseMessage inspectGoodsList(String scbh, String djdh);


    /**
     * 审核
     * @param scbh
     * @param djdh
     * @return
     */
    ResponseMessage verifyPreCheckList(String scbh, String djdh);


    /**
     * 收货上传数据
     * @param collectGoodsModels
     * @return
     */
    ResponseMessage uploadData(List<CollectGoodsModel> collectGoodsModels);

    /**
     * 生成预检单上传数据
     * @param preCheckModel
     * @return
     */
    ResponseMessage insertPreCheckData(List<PreCheckModel> preCheckModel);

}
