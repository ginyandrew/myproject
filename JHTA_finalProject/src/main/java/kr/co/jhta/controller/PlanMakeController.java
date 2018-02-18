package kr.co.jhta.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.gson.Gson;

import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.service.PlanDetailService;
import kr.co.jhta.service.PlanMakeService;
import oracle.net.aso.p;

@Controller
public class PlanMakeController {

	private static final Logger logger = LoggerFactory.getLogger(PlanMakeController.class);
	
	@Autowired
	PlanMakeService service;
	@Autowired
	PlanDetailService planDetailService;
	@Autowired
	MappingJackson2JsonView jsonView;
	
	@RequestMapping("/plan/make.tm")
	public String main(){
		return "preplanmake.jsp";
	}
	
	@RequestMapping("/plan/preplanmake.tm")
	public ModelAndView preplanmake() {
		
		List<CityImgVO> cityData = service.cityData();
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("cityData", (ArrayList<CityImgVO>)cityData);
		mav.setView(jsonView);
					
		return mav;
	}
	
	@RequestMapping("/plan/catedata.tm")
	public ModelAndView makedata(int cateno) {
		List<DestImgVO> destlist = service.getCitylistByUppercatecode(cateno);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("destlist", destlist);
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping("/plan/dataplanmake.tm") // test
	public ModelAndView dataplanmake(int no) {
		
		List<CityImgVO> cityData = service.cityData();
		List<DestImgVO> destList = service.getDestList();
		PlanVO plan = planDetailService.getPlanDataByNo(no);
		ModelAndView mav = new ModelAndView();
		mav.addObject("plan", plan);
		mav.addObject("destList", destList);
		mav.addObject("cityData", cityData);
		mav.setView(jsonView);
		//mav.setViewName("planmake.jsp");
		//logger.info("cityData:" + cityData);	
		return mav;
	}
	
	
	@RequestMapping("/plan/planmake.tm") // 상세일정 만들기 페이지로 넘어감.
	public ModelAndView planmake(int no) {
		
		PlanVO plan = planDetailService.getPlanDataByNo(no);
		List<DestImgVO> destList = service.getDestList();
		List<CityImgVO> cityData = service.cityData();
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("plan", plan);
		mav.addObject("planToJson", new Gson().toJson(plan));
		mav.addObject("destList", destList);
		mav.addObject("cityData", cityData);
		//mav.setView(jsonView);
		mav.setViewName("planmake.jsp");
		//logger.info("cityData:" + cityData);	
		return mav;
	}
	
	@RequestMapping("/plan/updateplan.tm")
	public ModelAndView updateplan(PlanVO plan) {
		
		if(plan.getLeaveDate() != null
				||	plan.getIsCompleted() == "F" || plan.getIsCompleted() == "T"
				|| 	plan.getThemeCode() != 0
				||	plan.getTitle() != null) {
			
			service.updatePlan(plan);
		}
		PlanVO planData = service.getPlan(plan.getNo());
		ModelAndView mav = new ModelAndView();
		mav.addObject("planData", planData);
		mav.setView(jsonView);
		return mav;
	}
	
	@RequestMapping("/plan/insertday.tm")
	public ModelAndView insertday(PlanDetailVO vo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "success");
		mav.setView(jsonView);
		
		service.insertPlanDetail(vo);
		PlanVO plan = planDetailService.getPlanDataByNo(vo.getPlanNo());
		plan.setPeriod(plan.getPeriod()+1);
		
		service.updatePlan(plan);
		return mav;
	}
	@RequestMapping("/plan/removeday.tm")
	public ModelAndView removeday(PlanDetailVO vo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "success");
		mav.setView(jsonView);
		//#{day}, #{planNo}, #{dest.no}, #{numbering})
		
		service.removePlanDetailByDay(vo);
		
		PlanVO plan = planDetailService.getPlanDataByNo(vo.getPlanNo());
		
		plan.setNo(vo.getPlanNo());
		plan.setPeriod(plan.getPeriod()-1);
		service.updatePlan(plan);
		return mav;
	}
	
	@RequestMapping("/plan/insertdest.tm")
	public ModelAndView insertdest(PlanDetailVO vo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "success");
		mav.setView(jsonView);
		service.insertPlanDetail(vo);
		return mav;
		
	}
	
	@RequestMapping("/plan/updatedestlist.tm")
	public ModelAndView updatedestlist(PlanDetailVO vo, String numarr) {
		String[] nums = numarr.split(",");
		int len = nums.length;
		for(int i=0;i<len;i++){
			int num = Integer.parseInt(nums[i]);
			vo.setNumbering(num);
			service.updatePlanDetail(vo);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "success");
		mav.setView(jsonView);
		return mav;
	}
	
	@RequestMapping("/plan/updatedestmemo.tm")
	public void updatedestmemo(PlanDetailVO vo) {
		
		
	}
	
	@RequestMapping("/plan/removedest.tm")
	public ModelAndView removedest(PlanDetailVO vo) {
		service.removePlanDetail(vo);
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "success");
		mav.setView(jsonView);
		return mav;
	}
	
	@RequestMapping("/plan/modify.tm")
	public ModelAndView planModify(int no) {// 플랜번호를 받아서 수정처리작업
		PlanVO plan = planDetailService.getPlanDataByNo(no);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("planData", plan);
		mav.setView(jsonView);
		return mav;
		
	}
	
	
	
	@RequestMapping("/plan/planmakestatecode.tm")
	public ModelAndView statecode() {
		List<StateCodeVO> stateCodeList = service.getAllStateCode();
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("stateCode", stateCodeList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping("/plan/planmakecity.tm")
	public ModelAndView city(int statecode) {
		List<CityVO> cityList = service.selCityByStateCode(statecode);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("city", cityList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	
	//planmake관련
		@RequestMapping("/plan/destlist.tm")
		public ModelAndView destlist(int cityno) {
			
			List<DestImgVO> destList = service.getDestListByCityNo(cityno);
			
			ModelAndView mav = new ModelAndView();
			mav.addObject("destList", destList);
			mav.setView(jsonView);
			
			return mav;
		}
	
	
	
	@RequestMapping("/plan/detaildest.tm")
	public ModelAndView detailDest(int destno) {
		HashMap<String, Object> detailDest = service.selDetailDestByNo(destno);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("dest", detailDest);
		mav.setView(jsonView);
		
		return mav;
		
	}
		
	@RequestMapping("/plan/plandest.tm")
	public ModelAndView planDest(int planno) {
		List<PlanDetailVO> planDetailList = service.getPlanDetailbyPlanNo(planno);
		ModelAndView mav = new ModelAndView();
		mav.addObject("planDetailList", planDetailList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping("/plan/plandata.tm")
	public ModelAndView plandata(int no) {
		PlanVO plandata = service.getPlan(no);
		ModelAndView mav = new ModelAndView();
		mav.addObject("plandata", plandata);
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping(value="/plan/NewPlan.tm",  method=RequestMethod.POST)
	public String makeNewPlanTest(PlanVO plan,String cityNo, String periodss, int userNo, HttpSession session) {
		UserVO user = new UserVO();
		user.setNo(userNo);
		plan.setUser(user);
		// 더미 플랜 상세정보 생성
		List<PlanDetailVO> dummyList = new ArrayList<>();
		String[] cityNoArr = cityNo.split(",");
		String[] periodArr = periodss.split(",");
		int len = cityNoArr.length;
		
		for(int i=0; i<len; i++){

		// 여행지 정보 설정
		DestVO dest = new DestVO();
		CityVO city = new CityVO();
		city.setNo(Integer.parseInt(cityNoArr[i]));
		dest.setCity(city);

		// 해당 도시의 여행 기간만큼 반복
		int days = Integer.parseInt(periodArr[i]);
		for(int day=1; day<=days; day++) {

		PlanDetailVO detail = new PlanDetailVO();

		// 정보 설정
		//detail.setNumbering(0);	// 임시
		detail.setDay(day);
		detail.setDest(dest);
		detail.setMemo(" ");
		// 리스트에 더하기
		dummyList.add(detail);
		}
		}
		plan.setDetails(dummyList);
		
		service.makeNewPlan(plan);
		logger.info(plan.getTitle() + " 정보를 추가함");
				
		return "redirect:/plan/planmake.tm?no="+plan.getNo();
		
		}
}
