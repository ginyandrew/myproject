package kr.co.jhta.controller;

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

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.service.InfoListService;

@Controller
public class InfoListController {

	private static final Logger logger = LoggerFactory.getLogger(InfoListController.class);
	
	@Autowired
	private InfoListService infoListService;
	@Autowired
	private MappingJackson2JsonView jsonView;
	
	@RequestMapping("/info/list.tm")
	public String main(Model model) {
		
		// 여행지 랭크를 주고 조회수가 높은 여덟개를 가져오기
		List<CityVO> famousCityList = infoListService.getFamousCityList(8);
		List<CityVO> cityList = infoListService.getAllCityList();
		
		Map<CategoryCodeVO, List<DestVO>> themeDestMap = infoListService.getThemeDestData();
		model.addAttribute("famousCityList", famousCityList);
		model.addAttribute("themeDestMap", themeDestMap);
		model.addAttribute("cityList", cityList);
		
		return "infolist";
	}
	
	@RequestMapping("/info/list/search.tm")
	public ModelAndView search(Integer cityNo, Integer code, String keyword) {
		ModelAndView mav = new ModelAndView();
		
		List<DestVO> destList = infoListService.searchDest(cityNo, code, keyword);
		
		mav.addObject("result", destList);
		mav.setView(jsonView);
		return mav;
	}
}
