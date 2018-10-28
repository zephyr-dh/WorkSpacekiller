package io.oacy.education.springbootnoob.repository.implemented;

import io.oacy.education.springbootnoob.domains.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;



@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("development")
public class UserRepositoryImplementedTest {

    @Autowired
    private UserRepositoryImplemented userRepositoryImplemented;

    @Test
    public void insert() {
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        user.setPassword("zhangsan");
        user.setBirthday(new Date());

        int result = this.userRepositoryImplemented.insert(user);
        Assert.assertEquals(1,result);

    }

    @Test
    public void deleteById() {
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(1);
        user.setPassword("zhangsan123");
        int result = this.userRepositoryImplemented.update(user);
        Assert.assertEquals(1,result);
    }

    @Test
    public void getById() {
        User user = this.userRepositoryImplemented.getById(1);
        Assert.assertNotNull(user);
    }
}