package jdbc.one;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jdbc.one.utils.DBUtils;

public class Demo_03 {
	public static void main(String[] args) throws Exception {
		// ���÷�װ��getConnection����������Ӷ���
		Connection conn = DBUtils.getConnection();
		String sql = "select * from demo2";
		// ��ȡstatement����
		Statement sta = conn.createStatement();
		ResultSet rs = sta.executeQuery(sql);
		while (rs.next()) {
			int id = rs.getInt(1);
			String str = rs.getString(2);
			System.out.println(id);
			System.out.println(str);
		}
		// ���÷�װ�Ĺر����Ӷ���ķ���
		DBUtils.closeConnection(conn);
	}
}
