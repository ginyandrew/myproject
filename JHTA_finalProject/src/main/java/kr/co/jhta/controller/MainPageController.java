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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.UserVO;
import kr.co.jhta.service.MainPageServiceImpl;


@Controller
public class MainPageController {

	private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
	
	
	@Autowired
	MainPageServiceImpl mainService ;
	@Autowired
	MappingJackson2JsonView jsonView;
	
	
	// 메인페이지 로딩하면 바로 전달할 값들
	@RequestMapping(value="/main.tm", method=RequestMethod.GET)
	public ModelAndView main(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();

		//인기3위내 일정 no 뽑기
		List<Map<String, Object>> data = mainService.GetTopThreePlan();
		mav.addObject("bestPlans", data);
	
		//인기2~4위내 도시 no,name,filename 뽑기
		List<HashMap<String, Object>> fiveCity = mainService.getTopFiveCity();
		mav.addObject("topFiveCity", fiveCity);
		
		//로그인전엔 mainf, 로그인후는 maint
		UserVO user = (UserVO) session.getAttribute("LOGIN_USER");
		if (user ==null){
			mav.setViewName("mainf");
			return mav;
		} else {
			int userNo = user.getNo();
			Map<String, Object> map = mainService.getUserRecord(userNo);			
			mav.addObject("userTotalInfo", map);
			
			mav.setViewName("maint");
			return mav;
		}
		
		//mav.setView(jsonView);
		
		
		
	}
	
	@RequestMapping(value="/jsonAutoList.tm")
	public ModelAndView autoList(String input) {
		ModelAndView mav = new ModelAndView();
		//String input = "서울";
		List<Map<String, Object>> selectedLocations = mainService.searchPlacesByName(input);
		mav.addObject("locations",selectedLocations);
		//mav.setViewName("mainf");
		mav.setView(jsonView);
		return mav;
	}
	
	//자동검색어- 'A'등 특정문자를 포함하는 모든 name의 장소/도시 가져오기
	@RequestMapping("/jsonPlaceslist.tm")
	public ModelAndView searchPlacesByName(){
		ModelAndView mav = new ModelAndView();
		
		List<HashMap<String, Object>> cityData = mainService.getTopFiveCity();
		mav.addObject("keywords",cityData);
		mav.setView(jsonView);
		return mav;
		
	}
	
	//달력의 특정날짜를 클릭했을 때 - 메세지 검사 
	@RequestMapping("/jsonCalendar.tm")
	public ModelAndView calendar(String selectedDate, int userNo){
		
		ModelAndView mav = new ModelAndView();
		
		int whetherUserHasPoint = mainService.checkPoint(selectedDate, userNo);
		mav.addObject("userPoint",whetherUserHasPoint);
		
		mav.setView(jsonView);
		return mav;
	}	
	
	//회원에게 메세지 보내고 포인트 적립하기	
	@RequestMapping("/jsonUserPointAndMsg.tm")
	public ModelAndView userPointAndMsg(String selectedDate, int userNo){
		ModelAndView mav = new ModelAndView();
		
		mainService.addPointMsgByCalendar(selectedDate, userNo);
		
		Map<String, Object> map = mainService.getUserRecord(userNo);
		mav.addObject("userUpdatedInfo", map);

		mav.setView(jsonView);
		return mav;
	}		 
	
}