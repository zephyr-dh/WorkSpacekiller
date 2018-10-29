package com.sanjiang.provider.util;

/**
 * 商品工具类
 *
 * @author kimiyu
 * @date 2018/4/19 14:21
 */
public class ProductUtil {


    private ProductUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 判断SPBM是否为生鲜
     *
     * @param spbm
     * @return
     */
    public static boolean checkFreshSPBM(String spbm) {

        return "21".equalsIgnoreCase(spbm.substring(0, 2)) && spbm.length() <= 13;
    }
}
