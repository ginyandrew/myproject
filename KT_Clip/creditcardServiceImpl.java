package com.kt.clip2.web.creditcard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.clip2.frw.transaction.TxMain;
import com.kt.clip2.web.creditcard.dao.WebCreditCardDao;
import com.kt.clip2.web.creditcard.vo.CreditCardBenefitEventVO;
 

@Service
@TxMain
public class WebCreditCardServiceImpl implements WebCreditCardService {

	protected static final Logger LOG = LoggerFactory.getLogger(WebCreditCardServiceImpl.class);

	@Autowired
	private WebCreditCardDao creditCardDao;

	
	@Override
	public List<CreditCardBenefitEventVO> getStoreByCardId(String cardId) {
		return creditCardDao.getStoreByCardId(cardId);
	}
	
	public List<CreditCardBenefitEventVO> getBeneritEventListByCardId(
			HashMap<String, Object> typeAndCardID) {
		
		List<CreditCardBenefitEventVO> benefits = new ArrayList<>();
		if (typeAndCardID.get("benefitType").equals("dsctSave") || typeAndCardID.get("benefitType").equals("etc")){
			benefits = creditCardDao.getBenefitListByCardId(typeAndCardID);
		} else if (typeAndCardID.get("benefitType").equals("event")){
			benefits = creditCardDao.getEventListByCardId(typeAndCardID);
		}
		return  benefits;
	}
	
	
}
