package io.oacy.education.newbie.springbootnewbie.controller;

import io.oacy.education.newbie.springbootnewbie.domains.Department;
import io.oacy.education.newbie.springbootnewbie.domains.ResponseDomain;
import io.oacy.education.newbie.springbootnewbie.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "save",consumes ="application/json" )
    public ResponseDomain<Department> save(Department department) {
        return new ResponseDomain<>(1, "成功", departmentService.insert(department));
    }
}
