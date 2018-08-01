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
		//这里使用封装类,而不是DAO继续使用封装类,是为了DAO层更好的复用起来
		RPCResponse<User> response = new RPCResponse<User>();
		try {
			//DAO层和我们之前开发的模式一样，没有使用封装类
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
