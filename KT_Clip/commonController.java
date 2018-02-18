/**
 * 
 */
package com.kt.clip2.web.common.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kt.clip2.frw.exception.MessageException;
import com.kt.clip2.web.common.service.WebCommonService;
import com.kt.clip2.web.common.vo.AgreePrivInfoVO;
import com.kt.clip2.web.common.vo.EventVO;
import com.kt.clip2.web.common.vo.FaqVO;
import com.kt.clip2.web.common.vo.GiftishowTermAgreeVO;
import com.kt.clip2.web.common.vo.NoticeVO;
import com.kt.clip2.web.common.vo.TermAgreeVO;
import com.kt.clip2.web.common.vo.UserAgreeInfoVO;
import com.kt.clip2.web.common.vo.UserThirdPartyAgreeInfoVO;

/**
 * Test를 위해 임시로 view를 리턴함.
 * spec 정의시 구현 후 코멘트 삭제 및 변경 해 주세요.
 * @author blucean
 *
 */
@Controller
@RequestMapping(value = "/cv2/wv/common")
public class CommonController {

	@Value("${app.svr}")
	private String appSvr;
	
	@Autowired
	private WebCommonService webCommonService;
	
	@RequestMapping(value = "/noticeList.do", method = RequestMethod.GET)
	public String noticeList(Model model,  @RequestParam(value="os", required=false) String os) {
		if(os == null || "".equals(os)) {
			os = "common";
		}
		int noticeCount = webCommonService.getNoticePage(os);
		model.addAttribute("noticePage", noticeCount);	
		model.addAttribute("os", os);
		
		return "/common/noticeList";
	}
	
	@RequestMapping(value = "/noticeDetail.do", method = RequestMethod.GET)
	public String noticeDetail(Model model, int idx) {
		
		NoticeVO noticeVO = webCommonService.getTheNotice(idx);
		
		String theContent = "";
		if(noticeVO.getMsgMode().equals("W")){
			theContent = noticeVO.getImgHost() + noticeVO.getUrl();
			noticeVO.setMsg(theContent);
		}  
		
		model.addAttribute("noticeDetail", noticeVO);
		return "/common/noticeDetail";
	}
	
	@RequestMapping(value = "/faqList.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String faqList(Model model, @RequestParam(value="faqPart", required=false) String faqPart) {
		
		List<FaqVO> faqVOs = new ArrayList<>();
		
		if( faqPart == null || faqPart.equals("")){
			faqVOs = webCommonService.getFaQListByPart("GNR");
			model.addAttribute("faqPart", "GNR");
		} else {
			faqVOs = webCommonService.getFaQListByPart(faqPart);
			model.addAttribute("faqPart", faqPart);
		} 
		
		model.addAttribute("faqList", faqVOs);
		return "/common/faqList";
	}
	

	@RequestMapping(value = "/termAgree.do", method = RequestMethod.GET)
	public String termAgree(Model model) {
		
		List<TermAgreeVO> termAgreeVOList = webCommonService.getTermAgreeInfo();
		
		for(TermAgreeVO vo : termAgreeVOList) {
			
			if("PRIV".equalsIgnoreCase(vo.getAgreeType())) {
				model.addAttribute("PRIV", vo.getAgreeUrl());
			} else if("TERM".equalsIgnoreCase(vo.getAgreeType())) {
				model.addAttribute("TERM", vo.getAgreeUrl());
			} else if("MERC".equalsIgnoreCase(vo.getAgreeType())) {
				model.addAttribute("MERC", vo.getAgreeUrl());
			} else if("MEMBUSE".equalsIgnoreCase(vo.getAgreeType())) {
				model.addAttribute("MEMBUSE", vo.getAgreeUrl());
			}
		}
		
		model.addAttribute("appSvr", appSvr);
		
		return "/common/termAgree";
	}
	
	@RequestMapping(value = "/termAgreeList.do", method = RequestMethod.GET)
	public String termAgreeList(Model model) {
	    List<TermAgreeVO> termAgreeVOList = webCommonService.getTermAgreeInfo();
        
        for(TermAgreeVO vo : termAgreeVOList) {
            
            if("PRIV".equalsIgnoreCase(vo.getAgreeType())) { // 개인정보 취급방침
                model.addAttribute("PRIV", vo.getAgreeUrl());
            } else if("TERM".equalsIgnoreCase(vo.getAgreeType())) { // 서비스 이용약관
                model.addAttribute("TERM", vo.getAgreeUrl());
            } else if("MERC".equalsIgnoreCase(vo.getAgreeType())) { // 위치정보이용동의
                model.addAttribute("MERC", vo.getAgreeUrl());
//            } else if("MEMBUSE".equalsIgnoreCase(vo.getAgreeType())) {
//                model.addAttribute("MEMBUSE", vo.getAgreeUrl());
            }
        }
		
        model.addAttribute("appSvr", appSvr);
        
		return "/common/termAgreeList";
	}
	
	@RequestMapping(value = "/noticeList/titles.do", method = RequestMethod.GET)
	public ModelAndView noticeTitles(int pageNo, String os){
		ModelAndView mv = new ModelAndView();
		HashMap<String, Object> map = new HashMap<>();
		map.put("pageNo", pageNo);
		map.put("os", os);
		List<NoticeVO> noticeTitles = webCommonService.getNoticeList(map);
		mv.addObject("noticeTitles", noticeTitles);
		return mv;
	}
	
	 
	
	// 추가 약관 동의
    /*@RequestMapping(value = "/termAgreeMore.do", method = RequestMethod.POST)
    public String termAgreeMore(@RequestParam(value = "custId", required = true) String custId, Model model) {
    
        model.addAttribute("custId", custId);
        
        return "/common/termAgreeMore";
    }*/
    
	// 추가 약관 동의
    @RequestMapping(value = "/termAgreeMore.do", method = RequestMethod.POST)
	public String termAgreeMore(@RequestParam(value = "custId", required = true) String custId, Model model) {
		
        AgreePrivInfoVO agreePrivInfo = webCommonService.getAgreePrivInfo();
        String agreeTermUrl = webCommonService.getAgreeTermInfo();
        String agreeMercUrl = webCommonService.getAgreeMercInfo();
        UserAgreeInfoVO userAgreeInfo = webCommonService.getUserAgreeInfo(custId);
        UserThirdPartyAgreeInfoVO userThirdPartyAgreeInfo = webCommonService.getUserThirdPartyAgreeInfo(custId);
        String memberAgreeUrl = webCommonService.getMemberAgreeInfo();
    	
        model.addAttribute("agreePrivInfo", agreePrivInfo);
        model.addAttribute("agreeTermUrl", agreeTermUrl);
        model.addAttribute("agreeMercUrl", agreeMercUrl);
        model.addAttribute("userAgreeInfo", userAgreeInfo);
        model.addAttribute("userThirdPartyAgreeInfo", userThirdPartyAgreeInfo);
        model.addAttribute("memberAgreeUrl", memberAgreeUrl);
		
		model.addAttribute("custId", custId);
		model.addAttribute("appSvr", appSvr);
		
		return "/common/termAgreeMore";
	}

    @RequestMapping(value = "/updateTermAgreeMore.do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public int updateTermAgreeMore(
    		 @RequestParam(value = "custId", required = true) String custId
    		,@RequestParam(value = "agree", required = true) String agree
    		) {
 		
 		Map<String,String> updateMap = new HashMap<String,String>();
 		updateMap.put("custId", custId);
 		updateMap.put("agree", agree);
 		
        return webCommonService.updateTermAgreeMore(updateMap);
    }
    

    // 기프티쇼 약관 동의 웹뷰
    @RequestMapping(value = "/termAgreeGiftishow.do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String termAgreeGiftishow(
			@RequestParam(value = "custId", required = true) String custId, Model model) {
		
		if("".equals(custId) || custId == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"custId"} );
		}
		
		List<GiftishowTermAgreeVO> giftishowTermAgreeList = webCommonService.getGiftishowTermAgreeList();
		
		model.addAttribute("giftishowTermAgreeList", giftishowTermAgreeList);
		model.addAttribute("custId", custId);
		
		return "/common/termAgreeGiftishow";
	}

    // 공지사항 팝업
    @RequestMapping(value = "/noticePopup.do", method = RequestMethod.GET)
    public String noticePopup(Model model) {
        
        NoticeVO noticeVO = webCommonService.getNoticePopup();
        
        String theContent = "";
        if(noticeVO.getMsgMode().equals("W")){
            theContent = noticeVO.getImgHost() + noticeVO.getUrl();
            noticeVO.setMsg(theContent);
        }  
        
        model.addAttribute("noticeDetail", noticeVO);
        return "/common/noticePopup";
    }
    
    // 이벤트 팝업
    @RequestMapping(value = "/eventPopup.do", method = RequestMethod.GET)
    public String eventPopup(Model model) {
        
        EventVO eventVO = webCommonService.getEventPopup();
        
        /*String theContent = "";
        if(noticeVO.getMsgMode().equals("W")){
            theContent = noticeVO.getImgHost() + noticeVO.getUrl();
            noticeVO.setMsg(theContent);
        }  */
        long current = System.currentTimeMillis();
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyyMMdd");
		String todayYMDHMS = formatYMD.format(new Date(current));
		
        model.addAttribute("eventList", eventVO);
        model.addAttribute("currentDate", todayYMDHMS);
        
        return "/common/eventPopup";
    }

    // 서비스 지연
    @RequestMapping(value = "/serviceDelay.do", method = RequestMethod.GET)
	public String serviceDelay() {
		
		return "/common/serviceDelay";
	}
    
    // 네트워크 차단
    @RequestMapping(value = "/networkOff.do", method = RequestMethod.GET)
	public String networkOff() {
		
		return "/common/networkOff";
	}
    
    // No Data
    @RequestMapping(value = "/noData.do", method = RequestMethod.GET)
	public String noData() {
		
		return "/common/noData";
	}
}
