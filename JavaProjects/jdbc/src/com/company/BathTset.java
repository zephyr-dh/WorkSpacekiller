package com.company;

import org.junit.Test;

import java.sql.*;


/**
 * Created by wujianlong on 2017/3/10.
 */
public class BathTset {

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Savepoint savepoint1=null;
        CallableStatement cstmt = null;

        try{
            conn = DriverManager.getConnection("jdbc:oracle:thin:@193.0.10.53:1521:sjos", "jddj", "jddj");
            conn.setAutoCommit(false);
            savepoint1 = conn.setSavepoint("Savepoint1");
            String procedure = "{call jdbc_test(?,?)}";
            cstmt = conn.prepareCall(procedure);

            long startTime = System.currentTimeMillis();

            for(int i=0;i<100;i++) {
                cstmt.setString(1, "testkey"+i);
                cstmt.setString(2, "testval"+i);
                //cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
                cstmt.addBatch();
            }
            int [] updateCounts = cstmt.executeBatch();
            conn.commit();
            System.out.printf( (System.currentTimeMillis() - startTime)+"毫秒");
        }
        catch (Exception ex){
            conn.rollback(savepoint1);
        }



    }
   @Test
    public void tesst(){

        System.out.printf("111");
    }



}
