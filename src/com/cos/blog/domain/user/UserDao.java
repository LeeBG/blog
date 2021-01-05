package com.cos.blog.domain.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cos.blog.config.DB;
import com.cos.blog.domain.user.dto.JoinReqDto;
import com.cos.blog.domain.user.dto.LoginReqDto;

public class UserDao {
	public User findByUsernameAndPassword(LoginReqDto dto) {
		String sql = "SELECT id,username,email,address from user WHERE username = ? AND password = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			rs=pstmt.executeQuery();
			
			if(rs.next()) { //참이면
				User user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.email(rs.getString("email"))
						.address(rs.getString("address"))
						.build();
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DB.close(conn,pstmt,rs);
		}
		return null;
	}
	
	public int findByUsername(String username) {
		String sql = "SELECT * from user WHERE username = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs=pstmt.executeQuery();
			
			if(rs.next()) { //참이면
				return 1;	//있어
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DB.close(conn,pstmt,rs);
		}
		return -1;
	}
	
	public int save(JoinReqDto dto) { //회원가입
		String sql = "INSERT INTO user(username,password,email,address,userRole,createDate) VALUES(?,?,?,?,'USER',now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getAddress());
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt);
		}
		return -1;
	}
	public void update() { //회원수정
		
	}
	public void usernameCheck() { //아이디 중복 체크
		
	}
	public void findById() {//아이디로 찾기
		
	}
}
