package kr.co.jhta.exception;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException() {
		super("잘못된 비밀번호입니다.");
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}
	
}
