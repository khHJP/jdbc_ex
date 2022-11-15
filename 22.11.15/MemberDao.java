package com.sh.member.model.dao;

import static com.sh.member.common.JdbcTemplate.close;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sh.member.model.dto.DeletedMember;
import com.sh.member.model.dto.Member;

public class MemberDao {

	/**
	 * DML 회원가입
	 */
	public int insertMember(Connection conn, Member member) { 
		PreparedStatement pstmt = null;
		String sql = "insert into member values (?, ?, ?, ?, ?, default, default)";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getGender());
			pstmt.setDate(4, member.getBirthday());
			pstmt.setString(5, member.getEmail());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e); 			
		} finally {
			close(pstmt); 
		}		
		return result;
	}
	

	/**
	 * DQL 이름검색
	 */
	public List<Member> findByName(Connection conn, String name) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where name like ?";
		List<Member> members = new ArrayList<>(); 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + name + "%");
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				members.add(new Member(
						rset.getString("id"),
						rset.getString("name"),
						rset.getString("gender"),
						rset.getDate("birthday"),
						rset.getString("email"),
						rset.getInt("point"),
						rset.getTimestamp("reg_date"))); 
			}
						
		} catch (Exception e) {
			throw new RuntimeException(e); 
		} finally {
			close(rset);
			close(pstmt);
		}
		return members;
	}

	/**
	 * DQL 전체회원조회
	 */
	public List<Member> findAll(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member";
		List<Member> members = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				members.add(new Member(
						rset.getString("id"),
						rset.getString("name"),
						rset.getString("gender"),
						rset.getDate("birthday"),
						rset.getString("email"),
						rset.getInt("point"),
						rset.getTimestamp("reg_date")));
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rset);
			close(pstmt);
		}

		return members;
	}


	/**
	 * DQL 아이디 조회
	 */
	public Member findById(Connection conn, String id) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where id = ?";
		Member member = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				member = new Member(
						rset.getString("id"), 
						rset.getString("name"),
						rset.getString("gender"),
						rset.getDate("birthday"),
						rset.getString("email"),
						rset.getInt("point"),
						rset.getTimestamp("reg_date"));
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return member;
	}

	/**
	 * DML 이름 수정 
	 */
	public int updateMemberName(Connection conn, String id, String newName) {
		PreparedStatement pstmt = null;
		String sql = "update member set name = ? where id = ?";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newName);
			pstmt.setString(2, id);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e); 
			
		} finally {
			close(pstmt); 
		}		
		
		return result;
	}

	/**
	 * DML 생일 수정 
	 */
	public int updateMemberBirthday(Connection conn, String id, Date newBirthday) {
		PreparedStatement pstmt = null;
		String sql = "update member set birthday = ? where id = ?";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, newBirthday);
			pstmt.setString(2, id);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * DML 이메일 수정 
	 */
	public int updateMemberEmail(Connection conn, String id, String newEmail) {
		PreparedStatement pstmt = null;
		String sql = "update member set email = ? where id = ?";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newEmail);
			pstmt.setString(2, id);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * DML 회원탈퇴
	 */
	public int deleteMember(Connection conn, String id) {
		PreparedStatement pstmt = null;
		String sql = "delete from member where id = ?";
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * DQL 탈퇴회원조회
	 */
	public List<DeletedMember> findDeletedMember(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member_del";
		List<DeletedMember> delMembers = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				delMembers.add(new DeletedMember(
						rset.getString("id"),
						rset.getString("name"),
						rset.getString("gender"),
						rset.getDate("birthday"),
						rset.getString("email"),
						rset.getInt("point"),
						rset.getTimestamp("reg_date"),
						rset.getTimestamp("del_date")));
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return delMembers;
	}

}
