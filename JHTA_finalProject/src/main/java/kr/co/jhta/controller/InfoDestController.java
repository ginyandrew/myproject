package kr.co.jhta.controller;

import java.math.BigDecimal; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.Criteria;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.HistoryVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.ReviewVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.service.InfoDestService;
import kr.co.jhta.service.PlanListService;

@Controller
public class InfoDestController {
	
	@Autowired
	InfoDestService destService;
	@Autowired
	MappingJackson2JsonView jsonView;
	@Autowired
	PlanListService planService;
	

	private static final Logger logger = LoggerFactory.getLogger(InfoDestController.class);	
	
	@RequestMapping("/info/destReview.tm")
	public String review(){
		ReviewVO review = new ReviewVO();
		
		destService.addNewReview(review);
		
		return "infodest";
	}
	
	@RequestMapping("/info/planlist.tm")
	public ModelAndView destPlan(int destNo){
		logger.info("no : " + destNo);
		List<PlanVO> planList = planService.getPlanDataByUserNo(destNo);
		ModelAndView mav = new ModelAndView();
		mav.addObject("planList", planList);
		mav.setView(jsonView);
		
		return mav;
	}
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping("/info/planDate.tm")
	public ModelAndView planDate(String date){
		
		ModelAndView mav = new ModelAndView();
		// Plan 데이터 수정 작업
		// 1일 더해진 날짜 만들기
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.DATE, 1);	
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info("날짜 변경 오류남: " + date);
			calendar.setTime(new Date(0));
		}
		String addedDate = sdf.format(calendar.getTime());
		logger.info("날짜" + addedDate);
		/*int period = */
		/*PlanVO vo = new PlanVO();
		vo.setPeriod(period);
		destService.updatePlanArriveDate(vo);*/
		mav.addObject("date", addedDate);
		mav.setView(jsonView);
		
        return mav;
	}
	@RequestMapping(value="/info/dest.tm", method=RequestMethod.GET)
	public String destPage(int destNo ,Model model, HttpSession session){
		DestVO dest = destService.searchDestByNo(destNo);
		
		List<CategoryCodeVO> category = destService.getCateByDestNo(destNo);
		
		// 여행지 번호를 받아서 이미지를 출력한다.
		List<DestImgVO> destImg = destService.searchDestImgByNo(destNo);
		logger.info("이미지 이름 :" + destImg);
		
		// 여행지 번호를 받아서 평가에 관한 갯수를 출력한다.
		Map<String, BigDecimal> emotionCount = destService.searchEmotionCountByDest(destNo);
		int total = 0;
		if(emotionCount != null){
			total += emotionCount.get("G").intValue();
			total += emotionCount.get("A").intValue();
			total += emotionCount.get("P").intValue();
			
		}
		Map<String, Integer> emotionRate = new HashMap<>();
		if(total!=0){
			emotionRate.put("G", (int) Math.round((double)emotionCount.get("G").intValue()/total * 100));
			emotionRate.put("A", (int) Math.round((double)emotionCount.get("A").intValue()/total * 100));
			emotionRate.put("P", (int) Math.round((double)emotionCount.get("P").intValue()/total * 100));
			
		} else {
			emotionRate.put("G", 0);
			emotionRate.put("A", 0);
			emotionRate.put("P", 0);
		}
		
		logger.debug("평가 : " + emotionRate);
		
		// 여행지 번호를 받아서 테마코드별 카운트
		Map<String, BigDecimal> themeCount = destService.searchCountByThemeCode(destNo);
		int themeTotal = 0;
		if(themeCount != null){
			
			themeTotal += themeCount.get("1").intValue();
			themeTotal += themeCount.get("2").intValue();
			themeTotal += themeCount.get("3").intValue();
			themeTotal += themeCount.get("4").intValue();
			themeTotal += themeCount.get("5").intValue();
			themeTotal += themeCount.get("6").intValue();
		}  				
		Map<String, Double> themeRate = new HashMap<>();
 		if(themeTotal != 0){
 			themeRate.put("one", (double)themeCount.get("1").intValue()/themeTotal * 100);
 			themeRate.put("two", (double)themeCount.get("2").intValue()/themeTotal * 100);
 			themeRate.put("three", (double)themeCount.get("3").intValue()/themeTotal * 100);
 			themeRate.put("four", (double)themeCount.get("4").intValue()/themeTotal * 100);
 			themeRate.put("five", (double)themeCount.get("5").intValue()/themeTotal * 100);
 			themeRate.put("six", (double)themeCount.get("6").intValue()/themeTotal * 100);
 		} else{
 			/*themeRate.put("one", 0.0); 
			themeRate.put("two", 0.0);
			themeRate.put("three", 0.0);
			themeRate.put("four", 0.0);
			themeRate.put("five", 0.0);
			themeRate.put("six", 0.0); */
 		}
 		logger.debug("테마코드 :" + themeRate);
 		
 		/*히스토리 삽입 시작*/
 		// 히스토리 가져오기 / 생성하기
		TreeSet<HistoryVO> history = (TreeSet<HistoryVO>)session.getAttribute("HISTORY");
		if(history == null) {
			history = new TreeSet<>();
		}
		
		// Dest 이미지 정보 삽입하기
		List<String> imgList = new ArrayList<>();
		for(DestImgVO img: destImg) {
			imgList.add(img.getFileName());
		}
		dest.setImgNameList(imgList);
 		
 		// 히스토리 삽입하기
 		HistoryVO<PlanVO> historyData = new HistoryVO("dest", dest);
		if(history.contains(historyData)){
			history.remove(historyData);
		}		
		history.add(historyData);
		session.setAttribute("HISTORY", history);
		
		/*히스토리 삽입 끝*/
 		
 		model.addAttribute("themeTotal", themeTotal);
 		model.addAttribute("themeRate", themeRate);
		model.addAttribute("category", category);
		model.addAttribute("dest", dest);
		model.addAttribute("destImg", destImg);
		model.addAttribute("emotionRate", emotionRate);
		
		return "infodest";
	} 
	@RequestMapping(value="/info/reviewlist.tm", method=RequestMethod.POST)
	public ModelAndView reviewList(int destNo){
		ModelAndView mav = new ModelAndView();
		List<ReviewVO> review = destService.searchReviewByDestNo(destNo);
		
		mav.addObject("review", review);
		
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping(value="/info/saveReview.tm")
	public ModelAndView saveReview(int userNo, int destNo, String data, String rating){
		ModelAndView mav = new ModelAndView();
		DestVO dest = new DestVO();
		dest.setNo(destNo);
		UserVO user = new UserVO();
		user.setNo(userNo);
		ReviewVO review = new ReviewVO();
		review.setData(data);
		review.setRating(rating);
		review.setDest(dest);
		review.setUser(user);
				
		destService.addNewReview(review);		
		mav.addObject("saveReview", destService);
		mav.setView(jsonView);
				
		return mav;
	}
	@RequestMapping(value="/info/deleteReview.tm",method=RequestMethod.GET)
	public String deleteReview(int destNo, int reviewNo,Model model){
		ModelAndView mav = new ModelAndView();
		
		destService.deleteReviewbyNo(reviewNo);
		model.addAttribute("delete", destService);
		mav.setView(jsonView);
		return "redirect:/info/dest.tm?destNo=" + destNo;
	}
	@RequestMapping(value="/info/addClip.tm",method=RequestMethod.GET)
	public ModelAndView addClip(int userNo, int destNo){
		ModelAndView mav = new ModelAndView();
		
		// 확인 작업
		if(destService.hasClipBoard(userNo, destNo)) {
			mav.addObject("result", "이미 포함된 일정입니다.");
		} else {

			destService.addClipBoard(userNo, destNo);
			
			mav.addObject("result", "success");
			}
		mav.setView(jsonView);
		return mav;
	}
	
	/*@RequestMapping(value="/info/destImg.tm",method=RequestMethod.GET)
	public ModelAndView getDestImg(int destNo){
		ModelAndView mav = new ModelAndView();
		
		DestImgVO destImg = destService.searchDestImgByDestNo(destNo);
		logger.info("이미지 이름 :" + destImg);
		mav.addObject("destImg", destImg);
		mav.setView(jsonView);
		return mav ;
	}*/
	
}
