package com.osstem.aems.sch.sch.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





















































import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.json.JsonParser;
import com.osstem.aems.main.model.MainVo;
import com.osstem.aems.main.service.MainService;
import com.osstem.aems.sch.sch.model.ApprSpkDto;
import com.osstem.aems.sch.sch.model.SchDtlDto;
import com.osstem.aems.sch.sch.model.SchMbrDto;
import com.osstem.aems.sch.sch.model.SchRegDto;
import com.osstem.aems.sch.sch.model.SchSchDto;
import com.osstem.aems.sch.sch.model.SendDto;
import com.osstem.aems.sch.sch.model.SendPsnDto;
import com.osstem.aems.sch.sch.service.DateUtil;
import com.osstem.aems.sch.sch.service.SchSchService;
import com.osstem.comm.code.model.CodeDto;
import com.osstem.comm.code.model.CodeVo;
import com.osstem.comm.com.util.string.StringUtil;
import com.osstem.comm.file.model.FileDto;
import com.osstem.comm.file.service.FileService;
import com.osstem.comm.page.service.PageService;
import com.osstem.comm.sawon.model.SawonDto2;
import com.osstem.comm.sawon.service.SawonService;
import com.osstem.comm.test.web.TestController;

@Controller
@RequestMapping(value="/sch/sch.do")
public class SchSchController {
	
	private Logger logger = Logger.getLogger(TestController.class);
	
	@Resource(name="MainService")
	private MainService mainService;

	@Resource(name="SchSchService")
	SchSchService schSchService;
	
	@Resource(name="PageService")
	private PageService pageService;
	
	@Resource(name="FileService")
	private FileService fileService;
	
	@Resource(name="SawonService")
	private SawonService sawonService;
	
	/*
	 * 공통 - 검색어 자동완성 
	 */
	@RequestMapping(params = "method=searchName")
	@ResponseBody
	public ModelAndView autoSearchName(HttpServletRequest req) throws UnsupportedEncodingException, SQLException{
		ModelAndView mav = new ModelAndView();

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();

		String input = (String)URLDecoder.decode(req.getParameter("input"), "UTF-8");
		map.put("input", input);

		String type = req.getParameter("type");
		map.put("type", type);
		
		if (type.equals("name")){;
			String mbrGB = req.getParameter("mbrGB");		// 원장 지위 
			map.put("mbrGB", mbrGB);
			list = schSchService.selectPsnHosInfo(map);
			mav.addObject("keys" , list);
			
		} else if (type.equals("schDetail")){
			HashMap<String, Object> schDtlMbrInfo = schSchService.selectSchDtlMbr(Integer.parseInt(input));
			mav.addObject("keys", schDtlMbrInfo);
			
		} else if (type.equals("title")){
			list = schSchService.selectSchTitle(map);
			mav.addObject("keys" , list);
		
		} else if (type.equals("titleNonTemp")){
			String gb_01 = (String)URLDecoder.decode(req.getParameter("mbrGB"), "UTF-8");
			map.put("gb01", gb_01);
			map.put("tempYn", "N");
			list = schSchService.selectSchTitle(map);
			mav.addObject("keys" , list);
		
		} else if (type.equals("sch")){
			list = schSchService.autoSearchSchBySchId(map);
			mav.addObject("keys" , list);
		
		} else if (type.equals("allName")){					// 모든 psn을 이름으로 검색.
			list = schSchService.selectAllPsnHosInfo(map);
			mav.addObject("keys" , list);
		
		} else if (type.equals("mbrName")){					// MBR에 등록된 적 있는 psn만 검색.
			list = schSchService.selectAllPsnHosInfoByNameMbr(map);
			mav.addObject("keys" , list);
			
		} else if (type.equals("group")){		// 상세한 내용이 아니라 그룹명 / 또는 이름만 일단 나오면 된다.
			list = schSchService.selectGrpByGrpName(map);
			mav.addObject("keys" , list);
		
		} else if (type.equals("psnNotInReg")){
			String schId = req.getParameter("schId");		// 원장 지위 
			map.put("schId", schId);
			list = schSchService.selectAllPsnHosInfoByNameNotInReg(map);		// 하나의 sch로 Reg에 등록된적 있는 psn은 제외 
			mav.addObject("keys", list);
			
		} else if (type.equals("deleteGrp")){
			schSchService.deleteGroupbyGrpId(map);
		
		} else if (type.equals("schByYearPlace")){
			String month = req.getParameter("month");
			String year = req.getParameter("year");
			map.put("gb02", "01");
			map.put("month", month);
			map.put("year", year);
			map.put("place", input);
			list = schSchService.selectSch01ByMonth(map);
			mav.addObject("keys", list);

		} else if (type.equals("schDtl")){
			String[] sch = req.getParameter("input").split(",");
			map.put("schId", sch[0]);
			map.put("schNo", sch[1]);
			String dtlContent = schSchService.selectSchDtl(map).getContent();
			mav.addObject("keys", dtlContent);
			
		} else if (type.equals("updateRest")){
			HttpSession session = req.getSession();
			String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
			String psnInfo = req.getParameter("psnInfo");
			JSONParser parser = new JSONParser();
			Object obj = null;
			try {
				obj = parser.parse(psnInfo);
			} catch (Exception e) {
				logger.error("",e);
			}
			JSONArray restYnArr = (JSONArray) obj;
			for (int i = 0; i < restYnArr.size() ; i++){
				String[] info = ((String)(restYnArr.get(i))).split(",");
				//String[] info = ((String)rest.get("info")).split(",");

				MainVo vo =new MainVo();
				vo.setSch_id(input);
				vo.setPsn_id(info[0]);
				vo.setRest_yn(info[1]);
				vo.setReg_id(sabun);
				mainService.updateRestYn(vo);
			}
		} else if (type.equals("mbrNameBySchId")){
			map.put("schId", input);
			HashMap<String, Object> result = schSchService. selectSchMbrInfoBySchId(map); 	// schId만 넣어주면 01~05 의 mbrName을 return.
			mav.addObject("keys", result);
		}
		
		mav.setViewName("pageJsonReport");

		return mav;
	}
	
	/**
	 * 세부일정 - (1) 강연회/연수회 등록 초기화면
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schDetailRegistView")
	public ModelAndView schDetailRegistView(@ModelAttribute("vo")SchSchDto vo,HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		String name = StringUtil.isNullToString(session.getAttribute("S_NAME"));
		String orgCd = StringUtil.isNullToString(session.getAttribute("S_EHR_ORG_CD"));
		String orgNm = StringUtil.isNullToString(session.getAttribute("S_ORG_NM"));
		
		String temp_yn = "N";				// 일정등록이므로 기본값 n.
		
		//일정탬플릿 관리 여부 
		String templet_gb = ObjectUtils.toString(vo.getTemplet_gb());
		
		//구분1
		String sch_gb_01 = ObjectUtils.toString(vo.getSch_gb_01());
		if(sch_gb_01.equals("")||sch_gb_01 == null) sch_gb_01 = "01";
	    
		//구분2
		String sch_gb_02 = ObjectUtils.toString(vo.getSch_gb_02());
		if(sch_gb_02.equals("")||sch_gb_02 == null) sch_gb_02 = "01";
		
		//공통 코드 가져오기
		CodeVo hrVo = new CodeVo();
		hrVo.setUp_comm_tree_cd("AEMS004");		 
		List<CodeDto> hrCodes = schSchService.getCodeList(hrVo); 	// 시간 (hour)
		mav.addObject("hr", hrCodes);		// 각 hrVo안에 comm_cd, comm_cd_nm 
		
		CodeVo minVo = new CodeVo();
		minVo.setUp_comm_tree_cd("AEMS005");		 
		List<CodeDto> minCodes = schSchService.getCodeList(minVo); 	// 분 (minutes)
		mav.addObject("min", minCodes);		// 각 minVo안에 comm_cd, comm_cd_nm 
		
		CodeVo subVo = new CodeVo();
		subVo.setUp_comm_tree_cd("AEMS0013");		 
		List<CodeDto> subCodes = schSchService.getCodeList(subVo); 	// 연제 주제
		mav.addObject("sub", subCodes);		// 각 subVo안에 comm_cd, comm_cd_nm 
		
		logger.debug(sch_gb_01 + "/" + sch_gb_02 + " = result");
	    mav.addObject("sch_gb_01", sch_gb_01);
	    mav.addObject("sch_gb_02", sch_gb_02);
	    mav.addObject("templet_gb", templet_gb);
	    mav.addObject("temp_yn", temp_yn);
	    mav.addObject("reg_id", sabun);
	    mav.addObject("org_nm", orgNm);
	    mav.addObject("org_cd", orgCd);
	    mav.addObject("name", name);
	    mav.addObject("sabun", sabun);
	    
		mav.setViewName("sch/sch/schDetailRegistView");
		
		return mav;
	}

	
	/*
	 * 세부일정 - (2) 강연회/연수회 내용 저장 (insert)
	 */
	@RequestMapping(params = "method=saveMultiData", method=RequestMethod.POST)
	public ModelAndView  saveMultiData (SchSchDto schSch, SchDtlDto schDtlList, SchMbrDto schMbrList) throws Exception{

		ModelAndView mav = new ModelAndView();
		schSchService.insertSchDtlMbr(schSch, schDtlList, schMbrList); 		// 저장.
		
		mav.setView(new RedirectView("sch.do?method=schDetailRegistView"));
		return mav;
	}
	
	
	/**
	 * 세부일정 - (3) 기존 일정 보기
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schDetailView")
	public ModelAndView schDetailView(HttpServletRequest request, 
			@RequestParam(value="schId", required=true) int schId) throws Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		mav.addObject("sabun", sabun);
		
		//공통 코드 가져오기
		CodeVo hrVo = new CodeVo();
		hrVo.setUp_comm_tree_cd("AEMS004");		 
		List<CodeDto> hrCodes = schSchService.getCodeList(hrVo); 	// 시간 (hour)
		mav.addObject("hr", hrCodes);		// 각 hrVo안에 comm_cd, comm_cd_nm 
		
		CodeVo minVo = new CodeVo();
		minVo.setUp_comm_tree_cd("AEMS005");		 
		List<CodeDto> minCodes = schSchService.getCodeList(minVo); 	// 분 (minutes)
		mav.addObject("min", minCodes);		// 각 minVo안에 comm_cd, comm_cd_nm 
		
		CodeVo subVo = new CodeVo();
		subVo.setUp_comm_tree_cd("AEMS0013");		 
		List<CodeDto> subCodes = schSchService.getCodeList(subVo); 	// 연제 주제
		mav.addObject("sub", subCodes);		// 각 subVo안에 comm_cd, comm_cd_nm 
				
		// 0. 먼저 sch_id에 따른 sch, sch_dtl, sch_mbr 정보를 가져온다.
		HashMap<String, Object> map = schSchService.selectSchDtlMbr(schId);
		// 1.  강연회인지 아닌지 구분하고
		map.put("input", schId);
		SchSchDto sch = (SchSchDto) map.get("sch");
		
		String gb01 = sch.getSch_gb_01();
		// 2. 강연회/연수회에 따라 gb01/02 페이지를 구분해 dto 값을 전달한다.
		mav.addObject("gb01", gb01);

		mav.addObject("info",map);

		mav.addObject("schDt", sch.getSch_sta_dt().substring(0, 10));
		mav.addObject("schStaHr", sch.getSch_sta_dt().substring(11, 13));
		mav.addObject("schFinHr", sch.getSch_fin_dt().substring(11, 13));
		
		// apprSch나 apprSpk에 schId가 이미 존재하면 삭제/수정이 불가능하다. >> 여기서 다른 걸로 변경.
		// send에 이미 등록되어있거나 이 강연의 날짜가 현재 또는 과거의 것이면 수정이 불가능하게 해야 한다.
		
		boolean ifCanModify = true;
		if (schSchService.selectApprSchCntBySchId(schId) > 0 ){
			ifCanModify = false;
		} else if (schSchService.selectApprSpkCntBySchId(schId) > 0){
			ifCanModify = false;
		}  else if (schSchService.selectSendSchCntBySchId(schId) > 0){
			ifCanModify = false;
		} 
		/*
		 * else if (schSchService.selectSchCntBySchIdNTime(schId) < 1){
			ifCanModify = false;
		}
		 */
		mav.addObject("ifCanModify",  ifCanModify);
		
		mav.setViewName("sch/sch/schDetailView");
		
		return mav;
	}
	
	
	/**
	 * 세부일정 - (4) 기존 일정 업데이트 (수정)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schUpdateView")
	public ModelAndView schUpdateView(HttpServletRequest request, SchSchDto schSch, SchDtlDto schDtlList, SchMbrDto schMbrList) throws Exception {

		ModelAndView mav = new ModelAndView();

		schSchService.updateSchDtlMbr(request, schSch, schDtlList, schMbrList);
		mav.setView(new RedirectView("sch.do?method=schTempletManageView"));
		return mav;
	}
	
	
	/**
	 * 일정탬플릿 - (1) 리스트 화면 (초기 or 검색)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schTempletManageView")
	public ModelAndView schTempletManageView (@ModelAttribute("vo")SchSchDto vo, HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 처음엔 gb이든 time 이든 안 들어감. 페이지만 들어감.
		int pageNo = Integer.parseInt(StringUtil.isNullToString(req.getParameter("pageNo"),"1"));
		map.put("pageNo", pageNo);
		mav.addObject("curPage", pageNo);

		if(req.getParameterValues("psnId") != null){
			String[] psnId = req.getParameterValues("psnId");
			map.put("psnId", psnId);
			mav.addObject("psnId", StringUtils.join(psnId, ","));
		}
		
		if(req.getParameter("mbrName") != null){
			String mbrName = req.getParameter("mbrName");
			mav.addObject("mbrName", mbrName);
		}
		
		String tempYn = ObjectUtils.toString(req.getParameter("tempYn"));
		if (tempYn == null || tempYn.isEmpty()){
			mav.addObject("tempYn", "");
		} else {
			map.put("tempYn", tempYn);
			mav.addObject("tempYn", tempYn);
		}
		
		if (req.getParameter("stDates")!= null && req.getParameter("stDates")!= ""){
			String staDt = req.getParameter("stDates");
			map.put("stadt", staDt);
			mav.addObject("stadt", staDt);
		}
		
		if(req.getParameter("fiDates") != null && req.getParameter("fiDates") != ""){
			String finDt = req.getParameter("fiDates");
			map.put("findt", finDt);
			mav.addObject("findt", finDt);
		}
		
		if (req.getParameterValues("gb101") != null){
			String[] gb101 = req.getParameterValues("gb101");
			map.put("gb01", gb101);
			mav.addObject("gb01", StringUtils.join(gb101, ","));
		}
		
		if (req.getParameterValues("gb102") != null){
			String[] gb102 = req.getParameterValues("gb102");
			map.put("gb02", gb102);
			mav.addObject("gb02", StringUtils.join(gb102,","));
		}
		
		// 출력할 리스트
		List<SchSchDto> list = schSchService.selectSchListByGbDt(map);
		mav.addObject("schList", list);
		
		int schTotalCnt = schSchService.selectSchCntByGbDt(map);
		mav.addObject("schTotalCnt", schTotalCnt);		// 엑셀 다운로드를 위해 총갯수도 전달.
		
		String pagination = pageService.strPage(schTotalCnt, pageNo, 10, 10, "movePage");
		mav.addObject("page", pagination);				// 페이지네이션 
		
		mav.setViewName("sch/sch/schTempletManageView");
		return mav;
	}
	
	
	/**
	 * 일정탬플릿 - (2) new 새로운 일정템플릿 등록 
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schInsertTemplet")
	public ModelAndView schInsertTemplet (@ModelAttribute("vo")SchSchDto vo,HttpServletRequest request) throws Exception{

		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		String name = StringUtil.isNullToString(session.getAttribute("S_NAME"));
		String orgCd = StringUtil.isNullToString(session.getAttribute("S_EHR_ORG_CD"));
		String orgNm = StringUtil.isNullToString(session.getAttribute("S_ORG_NM"));
		
		CodeVo subVo = new CodeVo();
		subVo.setUp_comm_tree_cd("AEMS0013");		 
		List<CodeDto> subCodes = schSchService.getCodeList(subVo); 	// 연제 주제
		mav.addObject("sub", subCodes);		// 각 subVo안에 comm_cd, comm_cd_nm 
		
		String temp_yn = "Y";			// 템플릿 등록이므로 기본 y
		
		//일정탬플릿 관리 여부 
		String templet_gb = ObjectUtils.toString(vo.getTemplet_gb());
		
		//구분1
		String sch_gb_01 = ObjectUtils.toString(vo.getSch_gb_01());
		if(sch_gb_01.equals("")||sch_gb_01 == null) sch_gb_01 = "01";
	    
		//구분2
		String sch_gb_02 = ObjectUtils.toString(vo.getSch_gb_02());
		if(sch_gb_02.equals("")||sch_gb_02 == null) sch_gb_02 = "01";
		
	    mav.addObject("sch_gb_01", sch_gb_01);
	    mav.addObject("sch_gb_02", sch_gb_02);
	    mav.addObject("templet_gb", templet_gb);
	    mav.addObject("temp_yn", temp_yn);
	    mav.addObject("reg_id", sabun);
	    mav.addObject("org_nm", orgNm);
	    mav.addObject("org_cd", orgCd);
	    mav.addObject("name", name);
	    mav.addObject("sabun", sabun);
	    
		mav.setViewName("sch/sch/schDetailRegistView");
		
		return mav;
	}

	
	/**
	 * 일정탬플릿 - (3) 일정을 템플릿으로 복사 등록 (진행중)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schCopyToTempletView")
	public ModelAndView schCopyToTempletView(HttpServletRequest request, 
			SchSchDto schSch, SchDtlDto schDtlList, SchMbrDto schMbrList) throws Exception {

		// 템플릿으로 저장하기 - copyTmp	- temp Y, sta/fin 날짜 null, 나머지 동일하게 새로 저장. sch/mb/dtl만 
		ModelAndView mav = new ModelAndView();

		// 담당명과 사번은 현재 로그인한 사람으로 변경한다. name/ sabun/ mod_id 에 저장.
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		String name = StringUtil.isNullToString(session.getAttribute("S_NAME"));
		
		schSch.setName(name);
		schSch.setSabun(sabun);
		schSch.setReg_id(sabun);
		
		schSchService.insertSchDtlMbr(schSch, schDtlList, schMbrList); 		// 저장.

		// 아직 등록 service를 진행하지 않았다.
		
		mav.setView(new RedirectView("sch.do?method=schTempletManageView"));
		return mav;
	}
	
	
	/*
	 * 일정탬플릿 - (4) 기존 일정 삭제 (어디까지의 테이블을 삭제해야하는지 문의 후 진행하기.) 
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=schDeleteView")
	public ModelAndView schDeleteView(int schId) throws Exception {

		// schId로 관련된 것 모두 삭제하기.
		ModelAndView mav = new ModelAndView();
		schSchService.deleteSchDtlMbr(schId);
		// 다 한 다음엔 다시 일정템플릿으로 redirect하기.
		mav.setView(new RedirectView("sch.do?method=schTempletManageView"));
		return mav;
	}
	
	
	/*
	 * 일정템플릿 - (5) 일정/템플릿의 모든 sch 엑셀다운  
	 * @return ModelAndView
	 */
	@RequestMapping(params = "method=schSchExcelDown")
	public ModelAndView schSchExcelDown(HttpServletRequest req ) throws Exception {
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 페이지 번호만 안들어갈뿐 모든 게 다 나와야지.
		if(req.getParameter("psnIds") != null && req.getParameter("psnIds") != ""){
			String[] psnId = req.getParameterValues("psnIds");
			map.put("psnId", psnId);
		}
		String tempYn = ObjectUtils.toString(req.getParameter("templetYn"));
		if (tempYn != null && !tempYn.isEmpty()){
			map.put("tempYn", tempYn);
		}
		if (req.getParameter("sta_dt")!= null && req.getParameter("sta_dt")!= ""){
			String staDt = req.getParameter("sta_dt");
			map.put("stadt", staDt);
		}
		if(req.getParameter("fin_dt") != null && req.getParameter("fin_dt") != ""){
			String finDt = req.getParameter("fin_dt");
			map.put("findt", finDt);
		}
		if (req.getParameter("gb01s") != null && req.getParameter("gb01s") != ""){
			String[] gb101 = req.getParameter("gb01s").split(",");
			map.put("gb01", gb101);
		}
		if (req.getParameter("gb02s") != null && req.getParameter("gb02s") != ""){
			String[] gb102 = req.getParameter("gb02s").split(",");
			map.put("gb02", gb102);
		}
		
		// 출력할 리스트
		List<SchSchDto> list = schSchService.selectSchListByGbDt(map);		// sch 전체 갯수가 필요하므로 pageNo 없이 보낸다.
		
		//mav.setViewName("pageJsonReport");
		mav.addObject("schList", list);
		mav.setViewName("SchSchExcelView");
		return mav;

	}
	
	
	/**
	 * 강의수강 안내 - (1) 초기화면 (조회 리스트)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schSendListSearchView")
	public ModelAndView schSendListSearchView(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		 
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 페이지 넘버를 받는다.
		int pageNo = Integer.parseInt(StringUtil.isNullToString(req.getParameter("pageNo"),"1"));
		map.put("pageNo", pageNo);
		mav.addObject("curPage", pageNo);
		
		// 검색 조건들이 담겨져왔는지 확인한다. (강연회/연수회 구분)
		if (req.getParameter("stDates")!= null && req.getParameter("stDates") != ""){
			String staDt = req.getParameter("stDates");
			map.put("stadt", staDt);
			mav.addObject("stadt", staDt);
		}
		
		if(req.getParameter("fiDates") != null && req.getParameter("fiDates") != "" ){
			String finDt = req.getParameter("fiDates");
			map.put("findt", finDt);
			mav.addObject("findt", finDt);
		}
		
		if (req.getParameterValues("gb101") != null){
			String[] gb101 = req.getParameterValues("gb101");
			map.put("gb01", gb101);
			mav.addObject("gb01", StringUtils.join(gb101,","));
		}
		
		if (req.getParameterValues("gb102") != null){
			String[] gb102 = req.getParameterValues("gb102");
			map.put("gb02", gb102);
			mav.addObject("gb02", StringUtils.join(gb102,","));
		}
		
		// 검색결과를 담은 map을 넘겨 해당 강의와 수신자정보를 담는다. (Tb_Send만 대상)
		int sendTotalCnt = schSchService.selectSchSendInfoCntByGbDt(map);
		
		List<HashMap<String, Object>> maps = schSchService.selectSchSendInfoByGbDt(map);
		for (HashMap<String, Object> m : maps){
			list.add(m);
		}
		
		String pagination = pageService.strPage(sendTotalCnt, pageNo, 10, 10, "movePage");
		mav.addObject("page", pagination);
		
		// 이게 출력할 send 리스트.
		mav.addObject("sendList",list);
		mav.setViewName("sch/sch/schSendListSearchView");

		return mav;
	}

	
	/**
	 * 강의수강 안내 - (2) 강의수강 안내 등록 초기화면
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schSendListRegistView")
	public ModelAndView schSendListRegistView(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		//시간 코드 가져오기
		CodeVo hrVo = new CodeVo();
		hrVo.setUp_comm_tree_cd("AEMS004");		 
		List<CodeDto> hrCodes = schSchService.getCodeList(hrVo); 	// 시간 (hour)
		mav.addObject("hr", hrCodes);		// 각 hrVo안에 comm_cd, comm_cd_nm 

		mav.setViewName("sch/sch/schSendListRegistView");
		return mav;
	}

	/**
	 * 강의수강 안내 - (3) 수신자 상세 정보 (리스트)
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params = "method=selectPsnListBySendId")
	public ModelAndView selectPsnListBySendId(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		int send_id = Integer.parseInt(req.getParameter("send_id"));
		
		// 이제 하나의 send를 클릭하면 해당 원장들의 리스트를 가져와보자.
		String result = schSchService.selectPsnBySendId(send_id);
		
		mav.addObject("psnList", result);
		mav.setViewName("pageJsonReport");

		return mav;
	}
	
	
	/*
	 * 강의수강 안내 - (4) 자동검색후 대상자 추가 (이름 or 그룹) 
	 */
	@RequestMapping(params = "method=selectPsnList")
	public ModelAndView  selectPsnList (HttpServletRequest req) throws Exception{

		ModelAndView mav = new ModelAndView();

		int id = Integer.parseInt(req.getParameter("id"));
		String type =  req.getParameter("type");
		String list = schSchService.selectPsnList(id, type);
		mav.addObject("psns", list);
		mav.setViewName("pageJsonReport");

		return mav;
	}
	
	/**
	 * 강의수강 안내 - (5) 발송목록 저장 (send msg + insert)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=insertSendList")
	public ModelAndView insertSendList( @RequestParam(value = "file", required=false) MultipartFile file,
			HttpServletRequest request, SendPsnDto sendPsnList, SendDto sendDto, FileDto fileDto) throws Exception {

		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		sendDto.setReg_id(sabun);
		for(SendPsnDto dto : sendPsnList.getSendPsnList()){
			dto.setReg_id(sabun);
		}
		try {
			// 먼저 이미지가 있으면 저장한다. 없으면 저장 안함.
			if(!file.isEmpty()){
				
				fileDto.setReg_id(sabun);
				List<FileDto> fileList = fileService.uploadFile(request, sabun);
				for(FileDto dto : fileList){
					// 이미지가 있으면 생성된 fileId를 받아서 다음 sendDto에 넣어줘야 한다.
					sendDto.setFile_id(dto.getFile_id());
				} 
			}
			// sendDto와 psnList를 저장해야지.
			schSchService.insertSendPsn(sendDto, sendPsnList, request); 		// 여기서 mtrefkey를 얻어와야 한다.
		} catch (IOException e) {
			e.printStackTrace();
		}
		mav.setView(new RedirectView("sch.do?method=schSendListSearchView"));
		return mav;
	}
	
	

	/*
	 * 강의수강 안내 - (6) 발송내역 확인 view  
	 */
	@RequestMapping(params = "method=schSendListDetailReView")
	public ModelAndView schSendListDetailReView(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="schId", required=true) int schId,
			@RequestParam(value="sendId", required=true) int sendId,
			@RequestParam(value="cnt", required=true) int cnt) throws Exception {
		ModelAndView mav = new ModelAndView();

		// (1) schId로 일정 상세정보 가져오기
		SchSchDto schsch = schSchService.selectSchBySchId(schId);
		mav.addObject("sch", schsch);
		mav.addObject("schDt", schsch.getSch_sta_dt().substring(0, 10));
		mav.addObject("schStaHr", schsch.getSch_sta_dt().substring(11, 13));
		mav.addObject("schFinHr", schsch.getSch_fin_dt().substring(11, 13));

		// (2) sendId로 수신자 리스트 가져오기.
		String psnList = schSchService.selctPsnSendListBySchId(sendId);
		mav.addObject("psnList", psnList);
		
		// (3) 총 수신자 명수 입력하기. 
		mav.addObject("totalCnt", cnt);
		
		// (4) 만약 fileId가 0이 아니면 이미지 첨부하기.
		SendDto send = schSchService.selectSendBySendId(sendId);
		mav.addObject("fileId", send.getFile_id());
		if (send.getFile_id() != 0){
			// encode image base64
			String base64Image = fileService.imageToBase64String(send.getFile_id());
			mav.addObject("base64Image", base64Image);
		}
		
		mav.setViewName("sch/sch/schSendListDetailReView");
		
		
		return mav;
	}

	
	/**
	 * 강의수강 안내 - (7) 면허번호 엑셀파일 읽기 (send msg + insert)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=readLiceNoExcel")
	public ModelAndView readLiceNoExcel( @RequestParam(value = "file", required=true) MultipartFile file,
			HttpServletRequest request ) throws Exception {

		ModelAndView mav = new ModelAndView();
		
		// 엑셀 파일 속 LICE NO를 읽는다.
		String result = schSchService.readExcelFile(file); 	
		mav.addObject("psnList", result);
		mav.setViewName("pageJsonReport");
		return mav;
	}
	
	
	/*
	 * 강의수강안내 - (8) 강의수강안내의 모든 sch send 엑셀다운  
	 * @return ModelAndView
	 */
	@RequestMapping(params = "method=schSendExcelDown")
	public ModelAndView schSendExcelDown(HttpServletRequest req ) throws Exception {
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 페이지 번호만 안들어갈뿐 모든 게 다 나와야지.
		if (req.getParameter("sta_dt")!= null && req.getParameter("sta_dt")!= ""){
			String staDt = req.getParameter("sta_dt");
			map.put("stadt", staDt);
		}
		if(req.getParameter("fin_dt") != null && req.getParameter("fin_dt") != ""){
			String finDt = req.getParameter("fin_dt");
			map.put("findt", finDt);
		}
		if (req.getParameter("gb01s") != null && req.getParameter("gb01s") != ""){
			String[] gb101 = req.getParameter("gb01s").split(",");
			map.put("gb01", gb101);
		}
		if (req.getParameter("gb02s") != null && req.getParameter("gb02s") != ""){
			String[] gb102 = req.getParameter("gb02s").split(",");
			map.put("gb02", gb102);
		}
		
		List<HashMap<String, Object>> list = schSchService.selectSchSendInfoByGbDt(map);
		mav.addObject("schSendList", list);
		mav.setViewName("SchSendExcelView");
		return mav;

	}
	
	/**
	 * 강의신청 관리 - (1) 초기화면 (조회리스트)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schResultListSearchView")
	public ModelAndView schResultListSearchView(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		int pageNo = Integer.parseInt(StringUtil.isNullToString(req.getParameter("pageNo"),"1"));

		map.put("pageNo", pageNo);
		mav.addObject("curPage", pageNo);
		
		// 검색조건을 모두 담는다.
		if (req.getParameter("stDates")!= null && req.getParameter("stDates") != ""){
			String staDt = req.getParameter("stDates");
			map.put("stadt", staDt);
			mav.addObject("stadt", staDt);
		}
		
		if(req.getParameter("fiDates") != null && req.getParameter("fiDates") != "" ){
			String finDt = req.getParameter("fiDates");
			map.put("findt", finDt);
			mav.addObject("findt", finDt);
		}
		
		if (req.getParameterValues("gb101") != null){
			String[] gb101 = req.getParameterValues("gb101");
			map.put("gb01", gb101);
			mav.addObject("gb01", StringUtils.join(gb101, ","));
		}
		
		if (req.getParameterValues("gb102") != null){
			String[] gb102 = req.getParameterValues("gb102");
			map.put("gb02", gb102);
			mav.addObject("gb02", StringUtils.join(gb102,","));
		}

		// 검색조건에 맞고, send/reg가 되었었던 sch 리스트를 뽑는다.
		int schCnt = schSchService.selectSchSendYnCnt(map);
		if (schCnt > 0){
			List<HashMap<String, Object>> maps = schSchService.selectSchSendYnList(map);
			
			int sendTotalCnt = schSchService.selectSchSendYnCnt(map);
			String pagination = pageService.strPage(sendTotalCnt, pageNo, 10, 10, "movePage");
			mav.addObject("page", pagination);
			
			// 이게 출력할 send 리스트.
			mav.addObject("sendList", maps);
		}
		
		mav.setViewName("sch/sch/schResultListSearchView");
		
		return mav;
	}
	
	
	/**
	 * 강의신청 관리 - (2) 각 일정마다 응답자 리스트 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=selectPsnListBySchId")
	public ModelAndView selectPsnListBySchId(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		int schId = Integer.parseInt(req.getParameter("sch_id"));
		
		// schId 하나당 응답자들의 리스트.
		String list = schSchService.selectPsnRcptListBySchId(schId);
		mav.addObject("psnList", list);
		mav.setViewName("pageJsonReport");
		return mav;
	}
	
	/**
	 * 강의신청 관리 - (3) 각 응답자 상세정보 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schResultPsnDetailView")
	public ModelAndView schResultPsnDetailView(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		mav.addObject("sabun", sabun);
		SawonDto2 sawonDto = sawonService.selectSawon2(sabun);
		mav.addObject("sawonName",sawonDto.getName());
		
		int schId = Integer.parseInt(request.getParameter("sch_id"));
		int psnId = Integer.parseInt(request.getParameter("psn_id"));
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("schId", schId);
		map.put("psnId", psnId);
		mav.addObject("schId", schId);
		mav.addObject("psnId", psnId);
		
		int ifPsnExistInReg = schSchService.selectRegCntByPsnSchId(map);
		mav.addObject("ifPsnExistInReg",  ifPsnExistInReg);			// 이미 reg에 존재하는 psn만 삭제버튼을 보여준다. 없을때 0 있을때 1이상.
		
		HashMap<String, Object> schmap = schSchService.selectSchDtlMbr(schId);
		SchSchDto sch = (SchSchDto) schmap.get("sch");

		mav.addObject("sch", sch);
		mav.addObject("schDt", sch.getSch_sta_dt().substring(0, 10));
		
		mav.addObject("schStaHr", Integer.parseInt(sch.getSch_sta_dt().substring(11, 13)));
		mav.addObject("schFinHr", Integer.parseInt(sch.getSch_fin_dt().substring(11, 13)));

		List<HashMap<String, Object>> list = schSchService.selectPsnByPsnIdforAdd(psnId);
		HashMap<String, Object> psnMap= list.get(0);
		mav.addObject("psnList", psnMap);
		
		HashMap<String, Object> regMap = schSchService.selectSchRegBySchIdPsnId(map);
		mav.addObject("regInfo", regMap);
		
		mav.setViewName("sch/sch/schResultPsnDetailView");
		
		
		return mav;
	}
	

	/**
	 * 강의신청 관리 - (4) 수락여부 수동등록 (영업사원이 신청y/n 등록)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schResultManualRegistView")
	public ModelAndView schResultManualRegistView(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("sch/sch/schResultManualRegistView");
		
		
		return mav;
	}

	
	/**
	 * 강의신청 관리 - (5) 강의내용 자동세팅   
	 * @throws Exception
	 */
	@RequestMapping(params = "method=selectSchInfoBySchId")
	public ModelAndView selectSchInfoBySchId(HttpServletRequest request, 
			@RequestParam(value="sch_id", required=true) int schId) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		// 0. 먼저 sch_id에 따른 sch, sch_dtl, sch_mbr 정보를 가져온다.
		HashMap<String, Object> map = schSchService.selectSchDtlMbr(schId);

		SchSchDto sch = (SchSchDto) map.get("sch");
		JSONObject json = new JSONObject();
		json.put("gb01", sch.getSch_gb_01());
		json.put("gb02", sch.getSch_gb_02());
		json.put("orgNm", sch.getOrg_nm());
		json.put("name", sch.getName());
		json.put("staHr", sch.getSch_sta_dt().substring(11, 13));
		json.put("finHr", sch.getSch_fin_dt().substring(11, 13));
		json.put("staDate", sch.getSch_sta_dt().substring(0, 10));
		json.put("finDate", sch.getSch_fin_dt().substring(0, 10));
		json.put("place", sch.getPlace());
		
		mav.addObject("sch", json);
		
		mav.setViewName("pageJsonReport");
		
		return mav;
	}
	
	/**
	 * 강의신청 관리 - (6) 원장정보 자동세팅  
	 * @throws Exception
	 */
	@RequestMapping(params = "method=selectPsnInfoByPsnId")
	public ModelAndView selectPsnInfoByPsnId(HttpServletRequest request, 
			@RequestParam(value="psn_id", required=true) int psnId,
			@RequestParam(value="sch_id", required=false) int schId) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		// 0. 먼저 sch_id에 따른 sch, sch_dtl, sch_mbr 정보를 가져온다.
		List<HashMap<String, Object>> list = schSchService.selectPsnByPsnIdforAdd(psnId);

		HashMap<String, Object> psn = list.get(0);
		
		JSONObject json = new JSONObject();
		json.put("psnId", psnId);
		json.put("name", psn.get("NAME"));
		json.put("hosname", psn.get("HOSNAME"));
		json.put("addr", psn.get("HOSADRS"));
		json.put("email", psn.get("EMAIL"));
		json.put("liceno", psn.get("LICENO"));
		json.put("phone", psn.get("PHONE"));
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		boolean ifPsnExist = false;
		if(schId != 0){
			map.put("psnId", psnId);
			map.put("schId", schId);
			int ifPsnRegExist = schSchService.selectRegCntByPsnSchId(map);
			if (ifPsnRegExist > 0){
				ifPsnExist = true;
			}
		}
		HashMap<String, Object> psnReg = new HashMap<String,Object>();
		if (ifPsnExist){
			psnReg = schSchService.selectSchRegBySchIdPsnId(map);
			json.put("spkyn", psnReg.get("SPKYN"));
			json.put("presyn", psnReg.get("PRES_YN"));
			json.put("restyn", psnReg.get("REST_YN"));
		} else {
			json.put("spkyn", "N");
			json.put("presyn", "N");
			json.put("restyn", "N");
		}
		
		mav.addObject("psn", json);
		
		mav.setViewName("pageJsonReport");
		
		return mav;
	}
	
	/*
	 * 강의신청관리 - (7) 강의신청 수동등록 (insert)
	 */
	@RequestMapping(params = "method=insertSchReg", method=RequestMethod.POST)
	public ModelAndView  insertSchReg (SchRegDto schReg, HttpServletRequest request) throws Exception{

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		schReg.setReg_id(sabun);
		schReg.setSal_emp_no(sabun);
		
		// 저장 또는 업데이트가 가능하다.
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("psnId", schReg.getPsn_id());
		map.put("schId", schReg.getSch_id());
		if (schSchService.selectSchRegCntBySchIdPsnId(map) > 0){
			schSchService.updateSchReg(schReg);// reg에 있으면 업데이트.
		} else {
			schSchService.insertSchReg(schReg); 		// reg에 없으면 등록.
		}
		mav.setView(new RedirectView("sch.do?method=schResultManualRegistBySchIdView&schId=" +schReg.getSch_id()));
		return mav;
	}
	
	/**
	 * 강의신청관리 - (8) 강의신청 수동등록 (insert) - by schId (강의리스트에서 클릭해서 들어갈 때)
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schResultManualRegistBySchIdView")
	public ModelAndView schResultManualRegistBySchIdView(HttpServletRequest request, 
			@RequestParam(value="schId", required=true) int schId) throws Exception {
		ModelAndView mav = new ModelAndView();
		 
		// 0. 먼저 sch_id에 따른 sch, sch_dtl, sch_mbr 정보를 가져온다.
		HashMap<String, Object> map = schSchService.selectSchDtlMbr(schId);

		// 1.  강연회인지 아닌지 구분하고
		map.put("input", schId);
		SchSchDto sch = (SchSchDto) map.get("sch");
		
		String gb01 = sch.getSch_gb_01();
		// 2. 강연회/연수회에 따라 gb01/02 페이지를 구분해 dto 값을 전달한다.
		mav.addObject("gb01", gb01);

		mav.addObject("sch", sch);

		mav.addObject("schDt", sch.getSch_sta_dt().substring(0, 10));
		mav.addObject("schStaHr", sch.getSch_sta_dt().substring(11, 13));
		mav.addObject("schFinHr", sch.getSch_fin_dt().substring(11, 13));
		
		List<HashMap<String, Object>> list = schSchService.selectSendPsnRcptListBySchId(schId);
		mav.addObject("psnList", list);
		
		mav.setViewName("sch/sch/schResultManualRegistBySchIdView");
		
		return mav;
	}
	
	
	/*
	 * 강의신청관리 - (9) 강의신청 수동삭제 (delete)
	 */
	@RequestMapping(params = "method=deleteRegPsn", method=RequestMethod.POST)
	public ModelAndView  deleteRegPsn (SchRegDto schReg, HttpServletRequest request) throws Exception{

		ModelAndView mav = new ModelAndView();

		schSchService.deleteRegPsn(schReg); 		// psnId, schId로 해당 reg 삭제.
		
		mav.setView(new RedirectView("sch.do?method=schResultListSearchView"));
		return mav;
	}
	
	
	/*
	 * 강의신청관리 - (10) 강의신청관리의 모든 sch reg 엑셀다운  
	 */
	@RequestMapping(params = "method=schRegExcelDown")
	public ModelAndView schRegExcelDown(HttpServletRequest req ) throws Exception {
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 페이지 번호만 안들어갈뿐 모든 게 다 나와야지.
		if (req.getParameter("sta_dt")!= null && req.getParameter("sta_dt")!= ""){
			String staDt = req.getParameter("sta_dt");
			map.put("stadt", staDt);
		}
		if(req.getParameter("fin_dt") != null && req.getParameter("fin_dt") != ""){
			String finDt = req.getParameter("fin_dt");
			map.put("findt", finDt);
		}
		if (req.getParameter("gb01s") != null && req.getParameter("gb01s") != ""){
			String[] gb101 = req.getParameter("gb01s").split(",");
			map.put("gb01", gb101);
		}
		if (req.getParameter("gb02s") != null && req.getParameter("gb02s") != ""){
			String[] gb102 = req.getParameter("gb02s").split(",");
			map.put("gb02", gb102);
		}
				
		// 검색조건에 맞고, send/reg가 되었었던 sch 리스트를 뽑는다.
		int schCnt = schSchService.selectSchSendYnCnt(map);
		if (schCnt > 0){
			List<HashMap<String, Object>> list = schSchService.selectSchSendYnList(map);
			mav.addObject("schRegList", list);
		}
		
		mav.setViewName("SchRegExcelView");
		return mav;

	}
	
	/**
	 * 강의신청관리 - (11) 강의신청관리 수동등록에서 REG에 등록된 PSN 삭제  
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteRegPsnList")
	public ModelAndView deleteRegPsn(HttpServletRequest request, 
			@RequestParam(value="schId", required=true) int schId,
			@RequestParam(value="psnId", required=true) String psnId) throws Exception {
		ModelAndView mav = new ModelAndView();
 
		String[] psnIds = psnId.split(","); 		// 공통 소스를 쓰다보니 이름은 schId지만 실제론 psnId.
		for (String psn_id : psnIds){
			// 하나의 schId와 psnId를 넣어서 reg에서 삭제하는 서비스를 돌린다.
			SchRegDto reg = new SchRegDto();
			reg.setSch_id(schId);
			reg.setPsn_id(Integer.parseInt(psn_id));
			schSchService.deleteRegPsn(reg);
		}
	
		mav.setView(new RedirectView("sch.do?method=schResultManualRegistBySchIdView&schId="+schId));
		return mav;
	}

	
	/*
	 * 모바일 강의 신청 - (1)
	 */
	@RequestMapping(params = "method=schSendMobileMsgView", method=RequestMethod.GET)
	public ModelAndView schSendMobileMsgView(HttpServletRequest request,
			@RequestParam(value="mtRefkey", required=true) String mtRefkey) throws Exception{
		ModelAndView mav = new ModelAndView();

		// AEMS_테이블명(SEND)_schId_sendId_psnID 
		//String mtrefkey = "AEMS_SEND_172_56_465_297198";		// 이게 들어오면 바로 발송이 가능하게끔 한다.

		String keys[] = mtRefkey.split("_");
		int schId = Integer.parseInt(keys[2]);
		int sendId = Integer.parseInt(keys[3]);
		int psnId = Integer.parseInt(keys[4]);
		
		List<HashMap<String, Object>> list = schSchService.selectPsnByPsnIdforAdd(psnId);
		HashMap<String, Object> psnMap= list.get(0);
		mav.addObject("psnList", psnMap);
		mav.addObject("schId",schId);
		mav.addObject("psnId",psnId);
		SchSchDto sch = schSchService.selectSchBySchId(schId);
		mav.addObject("sch", sch);
		
		// sendId로 file id도 보낼 수 있다.
		SendDto send = schSchService.selectSendBySendId(sendId);
		mav.addObject("fileId", send.getFile_id());
		if (send.getFile_id() != 0){
			String base64Image = fileService.imageToBase64String(send.getFile_id());
			mav.addObject("base64Image", base64Image);
		}
		mav.addObject("layout", "common/layout.vm");
		mav.setViewName("sch/sch/schMobileView");
		return mav;
	}	
	
	/*
	 * 강의 신청 응답 - 수동 저장 (insert)
	 */
	@RequestMapping(params = "method=insertManualYnRsp", method=RequestMethod.POST)
	public ModelAndView  saveMultiData (HttpServletRequest request, SchRegDto dto) throws Exception{

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		dto.setSal_emp_no(sabun);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("psnId", dto.getPsn_id());
		map.put("schId", dto.getSch_id());

		
		if (schSchService.selectSchRegCntBySchIdPsnId(map) > 0){
			// 이미 reg에 저장되어있으면 업데이트한다.
			schSchService.updateSchReg(dto);
		} else {
			// reg에 없는 새로운 값이면 새로 저장한다.
			schSchService.insertSchReg(dto);
		}
		 
		mav.setView(new RedirectView("sch.do?method=schResultListSearchView"));
		return mav;
	}
	
	//연자평가 모바일 뷰 -> 저장(평가코드 )
	@RequestMapping(params="method=insertMobileYn")
	public ModelAndView insertMobileYn(HttpServletRequest request, @RequestParam(value="sch_id", required=true) int schId,
			@RequestParam(value="psn_id", required=true) int psnId,
			@RequestParam(value="hope_spk_yn", required=true) String hope_spk_yn,
			@RequestParam(value="pres_yn", required=true) String pres_yn) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		SchRegDto reg = new SchRegDto();
		reg.setSch_id(schId);
		reg.setPsn_id(psnId);
		reg.setHope_spk_yn(hope_spk_yn);
		reg.setPres_yn(pres_yn);
		reg.setSelf_yn("Y");
		reg.setRest_yn("N");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("psnId", psnId);
		map.put("schId", schId);
		if (schSchService.selectSchRegCntBySchIdPsnId(map) > 0){
			// 이미 reg에 저장되어있으면 업데이트한다.
			schSchService.updateSchReg(reg);
		} else {
			// reg에 없는 새로운 값이면 새로 저장한다.
			schSchService.insertSchReg(reg);
		}
		mav.setViewName("pageJsonReport");
		return mav;
	}
	
	
	/**
	 * 연자평가 - (0) 리스트 화면 (초기 or 검색)
	 * @throws Exception
	 */
	/*@RequestMapping(params = "method=schApprView")
	public ModelAndView schApprView (@ModelAttribute("vo")SchSchDto vo, HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 처음엔 gb이든 time 이든 안 들어감. 페이지만 들어감.
		int pageNo = Integer.parseInt(StringUtil.isNullToString(req.getParameter("pageNo"),"1"));
		map.put("pageNo", pageNo);
		mav.addObject("curPage", pageNo);

		if (req.getParameter("stDates")!= null && req.getParameter("stDates") != ""){
			String staDt = req.getParameter("stDates");
			map.put("stadt", staDt);
			mav.addObject("stadt", staDt);
		}
		
		if(req.getParameter("fiDates") != null && req.getParameter("fiDates") != "" ){
			String finDt = req.getParameter("fiDates");
			map.put("findt", finDt);
			mav.addObject("findt", finDt);
		}
		
		if (req.getParameterValues("gb101") != null){
			String[] gb101 = req.getParameterValues("gb101");
			map.put("gb01", gb101);
			mav.addObject("gb01", StringUtils.join(gb101,","));
		}
		if (req.getParameterValues("gb102") != null){
			String[] gb102 = req.getParameterValues("gb102");
			map.put("gb02", gb102);
			mav.addObject("gb02", StringUtils.join(gb102,","));
		}

		if(req.getParameter("mbrName") != null && req.getParameter("mbrName") != ""){
			String mbrName = req.getParameter("mbrName");
			mav.addObject("mbrName", mbrName);
		}

		if(req.getParameter("psnId") != null && req.getParameter("psnId") != ""){
			String psnId = req.getParameter("psnId");
			map.put("psnId", psnId);
			mav.addObject("psnId", psnId);
		}
		
		 * 연자평가 초기 리스트 출력 테스트
		 
		List<HashMap<String, Object>> list = schSchService.selectApprSchList(map);
		mav.addObject("schList", list);
		
		int schTotalCnt = schSchService.selectApprSchCnt(map);
		String pagination = pageService.strPage(schTotalCnt, pageNo, 10, 10, "movePage");
		mav.addObject("page", pagination);				// 페이지네이션 
		
		 
		mav.setViewName("sch/sch/schApprView");
		return mav;
	}*/
	
	/**
	 * 연자평가 - (1) 평가 등록
	 * @throws Exception
	 */
	/*@RequestMapping(params = "method=schApprRegistView")
	public ModelAndView schApprRegistView(@ModelAttribute("vo")SchSchDto vo,HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		CodeVo hrVo = new CodeVo();
		hrVo.setUp_comm_tree_cd("AEMS004");		 
		List<CodeDto> hrCodes = schSchService.getCodeList(hrVo); 	// 시간 (hour)
		mav.addObject("hr", hrCodes);		// 각 hrVo안에 comm_cd, comm_cd_nm 
		
		mav.setViewName("sch/sch/schApprRegistView");
		
		return mav;
	}*/

	/**
	 * 연자평가 - (2) 특정 일정의 회차정보 테이블
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(params = "method=selectDtlListBySchId")
	public ModelAndView selectDtlListBySchId(HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();

		String sch_id = req.getParameter("schId");
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] mbrGb = new String[]{"01"};		// 확정패컬티 01번
		map.put("mbrGb", mbrGb);
		map.put("schId", sch_id);
		String apprTable = schSchService.selectDtlListBySchMbr(map);

		mav.addObject("apprTable", apprTable);
		mav.setViewName("pageJsonReport");
		return mav;
	}*/
	
	
	/**
	 * 연자평가 - (3) 등록된 연자평가 조회 /수정 뷰 
	 * @throws Exception
	 */
	/*@RequestMapping(params = "method=schApprDetailModifyView")
	public ModelAndView schApprDetailModifyView(@ModelAttribute("vo")SchSchDto vo,HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		mav.addObject("sabun", sabun);

		int schId = Integer.parseInt(request.getParameter("schId"));
		mav.addObject("schId", schId);
		
		// 해당 sch의 
		// 1. 기본 정보를 가져온다.
		SchSchDto sch = schSchService.selectSchBySchId(schId);
		mav.addObject("stDate", sch.getSch_sta_dt().substring(0, 10));
		mav.addObject("staTime", sch.getSch_sta_dt().substring(11, 16));
		mav.addObject("finTime", sch.getSch_fin_dt().substring(11, 16));
		mav.addObject("sch", sch);
		
		// 2. 연자평가 조회뷰를 가져온다 >> schId 로 dtl, 평가입력, 연자추천을 가져온다.
		List<HashMap<String, Object>> result = schSchService.selectApprSchDtlList(schId);
		mav.addObject("apprList", result);
		
		mav.setViewName("sch/sch/schApprDetailModifyView");
		
		return mav;
	}*/
	
	
	/*
	 * 연자평가 - (5) 연자평가 수정 저장 
	 */
	/*
	@RequestMapping(params = "method=updateApprSpk", method=RequestMethod.POST)
	public ModelAndView  updateApprSpk (ApprSpkDto apprSpkList, HttpServletRequest request) throws Exception{

		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		ModelAndView mav = new ModelAndView();
		apprSpkList.setReg_id(sabun);
		schSchService.updateApprSpkList(apprSpkList);
		
		mav.setView(new RedirectView("sch.do?method=schApprView"));
		return mav;
	}
	*/
	
	
	/*
	 * 연자평가 - (6) 연자평가의 모든 sch appr 엑셀다운  
	 * @return ModelAndView
	 */
	/*@RequestMapping(params = "method=schApprExcelDown")
	public ModelAndView schApprExcelDown(HttpServletRequest req ) throws Exception {
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (req.getParameter("sta_dt")!= null && req.getParameter("sta_dt")!= ""){
			String staDt = req.getParameter("sta_dt");
			map.put("stadt", staDt);
		}
		if(req.getParameter("fin_dt") != null && req.getParameter("fin_dt") != ""){
			String finDt = req.getParameter("fin_dt");
			map.put("findt", finDt);
		}
		if (req.getParameter("gb01s") != null && req.getParameter("gb01s") != ""){
			String[] gb101 = req.getParameter("gb01s").split(",");
			map.put("gb01", gb101);
		}
		if(req.getParameter("psnId") != null && req.getParameter("psnId") != ""){
			String psnId = req.getParameter("psnId");
			map.put("psnId", psnId);
		}
		
		
		// 연자평가 초기 리스트 출력 테스트
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		list = schSchService.selectApprSchList(map);
		
		mav.addObject("schApprList", list);
		mav.setViewName("SchApprExcelView");
		return mav;

	}*/
	
	
	
	
	/**
	 * 일정관리 - 등록일정조회 -> 연간
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=schYearSearchView")
	public ModelAndView schYearSearchView(@ModelAttribute("vo")SchSchDto vo,HttpServletRequest req) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		//구분1
		String sch_gb_01 = ObjectUtils.toString(vo.getSch_gb_01());
		if(sch_gb_01.equals("")) sch_gb_01 = "01";
		
		//구분2
	    String sch_gb_02 = ObjectUtils.toString(vo.getSch_gb_02());
		if(sch_gb_02.equals("")) sch_gb_02 = "01";
	    
	    Calendar cal = Calendar.getInstance();
		
		String year = ObjectUtils.toString(cal.get(Calendar.YEAR));//현재 년도 ex)2016
		int month = cal.get(Calendar.MONTH)+1; // 현재 달 ex)11
		
		String cur_year = ObjectUtils.toString(vo.getCur_year());//파라미터 년도
		if(cur_year.equals("")) cur_year = year; // 초기엔 "" >> 곧 현재 년도 year을 넣는다. - 2016
		
		Calendar selectday = Calendar.getInstance();			
		selectday.set(Integer.parseInt(cur_year),1,1);//전년도/다음년도 계산
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("gb01", sch_gb_01);
		map.put("gb02", sch_gb_02);
		map.put("year", cur_year);
		
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		int maxCnt = 0;
		if(sch_gb_01.equals("01")&&sch_gb_02.equals("01")){ 		// (1) 연구회인 경우 
			SchSchDto sch = new SchSchDto();
			sch.setSch_gb_01(sch_gb_01);
			sch.setSch_gb_02(sch_gb_02);
			sch.setSch_sta_dt(cur_year+"-01-01");
			sch.setSch_fin_dt(cur_year+"-12-31");
			int ifSchExist = schSchService.selectSchCnt(sch);

			if (ifSchExist > 0){	// 만약 해당 년도에 연구회가 있다면 
				map.put("firstDay", cur_year+"-01-01");
				map.put("lastDay", cur_year+"-12-31");

				List<HashMap<String, Object>> placeList = schSchService.selectSchPlaceNmByYear(map);
				mav.addObject("placeList", placeList);
				
				result = schSchService.selectSchByYearPlace(map);
				mav.addObject("schList", result);
			}
		} else if (sch_gb_01.equals("01")&&sch_gb_02.equals("03")){ // (2) FacultySeminar인 경우
			map.put("firstDay", cur_year+"-01-01");
			map.put("lastDay", cur_year+"-12-31");
			int ifSchExist = schSchService.selectSchMbrCntByYear(map);
			if (ifSchExist > 0 ){
				int maxTrCnt = schSchService.selectSchMbrMaxCntByYear(map);
				mav.addObject("maxTrCnt", maxTrCnt);
				result = schSchService.selectSchMbrByYear(map);
				mav.addObject("mbrList", result);
			}
			
		} else {													// (3) FacultySeminar/ 연구회가 '아닌 경우' 
			
			maxCnt = schSchService.selectSchCntByYear(map);
			if (maxCnt > 0 ){
				mav.addObject("maxTrCnt", maxCnt);			// 매 해중 가장 많은 sch를 갖는 달의 sch갯수를 찾아 tr를 돌린다
				result = schSchService.selectSchByYear(map);
			}
			mav.addObject("schList", result);
		}
		 
		mav.addObject("year", cur_year);//파라미터 날짜 
		mav.addObject("prev_year",selectday.get(Calendar.YEAR)-1);//전년도
	    mav.addObject("next_year",selectday.get(Calendar.YEAR)+1);//다음년도
	    mav.addObject("sch_gb_01", sch_gb_01);
	    mav.addObject("sch_gb_02", sch_gb_02);
	    mav.addObject("cur_year",Integer.parseInt(year));//현재 년도 - 숫자형으로 변환
	    mav.addObject("format_year",Integer.parseInt(cur_year));//파라미터 날짜 - 숫자형으로 변환
	    mav.addObject("cur_month", month);//현재 월
		mav.setViewName("sch/sch/schYearSearchView");
		
		return mav;
	}
	
	/**
	 * 일정관리 - 등록일정조회 -> 월간
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=schMonthSearchView")
	public ModelAndView schMonthSearchView (@ModelAttribute("vo")SchSchDto vo,HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		
		String cur_date = DateUtil.getCurYmd();//현재 날짜(YYYYMMDD)
		
		String schd_ymd = ObjectUtils.toString(vo.getSchd_ymd());//파라미터 날짜(YYYYMMDD)
		
		if(schd_ymd.equals("")) schd_ymd = cur_date;			// 특정날짜가 정해지지 않았다면 오늘을 출력한다.
		
		/* 달력 그리기 용도 */
		int select_year = Integer.parseInt(schd_ymd.substring(0,4));	// 출력할 날짜의 연도
		int select_month = Integer.parseInt(schd_ymd.substring(4,6));	// 출력할 날짜의 달
		int select_day = 1;	
		
		//달력 그리기 -> 날짜 가져오기
		ArrayList<Hashtable<String, String>> month_day_list = DateUtil.getNowMonthDay(select_year,select_month,select_day);
        
		//구분1
        String sch_gb_01 = ObjectUtils.toString(vo.getSch_gb_01());
		if(sch_gb_01.equals("")) sch_gb_01 = "01";					
		
		//구분2
	    String sch_gb_02 = ObjectUtils.toString(vo.getSch_gb_02());
		if(sch_gb_02.equals("")) sch_gb_02 = "01";
        
        mav.addObject("cur_date", schd_ymd);
        
     // 출력할 Sch 리스트의 조건을 map에 담아 result를 가져온다.
        HashMap<String, Object> m = new HashMap<String, Object>();				 
        mav.addObject("sch_gb_01", sch_gb_01);
        mav.addObject("sch_gb_02", sch_gb_02);
        String theMonth = "";
        if (select_month < 10){
        	theMonth = "0"+String.valueOf(select_month);
        } else {
        	theMonth = String.valueOf(select_month);
        }
        m.put("month", theMonth);
        m.put("gb01", sch_gb_01);
        m.put("gb02", sch_gb_02);
        m.put("year", String.valueOf(select_year));
        List<HashMap<String, Object>> result = schSchService.selectSchByMonth(m);
		mav.addObject("schList", result);
		
        mav.addObject("prev_date",DateUtil.getYmdaddMonth(schd_ymd,-1));		// 전달 20161017
        mav.addObject("next_date",DateUtil.getYmdaddMonth(schd_ymd,1));			// 다음달 20161217 이런 식으로 옴.
        mav.addObject("select_year", select_year);	
        mav.addObject("select_month", select_month);
        mav.addObject("month_day_list", month_day_list);
        mav.addObject("front_blank", DateUtil.strFrontBlank(month_day_list));
        mav.addObject("end_blank", DateUtil.strEndBlank(month_day_list));
        // 프론트 , 엔드는 이렇게 앞뒤로 채울 td를 보내준다!  <td> <div class="cld_date"></div> </td>
        
        
		mav.setViewName("sch/sch/schMonthSearchView");
		
		return mav;
	}
	
	
	
	/**
	 * 일정관리 - 등록일정조회 -> 주간
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=schWeekSearchView")
	public ModelAndView schWeekSearchView (@ModelAttribute("vo")SchSchDto vo,HttpServletRequest req) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		String cur_ymd = DateUtil.getCurYmd();//현재 날짜(YYYYMMDD) >> 20161118 이렇게 들어옴 
		
    	String cur_day = cur_ymd.substring(6);//현재 날짜(YYYYMM) >> 18

		String year = ObjectUtils.toString(req.getParameter("year"),cur_ymd.substring(0,4)); //파라미터 날짜 2016
		String month = ObjectUtils.toString(req.getParameter("month"),cur_ymd.substring(4,6)); //파라미터 날짜  11 
    		   month = month.length()<2?"0"+month:month;
    	String day = ObjectUtils.toString(req.getParameter("day"),cur_ymd.substring(6)); //파라미터 날짜 18
    		   day = day.length()<2?"0"+day:day;
    	
        //달력 그리기 -> 주간 그리기
    	String week_ymd = DateUtil.getNowWeekDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));	 // 20161113
	    
		String prev_ymd = DateUtil.getYmdaddDay(week_ymd, -7);	// 전주	// 20161106
    	String next_ymd = DateUtil.getYmdaddDay(week_ymd, 7);	// 다음주 // 20161120
		// 주간날짜리스트 정보
    	List<HashMap<String, Object>> week_list = DateUtil.getNowWeekList(week_ymd);
    	
    	HashMap<String, Object> map = new HashMap<String,Object>();
    	String startYear = ObjectUtils.toString(week_list.get(0).get("YEAR"));
    	String startMonth = ObjectUtils.toString(week_list.get(0).get("MONTH"));
    		   startMonth = startMonth.length() < 2 ? "0"+startMonth:startMonth;
    	String startSunDay = ObjectUtils.toString(week_list.get(0).get("DATE"));
    		   startSunDay = startSunDay.length() < 2 ? "0"+startSunDay:startSunDay;
    	map.put("staDt", startYear + "-" + startMonth + "-" + startSunDay);
    	
    	String endYear = ObjectUtils.toString(week_list.get(week_list.size()-1).get("YEAR"));
    	String endMonth = ObjectUtils.toString(week_list.get(week_list.size()-1).get("MONTH"));
    		   endMonth = endMonth.length() < 2 ? "0"+endMonth:endMonth;
    	String endSatDay = ObjectUtils.toString(week_list.get(week_list.size()-1).get("DATE"));
	   	       endSatDay = endSatDay.length() < 2 ? "0"+endSatDay:endSatDay;	   
    	map.put("finDt", endYear + "-" + endMonth + "-" + endSatDay);
	    
	    //구분1
        String sch_gb_01 = ObjectUtils.toString(vo.getSch_gb_01());
		if(sch_gb_01.equals("")) sch_gb_01 = "01";
		map.put("gb01", sch_gb_01);
		
		//구분2
	    String sch_gb_02 = ObjectUtils.toString(vo.getSch_gb_02());
		if(sch_gb_02.equals("")) sch_gb_02 = "01";
		map.put("gb02", sch_gb_02);
		
		List<HashMap<String, Object>> result = schSchService.selectSchByWeek(map);
		mav.addObject("schList", result);

		// 1시간은 13, 1.5시간은 26, 2시간은 39, 2.5시간은 52, .. (13씩 증가)
		
		HashSet<String> days = new HashSet<String>();		// 스케줄이 있는 요일만 모아두기.
		int[] counter = new int[]{0,0,0,0,0,0,0};
		for (int i = 0 ; i < result.size() ; i++){
			int theDay = Integer.parseInt(String.valueOf(result.get(i).get("THEDAY")));		//일~토 가 1~7 값으로 들어있음.
			counter[theDay - 1]++;
			days.add(String.valueOf(result.get(i).get("WEEKDAY")));	//월, 화, 수.. 등이 담김.
		}
		
		int tableWidth = 980;			// 기본 테이블 높이
		
		List<Integer> widths = new ArrayList<Integer>();
		for (int i = 0 ; i<counter.length ; i++){
			
			// logger.debug((i+1) + ":" + counter[i]);
			if(counter[i]>1){		
				// 1개 이상의 스케줄이 있다 
				widths.add(counter[i]*124);
				tableWidth = tableWidth + 124;		// 각 요일의 td 넓이 변경.
			} else if (counter[i] == 1){	
				// 1개의 스케줄이 있다
				widths.add(124);
			} else {								
				// 스케줄이 없다 
				widths.add(124);
			}
		}

		// 테이블 전체 폭을 넣는다.
		mav.addObject("tableWidth", tableWidth);
		
		// 테이블 속 각 td 폭을 넣는다.
		mav.addObject("widths", widths);
		
		mav.addObject("days", days);
		// 오늘날짜를 저장합니다.
        mav.addObject("cur_day",cur_day);
        // 년을 저장합니다.
        mav.addObject("year",year);//파라미터
        // 월을 저장합니다.
        mav.addObject("month",month);
        // 일을 저장합니다.
        mav.addObject("day",day);
        // 다음주년을 저장합니다.
        mav.addObject("next_year",next_ymd.substring(0, 4));
        // 다음주월을 저장합니다.
        mav.addObject("next_month",next_ymd.substring(4, 6));
        // 다음주일을 저장합니다.
        mav.addObject("next_day",next_ymd.substring(6));
        // 전주년을 저장합니다.
        mav.addObject("prev_year",prev_ymd.substring(0, 4));
        // 전주월을 저장합니다.
        mav.addObject("prev_month",prev_ymd.substring(4, 6));
        // 전주일을 저장합니다.
        mav.addObject("prev_day",prev_ymd.substring(6));
        // 주간리스트를 저장합니다.
        mav.addObject("arr_date_week_list",week_list);	
        
	    mav.addObject("sch_gb_01", sch_gb_01);
	    mav.addObject("sch_gb_02", sch_gb_02);
		mav.setViewName("sch/sch/schWeekSearchView");
		
		return mav;
	}
	
	
	
	/**
	 * 세부일정 등록 -> 기본정보 저장(insert)
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=insertSchInfo")
	public ModelAndView insertSchInfo(HttpServletRequest req, HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("pageJsonReport");
		return mav;
	}	

	@RequestMapping(params = "method=directorSearch")
	public ModelAndView directorSearch(HttpServletRequest req, HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView(ObjectUtils.toString(req.getParameter("forward")));
		mav.addObject("layout", "common/layout.vm");
		mav.addObject("result", "dd");
		return mav;
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
		수강안내 조건 검색의 결과 출력-
		@RequestMapping(params = "selectSchSendInfoByGbDtView")
		public ModelAndView selectSchSendInfoByGbDt(HttpServletRequest req) throws Exception {
			ModelAndView mav = new ModelAndView();
			mav.setView(new RedirectView("sch.do?method=schSendListSearchView"));
			return mav;
		}
	*/
	
	/*List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> map1 = new HashMap<String, Object>();
	map1.put("NAME", "이름1");
	map1.put("HOSNAME", "병원1");
	map1.put("ID", "11");
	HashMap<String, Object> map2 = new HashMap<String, Object>();
	map2.put("NAME", "이름2");
	map2.put("HOSNAME", "병원2");
	map2.put("ID", "12");
	HashMap<String, Object> map3 = new HashMap<String, Object>();
	map3.put("NAME", "이름3");
	map3.put("HOSNAME", "병원3");
	map3.put("ID", "13");
	HashMap<String, Object> map4 = new HashMap<String, Object>();
	map4.put("NAME", "이름4");
	map4.put("HOSNAME", "병원4");
	map4.put("ID", "14");
	list.add(map1);
	list.add(map2);
	list.add(map3);
	list.add(map4);*/
}
