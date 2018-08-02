package edu.servlet.two.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.servlet.two.domain.User;
import edu.servlet.two.utils.DBUtils;

public class UserDao {
	/**
	 * 依据用户名查询对应的用户信息,
	 * 如果找不到，返回null。
	 */
	public User findByUsername(String uname){
		User user = null;
		Connection conn = null;
		try{
			conn = DBUtils.getConnection();
			String sql = "SELECT * FROM t_user "
					+ "WHERE uname=?";
			PreparedStatement ps = 
				conn.prepareStatement(sql);
			ps.setString(1, uname);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUname(uname);
				user.setPwd(rs.getString("pwd"));
				user.setPhone(
						rs.getString("phone"));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			DBUtils.closeConnection(conn);
		}
		return user;
	}
	
	
	/**
	 * 删除指定id的用户
	 */
	public void delete(int id){
		Connection conn = null;
		try{
			conn = DBUtils.getConnection();
			String sql = "DELETE FROM t_user "
					+ "WHERE id=?";
			PreparedStatement ps = 
				conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			DBUtils.closeConnection(conn);
		}
		
	}
	
	/**
	 * 插入员工信息
	 */
	public void save(User user){
		Connection conn = null;
		try{
			conn = DBUtils.getConnection();
			String sql = "INSERT INTO "
					+ "t_user(uname,"
					+ "pwd,phone) "
					+ "VALUES(?,?,?)";
			PreparedStatement ps = 
				conn.prepareStatement(sql);
			ps.setString(1, user.getUname());
			ps.setString(2, user.getPwd());
			ps.setString(3, user.getPhone());
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			DBUtils.closeConnection(conn);
		}
		
	}
	
	/**
	 * 查询出所有员工的信息
	 */
	public List<User> findAll(){
		List<User> users = 
				new ArrayList<User>();
		Connection conn = null;
		try{
			conn = DBUtils.getConnection();
			String sql ="SELECT * FROM t_user";
			PreparedStatement ps = 
				conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				int id = rs.getInt("id");
				String uname = 
						rs.getString("uname");
				String pwd = 
						rs.getString("pwd");
				String phone = 
						rs.getString("phone");
				//将记录中的数据添加到对应的实体
				//对象里面
				User user = new User();
				user.setId(id);
				user.setUname(uname);
				user.setPwd(pwd);
				user.setPhone(phone);
				
				users.add(user);
			}
		}catch(Exception e){
			//记日志（保留现场）
			e.printStackTrace();
			/*
			 * 看异常能否恢复，如果不能够恢复
			 * (发生了系统异常，比如数据库服务
			 * 停止了)，则提示用户稍后重试。
			 * 如果能够恢复，则立即恢复。
			 */
			throw new RuntimeException(e);
		}finally{
			DBUtils.closeConnection(conn);
		}
		return users;
	}
}
