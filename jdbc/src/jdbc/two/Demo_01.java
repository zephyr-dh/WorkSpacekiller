package jdbc.two;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jdbc.two.utils.DBUtils;


public class Demo_01 {
	/**
	 * ����һ�ű�t9,id name password ����1����,1,aaa,111 ��ѯ��
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			// 1.������Ӷ���
			conn = DBUtils.getConnection();
			// 2.����statement����,����sql
			Statement sta = conn.createStatement();
			// ����
			String ddl=
					"create table t9(id int,"
					+ "name varchar(20),"
					+ "password varchar(20))";
			//ִ��sql���
			boolean flag=sta.execute(ddl);
			System.out.println(flag);
			//����
			String sql = "insert into t9 values" + "(1,'aaa','111'),(2,'bbb','222'),(3,'ccc','333')";
			int num = sta.executeUpdate(sql);
			System.out.println(num);
			// ��ѯ
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
