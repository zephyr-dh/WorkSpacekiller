package com.sanjiang.provider.service.impl.priceCollect;

import com.alibaba.dubbo.config.annotation.Service;
import com.sanjiang.constrants.DefaultValue;
import com.sanjiang.core.ResponseMessage;
import com.sanjiang.core.ResultCode;
import com.sanjiang.provider.Dao.PriceCollectDao;
import com.sanjiang.provider.constrants.SqlParam;
import com.sanjiang.provider.model.PriceCollectModel;
import com.sanjiang.provider.service.priceCollect.PriceCollectService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byinbo on 2018/6/21.
 */
@Service(
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        version = "1.0.0",
        timeout = 50000
)
@Slf4j
public class PriceCollectServiceImpl implements PriceCollectService{

    @Autowired
    private PriceCollectDao priceCollectDao;

    private static final String PRICE_COLLECT_SELECT_SPMC = "select spmc,glbh from xt_spda  where glbh in (select glbh from xt_ftm  where spbh=:as_spbh)";

    private static final String CJ_SELECT_SPBH_SCNAME_CZY = "select * from cj where scname =:scname and czy =:czy and SPBH =:as_spbh and " +
            "cj_date>= trunc(sysdate,'month') and cj_date<= LAST_DAY(Trunc(SYSDATE, 'MONTH')) + 1 - 1 / 86400";

    private static final String PRICE_COLLECT_INSERT = "insert into cj (scname,czy,glbh,spbh,cj_date,price,cprice,bz) values (:scname,:czy,:as_glbh,:as_spbh,sysdate,:price,:cPrice,:bz)";

    private static final String PRICE_COLLECT_UPDATE = "update cj set price=:price , bz =:bz where spbh=:as_spbh and scname=:scname";

    private static final String PRICE_COLLECT_SELECT_COUNT = "select count(*) from cj where cj_date>=trunc(sysdate) and (price !=0) and czy = ':czy'";


    @Override
    public ResponseMessage<Object> getSpmcAndGlbh(String spbh, String scname, String czy) {
        HikariDataSource hikariDataSource = priceCollectDao.cjDataSource();
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);

            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(SqlParam.AS_SPBH.value(), spbh);
            PriceCollectModel priceCollectModel = namedParameterJdbcTemplate.queryForObject(PRICE_COLLECT_SELECT_SPMC, paramMap, (resultSet, i) -> {
                PriceCollectModel priceCollectModel1 = new PriceCollectModel();
                priceCollectModel1.setSpmc(resultSet.getString("spmc"));
                priceCollectModel1.setGlbh(resultSet.getString("glbh"));
                return priceCollectModel1;
            });

            Map<String, String> paramMapCj = new HashMap<>();
            paramMapCj.put(SqlParam.AS_SPBH.value(), spbh);
            paramMapCj.put("scname", scname);
            paramMapCj.put("czy", czy);
            List<PriceCollectModel> priceCollectModelCj = namedParameterJdbcTemplate.query(CJ_SELECT_SPBH_SCNAME_CZY, paramMapCj, (resultSet, i) -> {
                    PriceCollectModel priceCollectModelCj1 = new PriceCollectModel();
                    priceCollectModelCj1.setGlbh(resultSet.getString("glbh"));
                    priceCollectModelCj1.setBz(resultSet.getString("bz"));
                    priceCollectModelCj1.setCPrice(resultSet.getDouble("cprice"));
                    priceCollectModelCj1.setCzy(resultSet.getString("czy"));
                    priceCollectModelCj1.setPrice(resultSet.getDouble("price"));
                    priceCollectModelCj1.setScname(resultSet.getString("scname"));
                    priceCollectModelCj1.setSpbh(resultSet.getString("spbh"));

                return priceCollectModelCj1;

            });

            if(!CollectionUtils.isEmpty(priceCollectModelCj)){
                PriceCollectModel p = priceCollectModelCj.get(0);
                p.setSpmc(priceCollectModel.getSpmc());
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", p);

            }else{
                return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", priceCollectModel);
            }

        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "获取商品名称失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage saveData(PriceCollectModel priceCollectModel) {
        HikariDataSource hikariDataSource = priceCollectDao.cjDataSource();
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            //  查询该记录是否存在
            Integer returnResult = namedParameterJdbcTemplate.getJdbcOperations().queryForObject("select count(*) from cj where scname = '"+priceCollectModel.getScname()+"' and spbh = '"+priceCollectModel.getSpbh()+"'", Integer.class);

            if(returnResult == 0) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("scname", priceCollectModel.getScname());
                paramMap.put("czy", priceCollectModel.getCzy());
                paramMap.put(SqlParam.AS_GLBH.value(), priceCollectModel.getGlbh());
                paramMap.put(SqlParam.AS_SPBH.value(), priceCollectModel.getSpbh());
                BigDecimal p1 = new BigDecimal(priceCollectModel.getPrice() == null ? 0 : priceCollectModel.getPrice());
                double price = p1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                paramMap.put("price", price);
                BigDecimal p2 = new BigDecimal(priceCollectModel.getCPrice() == null ? 0 : priceCollectModel.getCPrice());
                double cPrice = p2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                paramMap.put("cPrice", cPrice);
                paramMap.put("bz", priceCollectModel.getBz());

                SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

                log.info("要保存的采价信息：{}", priceCollectModel.toString());

                Integer result = priceCollectDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, PRICE_COLLECT_INSERT);
                return (null == result) ?
                        ResponseMessage.responseMessage(ResultCode.FAIL.code(), "采价保存失败！", null) :
                        ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "采价保存成功！", null);
            }else{
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("scname", priceCollectModel.getScname());
                paramMap.put(SqlParam.AS_SPBH.value(), priceCollectModel.getSpbh());
                BigDecimal p1 = new BigDecimal(priceCollectModel.getPrice() == null ? 0 : priceCollectModel.getPrice());
                double price = p1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                paramMap.put("price", price);
                paramMap.put("bz", priceCollectModel.getBz());

                SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);

                Integer result = priceCollectDao.operateSQL(namedParameterJdbcTemplate, sqlParameterSource, hikariDataSource, PRICE_COLLECT_UPDATE);
                return (null == result) ?
                        ResponseMessage.responseMessage(ResultCode.FAIL.code(), "采价更新失败！", null) :
                        ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "采价更新成功！", null);            }

        } catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "采价保存失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }
    }

    @Override
    public ResponseMessage count(String czy) {
        if(StringUtils.isEmpty(czy)){
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "操作员为空",null);
        }
        HikariDataSource hikariDataSource = priceCollectDao.cjDataSource();
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
            Integer returnResult = namedParameterJdbcTemplate.getJdbcOperations().queryForObject(PRICE_COLLECT_SELECT_COUNT.replace(":czy",czy), Integer.class);
            return ResponseMessage.responseMessage(ResultCode.SUCCESS.code(), "", returnResult);

        }catch (Exception ex) {
            log.info(DefaultValue.DEFAULT_EXEC_PROC_ERROR.value() + "：{}", ExceptionUtils.getMessage(ex));
            return ResponseMessage.responseMessage(ResultCode.FAIL.code(), "采价统计失败！", null);
        } finally {
            if (!hikariDataSource.isClosed()) {
                log.info(DefaultValue.DEFAULT_CLOSE_DATASOURCE.value());
                hikariDataSource.close();
            }
        }


    }
}
