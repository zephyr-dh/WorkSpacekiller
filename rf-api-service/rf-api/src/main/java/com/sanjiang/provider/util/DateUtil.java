package com.sanjiang.provider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by byinbo on 2018/5/15.
 */
public class DateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private DateUtil() {

    }

    /**
     * 将字符串转换为日期
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            LOG.error("给定的日期不正确，日期:{dateStr},需要的格式为：{pattern}", dateStr, pattern);
        }
        return date;
    }

    /**
     * 获取某一天的前几天或后几天
     * @param date      某个固定日期
     * @param pattern   日期格式
     * @param dayCount  前几天用-N，后几天用 N
     * @return
     */
    public static Date addDate(Date date,String pattern,int dayCount){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();

        return date;

    }
}
