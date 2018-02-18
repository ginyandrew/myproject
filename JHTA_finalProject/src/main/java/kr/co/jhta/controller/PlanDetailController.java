package kr.co.jhta.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.HistoryVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.exception.DataNotFoundException;
import kr.co.jhta.service.PlanDetailService;
import kr.co.jhta.view.PlanToExcelView;

@Controller
public class PlanDetailController {

	private static final Logger logger = LoggerFactory.getLogger(PlanDetailController.class);

	@Autowired
	private PlanDetailService planDetailService;

	@Autowired
	private MappingJackson2JsonView jsonView;

	@Autowired
	private PlanToExcelView excelView;

	@RequestMapping("/plan/detail.tm")
	public String main(@RequestParam int no, Model model, HttpSession session) {
		
		// 환율 정보 확인하기
		if(session.getAttribute("EXCHANGE") == null) {
			Map<String, Object> exchangeData = new HashMap<>();
			exchangeData.put("type", "KRW");
			exchangeData.put("unit", "￦");
			session.setAttribute("EXCHANGE", exchangeData);
		};
		
		// 사용자 정보 가져오기
		UserVO user = (UserVO) session.getAttribute("LOGIN_USER");
		Integer userNo = null;
		boolean authority = false;

		logger.info(String.format("%d", no));
		// Plan 정보 가져오기
		PlanVO plan = planDetailService.getPlanDataByNo(no);
				
		// Dest들의 대표 이미지 가져오기
		//
		
		// 세션에 최근에 본 목록에 대한 정보 담기: 순서가 보장되는 TreeSet을 사용한다.
		TreeSet<HistoryVO> history = (TreeSet<HistoryVO>)session.getAttribute("HISTORY");
		if(history == null) {
			history = new TreeSet<>();
		}
		HistoryVO<PlanVO> historyData = new HistoryVO("plan", plan);
		if(history.contains(historyData)){
			history.remove(historyData);
		}
		history.add(historyData);
		session.setAttribute("HISTORY", history);
		
		if (user != null) {
			userNo = user.getNo();
			// 조회수 작업
			planDetailService.addViews(no, userNo);
		}

		// 수정, 삭제권한 조회하기
		Integer writerNo = plan.getUser().getNo();
		if (userNo != null && (userNo == 1 || userNo == writerNo)) {
			authority = true;
		}
		
		// 조회수 정보 가져오기
		int views = planDetailService.getViewsByPlanNo(no);
		
		model.addAttribute("plan", plan);
		model.addAttribute("views", views);
		model.addAttribute("hasAuthority", authority);
		
		return "plandetail";
	}

	@RequestMapping("/plan/copy.tm")
	public ModelAndView copyPlan(PlanVO newPlan, int userNo, int planNo) {

		ModelAndView mav = new ModelAndView();

		newPlan.setUser(new UserVO(userNo));
		planDetailService.copyPlan(newPlan, planNo);

		// 성공시
		mav.addObject("result", "success");

		// 실패시
		//mav.

		mav.setView(jsonView);

		return mav;
	}

	@RequestMapping("/plan/excel.tm")
	public ModelAndView excelPlan(int no) {

		PlanVO plan = planDetailService.getPlanDataByNo(no);

		ModelAndView mav = new ModelAndView();
		mav.addObject("plan", plan);
		mav.setView(excelView);

		return mav;
	}

	@RequestMapping("/plan/delete.tm")
	public String delete(int no, HttpSession session, RedirectAttributes ra) {
		// 삭제 권한이 있는지 확인
		UserVO user = (UserVO) session.getAttribute("LOGIN_USER");
		Integer userNo = null;
		
		// Plan 정보 가져오기
		PlanVO plan = planDetailService.getPlanDataByNo(no);

		if (user == null) {
			// 다시 로그인해주세요
			return "redirect:/plan/detail.tm?no="+no;
		}
		userNo = user.getNo();

		// 수정, 삭제권한 조회하기
		Integer writerNo = plan.getUser().getNo();
		if (userNo != 1 && userNo != writerNo) {
			// 권한이 없습니다
			return "redirect:/plan/detail.tm?no="+no;
		}
		
		// 삭제 작업
		planDetailService.deletePlan(no);
		
		ra.addFlashAttribute("msgName", "alert");
		ra.addFlashAttribute("msg", "\"" + plan.getTitle() + "\" 일정이 삭제되었습니다.");
		ra.addFlashAttribute("msgType", "success");
		
		return "redirect:/plan/list.tm";
	}
}
