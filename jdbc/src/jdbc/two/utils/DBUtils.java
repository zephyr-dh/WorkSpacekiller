package jdbc.two.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBUtils {

	/**
	 * 指定所有参数连接数据源
	 * 
	 * @param url           数据库
	 * @param username      用户名
	 * @param password      密码
	 * @param driver        数据库连接驱动名
	 * @param initialSize   初始连接池连接个数
	 * @param maxTotal      最大活动连接数
	 * @param maxIdle       最大连接数
	 * @param maxWaitMillis 获得连接的最大等待毫秒数
	 * @param minIdle       最小连接数
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
	// 声明BasicDataSource对象,赋值为null
	private static BasicDataSource ds = null;
	// 静态块中完成读取参数的操作
	// 创建BasicDataSource对象的操作

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
			// 创建BasicDataSource对象
			ds = new BasicDataSource();
			// 设置属性
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

	// 获取连接池中connection对象的方法
	public static Connection getConnection() {
		try {
			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 归还连接池连接对象的方法
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				// 此处close是归还
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
