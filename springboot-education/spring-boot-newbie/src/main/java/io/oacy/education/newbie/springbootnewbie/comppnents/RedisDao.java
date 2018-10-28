package io.oacy.education.newbie.springbootnewbie.comppnents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisDao {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public void set(String key, String value) {
        /*
        opsForValue()	操作简单属性的数据
         */
        this.stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        this.stringRedisTemplate.delete(key);
    }

}
