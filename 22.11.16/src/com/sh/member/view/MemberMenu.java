package com.sh.member.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.sh.member.controller.MemberController;
import com.sh.member.model.dto.Member;
import com.sh.member.model.dto.MemberDel;

public class MemberMenu {

	private Scanner sc = new Scanner(System.in);
	private MemberController memberController = new MemberController();
	
	public void mainMenu() {
		
		String menu = "===== 회원관리프로그램 =====\n"
					+ "1. 회원 전체조회\n"
					+ "2. 아이디 조회\n"
					+ "3. 이름 검색\n"  // where name like '%abc%';
					+ "4. 회원 가입\n"
					+ "5. 회원 정보 수정\n"
					+ "6. 회원 탈퇴\n"
					+ "7. 탈퇴회원 조회\n"
					+ "0. 프로그램 종료\n"
					+ "=======================\n"
					+ "선택 : ";
		
		while(true) {
			System.out.print(menu);
			String choice = sc.next();
			
			Member member = null;
			int result = 0;
			List<Member> members = null;
			String id = null;
			String name = null;
			
			switch(choice) {
			case "1" : // 회원 전체조회
				members = memberController.findAll();
				displayMembers(members);
				break;
				
			case "2" : // 아이디 조회
				id = inputId("조회");
				member = memberController.findById(id);
				displayMember(member);
				break;
				
			case "3" : // 이름 검색
				displayMembers(memberController.findByName(inputName()));
				break;

			case "4" : // 회원 가입
				member = inputMember();
				System.out.println("> 입력 회원정보 : " + member);
				displayResult("회원가입", memberController.insertMember(member)); // 결과출력
				break;
				
			case "5" : // 회원 정보 수정
				updateMenu();
				break;
				
			case "6" : // 회원 탈퇴
				displayResult("회원삭제", memberController.deleteMember(inputId("삭제"))); 
				break;
				
			case "7" :
				displayMemberDel(memberController.findAllMemberDel());
				break;
			case "0" : return; // 종료
			default : 
				System.out.println("> 잘못 입력하셨습니다.");
			}
			
		}
		
	}
	
    private void displayMemberDel(List<MemberDel> members) {
    	if(members == null || members.isEmpty()) {
			// 조회결과가 없는 경우
			System.out.println("> 탈퇴회원이 없습니다.");
		}
		else {
			// 조회결과가 있는 경우
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s%-10s\n", 
							  "id", "name", "gender", "birthday", "email", "point", "regDate", "delDate");
			System.out.println("-----------------------------------------------------------------");
			for(MemberDel member : members) {
				System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s%-10s\n", 
						  		member.getId(), 
						  		member.getName(), 
						  		member.getGender(), 
						  		member.getBirthday(), 
						  		member.getEmail(), 
						  		member.getPoint(),
						  		// java utildate의 후손이라 format처리 가능 
						  		new SimpleDateFormat("yy/MM/dd hh:mm").format(member.getRegDate()),
						  		new SimpleDateFormat("yy/MM/dd hh:mm").format(member.getDelDate()));
			}
			System.out.println("-----------------------------------------------------------------");
		}
	}
	/**
	 * 검색할 이름 입력
	 * @return
	 */
	private String inputName() {
		System.out.print("> 조회할 회원 이름 입력 : ");
		return sc.next();
	}



	private void updateMenu() {
		String menu = "----------- 회원 정보 수정 -----------\n"
					+ "1. 이름 변경\n"
					+ "2. 생일 변경\n"
					+ "3. 이메일 변경\n"
					+ "0. 메인메뉴로 돌아가기\n"
					+ "-----------------------------------\n"
					+ "선택 : ";
		
		String id = inputId("수정");
		
		while(true) {
			Member member = memberController.findById(id);
			if(member == null) {
				System.out.println("> 조회된 회원이 없습니다.");
				return;
			}
			
			displayMember(member);
			
			System.out.print(menu);
			String choice = sc.next();
			String colName = null;
			Object newValue = null;
			
			switch(choice) {
			case "1" : 
				System.out.print("변경할 이름 : ");
				colName = "name";
				newValue = sc.next();
				break;
			case "2" : 
				System.out.print("변경할 생일 (예: 19990909) : ");
				colName = "birthday";
				newValue = Date.valueOf(LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyyMMdd")));
				break;
			case "3" : 
				System.out.print("변경할 이메일 : ");
				colName = "email";
				newValue = sc.next();
				break;
			case "0" : return;
			default : 
				System.out.println("잘못 입력하셨습니다.");
				continue;
			}
			displayResult("회원정보 수정", memberController.updateMember(id, colName, newValue));
		}
		
	}



	/**
	 * 한명의 member를 출력하는 메소드
	 * @param member
	 */
	private void displayMember(Member member) {
		if(member == null) {
			System.out.println("> 조회된 결과가 없습니다.");
		}
		else {
			System.out.println("----------------------------");
			System.out.println("ID	: " + member.getId());
			System.out.println("NAME 	: " + member.getName());
			System.out.println("GENDER 	: " + member.getGender());
			System.out.println("BIRTHDAY: " + member.getBirthday());
			System.out.println("EMAIL 	: " + member.getEmail());
			System.out.println("POINT 	: " + member.getPoint());
			System.out.println("REGDATE : " + member.getRegDate());
			
			System.out.println("----------------------------");
		}
	}
	
	// 아이디체크
	private boolean checkId(String id) {
		boolean bool = false;
		if(memberController.findById(id) != null) {
			bool = true;
			System.out.println("중복된 아이디입니다. 다시 입력해주세요.");
		}		
		return bool;
	}
	// 이메일체크
	private String checkEmail() {
		List<Member> members = memberController.findAll();
		
		System.out.print("> 이메일 : ");
		String email = sc.next();
				
		for(Member member : members) {
			if(email.equals(member.getEmail())) {
				System.out.println("중복된 이메일입니다. 다시 입력해주세요.");
			}			
		}
		return email;		
	}

	/**
	 * 사용자로부터 조회/삭제할 아이디 조회
	 * @param string
	 * @return
	 */
	private String inputId(String mode) {
		System.out.print("> " + mode + "할 아이디 입력 : ");
		return sc.next();
	}

	/**
	 * 여러명의 회원을 출력하는 메소드
	 * @param members
	 */
	private void displayMembers(List<Member> members) {
		if(members == null || members.isEmpty()) {
			// 조회결과가 없는 경우
			System.out.println("> 조회된 결과가 없습니다.");
		}
		else {
			// 조회결과가 있는 경우
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s\n", 
							  "id", "name", "gender", "birthday", "email", "point", "regDate");
			System.out.println("-----------------------------------------------------------------");
			for(Member member : members) {
				System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s\n", 
						  		member.getId(), 
						  		member.getName(), 
						  		member.getGender(), 
						  		member.getBirthday(), 
						  		member.getEmail(), 
						  		member.getPoint(), 
						  		member.getRegDate());
			}
			System.out.println("-----------------------------------------------------------------");
		}
	}

	private void displayResult(String type, int result) {
		System.out.println(type + " " + (result > 0 ? "성공!" : "실패!"));
	}

	/**
	 * 사용자로부터 회원정보를 입력받는 메소드
	 * @return
	 */
	private Member inputMember() {
		while (true) {
			System.out.println("> 새 회원정보를 입력하세요.");
			System.out.print("> ID : ");
			String id = sc.next();
			if (checkId(id))
				continue;

			System.out.print("> 이름 : ");
			String name = sc.next();
			System.out.print("> 성별(M/F) : ");
			String gender = sc.next();
			System.out.print("> 생일(1999-09-09) : ");
			Date birthday = Date.valueOf(sc.next());
			
			while(true) {
				if(checkEmail() == null) continue;
				else break;
			}  return new Member(id, name, gender, birthday, checkEmail(), 0, null);	
		} 
	}
}

	





