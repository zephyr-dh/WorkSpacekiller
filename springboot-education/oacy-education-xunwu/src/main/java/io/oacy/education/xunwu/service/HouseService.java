package io.oacy.education.xunwu.service;

import io.oacy.education.xunwu.base.status.HouseSubscribeStatus;
import io.oacy.education.xunwu.service.result.ServiceMultiResult;
import io.oacy.education.xunwu.service.result.ServiceResult;
import io.oacy.education.xunwu.web.dto.HouseDTO;
import io.oacy.education.xunwu.web.dto.HouseSubscribeDTO;
import io.oacy.education.xunwu.web.form.DatatableSearchForm;
import io.oacy.education.xunwu.web.form.HouseForm;
import io.oacy.education.xunwu.web.form.MapSearchForm;
import io.oacy.education.xunwu.web.form.RentSearchForm;
import javafx.util.Pair;

import java.util.Date;

public interface HouseService {

    /**
     * 新增
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> save(HouseForm houseForm);

    ServiceResult update(HouseForm houseForm);

    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearchForm searchBody);

    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id);

    /**
     * 移除图片
     * @param id
     * @return
     */
    ServiceResult removePhoto(Long id);

    /**
     * 更新封面
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult updateCover(Long coverId, Long targetId);

    /**
     * 新增标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult addTag(Long houseId, String tag);

    /**
     * 移除标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult removeTag(Long houseId, String tag);

    /**
     * 更新房源状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult updateStatus(Long id, int status);

    /**
     * 查询房源信息集
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearchForm rentSearch);

    /**
     * 全地图查询
     * @param mapSearchForm
     * @return
     */
    ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearchForm mapSearchForm);

    /**
     * 精确范围数据查询
     * @param mapSearchForm
     * @return
     */
    ServiceMultiResult<HouseDTO> boundMapQuery(MapSearchForm mapSearchForm);

    /**
     * 加入预约清单
     * @param houseId
     * @return
     */
    ServiceResult addSubscribeOrder(Long houseId);

    /**
     * 获取对应状态的预约列表
     */
    ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> querySubscribeList(HouseSubscribeStatus status, int start, int size);

    /**
     * 预约看房时间
     * @param houseId
     * @param orderTime
     * @param telephone
     * @param desc
     * @return
     */
    ServiceResult subscribe(Long houseId, Date orderTime, String telephone, String desc);

    /**
     * 取消预约
     * @param houseId
     * @return
     */
    ServiceResult cancelSubscribe(Long houseId);

    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     */
    ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> findSubscribeList(int start, int size);

    /**
     * 完成预约
     */
    ServiceResult finishSubscribe(Long houseId);
}
