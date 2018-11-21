package io.oacy.education.xunwu.repository;

import io.oacy.education.xunwu.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role,Long> {
    List<Role> findRolesByUserId(Long id);
}
