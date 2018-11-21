package io.oacy.education.xunwu.service;

import io.oacy.education.xunwu.domain.SupportAddress;
import io.oacy.education.xunwu.service.result.ServiceMultiResult;
import io.oacy.education.xunwu.service.result.ServiceResult;
import io.oacy.education.xunwu.web.dto.SubwayDTO;
import io.oacy.education.xunwu.web.dto.SubwayStationDTO;
import io.oacy.education.xunwu.web.dto.SupportAddressDTO;

import java.util.List;
import java.util.Map;

public interface AddressService {
    /**
     * 获取所有支持的城市列表
     *
     * @return
     */

    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 根据英文简写获取具体区域的信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    /**
     * 根据城市英文简写获取该城市所有支持的区域信息
     * @param cityName
     * @return
     */
    ServiceMultiResult findAllRegionsByCityName(String cityName);

    /**
     * 获取该城市所有的地铁线路
     * @param cityEnName
     * @return
     */
    List<SubwayDTO> findAllSubwayByCity(String cityEnName);

    /**
     * 获取地铁线路所有的站点
     * @param subwayId
     * @return
     */
    List<SubwayStationDTO> findAllStationBySubway(Long subwayId);

    /**
     * 获取地铁线信息
     * @param subwayId
     * @return
     */
    ServiceResult<SubwayDTO> findSubway(Long subwayId);

    /**
     * 获取地铁站点信息
     * @param stationId
     * @return
     */
    ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId);

    /**
     * 根据城市英文简写获取城市详细信息
     * @param cityEnName
     * @return
     */
    ServiceResult<SupportAddressDTO> findCity(String cityEnName);
}
