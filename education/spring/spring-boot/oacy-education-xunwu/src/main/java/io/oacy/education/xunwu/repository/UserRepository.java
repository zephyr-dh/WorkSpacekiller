package io.oacy.education.xunwu.repository;

import io.oacy.education.xunwu.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByName(String userName);
}
