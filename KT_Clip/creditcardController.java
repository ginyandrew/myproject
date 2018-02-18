/**
 * 
 */
package com.kt.clip2.web.creditcard.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kt.clip2.web.creditcard.service.WebCreditCardService;
import com.kt.clip2.web.creditcard.vo.CreditCardBenefitEventVO;

/**
 * @author blucean
 *
 */
@Controller("v2CreditCardController")
@RequestMapping(value = "/cv2/wv/creditcard")
public class CreditCardController {

	@Autowired
	private WebCreditCardService creditCardService;
	
	
	// 2016.09.09 웹뷰 호출 URL 수정
	//@RequestMapping(value= "/bcPay/store.do", method=RequestMethod.GET)
	@RequestMapping(value= "/storeInfo.do", method=RequestMethod.GET)
	public String getStoreByCardId(Model model, String cardId){
		
		List<CreditCardBenefitEventVO> storesByCardId = creditCardService.getStoreByCardId(cardId);
		
		int contentLi = storesByCardId.size() ;			// 실제 li 갯수 	ex)5
		int loop = (int)Math.ceil(contentLi / 4)+1;	
		int totalCount = loop * 4 ;						// 총 들어갈 li갯수 	ex)8
		int extraLi = totalCount - contentLi;			// 추가할 li갯수 	ex)3개
		
		for (int i=0 ; i< extraLi ; i++){
			CreditCardBenefitEventVO vo = new CreditCardBenefitEventVO();
			vo.setShopId(" ");
			storesByCardId.add(vo);
		}
		
		model.addAttribute("storesByCardId", storesByCardId);
		
		return "/creditcard/bcPayStore";
	}
	
	// 2016.09.09 웹뷰 호출 URL 수정
	//@RequestMapping(value = "/bcPay/benefit.do", method= RequestMethod.GET)
	@RequestMapping(value = "/benefitInfo.do", method= RequestMethod.GET)
	public String getBenefitEventByCardId(Model model, String cardId){
		
		model.addAttribute("cardId", cardId);
		
		// 특정 카드 아이디가 들어오면, 해당 카드의 모든 것을 하나로 모아 보여준다.
		// (1) 할인/적립혜택 가져오기
		HashMap<String, Object> dsctSaveKey = new HashMap<>();
		dsctSaveKey.put("cardId", cardId);
		dsctSaveKey.put("benefitType", "dsctSave");
		List<CreditCardBenefitEventVO> dsctSaveResult = new ArrayList<>();	
		dsctSaveResult = creditCardService.getBeneritEventListByCardId(dsctSaveKey);
		model.addAttribute("dsctSaveResult", dsctSaveResult);
		
		// (2) 기타혜택 가져오기
		HashMap<String, Object> etcKey = new HashMap<>();
		etcKey.put("cardId", cardId);
		etcKey.put("benefitType", "etc");
		List<CreditCardBenefitEventVO> etcResult = new ArrayList<>();	
		etcResult = creditCardService.getBeneritEventListByCardId(etcKey);
		model.addAttribute("etcResult", etcResult);
		
		// (3) 이벤트 가져오기
		HashMap<String, Object> eventKey = new HashMap<>();
		eventKey.put("cardId", cardId);
		eventKey.put("benefitType", "event");
		List<CreditCardBenefitEventVO> eventResult = new ArrayList<>();	
		eventResult = creditCardService.getBeneritEventListByCardId(eventKey);
		model.addAttribute("eventResult", eventResult);
		
		return "/creditcard/bcPayBenefit";
	}
	
	
	@RequestMapping(value = "/info.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String useHistory(Model model, 
			@RequestParam(value="benefitType", required=false) String benefitType, 
			@RequestParam(value="cardId", required=true) String cardId) {
		
		HashMap<String, Object> typeAndCardID = new HashMap<>();
		
		typeAndCardID.put("cardId", cardId);	
		model.addAttribute("cardId", cardId);
		
		List<CreditCardBenefitEventVO> benefitList = new ArrayList<>();		 
		
		if( benefitType == null || benefitType.isEmpty()){
			typeAndCardID.put("benefitType", "dsctSave");					 
			model.addAttribute("benefitType", "dsctSave");
		} else {
			typeAndCardID.put("benefitType", benefitType);					// 적립,할인 또는 기타혜택 을 뜻하는 값이 있을 경우 (구분값 : dsctSave, etc, event)
			model.addAttribute("benefitType", benefitType);
		}
		benefitList = creditCardService.getBeneritEventListByCardId(typeAndCardID);
		model.addAttribute("benefitList", benefitList);
		
		return "/creditcard/info";
	}
}
