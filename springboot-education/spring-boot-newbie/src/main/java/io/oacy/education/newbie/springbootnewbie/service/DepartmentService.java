package io.oacy.education.newbie.springbootnewbie.service;


import io.oacy.education.newbie.springbootnewbie.domains.Department;

import java.util.List;

public interface DepartmentService {

    Department insert(Department department);

    Department getById(Integer id);

    Department update(Department department);

    void deleteById(Integer id);

    List<Department> selectAll();

}
