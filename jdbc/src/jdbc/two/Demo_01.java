package jdbc.two;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jdbc.two.utils.DBUtils;


public class Demo_01 {
	/**
	 * 创建一张表t9,id name password 插入1数据,1,aaa,111 查询表
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			// 1.获得连接对象
			conn = DBUtils.getConnection();
			// 2.创建statement对象,操作sql
			Statement sta = conn.createStatement();
			// 建表
			String ddl=
					"create table t9(id int,"
					+ "name varchar(20),"
					+ "password varchar(20))";
			//执行sql语句
			boolean flag=sta.execute(ddl);
			System.out.println(flag);
			//插入
			String sql = "insert into t9 values" + "(1,'aaa','111'),(2,'bbb','222'),(3,'ccc','333')";
			int num = sta.executeUpdate(sql);
			System.out.println(num);
			// 查询
			String dql = "select * from t9";
			ResultSet rs = sta.executeQuery(dql);
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				System.out.println(id + "~" + name + "~" + password);
			}
			DBUtils.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			DBUtils.closeConnection(conn);
		} 

	}
}
