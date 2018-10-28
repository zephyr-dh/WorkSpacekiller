package io.oacy.education.newbie.springbootnewbie.service.implemented;

import io.oacy.education.newbie.springbootnewbie.domains.Department;
import io.oacy.education.newbie.springbootnewbie.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("development")
@Slf4j
public class DepartmentServiceImplTest {

    @Autowired
    DepartmentService departmentService;

    @Test
    public void insert() {
        Department department=new Department(null,"Tom","信息部");
        departmentService.insert(department);

    }

    @Test
    public void getById() {
        Department department=departmentService.getById(1);
        log.info(department.getDescr());
    }

    @Test
    public void update() {

    }

    @Test
    public void deleteById() {
    }
}