package kr.co.jhta.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.jhta.domain.UserVO;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	/*String loginPath;
	
	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}*/
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		
		/*// 요청경로 얻기: /path.tm의 형태로 온다
		String path = req.getRequestURI().replace(req.getContextPath(), "");
		
		// 로그인 페이지가 아니라면,
		//HttpSession session = req.getSession();
		if(!"/login.tm".equals(path)) {			
			//session.setAttribute("BEFORE_PAGE", path);
			//System.out.println("저장된 이전 경로: " + path);
		}		
		
		return true;*/
		
		// 로그인 상태 확인
		UserVO user = (UserVO)req.getSession().getAttribute("LOGIN_USER");
		
		if(user == null) {
			// 로그인 되지 않은 경우
			res.sendRedirect("/main.tm");
			return false;
		} 
		
		return true;
	}

}
