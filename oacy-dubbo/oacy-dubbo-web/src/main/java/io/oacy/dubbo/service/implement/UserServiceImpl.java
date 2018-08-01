package io.oacy.dubbo.service.implement;

import org.springframework.beans.factory.annotation.Autowired;

import io.oacy.dubbo.service.domain.User;
import io.oacy.dubbo.service.rpc.RPCResponse;
import io.oacy.dubbo.service.service.UserService;
import io.oacy.dubbo.web.UserDao;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public RPCResponse<User> findUserById(Long id) {
		//����ʹ�÷�װ��,������DAO����ʹ�÷�װ��,��Ϊ��DAO����õĸ�������
		RPCResponse<User> response = new RPCResponse<User>();
		try {
			//DAO�������֮ǰ������ģʽһ����û��ʹ�÷�װ��
			User result = userDao.findUserById(id);
			response.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage());
		}
		
		return response;
	}

	public RPCResponse<User> findUserThrowsException() {
		RPCResponse<User> response = new RPCResponse<User>();
		try {
			User result = userDao.findUserThrowsException();
			response.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}



}
