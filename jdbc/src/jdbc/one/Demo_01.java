package jdbc.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Demo_01 {

	public static void main(String[] args) throws Exception {
		//1.注册驱动
		Class.forName("com.mysql.jdbc.Driver");
		//2.获得Connection对象
		
		//url jdbc:mysql://数据库IP:端口号/库名
		//username 数据库用户名
		//password 数据库密码
		
		Connection conn=DriverManager
			.getConnection(
			"jdbc:mysql://db4free.net:3306/oacy_zephyr", 
			"oacy_zephyr", "oacy_zephyr");
		//验证是否连接成功
		System.out.println(conn);
		//证明接口实现类来自驱动
		System.out.println(conn.getClass());
		
		//3. 获取statement对象
		Statement sta=conn.createStatement();
		System.out.println(sta);
		
		//可以处理任何sql语句,常用语执行DDL和DCL
		//sta.execute(sql);
		
		//执行DML语句,insert,update,delete
		//sta.executeUpdate(sql)
		
		//执行DQL,返回值resultSet
		//sta.executeQuery(sql)
		
		String ddl="create table demo_1("
				+ "id int,"
				+ "name varchar(20))";
		boolean flag=sta.execute(ddl);
		//false代表没有返回结果集
		//true代表有返回结果集
		System.out.println(flag);
		
		//插入操作 DML
		//String dml="insert into demo_1 "
		//		+ " values(1,'hanmeimei')";
		//sta.executeUpdate(dml);
		
		//查询
		String dql="select 'hello' as a "
				+ "from dual";
		//使用execute(dql)执行查询语句
		//boolean flag=sta.execute(dql);
		//System.out.println(flag);
		
		ResultSet rs=sta.executeQuery(dql);
		while(rs.next()){
			String str=rs.getString("a");
			System.out.println(str);
		}
	}
}


