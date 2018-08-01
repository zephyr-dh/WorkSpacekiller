package io.oacy.dubbo.web.implement;

import org.springframework.stereotype.Repository;

import io.oacy.dubbo.service.domain.User;
import io.oacy.dubbo.web.UserDao;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	public User findUserById(long id) {
		User info = new User();
		info.setId(id);
		info.setEmail("xxxxxxxxxx@163.com");
		info.setMobile("13844445555");
		info.setUsername("������˧");
		info.setPassword("12345600");
		return info;
	}

	public User findUserThrowsException() {
		// �ó���������ڷ��ز���
		int i = 1 / 0;
		System.out.println(i);
		return null;
	}

}
