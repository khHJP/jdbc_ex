package com.sh.member.model.service;

import static com.sh.member.common.JdbcTemplate.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sh.member.model.dao.MemberDao;
import com.sh.member.model.dto.Member;
import com.sh.member.model.dto.MemberDel;

public class MemberService { // 여기선 try with resource 쓰지말기 
	
	private MemberDao memberDao = new MemberDao();

	public List<Member> findAll() {
		Connection conn = getConnection();
		List<Member> members = memberDao.findAll(conn);
		close(conn);
		return members;
	}

	public Member findById(String id) {
		Connection conn = getConnection();
		Member member = memberDao.findById(conn, id);
		close(conn);
		return member;
	}
	
	public List<Member> findByName(String name) {
		Connection conn = getConnection();
		List<Member> members = memberDao.findByName(conn, name);
		close(conn);
		return members;
	}


	public List<MemberDel> findAllMemberDel() {
		Connection conn = getConnection();
		List<MemberDel> members = memberDao.findAllMemberDel(conn);
		close(conn);
		return members;
	}


	// DML 
	public int insertMember(Member member) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.insertMember(conn, member);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e; // 롤백처리 이후 controller까지 다시 보내줘야함 
		} finally {
			close(conn);
		}
		return result;
	}
	
	public int deleteMember(String id) {
		int result = 0;
		Connection conn = getConnection();
		try {result = memberDao.deleteMember(conn, id);
		commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public int updateMember(String id, String colName, Object newValue) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.updateMember(conn, id, colName, newValue);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}
}
