package jdbc.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Demo_01 {

	public static void main(String[] args) throws Exception {
		//1.ע������
		Class.forName("com.mysql.jdbc.Driver");
		//2.���Connection����
		
		//url jdbc:mysql://���ݿ�IP:�˿ں�/����
		//username ���ݿ��û���
		//password ���ݿ�����
		
		Connection conn=DriverManager
			.getConnection(
			"jdbc:mysql://db4free.net:3306/oacy_zephyr", 
			"oacy_zephyr", "oacy_zephyr");
		//��֤�Ƿ����ӳɹ�
		System.out.println(conn);
		//֤���ӿ�ʵ������������
		System.out.println(conn.getClass());
		
		//3. ��ȡstatement����
		Statement sta=conn.createStatement();
		System.out.println(sta);
		
		//���Դ����κ�sql���,������ִ��DDL��DCL
		//sta.execute(sql);
		
		//ִ��DML���,insert,update,delete
		//sta.executeUpdate(sql)
		
		//ִ��DQL,����ֵresultSet
		//sta.executeQuery(sql)
		
		String ddl="create table demo_1("
				+ "id int,"
				+ "name varchar(20))";
		boolean flag=sta.execute(ddl);
		//false����û�з��ؽ����
		//true�����з��ؽ����
		System.out.println(flag);
		
		//������� DML
		//String dml="insert into demo_1 "
		//		+ " values(1,'hanmeimei')";
		//sta.executeUpdate(dml);
		
		//��ѯ
		String dql="select 'hello' as a "
				+ "from dual";
		//ʹ��execute(dql)ִ�в�ѯ���
		//boolean flag=sta.execute(dql);
		//System.out.println(flag);
		
		ResultSet rs=sta.executeQuery(dql);
		while(rs.next()){
			String str=rs.getString("a");
			System.out.println(str);
		}
	}
}


