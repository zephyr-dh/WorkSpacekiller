package io.oacy.education.newbie.springbootnewbie.service.implemented;

import io.oacy.education.newbie.springbootnewbie.domains.Department;
import io.oacy.education.newbie.springbootnewbie.repositories.mapper.DepartmentMapper;
import io.oacy.education.newbie.springbootnewbie.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "department")
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    @CachePut(key = "#department.id")
    public Department insert(Department department) {
        try {
            departmentMapper.insert(department);
            return department;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    @CachePut(key = "#department.id")
    public Department update(Department department) {
        departmentMapper.update(department);
        return department;
    }

    @Override
    @Cacheable(key = "#id")
    public void deleteById(Integer id) {
        try {
            departmentMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(key = "#id")
    public List<Department> selectAll() {
        try {
            return departmentMapper.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
