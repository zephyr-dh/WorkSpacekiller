package io.oacy.education.newbie.springcloudnewbieuserapi.service.implemented;

import io.oacy.education.newbie.springcloudnewbieuserapi.domain.User;
import io.oacy.education.newbie.springcloudnewbieuserapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements UserService {

    private static Map<Integer,User> map;

    static {
        map = new HashMap<>();
        IntStream.range(1, 6).forEach(i -> map.put(i, new User(i, "test" + i, "pwd" + i)));
    }

    @Override
    public User getById(Integer id) {
        return map.get(id);
    }

}
