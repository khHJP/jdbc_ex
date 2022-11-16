package com.sh.member.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sh.member.model.dto.Member;
import com.sh.member.model.dto.MemberDel;
import com.sh.member.model.exception.MemberException;

public class MemberDao {
	
	// SQL 쿼리를 관리하는 Properties 객체 생성
	private Properties prop = new Properties();
	
	// 기본생성자 -> MemberDao객체가 만들어질때 쿼리를 가져옴
	public MemberDao() {
		try {
			prop.load(new FileReader("resources/member-sql.properties"));	
//			System.out.println(prop); // 프로그램 시작하자마자 찍힘 -> mainMenu 호출 이전에 이미 sql이 로드
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/*
	  DQL
	  1. 회원 전체조회
	  2. 아이디 조회
	  3. 이름 검색
	  4. 탈퇴회원 조회
	 */
	
	/**
	 * <findAll 회원 전체조회>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 - ResultSet
	 * 3. ResultSet -> List<Member> (handleMemberResultSet 사용)
	 * 4. 자원반납 (try with resources 처리) 
	 * 5. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 6. List<Member> 반환
	 */
	public List<Member> findAll(Connection conn) {
		String sql = prop.getProperty("findAll"); // 메소드명으로 key값 설정해둔걸 불러옴! 
		List<Member> members = new ArrayList<>();
		
		// 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){			
			// 2. 실행 - ResultSet
			try(ResultSet rset = pstmt.executeQuery()){
				// 3. ResultSet -> List<Member>
				while(rset.next()) {
					Member member = handleMemberResultSet(rset);
					members.add(member);
				}
			}	
		} catch (Exception e) {
			throw new MemberException("회원 전체조회 오류!",e); // RuntimeException을 상속해 커스텀 예외 만들고, 예외처리 Service에 넘기기
		}		
		// 4. 자원반납: 전부 try with resources로 만들었기 때문에 close 할 필요 x
		return members;
	}
	
	/**
	 * <findById 아이디 조회>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 - ResultSet
	 * 3. rset.next() -> Member객체 (handleMemberResultSet 사용)
	 * 4. 자원반납 (try with resources 처리) 
	 * 5. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 6. Member객체 반환
	 */
	public Member findById(Connection conn, String id) {
		String sql = prop.getProperty("findById");
		Member member = null;
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			try(ResultSet rset = pstmt.executeQuery()){
				if(rset.next()) 
					member = handleMemberResultSet(rset);			
			}
		} catch (Exception e) {
			throw new MemberException("아이디 조회 오류!", e);
		}
		return member;
	}
	
	/**
	 * <findByName 이름 조회>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 - ResultSet
	 * 3. rset.next() -> Member객체 (handleMemberResultSet 사용)
	 * 4. 자원반납 (try with resources 처리) 
	 * 5. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 6. Member객체 반환
	 */
	public List<Member> findByName(Connection conn, String name) {
		String sql = prop.getProperty("findByName");
		List<Member> members = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + name + "%");
			try (ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					Member member = handleMemberResultSet(rset);
					members.add(member);
				}
			}
		} catch (Exception e) {
			throw new MemberException("이름 조회 오류!", e);
		}
		return members;
	}


	
	
	/**
	 * ResultSet을 받아 Member객체를 반환하는 메소드
	 * 블럭잡아 우클릭 -> Refactor -> Extract로 새 메소드로 추출 
	 */
	private Member handleMemberResultSet(ResultSet rset) throws SQLException { 
		Member member = new Member();
		member.setId(rset.getString("id"));
		member.setName(rset.getString("name"));
		member.setGender(rset.getString("gender"));
		member.setBirthday(rset.getDate("birthday"));
		member.setEmail(rset.getString("email"));
		member.setPoint(rset.getInt("point"));
		member.setRegDate(rset.getTimestamp("reg_date"));
		return member;
	}

	public List<MemberDel> findAllMemberDel(Connection conn) {
		String sql = prop.getProperty("findAllMemberDel");
		List<MemberDel> members = new ArrayList<>();
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			try(ResultSet rset = pstmt.executeQuery()){
				while(rset.next()) {
					MemberDel member = new MemberDel(handleMemberResultSet(rset)); // member반환하는 MemberDel생성자 
					member.setDelDate(rset.getTimestamp("del_date")); // del_date만 추가
					members.add(member);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return members;
	}

	/*
	  DML
	  1. 회원가입
	  2. 회원 정보 수정
	  3. 회원 탈퇴
	 */
	
	/**
	 * <insertMember 회원가입>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 
	 * 3. 자원반납 (try with resources 처리) 
	 * 4. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 5. result 반환
	 */

	public int insertMember(Connection conn, Member member) {
		String sql = prop.getProperty("insertMember");
		int result = 0;
		
		// 1. PreparedStatement 생성 - 미완성쿼리 & 값대입 + resource절로 close까지 완료 
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getGender());
			pstmt.setDate(4, member.getBirthday());
			pstmt.setString(5, member.getEmail());
			// 2. 실행 - int
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("회원가입오류!", e);
		}
		return result;
	}

	

	/**
	 * <deleteMember 회원탈퇴>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 
	 * 3. 자원반납 (try with resources 처리) 
	 * 4. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 5. result 반환
	 */
	public int deleteMember(Connection conn, String id) {
		String sql = prop.getProperty("deleteMember");
		int result = 0;
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("회원탈퇴오류!", e);
		}
		return result;
	}

	
	/**
	 * <updateMember 회원정보 수정>
	 * 1. PreparedStatement 생성 - 미완성쿼리 & 값대입
	 * 2. 실행 
	 * 3. 자원반납 (try with resources 처리) 
	 * 4. pstmt 생성 + 실행 시 예외처리 -> MemberException처리 -> throw to Service
	 * 5. result 반환
	 */
	public int updateMember(Connection conn, String id, String colName, Object newValue) {
		String sql = prop.getProperty("updateMember");
		int result = 0;
		sql = sql.replace("#", colName); // sql의 #에 받아온 컬럼명 (name/birthday/email) 대입
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setObject(1, newValue); // 변경할 값
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("회원정보 수정 오류!", e);
		}
		return result;
	}






}
