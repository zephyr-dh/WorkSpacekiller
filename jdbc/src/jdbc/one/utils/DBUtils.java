package jdbc.one.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtils {
	// ����һ����Ŀ��,���4������ֻ����ֵһ��
	// ʹ�þ�̬����
	private static String driver;
	private static String url;
	private static String userName;
	private static String passWord;
	// ��һ���оͿ�ʼ����,ʹ�þ�̬��
	static {
		try {
			// 1.����properties����
			Properties cfg = new Properties();
			// 2.ʹ��load����������
			// 2.1 ��ȡ�����ļ�����
			InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
			// 2.2ʹ��load����������
			cfg.load(in);
			// 3.����̬������ֵ
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
