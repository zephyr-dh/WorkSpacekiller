package io.oacy.education.springbootnoob.repository.implemented;

import io.oacy.education.springbootnoob.domains.User;
import io.oacy.education.springbootnoob.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@Repository
public class UserRepositoryImplemented implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int insert(User user) {
        String sql = "insert into user(id,username,password,birthday) values(?,?,?,?)";
        return this.jdbcTemplate.update(
                sql,
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getBirthday()
        );
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "delete from user where id = ?";
        return this.jdbcTemplate.update(sql,id);
    }

    @Override
    public int update(User user) {
        String sql = "update user set password = ? where id = ?";
        return this.jdbcTemplate.update(
                sql,
                user.getPassword(),
                user.getId()
        );
    }

    @Override
    public User getById(Integer id) {
        String sql = "select * from user where id = ?";
        return this.jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setBirthday(rs.getDate("birthday"));
            return user;
        }, id);
    }
}
