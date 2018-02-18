package com.kt.clip2.web.common.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kt.clip2.frw.exception.MessageException;
import com.kt.clip2.frw.transaction.TxMain;
import com.kt.clip2.frw.transaction.TxRequired;
import com.kt.clip2.web.common.dao.WebUserDao;
import com.kt.clip2.web.common.vo.CertificatePinVO;
import com.kt.clip2.web.common.vo.InfoUpdateVO;
import com.kt.clip2.web.common.vo.UpdateUserInfoVO;

/**
 * 멤버십 Service
 * 
 * @author		: Blucean
 * @version		: v2.0
 * @since		: 2016.08.02
 */
@TxMain
@Service
public class WebUserServiceImpl implements WebUserService{
	
	private static final Logger logger = LoggerFactory.getLogger(WebUserServiceImpl.class);
	
	@Autowired
	private WebUserDao webUserDao; 
	
	@Value("${webview.ipin.id}")
	private String ipinId;
	
	@Value("${webview.ipin.svcno}")
	private String ipinSvcNo;
	
	@Value("${webview.ipin.return.url}")
	private String ipinReturnUrl;
	
	@Value("${app.svr}")
	private String appSvr;
	
	@Override
	public InfoUpdateVO getUserInfo(int custId) {
		return webUserDao.getUserInfo(custId);
	}
	
	@Override
	@TxRequired
	public int updateUserInfo(UpdateUserInfoVO updateUserInfoVO) {
		return webUserDao.updateUserInfo(updateUserInfoVO);
	}
	
	@Override
	public String getAdAgree(int custId) {
		return webUserDao.getAdAgree(custId);
	}
	
	@Override
	@TxRequired
    public int updateAdAgree(int custId, String agreeYn) {
        return webUserDao.updateAdAgree(custId, agreeYn);
    }
	
	@Override
    public String getThirdPartyAgree(int custId, String serviceId) {
    	return webUserDao.getThirdPartyAgree(custId, serviceId);
    }
	
    @Override
    @TxRequired
    public int updateThirdPartyAgree(int custId, String serviceId, String agreeYn) {
        return webUserDao.updateThirdPartyAgree(custId, serviceId, agreeYn);
    }
    
    @Override
	@TxRequired
    public int updateBcPayAgree(int custId, String agreeYn) {
		int resultCnt = 0;
		resultCnt += webUserDao.updateBcPayAgree(custId, agreeYn);
		resultCnt += webUserDao.updateUserBcCardAgree(custId, agreeYn);
		
        return resultCnt;
    }
    
    @Override
	@TxRequired
    public int updateExtPayAgree(String payTypeCd, int custId, String agreeYn) {
		
    	int resultCnt = 0;
    	
    	if("bcpay".equals(payTypeCd)) {
    		resultCnt += webUserDao.updateExtPayAgree(payTypeCd, custId, agreeYn);
    		resultCnt += webUserDao.updateUserBcCardAgree(custId, agreeYn);
    	} else {
    		resultCnt = webUserDao.updateExtPayAgree(payTypeCd, custId, agreeYn);
    	}
        return resultCnt;
    }
    
    @Override
    public Map<String,String> encryptRequestInfo(String reqNum) {
    	
    	Map<String,String> returnMap = new HashMap<String,String>();
    	
    	String id = ipinId;
	    String srvNo   = ipinSvcNo; // TB
	    String retUrl      = "23" + appSvr + ipinReturnUrl;	//결과 수신 URL http://dev.api2.ktclip.net/cv2/common/user/certificatePinReceive.do
	    //String retUrl      = "23http://221.148.247.48" + ipinReturnUrl;	//(임시) 도메인 나오면 위의 것으로 변경해야함
		String exVar       = "0000000000000000";    // 복호화용 임시필드
		
		if(logger.isInfoEnabled()) {
			logger.info(" ## id : " + ipinId);
			logger.info(" ## srvNo : " + ipinSvcNo);
			logger.info(" ## reqNum : " + reqNum);
		}
		
	    // 암호화 모듈 선언
		com.sci.v2.ipin.secu.SciSecuManager seed  = new com.sci.v2.ipin.secu.SciSecuManager();

		// 1차 암호화
		String encStr = "";
		String reqInfo      = reqNum+"/"+id+"/"+srvNo+"/"+exVar;  // 데이터 암호화
		encStr              = seed.getEncPublic(reqInfo);

		// 위변조 검증 값 등록
		/*com.sci.v2.ipin.secu.hmac.SciHmac hmac = new com.sci.v2.ipin.secu.hmac.SciHmac();
		String hmacMsg = hmac.HMacEncriptPublic(encStr);*/
		
		String hmacMsg  = com.sci.v2.ipin.secu.hmac.SciHmac.HMacEncriptPublic(encStr);

		// 2차 암호화
		reqInfo  = seed.getEncPublic(encStr + "/" + hmacMsg + "/" + "00000000");  //2차암호화
		
		if(logger.isInfoEnabled()) {
			logger.info(" ## reqInfo : " + reqInfo);
			logger.info(" ## retUrl : " + retUrl);
		}
		
		returnMap.put("reqInfo", reqInfo);
		returnMap.put("retUrl", retUrl);
		
		return returnMap;
    }
    
    @Override
    public CertificatePinVO decryptRequestInfo(String reqNum, String retInfo) {
    	
    	// 변수 -------------------------------------------------------------------------------------------------------------
		String encPara      = "";
		String decPara      = "";
	    String encMsg       = "";                                                        // HMAC 메세지
	    String vDiscrNo     = "";                                                        // 가상식별번호
	    String name         = "";                                                        // 성명
	    String result       = "";                                                        // 결과값 (1인경우에만 정상)
		String age          = "";
	    String sex          = "";
		String ip           = "";
	    String authInfo     = "";														 // 발급수단정보	
	    String birth        = "";
	    String fgn          = "";                                                        // 외국인구분
		String discrHash    = "";                                                        // 중복가입확인정보
		String ciVersion	= "";														 // 연계정보 버젼
		String ciscrHash    = "";                                                        // 연계정보
		String msgChk       = "N";                                                    // 위조/변조 검증 결과

	    //-----------------------------------------------------------------------------------------------------------------
		
		try{

			String tranjectionReqNum = "";
			
			tranjectionReqNum = reqNum;
		
	        if(logger.isInfoEnabled()) {
	        	logger.info(" ## tranjectionReqNum : " + tranjectionReqNum);
				logger.info(" ## retInfo : " + retInfo);
			}

	        // 1. 암호화 모듈 (jar) Loading
	        com.sci.v2.ipin.secu.SciSecuManager sciSecuMg = new com.sci.v2.ipin.secu.SciSecuManager();

	        retInfo  = sciSecuMg.getDec(retInfo, tranjectionReqNum);
	        /*if(logger.isInfoEnabled()) {
				logger.info(" ## getDec retInfo : " + retInfo);
			}*/
	        StringBuffer retInfoTemp    = new StringBuffer("");

	        // 2.1차 파싱---------------------------------------------------------------
	        int inf1 = retInfo.indexOf("/",0);
	        int inf2 = retInfo.indexOf("/",inf1+1);

	        
			encPara  = retInfo.substring(0,inf1);         //암호화된 통합 파라미터
	        encMsg   = retInfo.substring(inf1+1,inf2);    //암호화된 통합 파라미터의 Hash값

	        
	        // 3.위/변조 검증 ---------------------------------------------------------------
	        if(sciSecuMg.getMsg(encPara).equals(encMsg)){
	            msgChk="Y";
	        }

	        
			if(msgChk.equals("N")){
				throw new MessageException("SVC.PIN_5001.500");
			}
			
			// 4.파라미터별 값 가져오기 ---------------------------------------------------------------
	        decPara  = sciSecuMg.getDec(encPara, tranjectionReqNum);

			int info1 = decPara.indexOf("/",0);
	        int info2 = decPara.indexOf("/",info1+1);
	        int info3 = decPara.indexOf("/",info2+1);
	        int info4 = decPara.indexOf("/",info3+1);
	    	int info5 = decPara.indexOf("/",info4+1);
	        int info6 = decPara.indexOf("/",info5+1);
	        int info7 = decPara.indexOf("/",info6+1);
	        int info8 = decPara.indexOf("/",info7+1);
	        int info9 = decPara.indexOf("/",info8+1);
	        int info10 = decPara.indexOf("/",info9+1);
	        int info11 = decPara.indexOf("/",info10+1);
	        int info12 = decPara.indexOf("/",info11+1);
	        int info13 = decPara.indexOf("/",info12+1);

			reqNum     = decPara.substring(0,info1);
	        vDiscrNo   = decPara.substring(info1+1,info2);
	        name       = decPara.substring(info2+1,info3);
	        result     = decPara.substring(info3+1,info4);
	        age        = decPara.substring(info4+1,info5);
	        sex        = decPara.substring(info5+1,info6);
	        ip         = decPara.substring(info6+1,info7);
	        authInfo   = decPara.substring(info7+1,info8);
	        birth      = decPara.substring(info8+1,info9);
	        fgn        = decPara.substring(info9+1,info10);
	        discrHash  = decPara.substring(info10+1,info11);
	        ciVersion  = decPara.substring(info11+1,info12);
			ciscrHash  = decPara.substring(info12+1,info13);	
			
			discrHash  = sciSecuMg.getDec(discrHash, tranjectionReqNum); //중복가입확인정보는 한번더 복호화
			ciscrHash  = sciSecuMg.getDec(ciscrHash, tranjectionReqNum); //연계정보는 한번더 복호화
			
			if(logger.isInfoEnabled()) {
				logger.info(" ## reqNum : " + reqNum);
				logger.info(" ## result : " + result);
				logger.info(" ## ciscrHash : " + ciscrHash);
			}
			
			CertificatePinVO certificatePinVO = new CertificatePinVO();
			certificatePinVO.setReqNum(reqNum);
			certificatePinVO.setvDiscrNo(vDiscrNo);
			certificatePinVO.setName(name);
			certificatePinVO.setResult(result);
			certificatePinVO.setAge(age);
			certificatePinVO.setSex(sex);
			certificatePinVO.setIp(ip);
			certificatePinVO.setAuthInfo(authInfo);
			certificatePinVO.setBirth(birth);
			certificatePinVO.setFgn(fgn);
			certificatePinVO.setDiscrHash(discrHash);
			certificatePinVO.setCiVersion(ciVersion);
			certificatePinVO.setCiscrHash(ciscrHash);
			
			if(logger.isInfoEnabled()) {
				logger.info(" ## certificatePinVO : " + certificatePinVO.toString());
			}
			
			return certificatePinVO;
			
    } catch(Exception ex){
        logger.error("[IPIN] Receive Test Error - {}",ex.getMessage());
        
        throw new MessageException("SVC.PIN_5002.500");
    }
		
  }
}
