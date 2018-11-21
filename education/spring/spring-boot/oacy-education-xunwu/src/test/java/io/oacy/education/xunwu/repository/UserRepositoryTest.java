package io.oacy.education.xunwu.repository;

import io.oacy.education.xunwu.ApplicationTests;
import io.oacy.education.xunwu.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserRepositoryTest extends ApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindOne(){
        User user=userRepository.findOne(1L);
        Assert.assertEquals("wali",user.getName());
    }
}