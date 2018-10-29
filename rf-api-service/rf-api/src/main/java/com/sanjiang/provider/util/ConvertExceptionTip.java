package com.sanjiang.provider.util;

/**
 * Created by byinbo on 2018/6/5.
 */
public class ConvertExceptionTip {

    public static String convert(String str){
//        String ss = "UncategorizedSQLException: CallableStatementCallback; uncategorized SQLException for SQL []; SQL state [72000]; error code [20001]; ORA-20001: 23423出车号或配送部门输入错误，不存在该信息\nORA-06512: at \"SJ.PKG_YJD\", line 67\nORA-06512: at \"SJ.PKG_YJD\", line 145\nORA-06512: at line 1\n; nested exception is java.sql.SQLException: ORA-20001: 23423出车号或配送部门输入错误，不存在该信息\nORA-06512: at \"SJ.PKG_YJD\", line 67\nORA-06512: at \"SJ.PKG_YJD\", line 145\nORA-06512: at line 1\n";
//        String ss = "UncategorizedSQLException: CallableStatementCallback; uncategorized SQLException for SQL []; SQL state [72000]; error code [20000]; ORA-20000: 45435门店号错误或不在当前服务器上\nORA-06512: at \"SJ.P_SCBH\", line 9\nORA-06512: at \"SJ.PKG_YJD\", line 20\nORA-06512: at line 1\n; nested exception is java.sql.SQLException: ORA-20000: 45435门店号错误或不在当前服务器上\nORA-06512: at \"SJ.P_SCBH\", line 9\nORA-06512: at \"SJ.PKG_YJD\", line 20\nORA-06512: at line 1\n";
        String[] data = str.split("ORA");
        if(data.length>1){
            return "ORA"+data[1];
        }else{
            return "失败";
        }
    }
}
