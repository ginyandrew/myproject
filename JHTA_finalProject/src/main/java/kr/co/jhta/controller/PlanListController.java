package kr.co.jhta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;
import kr.co.jhta.service.PlanListService;

@Controller
public class PlanListController {

	private static final Logger logger = LoggerFactory.getLogger(PlanListController.class);
	@Autowired
	PlanListService planListService;
	
	@Autowired
	MappingJackson2JsonView jsonView;
	
	@RequestMapping("/plan/list.tm")
	public String main(Model model) {
		
		CityImgVO cityimg = planListService.getImageForPlanList(0);
		List<StateCodeVO> states = planListService.getStates();
		
		Map<Integer, List<CityVO>> cityMap = new HashMap<>();
		
		// 모든 플렌을 가져옵니다! [2016-02-17 RE]
		List<PlanVO> getAllPlans = planListService.getAllPlans();
		
		for(StateCodeVO scVO : states) {
			// 1~8번은 무시함
			int code = scVO.getCode();
			if(code >= 9){
				cityMap.put(code, planListService.getCityByStateCode(code));
			} else {
				cityMap.put(code, null);
			}
		}
		
		//model.addAttribute("cities", cities);
		model.addAttribute("states", states);
		model.addAttribute("cityMap", cityMap);
		
		// 2016 02 12 
		model.addAttribute("getAllPlans" , getAllPlans);
		
		return "planlist";
	}
	
	// 2016 02 03 추가내용 
	@RequestMapping("/plan/search.tm")
	public ModelAndView search(Integer cityNo) {
		ModelAndView mav = new ModelAndView();
		
		// 서비스를 이용하여 Plan을 검색한다
		List<PlanVO> planList = planListService.getPlanDataByCityNo(cityNo);
		
		//이미지 한장만 나오게 했습니다
		CityImgVO cityimg = planListService.getImageForPlanList(cityNo);
		
		// 이미지 여러장 가져오게 할려고합니다!
		//List<CityImgVO> cityimg = planListService.getImageForPlanList(cityNo);
		// 검색한 결과를 담아 json으로 출력하게 한다.
		mav.addObject("planList", planList);
		// 2016 02 05 이미지가 보일수있게 해봅니다
		mav.addObject("cityimg", cityimg);
		mav.setView(jsonView);
	
		return mav;
	}
	
	
}
