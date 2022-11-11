package com.sh.member.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sh.member.model.dto.Member;


public class MemberDao {
	String driverClass = "oracle.jdbc.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "student";
	String password = "student";

	
	/**
	 * deleteMember : 데이터 삭제 함수 (DML)
	 */
	public int deleteMember(Member member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "delete from member where id = ?";
		
		try {
			// 1. 클래스 등록
			Class.forName(driverClass);
			
			// 2. Connection 생성 (autocommit false)
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			// 3. PreparedStatement 생성 + 미완성쿼리 대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			
			// 4. 실행
			result = pstmt.executeUpdate();
			
			// 5. 트랜잭션 처리 
			if(result > 0) {
				conn.commit();
			}
			else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		return result;
	}


	/**
	 * findByName (DQL)
	 * - 사용자 입력받은 이름으로 검색 (일부만 입력해도 포함된 여러 행 반환)
	 */
	public List<Member> findByName(String searchName) { 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where name like '%' || ? || '%'";
		// String names = '%' + searchName + '%'; 변수처리도 가능 
		List<Member> members = new ArrayList<>(); 
		
		try {// 1. 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성
			conn = DriverManager.getConnection(url, user, password);
			
			// 3. Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchName); 
			
			// 4. 쿼리실행 + 미완성쿼리 대입
			rset = pstmt.executeQuery();
			
			while(rset.next()) { // 여러 행이 리턴 
				String id = rset.getString("id"); 
				String name = rset.getString("name");
				String gender = rset.getString("gender");
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				int point = rset.getInt("point");
				Timestamp regDate = rset.getTimestamp("reg_date");

				Member member = new Member(id, name, gender, birthday, email, point, regDate);
				members.add(member);	
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		// 5. 자원반납
		} finally {
			try {
				rset.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return members;
	}


	/**
	 * 이름변경
	 * @param member
	 * @return
	 */
	public int updateName(Member member, String newName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update member set name = ? where id = ?";
		
		try {
			// 1. 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성 + auto-commit 설정
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			// 3.PreparedStatement 생성 + 대입
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, newName);
			pstmt.setString(2, member.getId());
			
			// 4. 실행
			result = pstmt.executeUpdate();
			
			// 5. 트랜잭션 처리 
			if(result > 0) { 
				conn.commit();
			}
			else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 생일변경
	 * @param member
	 * @return
	 */
	public int updateBirth(Member member, Date newBday) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update member set birthday = ? where id = ?";
		
		try {
			// 1. 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성 + auto-commit 설정
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			// 3.PreparedStatement 생성 + 대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, newBday);
			pstmt.setString(2, member.getId());
			
			// 4. 실행
			result = pstmt.executeUpdate();
			
			// 5. 트랜잭션 처리 
			if(result > 0) { 
				conn.commit();
			}
			else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return result;
	}

	/**
	 * 이메일변경
	 * @param member
	 * @return
	 */
	public int updateEmail(Member member, String newMail) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update member set email = ? where id = ?";
		
		try {
			// 1. 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성 + auto-commit 설정
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			// 3.PreparedStatement 생성 + 대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newMail);
			pstmt.setString(2, member.getId());
			
			// 4. 실행
			result = pstmt.executeUpdate();
			
			// 5. 트랜잭션 처리 
			if(result > 0) { 
				conn.commit();
			}
			else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return result;
	}

}


