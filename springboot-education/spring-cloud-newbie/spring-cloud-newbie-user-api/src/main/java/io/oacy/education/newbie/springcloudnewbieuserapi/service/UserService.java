package io.oacy.education.newbie.springcloudnewbieuserapi.service;

import io.oacy.education.newbie.springcloudnewbieuserapi.domain.User;

public interface UserService {
    User getById(Integer id);
}
