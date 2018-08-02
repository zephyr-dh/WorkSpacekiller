package edu.servlet.two.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBUtils {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static int initialSize;
	private static int maxTotal;
	private static int maxIdle;
	private static Long maxWaitMillis;
	private static int minIdle;
	private static BasicDataSource bs = null;

	static {
		Properties cfg = new Properties();
		InputStream inStream = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
		try {
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
			bs=new BasicDataSource();
			bs.setDriverClassName(driver);
			bs.setUrl(url);
			bs.setUsername(username);
			bs.setPassword(password);
			bs.setInitialSize(initialSize);
			bs.setMaxTotal(maxTotal);
			bs.setMaxIdle(maxIdle);
			bs.setMaxWaitMillis(maxWaitMillis);
			bs.setMinIdle(minIdle);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			return bs.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
