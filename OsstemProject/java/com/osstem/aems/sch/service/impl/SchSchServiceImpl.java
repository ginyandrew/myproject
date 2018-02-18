package com.osstem.aems.sch.sch.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

import com.osstem.aems.main.model.MainVo;
import com.osstem.aems.main.service.impl.MainDao;
import com.osstem.aems.sch.sch.model.ApprSpkDto;
import com.osstem.aems.sch.sch.model.SchDtlDto;
import com.osstem.aems.sch.sch.model.SchMbrDto;
import com.osstem.aems.sch.sch.model.SchRegDto;
import com.osstem.aems.sch.sch.model.SchSchDto;
import com.osstem.aems.sch.sch.model.SendDto;
import com.osstem.aems.sch.sch.model.SendPsnDto;
import com.osstem.aems.sch.sch.service.SchSchService;
import com.osstem.aems.stdInfo.psn.model.PsnDto;
import com.osstem.aems.stdInfo.psn.service.impl.StdInfoPsnDao;
import com.osstem.comm.code.model.CodeDto;
import com.osstem.comm.code.model.CodeVo;
import com.osstem.comm.code.service.impl.CodeDao;
import com.osstem.comm.com.property.CommPropertyService;
import com.osstem.comm.com.util.string.DateUtil;
import com.osstem.comm.com.util.string.StringUtil;
import com.osstem.comm.com.util.urlshortener.UrlShortener;
import com.osstem.comm.mms.model.MmsDto;
import com.osstem.comm.mms.service.MmsService;
import com.osstem.comm.page.service.PageService;
import com.osstem.comm.sawon.model.SawonDto2;
import com.osstem.comm.sawon.service.SawonService;

@Service("SchSchService")
public class SchSchServiceImpl implements SchSchService {

	private Logger logger = Logger.getLogger(SchSchService.class);
	
	@Resource(name="CommPropertyService")
	private CommPropertyService commPropertyService;
	
	@Resource(name = "SchSchDao")
	private SchSchDao schDao;
	
	@Resource(name = "StdInfoPsnDao")
	private StdInfoPsnDao PsnDao;
	
	@Resource(name = "CodeDao")
	private CodeDao codeDao;
	
	@Resource(name = "MainDao")
	private MainDao mainDao;
	
	@Resource(name="MmsService")
	private MmsService mmsService;
	
	@Resource(name="PageService")
	private PageService pageService;
	
	@Resource(name="SawonService")
	private SawonService sawonService;
	
	/*
	 *  공통 - 자동검색 
	 */
	@Override
	public List<HashMap<String, Object>> selectPsnHosInfo(HashMap<String, Object> map) throws SQLException {
		return schDao.selectPsnHosInfo(map);
	}
	@Override
	public List<HashMap<String, Object>> selectSchTitle(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchTitle(map);
	}
	@Override
	public List<HashMap<String, Object>> autoSearchSchBySchId(HashMap<String, Object> map) throws SQLException {
		return schDao.autoSearchSchBySchId(map);
	}
	@Override
	public List<HashMap<String, Object>> selectAllPsnHosInfo(HashMap<String, Object> map) throws SQLException {
		return schDao.selectAllPsnHosInfoByName(map);
	}
	@Override
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameMbr(HashMap<String, Object> map) throws SQLException {
		return schDao.selectAllPsnHosInfoByNameMbr(map);
	}
	@Override
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameNotInReg( HashMap<String, Object> map) throws SQLException {
		return schDao.selectAllPsnHosInfoByNameNotInReg(map);
	}
	/*
	 * 공통 - 코드 검색
	 */
	@Override
	public List<CodeDto> getCodeList(CodeVo codeVo) throws SQLException {
		return codeDao.getCodeLst(codeVo);
	}
	
	/*
	 * 세부일정 - 강연회/연수회 내용 저장 (insert)
	 */
	@Transactional(rollbackFor=Exception.class)
	public void insertSchDtlMbr(SchSchDto schSch, SchDtlDto schDtlList, SchMbrDto schMbrList) throws SQLException {

		// (1) sch 저장.
		schDao.insertSchSch(schSch);
		
		String regId = schSch.getReg_id();		// 글 등록자.
		int sch_id = schSch.getSeq();		// 방금 저장한 sch 고유번호.

		logger.debug(schDtlList.getSchDtlList());
		// (2) dtl 저장.
		if (schDtlList.getSchDtlList() != null){
			for (SchDtlDto vo : schDtlList.getSchDtlList()){
				vo.setId(sch_id);
				vo.setReg_id(regId);
				schDao.insertSchDtl(vo);		// 최종 dtlVO 저장	
			}
		}
		
		logger.debug(schMbrList.getSchMbrList());
		// (3) mbr 저장.
			if(schMbrList.getSchMbrList() != null){
			for (SchMbrDto vo: schMbrList.getSchMbrList()){
				vo.setId(sch_id);
				vo.setReg_id(regId);
				schDao.insertSchMbr(vo);		// 최종 mbrVO 저장
			}
		}
	}
	
	/*
	 *  수강안내 - 수강안내 전체 검색(non-Javadoc)
	 */
	@Override
	public List<SchSchDto> selectSchListByGbDt(HashMap<String, Object> map) throws SQLException {
		
		List<SchSchDto> schList = new ArrayList<SchSchDto>();
		
		List<SchSchDto> schs = schDao.selectSchByGbDt(map);		
		for (SchSchDto dto : schs){
			List<String> names = schDao.selectPsnBySchId(dto.getId());

			String result = "";
			if(names.size()>2 ){
				int size = names.size() - 1 ;
				String theName = names.get(0);		// 14
				result = theName + " 외 "+size + "명";	// 김세웅
			} else if (names.size() > 0 && names.size() <= 2) {
				StringBuffer buffer = new StringBuffer();
				buffer.setLength(0);
				for (String s : names){
					buffer.append(s+" ");
				}
				result = buffer.toString();
			} 
			
			// 이렇게 각 이름을 한 줄마다 만들었다. 그럼 dto에 담아준다.
			dto.setName(result); //
			schList.add(dto);
		}
		
		return schList;
	}
	
	/*
	 * 수강안내 - 수강안내 전체 갯수 검색
	 */
	@Override
	public int selectSchSendInfoCntByGbDt(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchSendInfoCntByGbDt(map);
	}
	
	/*
	 * 일정관리 - 일정 전체 갯수 검색
	 */
	@Override
	public int selectSchCntByGbDt(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchCntByGbDt(map);
	}
	
	/*
	 * 일정관리 - 일정 seq로 일정검색
	 */
	@Override
	public List<HashMap<String, Object>> selectSchBySchId(HashMap<String, Object> map) throws SQLException {
		return schDao.autoSearchSchBySchId(map);
	}

	/*
	 * 연자평가 - 연자평가 등록 대상 sch : send에 있으면서 appr_spk에는 없던 sch만 select.
	 */
	@Override
	public List<HashMap<String, Object>> selectNonApprSchTitle(HashMap<String, Object> map) throws SQLException {
		List<HashMap<String, Object>> list = schDao.selectNonApprSchTitle(map);
		return list;
	}
	
	/*
	 * 연자평가 - 하나의 schId 하나로 appr 발송목록 가져오기 : 특정 SchId의 Dtl중 특정 MBR (01~05)가 있는 DTL 리스트
	 */
	/*@Override
	public String selectDtlListBySchMbr(HashMap<String, Object> map) throws SQLException {

		int schId = Integer.parseInt(String.valueOf(map.get("schId")));
		String gb01 = schDao.selectSchBySchId(schId).getSch_gb_01();
		
		List<SchMbrDto> mbrs =schDao.selectSchMbr01BySchId(map);	// schId와 mbrGb 01를 넣어 확정 Faculty가 있는 mbr만 고른다.
		String result = "";
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		
		for (int i = 0 ; i< mbrs.size() ; i++){	
			// mbr1개당 dtl 하나이며, mbr정보를 넣어 해당 dtl 정보를 가져온다.
			List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(mbrs.get(i).getPsn_id());
			
			HashMap<String, Object> m = new HashMap<String, Object>();
			int schNo = mbrs.get(i).getNo();
			m.put("schNo", schNo);
			m.put("schId", mbrs.get(i).getId());
			SchDtlDto dtl = schDao.selectSchDtl(m);
			
			buffer.append("<tr id='schNo"+ schNo +"'>");	
			buffer.append("<td name='"+schNo+"'>"+ dtl.getSch_sta_dt().substring(11, 16) 
					+" ~ "+ dtl.getSch_fin_dt().substring(11,16) + "</td>");		// 시간
			buffer.append("<td name='"+ mbrs.get(i).getPsn_id() +"'>"+ psns.get(0).get("NAME") + "</td>");	// Faculty 이름
			// gb01이 01 -강연회- 이면 바로 주제를 보여주고, 02 -연수회- 이면 '강의내용'이라는 버튼과 함께 schId와 schNo을 숨겨 버튼으로 보낸다.
			if (gb01.equals("01")){
				buffer.append("<td>"+ dtl.getSubject()+"</td>");						// 발표주제
			} else if (gb01.equals("02")){
				String dtlIdNo = schId + "," +schNo;
				buffer.append("<td><span class='btn_pack sch gray'>"
						+ "<button type='button' onclick='content_popup(this);'>강의내용</button></span>"
						+ "<input type='hidden' value='"+dtlIdNo+"'></td>");	// 발송버튼
			}
			
			buffer.append("<td><span class='btn_pack sch gray' name='"+ dtl.getSch_sta_dt().substring(11, 16) 
					+" ~ "+ dtl.getSch_fin_dt().substring(11,16) +"'>"
					+ "<button type='button' onclick='msg_popup2(this);'>발송</button></span></td>");	// 발송버튼
			buffer.append("</tr>");
			
		}
		result = buffer.toString();
		return result;
	}*/
	
	/*
	 * 연자평가 - 하나의 schId 하나와 여러개의 schNo로 appr에 시퀸스와 함께 저장.
	 */
	/*@Override
	public void insertApprSpk(HttpServletRequest req, int schId) throws SQLException {

		HttpSession session = req.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("schId", schId);
		
		// 먼저 schId와 schNo를 모두 찾는다.
		List<String> mbrGbs = new ArrayList<String>();
		mbrGbs.add("01");
		mbrGbs.add("04");
		mbrGbs.add("05");
		map.put("mbrGb", mbrGbs);
		
		map.put("ifDtlExist", "Y");		// schNo가 0이 아닌 것만 고르자.
		List<SchMbrDto> mbrs =schDao.selectSchMbr01BySchId(map);	// schId와 mbrGb 01를 넣어 확정 Faculty가 있는 mbr만 고른다.
		
		// 해당 schNo를 for문을 돌리며 
		for (SchMbrDto mbr : mbrs){
			
			int schNo = mbr.getNo();
			map.put("schNo", schNo);
			
			// (1) 이미 apprSpk에 저장되어있는지 cnt를 센다.
			int ifSpkExist = schDao.selectApprSpkCntBySchIdNo(map);
			
			if (ifSpkExist < 1){		// spk에 아직 만들어지지 않았다면 새로 저장한다.
				MainVo main = new MainVo();
				main.setSch_id(String.valueOf(schId));
				main.setSch_no(String.valueOf(schNo));		
				main.setPsn_id(String.valueOf(mbr.getPsn_id()));
				main.setReg_id(sabun);
				mainDao.insertApprSpk(main);
			}
		}
	}*/
	
	/*
	 * 강의수강안내 - 강의안내 리스트 조건검색
	 */
	@Override
	public List<HashMap<String, Object>> selectSchSendInfoByGbDt(HashMap<String, Object> map) throws SQLException {

		// 검색조건을 담아 리스트를 출력했다.
		List<HashMap<String, Object>> list = schDao.selectSchSendInfoByGbDt(map);
		
		for (HashMap<String, Object> m : list){
			
			int send_Id = Integer.parseInt(String.valueOf(m.get("SEND_ID")));

			// 각 sendId별로 tb_send_psn에서 psn의 정보를 가져와야 한다.
			List<Integer> psn = schDao.selectRepPsnBySendId(send_Id);		// psn의 id와 총 명수만 가져왔다.
			
			// 총 명수를 넣고, id를 이용해 이름을 가져온다.

			// 1. 수신자 명수 입력.
			int cnt = psn.size();
			// int cnt = Integer.parseInt(String.valueOf(psn.get("CNT")));
			m.put("CNT", cnt);
			if(cnt > 1){
				m.put("MINUSCNT", cnt-1);
			}

			// 2. 0보다 크면 대표 수신자의 이름을 가져오기.
			if( cnt < 1 ){
				m.put("NAME", "정보없음");
			} else {
				String psnId = String.valueOf(psn.get(0));
				PsnDto dto = PsnDao.selectPsnInfo(psnId); 
				m.put("NAME", dto.getName());	// 2. 대표 수신자 이름 추가. 
			}
			
		}
		// 각 sendId 별로 뽑은 m - 여기에 sendId마다 psn를 모아 정보를 넣어줘야 한다.
		return list;
	}
	
	/*
	 * 강의신청관리 - 강의신청 리스트 조건검색
	 */
	@Override
	public List<HashMap<String, Object>> selectSchSendYnList(HashMap<String, Object> map) throws SQLException {
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();	// 최종 리턴할 값 
		
		// reg와 send에서 sch를 group by해서 중복없이 뽑는다.
		List<Integer> schs = schDao.selectSchNoOverlap();
		
		// reg 와 send의 schId를 중복없이 set에 누적해서 합친다.
		// 누적없은 schid를 검색조건과 함께 map으로 합쳐 다시 한번 schID를 걸러낸다. (이게 최종 schId)
		map.put("schId", schs);
		
		List<HashMap<String, Object>> finalSch = schDao.selectSchNoOverlapBySchId(map);
		
		for (HashMap<String, Object> sch : finalSch){
			int schId = Integer.parseInt(String.valueOf( sch.get("SCH_ID")));
			// 이제 걸러진 schId로 기본 정보와 응답자 psnList를 뽑는다.
			
			HashMap<String, Object> m = new HashMap<String, Object>();	// 여기에 최종 값을 모을 것이다.
			m.put("schId", schId);
			List<Integer> psns = schDao.selectPsnIdNoOverlapFromRegSend(m);
			// 이제 psns에 각 schId마다 중복없는 psnId 리스트들로 저장되었다.
			
			// (1) sch 기본 정보 얻기
			SchSchDto schDto = schDao.selectSchBySchId(schId);
			
			m.put("SCH_ID", schId);									// 강의 아이디
			m.put("TITLE", schDto.getTitle());							// 강의 제목
			m.put("GB01", schDto.getSch_gb_01());						// 강의구분 1
			m.put("GB02", schDto.getSch_gb_02());						// 강의구분 2
			m.put("STA_Dt", schDto.getSch_sta_dt().substring(0, 10));	// 강의 첫날
			m.put("FIN_Dt", schDto.getSch_fin_dt().substring(0, 10));	// 강의마지막날
			m.put("RANKING", sch.get("RANKING"));						// 순위
			if (psns.isEmpty()){
				m.put("CNT", 0);							// 전체 명수 (대표1명 제외)
				m.put("NAME", "정보없음"); 					// 대표 1명의 이름
			} else {
				
				// Integer[] arr = psns.toArray(new Integer[psns.size()]);
				int onePsnId = psns.get(0);
				HashMap<String, Object> psnInfo = schDao.selectPsnByPsnId(onePsnId);
				m.put("CNT", psns.size() - 1);							// 전체 명수 (대표1명 제외)
				m.put("NAME", psnInfo.get("NAME")); 					// 대표 1명의 이름
			}
			result.add(m);
		}
		
		return result;
	}
	
	/*
	 * 강의 신청 응답 - 수동 저장 (insert)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateSchReg(SchRegDto dto) throws SQLException {
		schDao.updateSchReg(dto);
	}
	
	@Override
	public int selectSchRegCntBySchIdPsnId(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchRegCntBySchIdPsnId(map);
	}
	
	@Override
	public int selectSchSendYnCnt(HashMap<String, Object> map)throws SQLException {
		
		// reg와 send에서 sch를 group by해서 중복없이 뽑는다.
		List<Integer> schs = schDao.selectSchNoOverlap();
		// reg 와 send의 schId를 중복없이 set에 누적해서 합친다.
		int size = schs.size();
		return size;
	}
	
	// 그룹명으로 그룹 검색
	@Override
	public List<HashMap<String, Object>> selectGrpByGrpName(HashMap<String, Object> map) throws SQLException {
		return schDao.selectGrpByGrpName(map);
	}
	
	// 그룹아이디로 그룹 삭제
	@Override
	public void deleteGroupbyGrpId(HashMap<String, Object> map) throws SQLException {
		// GRP_ID로 (1) tb_grp_psn (2) tb_prg 삭제
		schDao.deleteGrpPsnByGrpId(map);
		schDao.deleteGrpByGrpId(map);
	}
	
	// 자동검색 - 이름 or 그룹 
	@Override
	public String selectPsnList(int id, String type) throws SQLException {
		
		List<HashMap<String, Object>> maps = new ArrayList<HashMap<String, Object>>();
		
		if (type.equals("group")){
			List<Integer> psnIds = schDao.selectPsnByGrpId(id);
			for (int psnId : psnIds){
				
				 
				List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(psnId);
				HashMap<String, Object> psn = psns.get(0);

				if(psn.get("ACA_CD") == null){
					psn.put("ACANAME", "학교정보없음");
				} else {
					System.out.println("여긴가2");
					String acaName = schDao.selectAcaNameByCode((String)psn.get("ACA_CD"));
					psn.put("ACANAME", acaName);					
				}

				maps.add(psn);
			}
			
		} else if (type.equals("allName")){
			 
			List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(id);
			HashMap<String, Object> psn = psns.get(0);

			if(psn.get("ACA_CD") == null){
				psn.put("ACANAME", "학교정보없음");
			} else {
				System.out.println("여긴가2");
				String acaName = schDao.selectAcaNameByCode((String)psn.get("ACA_CD"));
				psn.put("ACANAME", acaName);					
			}
			
			maps.add(psn);
		}
		
		String result = "";
		
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		for (HashMap<String, Object> m : maps){
			buffer.append("<tr>");
			buffer.append("<input type='hidden' value="+ m.get("PSN_ID")+">");
			buffer.append("<td>"+ m.get("NAME") +"</td><td>"+ m.get("HOSNAME")+"</td><td>"+ m.get("ACANAME")+"</td><td>"+ m.get("AGE")+"세</td>");
			buffer.append("<td><button type='button' class='del' onclick='deleteTr(this);'><span class='hidden'>삭제</span></button></td>");
			buffer.append("</tr>");
		}
		result = buffer.toString();
		
		return result;
	}
	
	@Override
	public SchSchDto selectSchBySchId(int schId) throws SQLException {
		return schDao.selectSchBySchId(schId);
	}
	
	@Override
	public SchDtlDto selectSchDtl(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchDtl(map);
	}
	
	@Override
	public HashMap<String, Object> selectSchDtlMbr(int sch_id) throws SQLException {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		SchSchDto sch = schDao.selectSchBySchId(sch_id);
		List<SchDtlDto> dtls = schDao.selectSchDtlBySchId(sch_id);
		List<SchMbrDto> mbrs = schDao.selectSchMbrBySchId(sch_id);
		
		
		// 이 단순정보를 또 for문으로 돌기 쉽게 가공해야 한다.
		if (sch.getSch_gb_01().equals("01")){
			// 강연회면
			List<HashMap<String, Object>> gb01List = new ArrayList<HashMap<String, Object>>();
			
			for (SchDtlDto dtl :dtls){
				HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("schNo", dtl.getNo());
				m.put("staHr", dtl.getSch_sta_dt().substring(11,13));
				m.put("staMi", dtl.getSch_sta_dt().substring(14,16));
				m.put("finHr", dtl.getSch_fin_dt().substring(11,13));
				m.put("finMi", dtl.getSch_fin_dt().substring(14,16));
				m.put("subject", dtl.getSubject());
				if ( dtl.getSub_gb() != null &&  dtl.getSub_gb() != ""){
					m.put("subgb", dtl.getSub_gb());
				}
				
				for (SchMbrDto mbr : mbrs){
					if (mbr.getId() == sch.getId() && mbr.getNo() == dtl.getNo()){
						if(mbr.getPsn_id()!= 0 ){
							
							String psnId = Integer.toString(mbr.getPsn_id());
							PsnDto psn = PsnDao.selectPsnInfo(psnId);
							m.put("mbr"+mbr.getMbr_gb() , psn.getName());
							m.put("psn"+mbr.getMbr_gb(), mbr.getPsn_id());
						}
					}
				}
				gb01List.add(m);
			}
			map.put("list", gb01List);
			
		} else {
			// 연구회면 
			
			// 총 3개의 for문 - director/ faculty 
			List<HashMap<String, Object>> gb02List1 = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> gb02List2 = new ArrayList<HashMap<String, Object>>();
			for (SchMbrDto mbr : mbrs){

				String psnId = Integer.toString(mbr.getPsn_id());
				PsnDto psn = PsnDao.selectPsnInfo(psnId);
				HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("name" , psn.getName());
				m.put("psnId", mbr.getPsn_id());
				m.put("mbrGb", mbr.getMbr_gb());
				m.put("schNo", mbr.getNo());
				
				if(mbr.getMbr_gb().equals("04") ){
					gb02List1.add(m);
				} else if (mbr.getMbr_gb().equals("05")){
					gb02List2.add(m);
				}
			}
			
			// 그리고 전체 dtl 회차 - staDt, staHr, finHr, content, sch_no (각 회차)
			List<HashMap<String, Object>> gb02List3 = new ArrayList<HashMap<String, Object>>();
			for (SchDtlDto dtl : dtls){
				HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("staDt", dtl.getSch_sta_dt().substring(0, 10));
				m.put("staHr", dtl.getSch_sta_dt().substring(11,13));
				m.put("finHr",dtl.getSch_fin_dt().substring(11,13));
				m.put("content", dtl.getContent().replaceAll("<br />", "\r\n"));
				m.put("schNo", dtl.getNo());
				if ( dtl.getSub_gb() != null &&  dtl.getSub_gb() != ""){
					m.put("subgb", dtl.getSub_gb());
				}
				gb02List3.add(m);
			}
			map.put("list1", gb02List1);		// 연수회의 director
			map.put("list2", gb02List2);		// 연수회의 faculty
			map.put("list3", gb02List3);		// 연수회의 detail
		}
		map.put("sch", sch);
		map.put("dtl", dtls);
		map.put("mbr", mbrs);
		
		return map;
	}
	
	@Override
	public HashMap<String, Object> selectSchMbrInfoBySchId(HashMap<String, Object> map) throws SQLException {
		
		//  schI가 포함된 map을 받으면 mbrGb에 따라 중복없이 mbr들을 뽑는다.
		HashMap<String, Object> result = new HashMap<String, Object>();
		for (int i = 1 ; i <=5 ; i++){
			
			String gb = "0"+i;
			String[] mbrGb = new String[]{gb};
			map.put("mbrGb", mbrGb);
			List<SchMbrDto> mbrList = schDao.selectSchMbr01BySchId(map);		// 기본 mbr정보를 뽑았다면 여기 psnId를 이용해 name을 담는다.
			if(mbrList != null && !mbrList.isEmpty()){
				
				HashSet<Integer> psnIds  = new HashSet<Integer>();
				for (SchMbrDto dto : mbrList){
					psnIds.add(dto.getPsn_id());
				}

				List<String> psnNames = new ArrayList<String>();
				for (int psnId : psnIds){
					psnNames.add(String.valueOf(schDao.selectPsnByPsnIdforAdd(psnId).get(0).get("NAME")));
				}
				result.put("mbr"+gb, psnNames);		// mbr01, mbr02.. 이렇게 저장된다.
			}
		}
		
		return result;
	}
	
	@Override
	public HashMap<String, Object> selectSchRegBySchIdPsnId(
			HashMap<String, Object> map) throws SQLException {
		
		int cnt = schDao.selectSchRegCntBySchIdPsnId(map);
		
		if (cnt <  1){
			map.put("SPKYN", "X");
			map.put("PRES_YN", "X");
			map.put("SABUN", "-");
			map.put("cnt", cnt);
		} else {
			map = schDao.selectSchRegBySchIdPsnId(map);
			map.put("cnt", cnt);
		}
		return map;
	}
	
	@Override
	public List<HashMap<String, Object>> selectPsnByPsnIdforAdd(int psnId) throws SQLException {
		return schDao.selectPsnByPsnIdforAdd(psnId);
	}
	
	/*
	 * 강의수강 안내 발송내역
	 */
	@Override
	public String selctPsnSendListBySchId(int sendId) throws SQLException {
		
		// sendId로 해당 강의를 들은 사람들의 psnId를 뽑아낸다.
		List<Integer> psnIds = schDao.selctPsnSendListBySchId(sendId);
		
		String result = "";
		
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		
		for (int i = 0 ; i < psnIds.size() ; i++){
			List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(psnIds.get(i));
			HashMap<String, Object> psn = psns.get(0);
			
			buffer.append("<tr>");
			buffer.append("<td>"+ (i+1)+"</td>");
			buffer.append("<td>"+ psn.get("NAME")+"</td>");
			buffer.append(" <td>"+ psn.get("HOSNAME")+"</td>");
			if(psn.get("ACA_CD") == null){
				buffer.append("<td>학교정보없음</td>");
			} else {
				String acaName = schDao.selectAcaNameByCode((String)psn.get("ACA_CD"));
				buffer.append("<td>"+acaName+"</td>"); 					
			}
			buffer.append("<td>"+psn.get("AGE")+"</td>");
			buffer.append("</tr>");
			
		}
		result = buffer.toString();
		
		return result;
	}
	
	
	/*
	 * 강의신청관리 - 응답자 상세정보 리스트
	 */
	@Override
	public String selectPsnRcptListBySchId(int schId) throws SQLException {

		
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		String result = "";
		
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		
		buffer.append("<div id='pop_wrapper5'>");
		
		buffer.append("<div id='pop_header'>");
		buffer.append("<h1>수신자 상세정보 Pop-up</h1>");
		buffer.append("<p class='close'><img src='../images/btn_close.gif' alt='닫기' onclick='closePopup()' class='hand' /></p>");
		buffer.append("</div>");
		
		buffer.append("<div id='pop_container'>");
		buffer.append("<div id='pop_cttarea'>");
		
		buffer.append("<table class='tb_list04'>");
		
		buffer.append("<colgroup>");
		buffer.append("<col width='40px' /><col width='65px' /><col width='170px' />");
		buffer.append("<col width='90px' /><col width='65px' /><col width='65px' /><col width='*' />");
		buffer.append("</colgroup>");
		
		buffer.append("<thead>");
		buffer.append("<tr>");
		buffer.append("<th scope='row'>No.</th>");
		buffer.append("<th scope='row'>성 명</th>");
		buffer.append("<th scope='row'>병의원명</th>");
		buffer.append("<th scope='row'>연락처</th>");
		buffer.append("<th scope='row'>강연 희망</th>");
		buffer.append("<th scope='row'>참석 희망</th>");
		buffer.append("<th scope='row'>담당<br>영업사원</th>");
		buffer.append("</tr>");
		buffer.append("</thead>");
		
		buffer.append("</table>");			// 상단 테이블 마감 
				
		// 하단 결과 테이블 시작
		buffer.append("<div class='tb_scroll1' height='300px'>");
		buffer.append("<table width='100%' class='tb_list03' >");
		buffer.append("<colgroup>");
		buffer.append("<col width='40px' /><col width='65px' /><col width='170px' />");
		buffer.append("<col width='90px' /><col width='65px' /><col width='65px' /><col width='*' />");
		buffer.append("</colgroup>");
		
		// List<Integer> psnIds = schDao.selectPsnListBySchId(schId);
		
		// (1) 중복성없는 psnId들 뽑기.
		List<Integer> psnIds1 = schDao.selectPsnIdNoOverlapFromReg(schId);  //-1 reg에서 뽑는다.
		List<Integer> psnIds2 = schDao.selectPsnIdNoOverlapFromSend(schId); //-2 send에서 psnId를 뽑는다.

		// (2) psnId들의 중복성을 제거한다.
		HashSet<Integer> psns = new HashSet<Integer>();
		for(int i : psnIds1){
			psns.add(i);
		}
		for(int i : psnIds2){
			psns.add(i);
		}
		Integer[] arr = psns.toArray(new Integer[psns.size()]);	// set을 array로 변경해 순서 부여.
		
		// 각각의 sch의 안내를 들은 psn모음들을 출력한다. reg/또는 send에 담당사원이 있을 것이다. 
		// reg에 있으면 reg를 먼저 찾고, reg에서 없으면 send에서 찾아온다.
		
		for (int i = 0 ; i < arr.length ; i++){
		
			List<HashMap<String, Object>> psnInfo = schDao.selectPsnByPsnIdforAdd(arr[i]);	
			HashMap<String, Object> info = psnInfo.get(0);

			HashMap<String, Object> map= new HashMap<String, Object>();
			map.put("schId", schId);
			map.put("psnId", arr[i]);
			
			int cnt = schDao.selectSchRegCntBySchIdPsnId(map);
			buffer.append("<tr>");
			buffer.append("<td>"+ (i+1)+"</td>");
			buffer.append("<td><a id='"+ arr[i] + "," + schId +"'href='#' onclick='movePageToManage(this);return false;'>"+ info.get("NAME")+"</a></td>");
			buffer.append("<td class='alignl'>"+ info.get("HOSNAME") +"</td>");
			buffer.append("<td>"+ info.get("PHONE")+"</td>");		// 여기까진 기본 정보.
			
			if (cnt < 1){
				buffer.append("<td>응답전</td>");
				buffer.append("<td>응답전</td>");
				buffer.append("<td>-</td>");
				buffer.append("<input type='hidden' name='sawonId' value></td>");

			} else {
				// Reg에 psn의 정보가 있다.
				HashMap<String, Object> regInfo = schDao.selectSchRegBySchIdPsnId(map);
				if(regInfo.get("SPKYN").equals("Y")){
					buffer.append("<td>O</td>");
				} else {
					buffer.append("<td>X</td>");
				}
				if (regInfo.get("PRES_YN").equals("Y")){
					buffer.append("<td>O</td>");
				} else {
					buffer.append("<td>X</td>");
				}
				
				String sabun = ObjectUtils.toString(regInfo.get("SABUN"));
				if(sabun.equals("")|| sabun == null) {
					buffer.append("<td>-");
					buffer.append("<input type='hidden' name='sawonId' value></td>");
				} else {
					SawonDto2 sawonDto = sawonService.selectSawon2(String.valueOf(regInfo.get("SABUN")));
					buffer.append("<td>"+ sawonDto.getName());
					buffer.append("<input type='hidden' name='sawonId' value='"+ sawonDto.getSabun() + "'></td>");
				}
			}
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		buffer.append("</div></div></div></div>");
		
		result = buffer.toString();
		
		return result;
	}
	
	@Override
	public List<HashMap<String, Object>> selectSendPsnRcptListBySchId(int schId) throws SQLException {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("schId", schId);
			m.put("presYn", "Y");
			List<Integer> psnList = schDao.selectPsnIdNoOverlapFromRegSend(m);		// 중복없는 psnList 출력.
			// 각각의 sch의 안내를 들은 psn모음들을 출력한다. reg/또는 send에 담당사원이 있을 것이다. 
			// reg에 있으면 reg를 먼저 찾고, reg에서 없으면 send에서 찾아온다.
			
			for (int psnId : psnList){

				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				List<HashMap<String, Object>> psnInfo = schDao.selectPsnByPsnIdforAdd(psnId);	
				HashMap<String, Object> info = psnInfo.get(0);

				HashMap<String, Object> map= new HashMap<String, Object>();
				map.put("schId", schId);
				map.put("psnId", psnId);
				
				int cnt = schDao.selectSchRegCntBySchIdPsnId(map);
				resultMap.put("psnId", psnId);
				resultMap.put("name", info.get("NAME"));
				resultMap.put("hosName", info.get("HOSNAME"));
				resultMap.put("phone", info.get("PHONE"));
				
				if (cnt < 1){
					resultMap.put("spkHope", "응답전");		// 강연희망 
					resultMap.put("presHope", "응답전");		// 참석희망
					resultMap.put("sawon", "-");			// 담당사원이름
					resultMap.put("selfYn", "X");				// 본인응답여부
					resultMap.put("restYn", "X");			// 발송제한여부
					resultMap.put("ifCanDelete", "N");
				} else {
					// Reg에 psn의 정보가 있다.
					int ifCanDelete = schDao.checkIfCanDeleteRegPsn(map);
					if(ifCanDelete > 0){
						resultMap.put("ifCanDelete", "N");		// appr_spk 에 존재하면 삭제 불가능.
					} else {
						resultMap.put("ifCanDelete", "Y");		// 존재하지 않으면 삭제 가능.
					}
					
					HashMap<String, Object> regInfo = schDao.selectSchRegBySchIdPsnId(map);
					if(regInfo.get("SPKYN").equals("Y")){
						resultMap.put("spkHope", "O");
					} else {
						resultMap.put("spkHope", "X");
					}
					if (regInfo.get("PRES_YN").equals("Y")){
						resultMap.put("presHope", "O");
					} else {
						resultMap.put("presHope", "X");
					}
					if (regInfo.get("SELF_YN").equals("Y")){
						resultMap.put("selfYn", "O");
					} else {
						resultMap.put("selfYn", "X");
					}
					resultMap.put("restYn", regInfo.get("REST_YN"));
					
					String sabun = ObjectUtils.toString(regInfo.get("SABUN"));
					if(sabun.equals("")|| sabun == null) {
						resultMap.put("sawon", "-");
					} else {
						SawonDto2 sawonDto = sawonService.selectSawon2(String.valueOf(regInfo.get("SABUN")));
						resultMap.put("sawon", sawonDto.getName());
						// sawonDto.getSabun()				// 사번도 뽑을 수 있음.
					}
				}
				list.add(resultMap);
			}
			logger.debug("정렬 ");
		return list;
	}
	
	@Override
	public SendDto selectSendBySendId(int sendId) throws SQLException {
		return schDao.selectSendBySendId(sendId);
	}
	
	// 하나의 sendId 메세지를 함께 받은 원장리스트 검색
	@Override
	public String selectPsnBySendId(int send_id) throws SQLException {

		
		List<HashMap<String, Object>> list = schDao.selectPsnBySendId(send_id);
		 
		String result = "";
		
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		
		buffer.append("<div id='pop_wrapper'>");
		buffer.append("<div id='pop_header'>");
		buffer.append("<h1>수신자 상세정보 Pop-up</h1>");
		buffer.append("<p class='close'><img src='../images/btn_close.gif' alt='닫기' onclick='closePopup()' class='hand' /></p>");
		buffer.append("</div>");
		buffer.append("<div id='pop_container'>");
		buffer.append("<div id='pop_cttarea'>");
		buffer.append("<table class='tb_list04'>");
		buffer.append("<colgroup>");
		buffer.append("<col width='40px' /><col width='80px' /><col width='250px' />");
		buffer.append("<col width='100px' /><col width='*' />");
		buffer.append("</colgroup>");
		buffer.append("<thead>");
		buffer.append("<tr>");
		
		buffer.append("<th scope='row'>No.</th>");
		buffer.append("<th scope='row'>성 명</th>");
		buffer.append("<th scope='row'>병의원명</th>");
		buffer.append("<th scope='row'>연락처</th>");
		buffer.append("<th scope='row'>발송성공여부</th>");
		
		buffer.append("</tr>");
		buffer.append("</thead>");
		buffer.append("</table>");
		buffer.append("<div class='tb_scroll1' height='300px'>");
		buffer.append("<table width='100%' class='tb_list03' >");
		buffer.append("<colgroup>");
		buffer.append("<col width='40px' /><col width='80px' /><col width='250px' />");
		buffer.append("<col width='100px' /><col width='*' />");
		buffer.append("</colgroup>");
		
		// 여기부터 반복
		for (int i = 0 ; i < list.size() ; i++){

			HashMap<String, Object> map = list.get(i);
			
			buffer.append("<tr>");
			buffer.append("<td>"+ (i+1) +"</td>");
			buffer.append("<td><a href='#'>"+ map.get("NAME") +"</a></td>");	// 원장 이름
			buffer.append("<td class='alignl'>"+ map.get("INFO") +"</td>");		// 병원이름
			buffer.append("<td>"+ map.get("PHONE")+"</td>");					// 메세지 발송 성공여부
			
			// 수신자 발송 여부 확인하기.		- null 포인트로 에러 남.
			String pMtRefkey = (String)map.get("MT_REFKEY");
			MmsDto selectMms = mmsService.selectMms(pMtRefkey);
			
			// 발송성공 확인
			String sendResult = selectMms.getMt_report_code_ib();
			if (sendResult != null && sendResult.equals("1000")) {
				logger.debug("MMS발송 완료!!!");
				buffer.append("<td>O</td>");
			} else {
				logger.debug("MMS발송 미완료!!!");
				buffer.append("<td>X</td>");
			}
			buffer.append(" </tr>");
		}
		// 반복 종료
	
		buffer.append(" </table>");
		buffer.append("</div></div></div></div>");

		result = buffer.toString();
		
		return result;
	}
	
	@Override
	public int selectApprSpkCntBySchId(int schId) throws SQLException {
		return schDao.selectApprSpkCntBySchId(schId);
	}
	
	@Override
	public int selectApprSchCntBySchId(int schId) throws SQLException {
		return schDao.selectApprSchCntBySchId(schId);
	}
	
	@Override
	public void updateSchDtlMbr(HttpServletRequest request, SchSchDto schSch, SchDtlDto schDtlList,
			SchMbrDto schMbrList) throws SQLException {
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		int schId = schSch.getId();
		// 먼저 sch로 싹 지우고
		// 이걸 다시 저장한다.
		schDao.deleteSchMbr(schId);
		schDao.deleteSchDtl(schId);

		schSch.setMod_id(sabun);
		schDao.updateSchSchBySchId(schSch);
		
		// (1) 멤버 업데이트 - 
		String regId = schSch.getReg_id();
		String regDt = schSch.getReg_dt().substring(0,16 );

		// (2) 디테일 업데이트		
		if (schDtlList.getSchDtlList().size() > 0){
			for (SchDtlDto dto : schDtlList.getSchDtlList()){
				dto.setId(schId);
				dto.setReg_id(regId);
				dto.setReg_dt(regDt);
				dto.setMod_id(sabun);
				schDao.insertSchDtl(dto);
				// modDt 는 오늘 날짜
			}
		}
		
		List<SchMbrDto> mbrlist = schMbrList.getSchMbrList();
		if (mbrlist != null){
			for (SchMbrDto dto : schMbrList.getSchMbrList()){
				dto.setId(schId);
				dto.setReg_id(regId);
				dto.setReg_dt(regDt);
				dto.setMod_id(sabun);
				schDao.insertSchMbr(dto);
				// modDt는 오늘 날짜
			}
		}
	}
	
	@Override
	public void copySchIntoTmp(SchSchDto schSch, SchDtlDto schDtlList,SchMbrDto schMbrList) throws SQLException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void deleteSchDtlMbr(int sch_id) throws SQLException {
		schDao.deleteSchMbr(sch_id);
		schDao.deleteSchDtl(sch_id);
		schDao.deleteSchSch(sch_id);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void insertSchReg(SchRegDto schReg) throws SQLException {
		
		schDao.insertSchReg(schReg);
	}
	
	// 하나의 send의  해당 원장들의 리스트 저장.
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void insertSendPsn(SendDto send, SendPsnDto sendPsn,  HttpServletRequest request)
			throws SQLException {
		
		HttpSession session = request.getSession();
		String sabun = StringUtil.isNullToString(session.getAttribute("S_USER_ID"));
		
		// (1) send 저장.
		schDao.insertSend(send);
		
		int send_id = send.getSeq();		// 방금 저장한 sch 고유번호.
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sendId", send_id);

		// (2) send_psn 저장.
		for (SendPsnDto vo : sendPsn.getSendPsnList()){
			
			int psnId = vo.getPsn_id();
			map.put("psnId", psnId );

			int ifExist = schDao.checkIfSendPsnExist(map);

			if (ifExist == 0){
				
				// AEMS_테이블명(SEND)_schId_sendId_psnID  
				String mt_refkey = StringUtil.isNullToString(commPropertyService.getString("MT_REFKEY_GB"), "AEMS0") + "_SEND_" +send.getSch_id() + "_" + send_id + "_" + vo.getPsn_id();
	
				PsnDto psn = PsnDao.selectPsnInfo(String.valueOf(sendPsn.getPsn_id()));
				List<HashMap<String, Object>> psnInfo = schDao.selectPsnByPsnIdforAdd(psnId);
				String psnName = String.valueOf(psnInfo.get(0).get("NAME"));
				String phone = String.valueOf(psnInfo.get(0).get("PHONE"));

				// phone과 mtRefkey 가 있으면 발송을 할 수 있다. 
				SchSchDto dto = schDao.selectSchBySchId(send.getSch_id());
				 
				String content = "";
				String url = StringUtil.isNullToString(commPropertyService.getString("BASE_URL")) + StringUtil.isNullToString(commPropertyService.getString("CONTENT_ROOT")) + "/sch/sch.do?method=schSendMobileMsgView&mtRefkey="+ mt_refkey;
				// 운영서버일 경우 UrlShortener 사용
				if (StringUtil.isNullToString(commPropertyService.getString("CONTENT_ROOT")).isEmpty()) {
					url = UrlShortener.getShortUrl(url);
				}
				
				if (dto.getSch_gb_01().equals("01")){
					// 강연회이면 
					content = dto.getTitle() + "\r\n\r\n" + "발표와 토론! 디렉터/패컬티 발전을 위해 가르치는 선생님들의 발표, 토론의 장에 "+ psnName+" 님을 초대합니다!" + "\r\n" + url;
				} else {
					// 연수회이면 
					// 내용
					// String content = "교육관리 테스트 입니다. http://www.naver.com";
					content = dto.getTitle() + " 강의에 초대합니다!" + "\r\n"+ "참석을 원하시면 하단 링크를 통해 신청해주세요!" + "\r\n" + url;
				}
				// 제목
				// String subject = "교육관리 MMS발송 테스트";
				String subject = "[오스템 AIC 강의 안내]";
				// 받는사람 번호
				String recipientNum = phone;
				// 보내는 사람 번호
				String callback = StringUtil.isNullToString(commPropertyService.getString("CALL_BACK_NUM"));
				
				// MMS 발송
				MmsDto mmsDto = new MmsDto();
				// 발송구분값 
				mmsDto.setMt_refkey(mt_refkey);
				// 발송일시 (즉시)
				mmsDto.setDate_client_req(DateUtil.getCurrentDateTimeAsString());
				// 제목
				mmsDto.setSubject(subject);
				// 내용
				mmsDto.setContent(content);
				// 받는사람 번호
				mmsDto.setRecipient_num(recipientNum);
				// 보내는 사람 번호
				mmsDto.setCallback(callback);
				
				logger.debug("- insertMms");
				MmsDto insertMms = mmsService.insertMms(mmsDto);
				logger.debug(insertMms.toString());
				
				// 발송 테이블 MT_REFKEY 업데이트 
				vo.setMt_refkey(insertMms.getMt_refkey());
				vo.setSend_id(send_id);
				logger.debug("- SendPsnDto:" + vo.toString());
				logger.debug("- insertSendPsn");
				
				schDao.insertSendPsn(vo);
			}
		}
	}
	
	/*
	 * 연자평가 초기 리스트 출력 
	 */
	/*@Override
	public List<HashMap<String, Object>> selectApprSchList(
			HashMap<String, Object> map) throws SQLException {
		
			List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();	// 최종값을 담을 곳.
			
			// map엔 최소 pageNo가 담겨있다. 

			List<HashMap<String, Object>> schList = schDao.selectApprSchList(map);	// appr_spk에 등록된 sch만 출력했다. 
			for (HashMap<String, Object> m : schList){
				// logger.debug("각 appr Sch : " + m.toString());
				
				HashMap<String, Object> eachSch = new HashMap<String, Object>();	// 최종값을 담을 map
				
				// 1. sch 기본 정보를 담는다.
				eachSch.put("ranking", m.get("RANKING"));
				eachSch.put("gb02", m.get("GB02"));
				eachSch.put("title", m.get("TITLE"));
				eachSch.put("stadt", m.get("STADT"));
				eachSch.put("findt", m.get("FINDT"));
				
				
				// 2. 각 schId로 mbr 정보와 평균점수, 연자추천값을 가져온다.
				int schId = Integer.parseInt(String.valueOf(m.get("SCHID")));
				eachSch.put("schId", schId);
				
				List<SchMbrDto> mbrs = schDao.selectSchMbrBySchId(schId);		// (1) mbr 정보
				if (mbrs.size() == 1){
					int mbr1 = mbrs.get(0).getPsn_id();
					List<HashMap<String, Object>> psn1 = schDao.selectPsnByPsnIdforAdd(mbr1);
					eachSch.put("mbr", psn1.get(0).get("NAME"));
				} else if (mbrs.size() == 2){
					int mbr1 = mbrs.get(0).getPsn_id();
					List<HashMap<String, Object>> psn1 = schDao.selectPsnByPsnIdforAdd(mbr1);
					int mbr2 = mbrs.get(1).getPsn_id();
					List<HashMap<String, Object>> psn2 = schDao.selectPsnByPsnIdforAdd(mbr2);
					eachSch.put("mbr", psn1.get(0).get("NAME") + "<br/>" + psn2.get(0).get("NAME"));
				} else if (mbrs.size() > 2){
					int mbr1 = mbrs.get(0).getPsn_id();
					List<HashMap<String, Object>> psn1 = schDao.selectPsnByPsnIdforAdd(mbr1);
					eachSch.put("mbr", psn1.get(0).get("NAME") + " 외 " + (mbrs.size()-1) + "명");
				} else {
					eachSch.put("mbr", "정보없음");
				}
				
				// (2) 평균점수
				HashMap<String, Object> sch = new HashMap<String, Object>();
				sch.put("schId", schId);
				int ifSpkPsnExist = schDao.selectApprSpkPsnCntBySchId(sch);
				if (ifSpkPsnExist > 0){
					String apprCdAvg = schDao.selectApprCdAvgBySchId(schId);
					eachSch.put("apprCd", apprCdAvg+"점");
				} else {
					eachSch.put("apprCd", "응답전");
				}
				
				// (3) 연자추천여부 - schid/schno/psnid를 넣어 appr에 recm 이 있는지 확인해야하는데..
				// schId 있음.
				String[] mbrGb = new String[]{"01"};
				eachSch.put("mbrGb", mbrGb);
				
				int recmYCnt  = 0;
				int recmNCnt  = 0;
				List<SchMbrDto> mbr01s =schDao.selectSchMbr01BySchId(eachSch);	// schId와 mbrGb 01를 넣어 확정 Faculty가 있는 mbr만 고른다.
				for (SchMbrDto mbr : mbr01s){
					HashMap<String, Object> psn = new HashMap<String, Object>();
					psn.put("schId", schId);
					psn.put("psnId", mbr.getPsn_id());
					psn.put("schNo", mbr.getNo());
					
					int ifApprExist = schDao.selectApprSchCntByPsnId(psn);
					if (ifApprExist > 0){
						ApprSpkDto apprSpk =  schDao.selectApprSch(psn);
						if (StringUtil.isNullToString(apprSpk.getRecm_yn()).equals("Y")) {
							recmYCnt++;
						} else if (StringUtil.isNullToString(apprSpk.getRecm_yn()).equals("N")) {
							recmNCnt++;
						}
					}
				}
				
				// 만약 연자추천이 있었다면
				if (recmYCnt > recmNCnt 
						|| (recmYCnt != 0 && recmYCnt == recmNCnt)) {
					eachSch.put("recmYn", "O");
				}
				// 연자추천이 없었다면
				else if (recmYCnt < recmNCnt) {
					eachSch.put("recmYn", "X");
				}
				// 연자추천 값이 없는경우 
				else {
					eachSch.put("recmYn", "---");
				}
				
				result.add(eachSch);
			}
		
		return result;
	}*/
	
	
	
	/*
	 * 연자평가 초기 리스트 총 갯수
	 */
	/* @Override
	public int selectApprSchCnt(HashMap<String, Object> map)
			throws SQLException {
		return schDao.selectApprSchCnt(map);
	} */
	
	/*
	 * 연자평가 조회/수정 뷰
	 
	@Override
	public List<HashMap<String, Object>> selectApprSchDtlList(int schId)
			throws SQLException {
		 
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] mbrGb = new String[]{"01"};
		map.put("schId", schId);
		map.put("mbrGb", mbrGb);
		
		List<SchMbrDto> mbrs =schDao.selectSchMbr01BySchId(map);	// schId와 mbrGb 01를 넣어 확정 Faculty가 있는 mbr만 고른다.
		
		for (SchMbrDto mbr : mbrs){
			// mbr1개당 dtl 하나이며, mbr정보를 넣어 해당 dtl 정보를 가져온다.

			HashMap<String, Object> m = new HashMap<String, Object>();
			int schNo = mbr.getNo();
			m.put("schNo", schNo);
			m.put("psnId", mbr.getPsn_id());
			m.put("schId", mbr.getId());
			SchDtlDto dtl = schDao.selectSchDtl(m);
			
			m.put("time", dtl.getSch_sta_dt().substring(11, 16)
					+" ~ "+ dtl.getSch_fin_dt().substring(11,16));		// 시간
			m.put("subject", dtl.getSubject());							// 제목

			List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(mbr.getPsn_id());
			m.put("mbr",  psns.get(0).get("NAME"));						// Faculty 이름


			// appr Spk 에 등록되어 있어야 연자추천, 연자평가 점수를 가져올 수 있다.
			int apprCntByPsn = schDao.selectApprSchCntByPsnId(m);			// 해당 연자가 appr에등록되어있는지 확인.
			if (apprCntByPsn > 0){											// 등록되어 있다면.
				// 연자추천 가져오기
				ApprSpkDto spks = schDao.selectApprSch(m);
				//logger.debug("각 회차당 연자정보:"+spks);
				m.put("apprId", spks.getAppr_id());
				
				String recm = ObjectUtils.toString(spks.getRecm_yn());
				if (recm != "" && recm != null ){		// 연자 추천 여부.
					if (recm.equals("Y")){
						m.put("recm", "O");
					} else if (recm.equals("N")){
						m.put("recm", "X");
					}
				} else {
					m.put("recm", null);
				}
				
				int ifSpkPsnExist = schDao.selectApprSpkPsnCntBySchId(m);
				if (ifSpkPsnExist > 0){
					
					// apprId로 연자평가점수 가져오기	(cnt1 -우수/ cnt2- 보통/ cnt3- 미흡)
					int apprId = spks.getAppr_id();
					List<HashMap<String, Object>> apprCds = schDao.selectApprCdsByApprId(apprId);
					for (HashMap<String, Object> apprCd : apprCds){
						if(apprCd.get("APPR_CD").equals("01")){			
							m.put("cnt1", apprCd.get("CNT"));
						} else if(apprCd.get("APPR_CD").equals("02")){
							m.put("cnt2", apprCd.get("CNT"));
						} else if(apprCd.get("APPR_CD").equals("03")){
							m.put("cnt3", apprCd.get("CNT"));
						}
					}
				} 
				
			} 
			result.add(m);
		}
		
		return result;
	}*/
	
	/*
	 * 연자평가 업데이트
	 */
	/*
	@Override
	public void updateApprSpkList(ApprSpkDto apprSpkList) throws SQLException {
		
		 
		String sabun = apprSpkList.getReg_id();
		
		// appr_spk 리스트를 받았다. 이미 appr 가 있으면 업데이트, 없으면 저장한다.
		List<ApprSpkDto> apprList = apprSpkList.getApprSpkList();
		for (ApprSpkDto appr : apprList){
			
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("psnId", appr.getPsn_id());
			m.put("schId", appr.getSch_id());
			m.put("schNo", appr.getSch_no());
			int ifApprExist = schDao.selectApprSchCntByPsnId(m);
			
			if(ifApprExist > 0){						// 이미 있으니 업데이트.
				ApprSpkDto dto = schDao.selectApprSch(m);
				dto.getAppr_id();
				
				MainVo main = new MainVo();
				main.setAppr_id(String.valueOf(dto.getAppr_id()));
				main.setLipsn_id(sabun);
				if(appr.getRecm_yn().isEmpty() || appr.getRecm_yn() == "" || appr.getRecm_yn() == null){
					main.setRecm_yn("");
				} else {
					main.setRecm_yn(appr.getRecm_yn());
				}
				mainDao.updateApprRecmYn(main); 
				
			} else {									// 없으면 새로 생성.
				appr.setReg_id(sabun);
				schDao.insertApprSpkRecmYn(appr);			
			}
		}
	}
	*/
	
	@Override
	public void deleteRegPsn(SchRegDto dto) throws SQLException {
		 schDao.deleteRegPsn(dto);
	}
	 
	@Override
	public int selectRegCntByPsnSchId(HashMap<String, Object> map) throws SQLException {
		return schDao.selectRegCntByPsnSchId(map);
	}
	
	@Override
	public String readExcelFile(MultipartFile file) throws SQLException {
		
		String result = "";					// 출력할 버퍼스트링 init
		
		StringBuffer buffer = new StringBuffer();
		buffer.setLength(0);
		
		int i = 0;
		try {
			
			Workbook workbook = WorkbookFactory.create(file.getInputStream());	// excel file로부터 workbook객체 생성.
			//HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream()); 
			
			Sheet worksheet = workbook.getSheetAt(0); 		// 첫째 sheet을 나타내는 worksheet 객체 생성.
			
			while(i <= worksheet.getLastRowNum()){				// sheet 내에 있는 마지막 줄에 닿을때까지
				
				Row row = worksheet.getRow(i++);			// excel 내에 row 하나씩을 생성.
				
				if (row != null){
				 
					if (row.getCell(0) != null  && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK 
							&& row.getCell(0).getCellType() != Cell.CELL_TYPE_ERROR
							&& row.getCell(0).getCellType() != Cell.CELL_TYPE_BOOLEAN){	// 만약 cell이 공란이 아니라면
						
						Cell cell = row.getCell(0);
						String value = null;
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA :
							value = cell.getCellFormula();
						    break;
						case Cell.CELL_TYPE_NUMERIC :
						    value = String.valueOf(cell.getNumericCellValue()); //double
						    break;
						case Cell.CELL_TYPE_STRING :
						    value = cell.getStringCellValue(); //String
						    break;
						}
						
						if (value.trim() != null && value.trim() != "" && !value.trim().isEmpty()){
							int liceNo = (int)Double.parseDouble(value);
							String lice_no = String.valueOf(liceNo);
							PsnDto psnDto = new PsnDto();
							psnDto.setLice_no(lice_no);
							int ifPsnExist = PsnDao.isChkPsnNum(psnDto);
	
							if (ifPsnExist > 0 ){
								int psnId = schDao.selectPsnByLiceNo(liceNo);
							
								List<HashMap<String, Object>> psns = schDao.selectPsnByPsnIdforAdd(psnId);
								HashMap<String, Object> psn = psns.get(0);
			
								if(psn.get("ACA_CD") == null){
									psn.put("ACANAME", "학교정보없음");
								} else {
									String acaName = schDao.selectAcaNameByCode((String)psn.get("ACA_CD"));
									psn.put("ACANAME", acaName);					
								}
								
								buffer.append("<tr>");
								buffer.append("<input type='hidden' value="+ psnId +">");
								buffer.append("<td>"+ psn.get("NAME") +"</td><td>"+ psn.get("HOSNAME")+"</td><td>"+ psn.get("ACANAME")+"</td><td>"+ psn.get("AGE")+"세</td>");
								buffer.append("<td><button type='button' class='del' onclick='deleteTr(this);'><span class='hidden'>삭제</span></button></td>");
								buffer.append("</tr>");
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result = buffer.toString();
		
		return result;
	}

	@Override
	public List<HashMap<String, Object>> selectSchByMonth(HashMap<String, Object> map) throws SQLException {

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		
		if (map.get("gb01").equals("01")){
			result = schDao.selectSch01ByMonth(map);
		} else {
			result = schDao.selectSch02ByMonth(map);
		}
		
		return result;
	}

	@Override
	public List<HashMap<String, Object>> selectSchByWeek( HashMap<String, Object> map) throws SQLException {

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		
		if (map.get("gb01").equals("01")){
			result = schDao.selectSch01ByWeek(map);
		} else {
			result = schDao.selectSch02ByWeek(map);
		}
		
		return result;
	}
	
	@Override
	public List<HashMap<String, Object>> selectSchByYear( HashMap<String, Object> map) throws SQLException {
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		// 해당 연도의 첫날과 마지막날을 map에 넣어야 한다.
		String cur_year = String.valueOf(map.get("year"));	// 현재 연도 가져옴.
		// 해당 연도의 각 달의 첫날, 다음달 첫날을 넣어 각 달마다 해당하는 스케줄들을 가져온다.

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		
		for (int i = 0 ; i < 12 ; i++){	// i는 1월~12월 
			
			Calendar c1 = Calendar.getInstance();
			c1.set(Integer.parseInt(cur_year), i, 1);	// 해당 년도 각 달의 첫째날 세팅.
			Date stDate = c1.getTime();
			String firstDay = fmt.format(stDate);
			
			int endDay = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			Calendar c2 = Calendar.getInstance();
			c2.set(Integer.parseInt(cur_year), i, endDay);
			Date finDate = c2.getTime();
			String lastDay = fmt.format(finDate);
			
			map.put("firstDay", firstDay);
			map.put("lastDay", lastDay);
			
			if (map.get("gb01").equals("01")){
				List<HashMap<String, Object>> list01 = schDao.selectSch01ByYear(map);
				for(HashMap<String,Object> m : list01 ){
					result.add(m);
				}
			} else {
				List<HashMap<String, Object>> list02 = schDao.selectSch02ByYear(map);
				for(HashMap<String,Object> m : list02 ){
					result.add(m);
				}
			}
		}
		return result;
	}
	
	@Override
	public int selectSchCntByYear(HashMap<String, Object> map) throws SQLException {
		
		// 최대 몇번 tr를 돌릴 것인지 알아온다.
		String cur_year = String.valueOf(map.get("year"));	// 현재 연도 가져옴.
		map.put("firstDay", cur_year + "-01-01");
		map.put("lastDay",cur_year + "-12-31");

		int maxCnt = 0;
		if (map.get("gb01").equals("01")){
			maxCnt = schDao.selectSch01CntByYear(map);
		} else {
			maxCnt = schDao.selectSch02CntByYear(map);
		}
		return maxCnt;
	}
	
	@Override
	public int selectSchCnt(SchSchDto sch) throws SQLException {
		return schDao.selectSchCnt(sch);
	}
	
	@Override
	public List<HashMap<String, Object>> selectSchByYearPlace(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchByYearPlace(map);
	}
	
	@Override
	public List<HashMap<String, Object>> selectSchPlaceNmByYear(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchPlaceNmByYear(map);
	}
	
	@Override
	public List<HashMap<String, Object>> selectSch01ByMonth(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSch01ByMonth(map);
	}
	
	@Override
	public int selectSchMbrCntByYear(HashMap<String, Object> map)throws SQLException {
		return schDao.selectSchMbrCntByYear(map);
	}
	
	@Override
	public List<HashMap<String, Object>> selectSchMbrByYear(HashMap<String, Object> map) throws SQLException {
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String cur_year = String.valueOf(map.get("year"));	// 현재 연도 가져옴.
		// 해당 연도의 각 달의 첫날, 다음달 첫날을 넣어 각 달마다 해당하는 스케줄들을 가져온다.

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		
		String[] mbrs = new String[]{"01","02"};		// faculty 확정/ 예비
		
		for (int m = 0 ; m < mbrs.length ; m++){
			map.put("mbrGb", mbrs[m]);
			
			for (int i = 0 ; i < 12 ; i++){	// i는 1월~12월 
				
				Calendar c1 = Calendar.getInstance();
				c1.set(Integer.parseInt(cur_year), i, 1);	// 해당 년도 각 달의 첫째날 세팅.
				Date stDate = c1.getTime();
				String firstDay = fmt.format(stDate);
				
				int endDay = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
				
				Calendar c2 = Calendar.getInstance();
				c2.set(Integer.parseInt(cur_year), i, endDay);
				Date finDate = c2.getTime();
				String lastDay = fmt.format(finDate);
				
				map.put("firstDay", firstDay);
				map.put("lastDay", lastDay);
				
				int ifSchExist = schDao.selectSchMbrCntByYear(map);
				if (ifSchExist > 0){
					List<HashMap<String, Object>> list01 = schDao.selectSchMbrByYear(map);
					for(HashMap<String,Object> mbrInfo : list01 ){
						int psnId = Integer.parseInt(String.valueOf(mbrInfo.get("PSN_ID")));
						mbrInfo.put("NAME", schDao.selectPsnByPsnIdforAdd(psnId).get(0).get("NAME"));
						result.add(mbrInfo);
					}
				}
				
				// 가장 큰 rownum도 찾아야한다.
			}
		}
		return result;
	}
	
	@Override
	public int selectSchMbrMaxCntByYear(HashMap<String, Object> map) throws SQLException {
		return schDao.selectSchMbrMaxCntByYear(map);
	}
	
	@Override
	public int selectSendSchCntBySchId(int schId) throws SQLException {
		return schDao.selectSendSchCntBySchId(schId);
	}
	
	@Override
	public int selectSchCntBySchIdNTime(int schId) throws SQLException {
		return schDao.selectSchCntBySchIdNTime(schId);
	}
}
