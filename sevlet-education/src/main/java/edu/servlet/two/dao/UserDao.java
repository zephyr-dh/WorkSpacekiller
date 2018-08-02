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
	 * �����û�����ѯ��Ӧ���û���Ϣ,
	 * ����Ҳ���������null��
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
	 * ɾ��ָ��id���û�
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
	 * ����Ա����Ϣ
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
	 * ��ѯ������Ա������Ϣ
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
				//����¼�е�������ӵ���Ӧ��ʵ��
				//��������
				User user = new User();
				user.setId(id);
				user.setUname(uname);
				user.setPwd(pwd);
				user.setPhone(phone);
				
				users.add(user);
			}
		}catch(Exception e){
			//����־�������ֳ���
			e.printStackTrace();
			/*
			 * ���쳣�ܷ�ָ���������ܹ��ָ�
			 * (������ϵͳ�쳣���������ݿ����
			 * ֹͣ��)������ʾ�û��Ժ����ԡ�
			 * ����ܹ��ָ����������ָ���
			 */
			throw new RuntimeException(e);
		}finally{
			DBUtils.closeConnection(conn);
		}
		return users;
	}
}
