package kr.co.jhta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.exception.InvalidPasswordException;
import kr.co.jhta.exception.UserNotFoundException;
import kr.co.jhta.service.CommonService;

/**
 * 공통 기능(로그인, 로그아웃, 회원가입, 환율 정보 설정, 언어 설정)을 처리하는 컨트롤러 정의
 * @author 성인
 */
@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;
	@Autowired
	private MappingJackson2JsonView jsonView;
		
	/**
	 * 로그인을 시도할 때 오게 되는 페이지 매핑
	 * @param email 이메일(아이디)
	 * @param password 입력받은 비밀번호
	 * @param beforepage 이전 페이지(로그인 후 이동할 페이지)
	 * @param session 로그인 정보를 담을 세션
	 * @param ra 임시 메시지를 담을 RedirectAttributes 객체
	 * @return 이동할 페이지(이전 페이지)로 리다이렉트
	 */
	@RequestMapping("/login.tm")
	public String login(String email, String password, String beforepage, HttpSession session, RedirectAttributes ra) {

		UserVO user = null;
		// 로그인하기
		try {
			user = commonService.findUser(email, password);
			
			session.setAttribute("LOGIN_USER", user);
			// 메시지 설정?
			ra.addFlashAttribute("msgName", "login");
			ra.addFlashAttribute("msg", user.getName() + "님 환영합니다.");
			ra.addFlashAttribute("msgType", "success");
		} catch (UserNotFoundException | InvalidPasswordException e) {
			// 로그인 실패 메시지?
			ra.addFlashAttribute("msgName", "login");
			ra.addFlashAttribute("msg", e.getMessage());
			ra.addFlashAttribute("msgType", "danger");
		}

		return "redirect:" + beforepage;
	}
	
	/**
	 * 로그아웃을 시도할 때 오게 되는 페이지 매핑
	 * @param beforepage 이전 페이지(로그아웃 후 이동할 페이지)
	 * @param session 로그인 정보를 담을 세션
	 * @return 이동할 페이지(이전 페이지)로 리다이렉트
	 */
	@RequestMapping("/logout.tm")
	public String logout(String beforepage, HttpSession session) {
		// 로그아웃하기
		// 세션정보 지우기
		session.removeAttribute("LOGIN_USER");
		
		// 이전페이지로 이동
		return "redirect:" + (beforepage==null?"/main.tm":beforepage);
	}
	
	/**
	 * 회원 가입을 Ajax로 처리하고 결과를 알리는 페이지 매핑
	 * @param registerData 회원가입하는 사용자의 정보
	 * @return 회원가입 결과와 메시지를 Json 형태로 담은 문자열
	 */
	@RequestMapping("/register.tm")
	public ModelAndView register(UserVO registerData) {
		
		ModelAndView mav = new ModelAndView();
		
		commonService.registerNewUser(registerData);		
		
		mav.addObject("result", "1");
		mav.addObject("msg", "회원가입이 완료되었습니다.");
		mav.setView(jsonView);
		
		return mav;		
	}
	
	/**
	 * 사용자가 받은 메시지를 Json 형태로 받아오는 Ajax 처리 페이지 매핑
	 * @param no 사용자 번호
	 * @return 사용자가 받은 (최근 열 건의) 메시지 정보
	 */
	@RequestMapping("/message.tm")
	public ModelAndView getMessage(int no) {
		ModelAndView mav = new ModelAndView();
		
		List<MessageVO> messageList = commonService.getRecentMessage(no, 10);
		
		mav.addObject("messageList", messageList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	/**
	 * 사용자가 원하는 환율 정보를 설정하는 페이지 매핑
	 * @param exchange 화폐단위(KRW, USD, JPY)
	 * @param beforepage 설정 후 이동할 페이지
	 * @param session 환율 정보를 담을 세션
	 * @return 리다이렉트 페이지(이전 페이지)로 이동
	 */
	@RequestMapping("/exchange.tm")
	public String setExchange(String exchange, String beforepage, HttpSession session) {

		exchange = exchange.toUpperCase();
		
		// 화폐단위와 단위를 담을 맵 생성
		Map<String, Object> exchangeData = new HashMap<>();
		
		switch(exchange) {
		case "USD":
			exchangeData.put("type", exchange);
			exchangeData.put("unit", "＄");
			break;
		case "JPY":
			exchangeData.put("type", exchange);
			exchangeData.put("unit", "￥");
			break;
		default:
			exchangeData.put("type", "KRW");
			exchangeData.put("unit", "￦");
		}
		
		session.setAttribute("EXCHANGE", exchangeData);
		
		return "redirect:" + beforepage;
	}
	
	/**
	 * 표시 언어 정보를 세션에 설정하는 페이지 매핑
	 * @param lang 설정 언어(en, jp, cn, kr)
	 * @param beforepage 설정 후 이동할 페이지
	 * @param session 언어 정보를 담을 세션
	 * @return 리다이렉트 페이지(이전 페이지)로 이동
	 */
	@RequestMapping("/language.tm")
	public String setLanguage(String lang, String beforepage, HttpSession session) {
		
		switch(lang) {
		case "en":	// 영어
		case "ja":	// 일본어
		case "zh":	// 중국어
			session.setAttribute("LANGUAGE", lang);
			break;
		case "ko":
		default:
			session.setAttribute("LANGUAGE", "ko");
			lang = "ko";
		}
		
		// beforepage 분석
		if(beforepage.indexOf("?") > 0) {
			// 이전 쿼리스트링이 있다면
			return "redirect:" + beforepage + "&lang=" + lang;
		} else {
			return "redirect:" + beforepage + "?lang=" + lang;
		}
	}
}
