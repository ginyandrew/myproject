package kr.co.jhta.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionAdvice {
	
	/*@ExceptionHandler(Exception.class)
	public String commonError(Exception e) {
		System.out.println("에러 메시지: " + e.getMessage());
		
		return "/error/error.jsp";
	}*/
}
