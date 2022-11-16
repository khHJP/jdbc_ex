package com.sh.member.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class MemberDel extends Member {
	
	private Timestamp delDate;
	
	// 생성자
	public MemberDel() {
		super();
	}

	public MemberDel(String id, String name, String gender, Date birthday, String email, int point, Timestamp regDate,
			Timestamp delDate) {
		super(id, name, gender, birthday, email, point, regDate);
		this.delDate = delDate;
	}

	// Member 객체를 반환하는 생성자 만들기
	public MemberDel(Member member) {
		super(member.getId(), member.getName(), member.getGender(), member.getBirthday(), member.getEmail(),
				member.getPoint(), member.getRegDate());
	}

	// getter/setter
	public Timestamp getDelDate() {
		return delDate;
	}

	public void setDelDate(Timestamp delDate) {
		this.delDate = delDate;
	}

	// toString
	@Override
	public String toString() {
		return "MemberDel [delDate=" + delDate + ", toString()=" + super.toString() + "]";
	}
	
	


	
	
	
}
