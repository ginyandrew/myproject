/**
 * 
 */
package com.kt.clip2.web.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.clip2.frw.cache.ReadCache;
import com.kt.clip2.frw.transaction.TxMain;
import com.kt.clip2.frw.transaction.TxRequired;
import com.kt.clip2.web.common.dao.WebCommonDao;
import com.kt.clip2.web.common.vo.AgreePrivInfoVO;
import com.kt.clip2.web.common.vo.EventVO;
import com.kt.clip2.web.common.vo.FaqVO;
import com.kt.clip2.web.common.vo.GiftishowTermAgreeVO;
import com.kt.clip2.web.common.vo.NoticeVO;
import com.kt.clip2.web.common.vo.TermAgreeVO;
import com.kt.clip2.web.common.vo.UserAgreeInfoVO;
import com.kt.clip2.web.common.vo.UserThirdPartyAgreeInfoVO;

/**
 * @author Bonyu.Gu
 *
 */
@Service
@TxMain
public class WebCommonServiceImpl implements WebCommonService {

	protected static final Logger logger = LoggerFactory.getLogger(WebCommonServiceImpl.class);
	
	@Autowired
	private WebCommonDao webCommonDao;
	
	public List<TermAgreeVO> getTermAgreeInfo() {
		return webCommonDao.getTermAgreeInfo();
	}

	public int getNoticePage(String os){
		return webCommonDao.getNoticePage(os);
	}
	
	@ReadCache(namespace = "WebCommonServiceImpl_getNoticeList", keyIndex = {1})
	public List<NoticeVO> getNoticeList(HashMap<String, Object> map) {
		return webCommonDao.getNoticeList(map);
	}
	
	@ReadCache(namespace = "WebCommonServiceImpl_getTheNotice", keyIndex = {1})
	public NoticeVO getTheNotice(int idx) {
		return webCommonDao.getTheNotice(idx);
	}
	
	@ReadCache(namespace = "WebCommonServiceImpl_getFaQListByPart", keyIndex = {1})
	public List<FaqVO> getFaQListByPart(String part) {
		return webCommonDao.getFaQListByPart(part);
	}
    
    public List<GiftishowTermAgreeVO> getGiftishowTermAgreeList() {
        return webCommonDao.getGiftishowTermAgreeList();
    }  
    
    // 공지사항 팝업
    public NoticeVO getNoticePopup() {
        return webCommonDao.getNoticePopup();
    }
    
    // 이벤트 팝업
    public EventVO getEventPopup() {
        return webCommonDao.getEventPopup();
    }
    
    public AgreePrivInfoVO getAgreePrivInfo() {
    	return webCommonDao.getAgreePrivInfo();
    }

    public String getAgreeTermInfo() {
    	return webCommonDao.getAgreeTermInfo();
    }
    
    public String getAgreeMercInfo() {
    	return webCommonDao.getAgreeMercInfo(); 
    }
   
    public UserAgreeInfoVO getUserAgreeInfo(String custId) {
    	return webCommonDao.getUserAgreeInfo(custId);
    }
   
    public UserThirdPartyAgreeInfoVO getUserThirdPartyAgreeInfo(String custId) {
    	return webCommonDao.getUserThirdPartyAgreeInfo(custId);
    }

    public String getMemberAgreeInfo() {
    	return webCommonDao.getMemberAgreeInfo();
    }
    
    @TxRequired
    public int updateTermAgreeMore(Map<String,String> updateMap) {
    	
    	String custId = updateMap.get("custId");
    	String agree = updateMap.get("agree");
		
		String[] agreeString = new String[agree.split("[|]").length];
		agreeString = agree.split("[|]");
		String agreePri = null; // 개인정보 수집/이용 동의(필수)
		String agreeAd = null; // 정보/광고 수신동의(선택)
		String agreeBcStamp = null; // BC스탬프 이용약관 동의(선택), BC스탬프 개인정보 수집/이용 동의(선택), BC스탬프 개인정보 제3자 제공에 대한 동의(선택)
		
		if(logger.isInfoEnabled()) {
			logger.info(" ## agreeString.length : " + agreeString.length);
		}
		
		for(String agreeSet : agreeString) {
			
			if(logger.isInfoEnabled()) {
				logger.info(" ## agreeSet : " + agreeSet);
			}
			
			//int agreeSetCnt = agreeSet.split("[:]").length;
			String[] agreeKeyValue = new String[agreeSet.split("[:]").length];
			agreeKeyValue = agreeSet.split("[:]");
			String agreeKey = agreeKeyValue[0];
			String agreeValue = agreeKeyValue[1];
			
			if("agreePri".equals(agreeKey)) {
				agreePri = agreeValue;
			} else if("agreeAd".equals(agreeKey)) {
				agreeAd = agreeValue;
			} else if("agreeBcStamp".equals(agreeKey)) {
				agreeBcStamp = agreeValue;
			}
		}
		
		// user_info 개인정보수집, 정보광고 Update
		Map<String,String> updateUserAgreeMap = new HashMap<String,String>();
		updateUserAgreeMap.put("custId", custId);
		updateUserAgreeMap.put("agreePri", agreePri);
		updateUserAgreeMap.put("agreeAd", agreeAd);
		int updateCnt = webCommonDao.updateTermAgreeMore(updateUserAgreeMap);
		
		// user_extpay_agree BcStamp Update
		Map<String,String> updateBcStampMap = new HashMap<String,String>();
		updateBcStampMap.put("payTypeCd", "bckock" );
		updateBcStampMap.put("custId", custId);
		updateBcStampMap.put("agreeYn", agreeBcStamp);
		updateCnt += webCommonDao.userExtpayAgreeUpdate(updateBcStampMap);
		
    	return updateCnt;
    }
}
