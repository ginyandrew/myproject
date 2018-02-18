package kr.co.jhta.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.service.CommonService;
import kr.co.jhta.service.HostService;

/**
 * 관리자 페이지 매핑
 * @author 성인
 *
 */
@Controller
public class HostController {

	private static final Logger logger = LoggerFactory.getLogger(HostController.class);

	/**
	 * 사진이 저장되는 경로, 현재 임시값을 넣어둠
	 */
	// 임시값
	private static final String basePath = "C:/Users/JHTA/git/final-team2/team2/src/main/webapp/resources/img/";
	private static final String cityImgDirectory = basePath + "city";
	private static final String destImgDirectory = basePath + "dest";
	
	@Autowired
	private HostService hostService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MappingJackson2JsonView jsonView;
	
	@RequestMapping("/host/main.tm")
	public String main(Model model) {
		model.addAttribute("stateCodeList", hostService.getStateCode());
		model.addAttribute("cityList", hostService.getCity());
		model.addAttribute("categoryList", hostService.getCategoryList());
		return "host";
	}
	
	/**
	 * 모든 회원의 정보를 가져온다
	 * @param orderBy
	 * @param orderType
	 * @return 회원 정보 json
	 */
	@RequestMapping("/host/user.tm")
	public ModelAndView getUser() {//String orderBy, String orderType) {
		ModelAndView mav = new ModelAndView();
		
		List<UserVO> userList = hostService.getUser();	
		
		mav.addObject("users", userList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	/**
	 * 모든 도시의 정보를 가져온다
	 * @return 도시 정보 json
	 */
	@RequestMapping("/host/city.tm")
	public ModelAndView getCity() {
		ModelAndView mav = new ModelAndView();
		
		List<CityVO> cityList = hostService.getCity();	
		//List<StateCodeVO> stateCodeList = hostService.getStateCode();
		
		mav.addObject("cities", cityList);
		/*mav.addObject("stateCodeList", stateCodeList);*/
		mav.setView(jsonView);
		
		return mav;
	}
	
	/**
	 * 모든 여행지의 정보를 가져온다
	 * @return 여행지 정보 json
	 */
	@RequestMapping("/host/dest.tm")
	public ModelAndView getDest() {
		ModelAndView mav = new ModelAndView();
		
		List<DestVO> destList = hostService.getDest();	
		/*List<CityVO> cityList = hostService.getCity();*/
		
		mav.addObject("dests", destList);
		/*mav.addObject("cityList", cityList);*/
		mav.setView(jsonView);
		
		return mav;
	}
	
	/**
	 * 모든 일정의 정보를 가져온다
	 * @return 일정 정보 json
	 */
	@RequestMapping("/host/plan.tm")
	public ModelAndView getPlan() {
		ModelAndView mav = new ModelAndView();
		
		List<PlanVO> planList = hostService.getPlan();	
		
		mav.addObject("plans", planList);
		mav.setView(jsonView);
		
		return mav;
	}
	
	@RequestMapping(value="/host/sendmsg.tm", method=RequestMethod.POST)
	public String sendMsg(String usernolist, String message, RedirectAttributes ra) {
		
		String[] noArr = usernolist.split(",");
				
		hostService.sendMsg(noArr, message);
		
		ra.addFlashAttribute("msg", noArr.length + "명의 회원에게 쪽지를 보냈습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "user");
		
		return "redirect:/host/main.tm";
	}
	
	@RequestMapping("/host/plan/remove.tm")
	public String removePlan(String data, RedirectAttributes ra) {
		
		String[] noArr = data.split(",");
		hostService.removePlan(noArr);
		
		ra.addFlashAttribute("msg", noArr.length>1? noArr.length + "개의 일정을 삭제했습니다.":noArr[0] + "번 일정을 삭제했습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "plan");
		
		return "redirect:/host/main.tm";
	}
	
	@RequestMapping("/host/dest/remove.tm")
	public String removeDest(String data, RedirectAttributes ra) {
		
		String[] noArr = data.split(",");
		hostService.removeDest(noArr);
		
		ra.addFlashAttribute("msg", noArr.length>1? noArr.length + "개의 여행지를 삭제했습니다.":noArr[0] + "번 여행지를 삭제했습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "dest");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 여행지 정보를 입력받은 값으로 갱신한다
	 * @param dest 입력받은 여행지 정보
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping(value="/host/dest/modify.tm", method=RequestMethod.POST)
	public String modifyDest(DestVO dest, MultipartFile file, int category, Integer categorysub, RedirectAttributes ra) {	
		
		List<CategoryCodeVO> categoryList = new ArrayList<>();
		categoryList.add(new CategoryCodeVO(category, null));
		if(categorysub != null){
			categoryList.add(new CategoryCodeVO(categorysub, null));
		}
		dest.setCategoryList(categoryList);
		
		hostService.updateDest(dest);
		
		int destNo = dest.getNo();
		
		if(!file.isEmpty()){
			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			
			logger.debug(String.format("%d: %s", destNo, filename));
			
			// 경로에 파일 저장하기
			File uploadFile = new File(destImgDirectory, filename);
			try {
				FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
				hostService.addNewDestImage(destNo, filename);
			} catch (IOException e) {
				//e.printStackTrace();
				logger.debug(e.getMessage());
			}
		}
		
		ra.addFlashAttribute("msg", dest.getNo() + "번 여행지의 정보를 수정하였습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "dest");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 여행지 정보를 새롭게 입력받은 값으로 저장한다.
	 * @param dest 입력받은 여행지 정보
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping(value="/host/dest/add.tm", method=RequestMethod.POST)
	public String addDest(DestVO dest, MultipartFile file, int category, Integer categorysub, RedirectAttributes ra) {	
		
		List<CategoryCodeVO> categoryList = new ArrayList<>();
		categoryList.add(new CategoryCodeVO(category, null));
		if(categorysub != null){
			categoryList.add(new CategoryCodeVO(categorysub, null));
		}
		dest.setCategoryList(categoryList);
		
		// 여행지 정보를 추가하면서 추가한 여행지의 번호를 얻음
		int destNo = hostService.addDest(dest);
		
		if(!file.isEmpty()){
			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			
			logger.debug(String.format("%d: %s", destNo, filename));
			
			// 경로에 파일 저장하기
			File uploadFile = new File(destImgDirectory, filename);
			try {
				FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
				hostService.addNewDestImage(destNo, filename);
			} catch (IOException e) {
				//e.printStackTrace();
				logger.debug(e.getMessage());
			}
		}
		
		ra.addFlashAttribute("msg", dest.getName() + " 여행지의 정보를 추가했습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "dest");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 여행지 정보에 이미지를 추가하고 DB에 정보를 저장한다.
	 * @param destNo 여행지 번호
	 * @param file 이미지 파일
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	/*@RequestMapping(value="/host/dest/addImg.tm", method=RequestMethod.POST)
	public String addDestImage(Integer destNo, MultipartFile file, RedirectAttributes ra) {

		String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		
		logger.debug(String.format("%d: %s", destNo, filename));
		
		// 경로에 파일 저장하기
		File uploadFile = new File(destImgDirectory, filename);
		try {
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
			hostService.addNewDestImage(destNo, filename);
			ra.addFlashAttribute("msg", filename + " 파일을 업로드 하였습니다.");
			ra.addFlashAttribute("msgType", "success");
		} catch (IOException e) {
			//e.printStackTrace();
			logger.debug(e.getMessage());
			ra.addFlashAttribute("msg", filename + " 파일 업로드에 실패했습니다.");
			ra.addFlashAttribute("msgType", "danger");
		}
		ra.addFlashAttribute("setTable", "dest");
		
		return "redirect:/host/main.tm";
	}*/
	
	/**
	 * 도시 정보를 입력받은 값으로 갱신한다
	 * @param city 입력받은 도시 정보
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping(value="/host/city/modify.tm", method=RequestMethod.POST)
	public String modifyCity(CityVO city, MultipartFile file, RedirectAttributes ra) {	
		
		hostService.updateCity(city);
		
		int cityNo = city.getNo();
		
		if(!file.isEmpty()){
			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			
			logger.debug(String.format("%d: %s", cityNo, filename));
			
			// 경로에 파일 저장하기
			File uploadFile = new File(cityImgDirectory, filename);
			try {
				FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
				hostService.addNewCityImage(cityNo, filename);
			} catch (IOException e) {
				//e.printStackTrace();
				logger.debug(e.getMessage());
			}
		}
		
		
		ra.addFlashAttribute("msg", city.getNo() + "번 도시의 정보를 수정하였습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "city");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 도시 정보에 이미지를 추가하고 DB에 정보를 저장한다.
	 * @param cityNo 도시 번호 
	 * @param file 이미지 파일
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	/*@RequestMapping(value="/host/city/addImg.tm", method=RequestMethod.POST)
	public String addCityImage(Integer cityNo, MultipartFile file, RedirectAttributes ra) {

		String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		
		logger.debug(String.format("%d: %s", cityNo, filename));
		
		// 경로에 파일 저장하기
		File uploadFile = new File(cityImgDirectory, filename);
		try {
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
			hostService.addNewCityImage(cityNo, filename);
			ra.addFlashAttribute("msg", filename + " 파일을 업로드 하였습니다.");
			ra.addFlashAttribute("msgType", "success");
		} catch (IOException e) {
			//e.printStackTrace();
			logger.debug(e.getMessage());
			ra.addFlashAttribute("msg", filename + " 파일 업로드에 실패했습니다.");
			ra.addFlashAttribute("msgType", "danger");
		}
		ra.addFlashAttribute("setTable", "city");
		
		return "redirect:/host/main.tm";
	}*/
	/*public String addManyCityImage(Integer cityNo, MultipartFile[] file, RedirectAttributes ra) {
		for(MultipartFile f: file) {
			if(f==null) continue;
			
			String filename = System.currentTimeMillis() + "_" + f.getOriginalFilename();
			
			logger.debug(String.format("%d: %s", cityNo, filename));
			
			// 경로에 파일 저장하기
			File uploadFile = new File(cityImgDirectory, filename);
			try {
				FileCopyUtils.copy(f.getInputStream(), new FileOutputStream(uploadFile));
				hostService.addNewCityImage(cityNo, filename);
				ra.addFlashAttribute("msg", filename + " 파일을 업로드 하였습니다.");
				ra.addFlashAttribute("msgType", "success");
			} catch (IOException e) {
				//e.printStackTrace();
				logger.debug(e.getMessage());
				ra.addFlashAttribute("msg", filename + " 파일 업로드에 실패했습니다.");
				ra.addFlashAttribute("msgType", "danger");
			}
		}
		ra.addFlashAttribute("setTable", "city");
		
		return "redirect:/host/main.tm";
	}*/
	
	/**
	 * 회원들을 탈퇴 처리하는 페이지 매핑
	 * @param data 탈퇴처리할 회원번호를 구분문자(!)로 연결한 문자열 
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping("/host/user/remove.tm")
	public String dropUser(String data, RedirectAttributes ra) {
		
		String[] noArr = data.split(",");
		hostService.dropUser(noArr);
		
		ra.addFlashAttribute("msg", noArr.length + "");
		ra.addFlashAttribute("msg", noArr.length>1? noArr.length + "명의 회원을 탈퇴처리하였습니다.":noArr[0] + "번 회원을 탈퇴처리하였습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "user");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 회원 정보를 입력받은 정보로 갱신하는 페이지 매핑
	 * @param user 입력된 회원 정보
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping(value="/host/user/modify.tm", method=RequestMethod.POST)
	public String modifyUser(UserVO user, RedirectAttributes ra) {	
		
		hostService.updateUser(user);
		
		ra.addFlashAttribute("msg", user.getNo() + "번 회원의 정보를 수정하였습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "user");
		
		return "redirect:/host/main.tm";
	}
	
	/**
	 * 회원을 새롭게 추가하는 페이지 매핑
	 * @param user 입력받은 회원 정보
	 * @param ra 결과 메시지 등 정보를 담을 객체
	 * @return 이동할 페이지(관리자페이지)
	 */
	@RequestMapping(value="/host/user/add.tm", method=RequestMethod.POST)
	public String addUser(UserVO user, RedirectAttributes ra) {
		
		commonService.registerNewUser(user);
		
		ra.addFlashAttribute("msg", user.getName() + " 회원의 정보를 추가했습니다.");
		ra.addFlashAttribute("msgType", "success");
		ra.addFlashAttribute("setTable", "user");
		
		return "redirect:/host/main.tm";
	}
}
