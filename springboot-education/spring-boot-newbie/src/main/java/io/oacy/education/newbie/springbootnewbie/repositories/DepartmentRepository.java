package io.oacy.education.newbie.springbootnewbie.repositories;

import io.oacy.education.newbie.springbootnewbie.domains.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, Integer> {
}
