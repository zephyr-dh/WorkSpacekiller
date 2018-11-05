package com.company;

import java.sql.*;

public class Main {



    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Savepoint savepoint1=null;
        try{
            conn = DriverManager.getConnection("jdbc:oracle:thin:@193.0.10.53:1521:sjos", "jddj", "jddj");
            //Assume a valid connection object conn
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            //set a Savepoint
            savepoint1 = conn.setSavepoint("Savepoint1");
            String SQL = "INSERT INTO JDBCTEST " +
                    "VALUES ('Rita', 'Tez')";
            int a=stmt.executeUpdate(SQL);

            //Submit a malformed SQL statement that breaks
             SQL = "INSERT INTOee JDBCTEST " +
                    "VALUES ('Sita', 'Tez')";
            stmt.executeUpdate(SQL);

            // If there is no error, commit the changes.
            conn.commit();

        }catch(SQLException se){
            // If there is any error.
            conn.rollback(savepoint1);

        }



    }
}
