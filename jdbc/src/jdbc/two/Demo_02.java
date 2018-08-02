package jdbc.two;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import jdbc.two.utils.DBUtils;

public class Demo_02 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("ÇëÊäÈëÓÃ»§Ãû");
		String name = sc.nextLine();
		System.out.println("ÇëÊäÈëÃÜÂë");
		String password = sc.nextLine();
		Demo_02 demo=new Demo_02();
		boolean flag = demo.login(name, password);
//		boolean flag=true;
		if (flag) {
			System.out.println("»¶Ó­" + name);
		} else {
			System.out.println("µÇÂ¼Ê§°Ü");
		}
		sc.close();
	}

	public boolean login(String name, String password) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
//			String sql=
//			"select count(*) from t9 "
//			+ "where name='"+name+"' and "
//					+ "password='"+password+"'";
//			System.out.println(sql);
//			Statement sta=conn.createStatement();
//			ResultSet rs=sta.executeQuery(sql);
			String sql = "select count(*) from t9" + " where name=? and password=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int n = rs.getInt(1);
				return n >= 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConnection(conn);
		}
		return false;
	}

}
