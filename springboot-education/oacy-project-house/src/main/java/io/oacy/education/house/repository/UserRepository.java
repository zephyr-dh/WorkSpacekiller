package io.oacy.education.house.repository;

import io.oacy.education.house.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
}
