package io.oacy.education.xunwu.service;

import io.oacy.education.xunwu.domain.User;

public interface UserService {
    User findUserByName(String userName);
}
