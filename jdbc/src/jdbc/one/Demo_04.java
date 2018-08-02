package jdbc.one;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Demo_04 {
	/**
	 * ��ȡdb.properties�е�4���ַ���
	 * @throws IOException 
	 */
	public static void main(String[] args){
		//Properties����Ϊ�˴���*.properties�ļ�
		//�����api
		//���ĵײ����ı��ļ�io
		//Properties����ʵ����map�ӿ�
		//�ڲ��洢�ṹ��ɢ�б�
		//�޶�key��value���붼��String
		//1.����Properties����
		Properties cfg=new Properties();
		//2.ʹ��load������ȡ�����ļ�
		//2.1׼�������ļ�������
		InputStream in=Demo_04.class.getClassLoader()
				.getResourceAsStream("db.properties");
		//2.2load������ȡ��
		try {
			cfg.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//3.��ȡÿһ��value��ֵ
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
