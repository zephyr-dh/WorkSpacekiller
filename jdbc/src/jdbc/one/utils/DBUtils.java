package jdbc.one.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtils {
	// 由于一个项目中,这个4个变量只被赋值一次
	// 使用静态变量
	private static String driver;
	private static String url;
	private static String userName;
	private static String passWord;
	// 在一运行就开始加载,使用静态块
	static {
		try {
			// 1.创建properties对象
			Properties cfg = new Properties();
			// 2.使用load方法加载流
			// 2.1 获取配置文件的流
			InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
			// 2.2使用load方法加载流
			cfg.load(in);
			// 3.给静态变量赋值
			driver = cfg.getProperty("jdbc.driver");
			url = cfg.getProperty("jdbc.url");
			userName = cfg.getProperty("jdbc.username");
			passWord = cfg.getProperty("jdbc.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws Exception {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, userName, passWord);
		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
		}
	}
}
