package io.oacy.education.newbie.springbootnewbie.service.implemented;

import io.oacy.education.newbie.springbootnewbie.domains.Department;
import io.oacy.education.newbie.springbootnewbie.repositories.mapper.DepartmentMapper;
import io.oacy.education.newbie.springbootnewbie.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    public void insert(Department department) {
        try {
            departmentMapper.insert(department);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Department getById(Integer id) {
        try {
            return departmentMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Department department) {
        departmentMapper.update(department);
    }

    @Override
    public void deleteById(Integer id) {
        try {
            departmentMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Department> selectAll() {
        try {
            return departmentMapper.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
