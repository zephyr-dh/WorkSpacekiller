package io.oacy.dubbo.service.service;

import io.oacy.dubbo.service.domain.User;
import io.oacy.dubbo.service.rpc.RPCResponse;

public interface UserService {
	RPCResponse<User> findUserById(Long id);
	RPCResponse<User> findUserThrowsException();
}
