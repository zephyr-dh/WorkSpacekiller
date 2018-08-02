package jdbc.two.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBUtils {

	/**
	 * ָ�����в�����������Դ
	 * 
	 * @param url           ���ݿ�
	 * @param username      �û���
	 * @param password      ����
	 * @param driver        ���ݿ�����������
	 * @param initialSize   ��ʼ���ӳ����Ӹ���
	 * @param maxTotal      ���������
	 * @param maxIdle       ���������
	 * @param maxWaitMillis ������ӵ����ȴ�������
	 * @param minIdle       ��С������
	 * 
	 */
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static int initialSize;
	private static int maxTotal;
	private static int maxIdle;
	private static Long maxWaitMillis;
	private static int minIdle;
	// ����BasicDataSource����,��ֵΪnull
	private static BasicDataSource ds = null;
	// ��̬������ɶ�ȡ�����Ĳ���
	// ����BasicDataSource����Ĳ���

	static {
		try {
			Properties cfg = new Properties();
			InputStream inStream = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
			cfg.load(inStream);
			driver = cfg.getProperty("jdbc.driver");
			url = cfg.getProperty("jdbc.url");
			username = cfg.getProperty("jdbc.username");
			password = cfg.getProperty("jdbc.password");
			initialSize = Integer.parseInt(cfg.getProperty("jdbc.initialSize"));
			maxTotal = Integer.parseInt(cfg.getProperty("jdbc.maxTotal"));
			maxIdle = Integer.parseInt(cfg.getProperty("jdbc.maxIdle"));
			maxWaitMillis = Long.parseLong(cfg.getProperty("jdbc.maxWaitMillis"));
			minIdle = Integer.parseInt(cfg.getProperty("jdbc.maxWaitMillis"));
			// ����BasicDataSource����
			ds = new BasicDataSource();
			// ��������
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setInitialSize(initialSize);
			ds.setMaxTotal(maxTotal);
			ds.setMaxIdle(maxIdle);
			ds.setMaxWaitMillis(maxWaitMillis);
			ds.setMinIdle(minIdle);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ��ȡ���ӳ���connection����ķ���
	public static Connection getConnection() {
		try {
			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// �黹���ӳ����Ӷ���ķ���
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				// �˴�close�ǹ黹
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
