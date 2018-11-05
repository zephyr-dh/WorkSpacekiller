package io.oacy.education.house.repository;

import io.oacy.education.house.ApplicationTests;
import io.oacy.education.house.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserRepositoryTest extends ApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void findOne(){
        Optional<User> user=userRepository.findById(1L);
        Assert.assertEquals("waliwali",user.get().getName());
    }
}