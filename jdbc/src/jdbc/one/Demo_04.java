package jdbc.one;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Demo_04 {
	/**
	 * 读取db.properties中的4个字符串
	 * @throws IOException 
	 */
	public static void main(String[] args){
		//Properties就是为了处理*.properties文件
		//而设计api
		//它的底层是文本文件io
		//Properties本身实现了map接口
		//内部存储结构是散列表
		//限定key和value必须都是String
		//1.创建Properties对象
		Properties cfg=new Properties();
		//2.使用load方法读取配置文件
		//2.1准备配置文件生成流
		InputStream in=Demo_04.class.getClassLoader()
				.getResourceAsStream("db.properties");
		//2.2load方法读取流
		try {
			cfg.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//3.获取每一个value的值
		String driver=cfg
				.getProperty("jdbc.driver");
		String url=cfg.getProperty("jdbc.url");
		String username=cfg.getProperty("jdbc.username");
		String password=cfg.getProperty("jdbc.password");
		System.out.println(driver);
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
	}
}
