package kr.co.jhta.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.service.MypageService;

@Controller
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Autowired
	private MypageService mypageservice;
	@Autowired
	private MappingJackson2JsonView jsonView;
	
	//마이페이지 이동
	@RequestMapping("/mypage.tm")
	public String main(HttpSession session) {
		return "mypage";
	}
	
	//현재 접속한 유저 정보
	//session에서 LOGIN_USER 안에 담겨있다.
	@RequestMapping("/mypageuserinfo.tm")
	public ModelAndView userinfo(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		UserVO loginUser = (UserVO)session.getAttribute("LOGIN_USER");
		
		mav.addObject("LOGIN_USER", loginUser);
		mav.setView(jsonView);
		
		return mav;
	}
	
	//회원정보수정
	@RequestMapping("/mypageUpdate.tm")
	public String updateUser(UserVO user){
		String trimgender = user.getGender().trim();
		user.setGender(trimgender);
		mypageservice.updateUser(user);
		return "redirect:/mypage.tm";
	}
	
	//회원탈퇴
	@RequestMapping("/userdelete.tm")
	public String dropUserByNo(int no, HttpSession session){
		session.invalidate();
		
		mypageservice.dropUserByNo(no);
		return "redirect:/main.tm";
	}
	
	// 비밀번호 변경 처리
	@RequestMapping("/changepwd.tm")
	public ModelAndView changePassword(String password, String newPassword, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		logger.info(password + ":" + newPassword);
		// 사용자 정보 얻기
		UserVO user = (UserVO)session.getAttribute("LOGIN_USER");
		if(user == null) {
			// 사용자 정보가 없을 때
			mav.addObject("result", "사용자 정보가 잘못되었습니다");
			mav.setView(jsonView);
			return mav;
		};
		logger.info("기존비번: " + user.getPassword());
		
		// 입력받은 기존암호 암호화
		String encPwd = DigestUtils.sha256Hex(password);
		logger.info("입력받은 기존비번: " + encPwd);
		if(!user.getPassword().equals(encPwd)) {
			// 기존 암호가 잘못 입력되었을 때
			mav.addObject("result", "잘못된 암호입니다");
			mav.setView(jsonView);
			return mav;
		}
		
		
		// 비밀번호 변경 작업 실행
		String newEncPwd = DigestUtils.sha256Hex(newPassword);
		user.setPassword(newEncPwd);
		
		// 비밀번호 변경 기능 실행
		mypageservice.updateUser(user);
		
		mav.addObject("result", "암호가 변경되었습니다");
		
		// 세션에 새 정보 입력
		session.setAttribute("LOGIN_USER", user);
		
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping("/getUserPlanList.tm")
	public ModelAndView getUserPlanList(int userNo) {
	ModelAndView mav = new ModelAndView();
	
	List<HashMap<String, Object>> theUserPlanLists = mypageservice.getUserPlanList(userNo);
	
	
	mav.addObject("userPlanLists", theUserPlanLists);
	
	mav.setView(jsonView);	
	
	return mav;
	}
	
	@RequestMapping("/getClipboardList.tm")
	public ModelAndView getClipboardList(int userNo) {
		
		ModelAndView mav = new ModelAndView();
		
		// 클립보드 정보 가져오기
		List<DestVO> clipboardList = mypageservice.getClipboardDataByUserNo(userNo);
		
		mav.addObject("clipboardList", clipboardList);
		mav.setView(jsonView);
		
		return mav;
	}
}
