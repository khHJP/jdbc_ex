package com.sh.member.controller;

import java.sql.Date;
import java.util.List;

import com.sh.member.model.dao.MemberDao;
import com.sh.member.model.dto.Member;


/**
 * mvc패턴에서 가장 중요한 부분이 controller  
 * 실제 처리를 맡는 부분 
 */
public class MemberController {
	private MemberDao dao = new MemberDao(); // DAO객체에 일을 시킴 -> 

	/**
	 * MemberMenu로부터 DB에 새 정보 insert 요청 
	 * 	-> DAO에 전달하는 역할
	 */
	public int insertMember(Member member) {
		int result = dao.insertMember(member);
		return result;
	}
	// 삭제
	public int deleteMember(Member member) {
		int result = dao.deleteMember(member);
		return result;
	}

	public List<Member> findAll() {
		List<Member> members = dao.findAll();
		return members;
	}
	
	public Member findById(String id) {
		Member member = dao.findById(id);
		return member;
	}

	public List<Member> findByName(String searchName) {
		List<Member> members = dao.findByName(searchName);
		return members;
	}
	public int updateName(Member member, String newName) {
		int result = dao.updateName(member, newName);
		return result;
	}
	public int updateBirth(Member member, Date newBday) {
		int result = dao.updateBirth(member, newBday);
		return result;
	}
	public int updateEmail(Member member, String newEmail) {
		int result = dao.updateEmail(member, newEmail);
		return result;
	}

}
