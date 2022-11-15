package com.sh.member.model.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.sh.member.common.JdbcTemplate.*;

import com.sh.member.model.dao.MemberDao;
import com.sh.member.model.dto.DeletedMember;
import com.sh.member.model.dto.Member;

public class MemberService {


	private MemberDao memberDao = new MemberDao();	
	String driverClass = "oracle.jdbc.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "student";
	String password = "student";

	/**
	 * DML 회원가입 
	 */
	public int insertMember(Member member) {
		int result = 0;
		Connection conn = getConnection();
			
		try{ 
			result = memberDao.insertMember(conn, member); 
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return result;
	}


	public List<Member> findByName(String name) {
		Connection conn = getConnection();
		List<Member> members = memberDao.findByName(conn, name); 
		close(conn);
		return members;
	}

	/**
	 * DQL 전체회원 조회
	 * 
	 */
	public List<Member> findAll() {
		Connection conn = getConnection();
		List<Member> members = memberDao.findAll(conn);
		close(conn);
		return members;
	}

	/**
	 * DQL 아이디 조회
	 */
	public Member findById(String id) {
		Connection conn = getConnection();
		Member member = memberDao.findById(conn, id);
		close(conn);		
		return member;
	}

	/**
	 * DML 이름변경 
	 */
	public int updateMemberName(String id, String newName) {
		int result = 0;
		Connection conn = getConnection();
		try{ 
			result = memberDao.updateMemberName(conn, id, newName); 
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return result;
	}

	/**
	 * DML 생일변경 
	 */
	public int updateMemberBirthday(String id, Date newBirthday) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = memberDao.updateMemberBirthday(conn, id, newBirthday);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return result;
	}

	/**
	 * DML 이메일변경 
	 */
	public int updateMemberEmail(String id, String newEmail) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = memberDao.updateMemberEmail(conn, id, newEmail);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return result;
	}

	/**
	 * DML 회원탈퇴
	 */
	public int deleteMember(String id) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = memberDao.deleteMember(conn, id);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * DQL 탈퇴회원 조회
	 */
	public List<DeletedMember> findDeletedMember() {
		Connection conn = getConnection();
		List<DeletedMember> delMembers = memberDao.findDeletedMember(conn);
		close(conn);
		return delMembers;
	}
	
	