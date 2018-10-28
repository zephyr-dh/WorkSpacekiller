package io.oacy.education.newbie.springbootnewbie.service;


import io.oacy.education.newbie.springbootnewbie.domains.Department;

import java.util.List;

public interface DepartmentService {
    void insert(Department department);

    Department getById(Integer id);

    void update(Department department);

    void deleteById(Integer id);

    List<Department> selectAll();
}
