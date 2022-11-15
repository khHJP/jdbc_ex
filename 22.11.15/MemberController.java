package com.sh.member.controller;

import java.sql.Date;
import java.util.List;

import com.sh.member.model.dto.DeletedMember;
import com.sh.member.model.dto.Member;
import com.sh.member.model.service.MemberService;

public class MemberController {

	private MemberService memberService = new MemberService();

	public int insertMember(Member member) { // service로 연결만! 
		return memberService.insertMember(member);
	}

	public List<Member> findByName(String name) {
		return memberService.findByName(name);
	}

	public List<Member> findAll() {
		return memberService.findAll();
	}

	public Member findById(String id) {
		return memberService.findById(id) ;
	}

	public int updateMemberName(String id, String newName) {
		return memberService.updateMemberName(id, newName);
	}

	public int updateMemberBirthday(String id, Date newBirthday) {
		return memberService.updateMemberBirthday(id, newBirthday);
	}

	public int updateMemberEmail(String id, String newEmail) {
		return memberService.updateMemberEmail(id, newEmail);
	}

	public int deleteMember(String id) {
		return memberService.deleteMember(id);
	}

	public List<DeletedMember> findDeltedMember() {
		return memberService.findDeletedMember();
	}
	
}
