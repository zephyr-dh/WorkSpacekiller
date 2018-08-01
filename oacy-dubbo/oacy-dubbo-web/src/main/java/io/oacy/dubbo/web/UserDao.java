package io.oacy.dubbo.web;

import io.oacy.dubbo.service.domain.User;

public interface UserDao {

	public User findUserById(long id);

	public User findUserThrowsException();

}
