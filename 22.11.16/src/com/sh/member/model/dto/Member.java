package com.sh.member.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * DTO Data Transfer Object
 * VO Value Object
 *
 */
public class Member {
	
	private String id;
	private String name;
	private String gender; // M F
	private Date birthday;
	private String email;
	private int point;
	private Timestamp regDate;
	
	// 생성자
	public Member() {
		super();
	}

	public Member(String id, String name, String gender, Date birthday, String email, int point, Timestamp regDate) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.point = point;
		this.regDate = regDate;
	}

	// getter/setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	// toString
	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + ", gender=" + gender + ", birthday=" + birthday + ", email="
				+ email + ", point=" + point + ", regDate=" + regDate + "]";
	}
	
	
	
	
}
