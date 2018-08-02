package jdbc.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Demo_02 {
	public static void main(String[] args) throws Exception {
		//1.ע������
		Class.forName("com.mysql.jdbc.Driver");
		//2.������Ӷ���
		Connection conn=DriverManager
				.getConnection(
				"jdbc:mysql://db4free.net:3306/oacy_zephyr", 
				"oacy_zephyr", 
				"oacy_zephyr");
			//��֤�Ƿ����ӳɹ�
		System.out.println(conn);
		
		//3.ͨ��conn����statement����
		Statement sta=conn.createStatement();
		//3.1������
		String sql1="create table demo2("
				+ "id int,"
				+ "name varchar(20))";
		sta.execute(sql1);
		//3.2��������
		String sql2="insert into demo2 "
				+ "values (1,'aaa')";
		String sql3="insert into demo2 "
				+ "values (2,'bbb'),(3,'ccc')";
		int n1=sta.executeUpdate(sql2);
		System.out.println(n1);
		int n2=sta.executeUpdate(sql3);
		System.out.println(n2);
		//3.3��ѯ
		String sql4="select * from demo2";
		ResultSet rs=sta.executeQuery(sql4);
		while(rs.next()){
			int id=rs.getInt("id");
			String name=rs.getString("name");
			System.out.println(id+"---"+name);
		}
		
		//4.�ر����Ӷ���
		conn.close();
	}
}
