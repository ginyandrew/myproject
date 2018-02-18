package kr.co.jhta.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.gson.Gson;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.Criteria;
import kr.co.jhta.domain.CriteriaReview;
import kr.co.jhta.domain.HistoryVO;
import kr.co.jhta.domain.Navigation;
import kr.co.jhta.domain.NavigationReview;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.service.InfoCityServiceImpl;

@Controller
public class InfoCityController {

	private static final Logger logger = LoggerFactory.getLogger(InfoCityController.class);
	
	@Autowired
	InfoCityServiceImpl infoCityService;
	@Autowired
	MappingJackson2JsonView jsonView;
	
	
	// cityInfo 도시정보 페이지 로딩하면 바로 전달할 값들  
	@RequestMapping("/info/city.tm")
	public String main(Criteria c, Model model, HttpSession session) {	
		
		//도시 번호 쿼리스트링으로 전달받는다고 가정하기.
		//해당 도시사진 
		List<CityImgVO> cityImgData = infoCityService.getTheCityImgsByCityNo(c.getCityNo());
		model.addAttribute("theCityImgData",cityImgData);
		model.addAttribute("imgSize", cityImgData.size());
		
		//해당 도시정보
		CityVO cityData = infoCityService.getCityInfoByCityNo(c.getCityNo());
		model.addAttribute("theCityData", cityData);
		
		//상위 카테고리 가져오기
		List<CategoryCodeVO> upperCategory = infoCityService.getUpperCategoryCode();
		model.addAttribute("theUpperCategories", upperCategory);
		
		
		// 히스토리 삽입하기
		TreeSet<HistoryVO> history = (TreeSet<HistoryVO>)session.getAttribute("HISTORY");
		if(history == null) {
			history = new TreeSet<>();
		}
		
		// cityData에 이미지 정보 설정하기
		List<String> imgList = new ArrayList<>();
		for(CityImgVO imgData: cityImgData) {
			imgList.add(imgData.getFileName());
		}
		cityData.setImgNameList(imgList);
		
		
		HistoryVO<PlanVO> historyData = new HistoryVO("city", cityData);
		if(history.contains(historyData)){
			history.remove(historyData);
		}		
		history.add(historyData);
		session.setAttribute("HISTORY", history);		
		
		return "infocity";		
	}	
	
	// AJAX 컨트롤러들 ****************************
	
	// AJAX(0) 위도,경도,작년 연도를 받아서 작년 1~12월15일씩의 기온을 반환한다.
	@RequestMapping("/info/jsonMonthTemp.tm")
	public ModelAndView getMonthlyTempOfTheCity(String cityLng, String cityLat, String lastYear) throws Exception{
		ModelAndView mav = new ModelAndView();
		System.out.println("위도는 이래요" +cityLng);
		 
		String basicUrl = "https://api.forecast.io/forecast/b3c35d762e71470531e8161c4826b528/";
		System.out.println("위도경도정보는 여기에!" + cityLng+ cityLat +lastYear);
		List<Object> weatherGraphData = new ArrayList<>();
				
		for (int i=1; i<=12; i++){
		
			String theUrl = basicUrl + cityLat +","+ cityLng +","+  lastYear + "-0" + i + "-15T12:00:00";
		
			if (i>=10){				
				theUrl = basicUrl + cityLat +","+ cityLng +","+ lastYear + "-" + i + "-15T12:00:00";
			}		
			
			URL url = new URL(theUrl);
			InputStream is = url.openStream();			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));			
			
			Gson gson = new Gson();								//위 url로 얻어낸 텍스트는 json 의 구조와 똑같이 적혀있는 자바스트링이다.
			//gson의 toJson은 json형태의 텍스트로 바꿔주고, fromJson은 json형태의 텍스트를 객체로 바꿔준다.
			HashMap map = gson.fromJson(br, HashMap.class);		//json처럼 적혀있는 자바 String 인 br를, hashMap (class)타입의 hashmap객체로 만들어라.
			Double temperatureFr = (double)(((Map)map.get("currently")).get("temperature"));
			Double temperature = (temperatureFr-32)*5/9;
			Double humidity = (double)(((Map)map.get("currently")).get("humidity"));			
			Object theMonthDate = i ;
	
			List<Object> dataArr = new ArrayList<>();
			
			dataArr.add(theMonthDate);
			dataArr.add(temperature);
			dataArr.add(humidity);
			weatherGraphData.add(dataArr);
		}	
		mav.addObject("weatherData",weatherGraphData);
		
		mav.setView(jsonView);
		return mav;
	}	
	
	// AJAX(1) 특정 도시번호를 넣으면 해당 도시의 4개 여행지 가져오기
	@RequestMapping("/info/jsonFourDestByCityNo.tm")
	public ModelAndView getLowerCategory(Criteria c){

		ModelAndView mav = new ModelAndView();
		//특정 도시번호를 넣으면 해당 도시의 4개 여행지 출력
		List<Map<String, Object>> allDestDatas = infoCityService.getFourDestData(c);
		mav.addObject("allDestData", allDestDatas);
		System.out.println(c.getCityNo());
		//해당 도시의 여행지들 페이지네이션
		Navigation navigation = new Navigation(infoCityService.getTotalRows(c),c);
		mav.addObject("allDestNavi", navigation); 
					
		mav.setView(jsonView);
		return mav;
	}
	
		
	// AJAX(2) 여행지 상위카테고리 누르면 하위 카테고리 가져오기 
	@RequestMapping("/info/jsonLowerCateNo.tm")
	public ModelAndView getLowerCategory(int category){
		ModelAndView mav = new ModelAndView();
		List<CategoryCodeVO> lowerCateCode = infoCityService.getLowerCategoryCode(category);
		mav.addObject("lowerCateCodes", lowerCateCode);
		
		mav.setView(jsonView);
		
		return mav;
	}
			
	// AJAX(3) 특정 도시번호를 넣으면 해당 도시의 리뷰 3개씩 출력
	@RequestMapping("/info/jsonCityReview.tm")
	public ModelAndView getReviewsByCityNo(CriteriaReview cReview){
		ModelAndView mav = new ModelAndView();
		
		List<HashMap<String, Object>> threeReviews = infoCityService.getReviewsByCityNo(cReview);
		
		mav.addObject("theThreeReviews", threeReviews);
		
		//해당 도시의 리뷰들 페이지네이션
		NavigationReview navigationReview = new NavigationReview(infoCityService.getTotalReviewRows(cReview),cReview);
		mav.addObject("theNaviReview", navigationReview);
		
		mav.setView(jsonView);
		return mav;
	}
		
		
		//test페이지
		@RequestMapping("/jsonCity.tm")
		public ModelAndView searchPlacesByName(){
		ModelAndView mav = new ModelAndView();
		CityVO cityData = infoCityService.getCityInfoByCityNo(100);
		mav.addObject("theCityData", cityData);
		
		mav.setView(jsonView);
		
		return mav;
			
		}
		
	}