package com.sh.member.view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.sh.member.controller.MemberController;
import com.sh.member.model.dto.DeletedMember;
import com.sh.member.model.dto.Member;

public class MemberMenu {

	private MemberController memberController = new MemberController();
	private Scanner sc = new Scanner(System.in);
	
	public void mainMenu() {
		String menu = "===== 회원관리프로그램 =====\n"
                + "1. 회원 전체조회\n"
                + "2. 아이디 조회\n"
                + "3. 이름 검색\n" 
                + "4. 회원 가입\n"
                + "5. 회원 정보 수정\n"
                + "6. 회원 탈퇴\n"
                + "7. 탈퇴회원 조회\n"
                + "0. 프로그램 종료\n"
                + "=======================\n"
                + "선택 : ";
		
		do {
			System.out.println(menu);
			String choice = sc.next();
			Member member = null;
			int result = 0;
			// 2. 아이디조회용
			String id = null;
			// 3. 이름검색용
			String name = null; 
			List<Member> members = null;
			List<DeletedMember> delMembers = null;
			
			switch(choice) {
			case "1" : // 회원 전체조회
				members = memberController.findAll();
				displayMembers(members);
				break;
				
			case "2" : // 아이디 조회
				id = inputId("조회"); // 사용자 입력받기
				member = memberController.findById(id);
				displayMember(member);
				break;	
				
			case "3" : // 이름부분검색 (pk, uq가 안걸려있음 -> 여러건조회)
				name = inputName("검색"); //사용자입력받기
				members = memberController.findByName(name);
				displayMembers(members);
				break;
				
			case "4" : // 회원가입
				member = inputMember();
				System.out.println("> 입력정보 확인 : " + member);
				result = memberController.insertMember(member);
				displayResult("회원가입", result);
				break;
				
			case "5" : // 회원정보 수정
				updateMenu();
				break;
				
			case "6" : // 회원탈퇴 
				id = inputId("삭제");
				result = memberController.deleteMember(id);
				displayResult("회원삭제", result);
				break;
				
			case "7" : // 탈퇴회원 조회
				delMembers = memberController.findDeltedMember();
				displayDeletedMembers(delMembers);
				break;
				
			case "0" : return;
			default : System.out.println("> 잘못 입력하셨습니다.");
			}
			
			
			
		} while(true);
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
			displayMember(member);
			if(member == null)
				return; // 메인메뉴로 돌아감.
			
			System.out.print(menu);
			String choice = sc.next();
			int result = 0;
			
			switch(choice) {
			case "1" : 
				System.out.print("변경할 이름 : ");
				String newName = sc.next();
				result = memberController.updateMemberName(id, newName);
				displayResult("회원명 수정", result);
				break;
				
			case "2" : 
				System.out.print("변경할 생일 (19990909) : ");
				Date newBirthday = Date.valueOf(LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyyMMdd")));
				result = memberController.updateMemberBirthday(id, newBirthday);
				displayResult("생일 수정", result);
				break;
				
			case "3" : 
				System.out.print("변경할 이메일 : ");
				String newEmail = sc.next();
				result = memberController.updateMemberEmail(id, newEmail);
				displayResult("이메일 수정", result);
				break;
				
			case "0" : return;
			default : System.out.println("잘못 입력하셨습니다.");
			}
		}

		
	}


	/**
	 * 탈퇴회원 출력메소드
	 * @param delMembers
	 */
	private void displayDeletedMembers(List<DeletedMember> delMembers) {
		if(delMembers == null || delMembers.isEmpty()) {
			System.out.println("> 조회된 결과가 없습니다.");
		}
		else {
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s%-10s\n", 
							  "id", "name", "gender", "birthday", "email", "point", "regDate", "delDate");
			System.out.println("-----------------------------------------------------------------");
			for(DeletedMember member : delMembers) {
				System.out.printf("%-10s%-10s%-10s%-20s%-20s%-10s%-10s\n", 
						  		member.getId(), 
						  		member.getName(), 
						  		member.getGender(), 
						  		member.getBirthday(), 
						  		member.getEmail(), 
						  		member.getPoint(), 
						  		member.getRegDate(),
						  		member.getDelDate());
			}
			System.out.println("-----------------------------------------------------------------");
		}
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
	
	/**
	 * 한명의 회원을 출력하는 메소드
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
	
	/**
	 * 결과출력 
	 */
	private void displayResult(String mode, int result) {
		System.out.printf("%s %s\n",mode, result > 0 ? "성공!" : "실패!");
	}

	/**
	 * 사용자 입력값 : 아이디
	 */
	private String inputId(String mode) {
		System.out.print("> " + mode + "할 아이디 입력 : ");
		return sc.next();
	}

	/**
	 * 사용자 입력값 : 이름 
	 */
	private String inputName(String mode) {
		System.out.print("> " + mode + "할 이름 입력 : ");
		return sc.next();
	}


	/**
	 * 새 회원정보 입력
	 */
	private Member inputMember() {
		System.out.println("> 새 회원정보를 입력하세요.");
		System.out.print("> 아이디 : ");
		String id = sc.next();
		System.out.print("> 이름 : ");
		String name = sc.next();
		System.out.print("> 성별(M/F) : ");
		String gender = sc.next();
		System.out.print("> 생일(19990909) : ");
		LocalDate _birthday = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyyMMdd")); // parse로 받아온 생일 변경 
		Date birthday = Date.valueOf(_birthday); // LocalDate를 받는 함수 있음!
		System.out.print("> 이메일 : ");
		String email = sc.next();
	
		return new Member(id, name, gender, birthday, email, 0, null);
	}

}
