package io.oacy.education.newbie.springbootnewbie.repositories.mapper;

import io.oacy.education.newbie.springbootnewbie.domains.Department;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Insert("insert into department(id,name,descr) values(#{id},#{name},#{descr})")
    @Options(useGeneratedKeys = true)
    void insert(Department department) throws Exception;

    @Select("select id,name,descr from department where id = #{id}")
    Department getById(@Param("id") Integer id) throws Exception;

    @Update("update department set descr = #{descr} where id = #{id}")
    void update(Department department);

    @Update("delete from department where id = #{id}")
    void deleteById(@Param("id") Integer id) throws Exception;


    @Select("select * from department")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "name"),
            @Result(column = "descr", property = "descr")})
    List<Department> selectAll() throws Exception;
}
