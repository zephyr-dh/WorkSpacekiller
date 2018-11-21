package io.oacy.education.xunwu.service.implement;

import io.oacy.education.xunwu.base.status.HouseSubscribeStatus;
import io.oacy.education.xunwu.repository.*;
import io.oacy.education.xunwu.service.HouseService;
import io.oacy.education.xunwu.service.QiniuService;
import io.oacy.education.xunwu.service.result.ServiceMultiResult;
import io.oacy.education.xunwu.service.result.ServiceResult;
import io.oacy.education.xunwu.web.dto.HouseDTO;
import io.oacy.education.xunwu.web.dto.HouseSubscribeDTO;
import io.oacy.education.xunwu.web.form.DatatableSearchForm;
import io.oacy.education.xunwu.web.form.HouseForm;
import io.oacy.education.xunwu.web.form.MapSearchForm;
import io.oacy.education.xunwu.web.form.RentSearchForm;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HousePictureRepository housePictureRepository;

    @Autowired
    private HouseTagRepository houseTagRepository;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Autowired
    private HouseSubscribeRespository subscribeRespository;

    @Autowired
    private QiniuService qiNiuService;

    @Override
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        return null;
    }

    @Override
    public ServiceResult update(HouseForm houseForm) {
        return null;
    }

    @Override
    public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearchForm searchBody) {
        return null;
    }

    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        return null;
    }

    @Override
    public ServiceResult removePhoto(Long id) {
        return null;
    }

    @Override
    public ServiceResult updateCover(Long coverId, Long targetId) {
        return null;
    }

    @Override
    public ServiceResult addTag(Long houseId, String tag) {
        return null;
    }

    @Override
    public ServiceResult removeTag(Long houseId, String tag) {
        return null;
    }

    @Override
    public ServiceResult updateStatus(Long id, int status) {
        return null;
    }

    @Override
    public ServiceMultiResult<HouseDTO> query(RentSearchForm rentSearch) {
        return null;
    }

    @Override
    public ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearchForm mapSearchForm) {
        return null;
    }

    @Override
    public ServiceMultiResult<HouseDTO> boundMapQuery(MapSearchForm mapSearchForm) {
        return null;
    }

    @Override
    public ServiceResult addSubscribeOrder(Long houseId) {
        return null;
    }

    @Override
    public ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> querySubscribeList(HouseSubscribeStatus status, int start, int size) {
        return null;
    }

    @Override
    public ServiceResult subscribe(Long houseId, Date orderTime, String telephone, String desc) {
        return null;
    }

    @Override
    public ServiceResult cancelSubscribe(Long houseId) {
        return null;
    }

    @Override
    public ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> findSubscribeList(int start, int size) {
        return null;
    }

    @Override
    public ServiceResult finishSubscribe(Long houseId) {
        return null;
    }
}
