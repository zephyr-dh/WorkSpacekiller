package io.oacy.education.springbootnoob.repository;

import io.oacy.education.springbootnoob.domains.User;

public interface UserRepository {

    int insert(User user);

    int deleteById(Integer id);

    int update(User user);

    User getById(Integer id);

}
