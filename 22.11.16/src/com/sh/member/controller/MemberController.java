package com.sh.member.controller;

import java.util.List;

import com.sh.member.model.dto.Member;
import com.sh.member.model.dto.MemberDel;
import com.sh.member.model.service.MemberService;

public class MemberController {

	private MemberService memberService = new MemberService();

	public List<Member> findAll() {
		List<Member> members = null;
		
		try{
			members = memberService.findAll(); // Service가 던진 예외 처리
		} catch (Exception e) {
			// 1. 예외로그 남기기
			e.printStackTrace();
			
			// 2. 사용자에게 적절한 메세지 전달 -> 사용자 입력값이 아닌 코드의 문제이기 때문. 
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return members;
	}

	public Member findById(String id) {
		Member member = null;
		try {
			member = memberService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return member;
	}



	public List<MemberDel> findAllMemberDel() {
		List<MemberDel> members = null;
		try {
			members = memberService.findAllMemberDel();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return members;
	}

	public List<Member> findByName(String name) {
		List<Member> members = null;
		try {
			members = memberService.findByName(name);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return members;
	}

	
	/**
	 * DML 
	 */
	public int insertMember(Member member) {
		int result = 0;
		try {
			result = memberService.insertMember(member);
		} catch (Exception e) {
//			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return result;
	}
	
	public int deleteMember(String id) {
		int result = 0;
		try {
			result = memberService.deleteMember(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		return result;
	}

	public int updateMember(String id, String colName, Object newValue) {
		int result = 0;
		try {
			result = memberService.updateMember(id, colName, newValue);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[" + e.getMessage() + "] 관리자에게 문의바랍니다.");
		}
		
		return result;
	}
}
