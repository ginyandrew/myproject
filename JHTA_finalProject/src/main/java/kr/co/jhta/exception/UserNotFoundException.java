package kr.co.jhta.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("회원정보가 없습니다.");
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
