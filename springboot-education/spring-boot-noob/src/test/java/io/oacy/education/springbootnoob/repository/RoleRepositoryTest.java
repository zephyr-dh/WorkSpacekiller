package io.oacy.education.springbootnoob.repository;


import io.oacy.education.springbootnoob.domains.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("development")
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testInsert() {
        Role role = new Role();
        role.setName("管理员");
        role.setDescr("测试");
        Role result = this.roleRepository.save(role);
        System.out.println(result);
    }

    @Test
    public void testFindOne() {
        Optional<Role> role = this.roleRepository.findById(1);
        System.out.println(role.get());
    }

    @Test
    public void testUpdate() {
        Role role = new Role();
        role.setId(1);
        role.setName("管理员");
        role.setDescr("控制权限");
        Role result = this.roleRepository.save(role);
        System.out.println(result);
    }

    @Test
    public void testDelete() {
        this.roleRepository.deleteById(1);
    }
}