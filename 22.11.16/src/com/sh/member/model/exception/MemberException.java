package com.sh.member.model.exception;

public class MemberException extends RuntimeException {

	// constructor from super
	public MemberException() {
		super();
	}

	public MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
										// Error, Exception의 부모 Throwable
	public MemberException(String message, Throwable cause) { // 메시지 + 원래 발생한 예외 (중첩예외)
		super(message, cause);			
	}

	public MemberException(String message) { // 메시지
		super(message);
	}

	public MemberException(Throwable cause) {
		super(cause);
	}
	
	
}
