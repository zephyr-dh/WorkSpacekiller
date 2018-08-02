package jdbc.one;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jdbc.one.utils.DBUtils;

public class Demo_05 {
	public static void main(String[] args) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from demo2";
			Statement sta = conn.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				System.out.println(id + "~~" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConnection(conn);
		}
	}
}
