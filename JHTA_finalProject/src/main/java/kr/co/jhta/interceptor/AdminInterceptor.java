package kr.co.jhta.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.jhta.domain.UserVO;

public class AdminInterceptor extends HandlerInterceptorAdapter {

	public final static int ADMIN_NO = 1; 
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		
		UserVO user = (UserVO)req.getSession().getAttribute("LOGIN_USER");
		
		// 관리자 확인
		if(user == null) {
			// 로그인 되지 않은 경우
			res.sendRedirect("/main.tm");
			return false;
		} else if(user.getNo() != ADMIN_NO) {
			// 관리자가 아닌 경우
			res.sendRedirect("/main.tm");
			return false;
		}
		
		// 관리자인 경우		
		return true;
	}

}
