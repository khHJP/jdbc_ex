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

/**
 * DAO 
 * - Database Access Object
 * - DB에 접근하는 객체. DB의 접근은 DAO만 하기로 약속. 
 */
public class MemberDao {
	// 드라이버 클래스 이름 필드로 선언
	String driverClass = "oracle.jdbc.OracleDriver";
	// DB 접속에 공통적으로 사용할 url, user, password 필드 선언
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
	 * insertMember : 데이터 입력 함수 (DML)
	 */
	public int insertMember(Member member) {
		// 사용할 변수
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		// 실행할 미완성쿼리
		String sql = "insert into member values (?, ?, ?, ?, ?, default, default)";
				
		try {
			// 1. 드라이버 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성 (auto-commit - false설정)
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);

			// 3. PreparedStatement 생성 + 미완성쿼리 값대입 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getGender());
			pstmt.setDate(4, member.getBirthday());
			pstmt.setString(5, member.getEmail());
			
			// 4. 실행 -> int 반환 (처리된 행 수)
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
			
			// 6. 자원반납 
		} finally { // finally는 정상실행 되지 않더라도 실행됨. 그래서 nullpointer를 우려해 Exception으로 변경하는 것. 
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
	 * findAll : 모든 행 반환 함수(DQL)
	 * 여러 건 조회하는 경우
	 * n행 조회 - ArrayList에 요소 추가후 반환
	 * 0행 조회 - 빈 ArrayList 반환
	 */
	public List<Member> findAll() {
		Connection conn = null; // 매번 지역변수로 선언해야함. 쿼리문마다 새 connection을 여는 것. 
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member order by reg_date desc";
		List<Member> members = new ArrayList<>(); // 미리 선언 -> 0행이 리턴되어도 빈 list객체 반환
		
		try {
			// 1. 클래스 등록
			Class.forName(driverClass);
			
			// 2. Connection 생성
			conn = DriverManager.getConnection(url, user, password);
					
			// 3. PreparedStatement 생성 / 값대입은 필요없음. 미완성쿼리가 아니라서!  
			pstmt = conn.prepareStatement(sql);
			
			// 4. 실행 - ResultSet반환
			rset = pstmt.executeQuery(); // DQL
			
			// 5. ResultlSet -> List<Member> 변환
			while(rset.next()) { // 여러 행이 리턴 -> rest.next()로 한 행씩 접근. 남은행이 없다면 false로 반복종료. 
				String id = rset.getString("id"); // 컬럼명 대소문자 구분안함
				String name = rset.getString("name");
				String gender = rset.getString("gender");
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				int point = rset.getInt("point");
				Timestamp regDate = rset.getTimestamp("reg_date");
				// 행에서 받아온 정보로 member 객체 생성
				Member member = new Member(id, name, gender, birthday, email, point, regDate);
				// member객체 List에 담기 (한 행) 
				members.add(member);	
			}
	
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			// 6. 자원반납 
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
	 * findById: 사용자 입력받은 id로 찾은 행(member객체) 반환 (DQL)
	 * 한 건 조회인 경우 
	 * 1행 조회 - member객체 반환
	 * 0행 조회 - Member member = null/ null 반환
	 * @param id
	 * @return
	 */
	public Member findById(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where id = ?";
		Member member = null; // 반환할 멤버객체
		
		try {
			// 1. 클래스등록
			Class.forName(driverClass);
			
			// 2. Connection 생성
			conn = DriverManager.getConnection(url, user, password);
			
			// 3. PrepareStatement 생성 + 미완성쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id); // select * from member where id = 'honggd' ; 타입을 맞춰줘야함 
			
			// 4. 실행 - ResultSet 반환 
			rset = pstmt.executeQuery();
			
			// 5. ResultSet -> Member 변환 
			while(rset.next()) { // 행이 있을 때
				// 기본생성자 + setter 조합으로 만들어보기
				member = new Member();
				member.setId(rset.getString("ID")); // 컬럼명 대소문자 구분 X
				member.setName(rset.getString("NAME"));
				member.setGender(rset.getString("GENDER"));
				member.setBirthday(rset.getDate("BIRTHDAY"));
				member.setEmail(rset.getString("EMAIL"));
				member.setPoint(rset.getInt("POINT"));
				member.setRegDate(rset.getTimestamp("REG_DATE")); // DB쪽 컬럼명과 동일하게! 
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			// 6. 자원반납
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
				
		return member; // 최종리턴은 member임을 주의! 수정잘할것
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
			
			while(rset.next()) { // 여러 행이 리턴 -> rest.next()로 한 행씩 접근. 남은행이 없다면 false로 반복종료. 
				String id = rset.getString("id"); // 컬럼명 대소문자 구분안함
				String name = rset.getString("name");
				String gender = rset.getString("gender");
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				int point = rset.getInt("point");
				Timestamp regDate = rset.getTimestamp("reg_date");
				// 행에서 받아온 정보로 member 객체 생성
				Member member = new Member(id, name, gender, birthday, email, point, regDate);
				// member객체 List에 담기 (한 행) 
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















