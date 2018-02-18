package com.osstem.aems.sch.sch.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.osstem.aems.sch.sch.model.ApprSpkDto;
import com.osstem.aems.sch.sch.model.SchDtlDto;
import com.osstem.aems.sch.sch.model.SchMbrDto;
import com.osstem.aems.sch.sch.model.SchRegDto;
import com.osstem.aems.sch.sch.model.SchSchDto;
import com.osstem.aems.sch.sch.model.SendDto;
import com.osstem.aems.sch.sch.model.SendPsnDto;
import com.osstem.comm.code.model.CodeDto;
import com.osstem.comm.code.model.CodeVo;

public interface SchSchService {

	public HashMap<String, Object> selectSchMbrInfoBySchId(HashMap<String, Object> map) throws SQLException;
	
	public SchDtlDto selectSchDtl (HashMap<String, Object> map) throws SQLException;
	
	public int selectSendSchCntBySchId(int schId) throws SQLException;
	
	public int selectSchCntBySchIdNTime(int schId) throws SQLException;
	
	public int selectSchMbrMaxCntByYear (HashMap<String, Object> map) throws SQLException;
	
	public int selectSchMbrCntByYear (HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchMbrByYear (HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSch01ByMonth (HashMap<String, Object> map) throws SQLException;

	public List<HashMap<String, Object>> selectSchByYearPlace(HashMap<String, Object> map) throws SQLException;

	public List<HashMap<String, Object>> selectSchPlaceNmByYear(HashMap<String, Object> map) throws SQLException;
	
	public int selectSchCnt(SchSchDto sch) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchByYear(HashMap<String, Object> map) throws SQLException;
	
	public int selectSchCntByYear(HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchByMonth(HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchByWeek(HashMap<String, Object> map) throws SQLException;

	public String readExcelFile(MultipartFile file) throws SQLException;
	
	public void deleteGroupbyGrpId(HashMap<String, Object> map) throws SQLException;
	
	public int selectRegCntByPsnSchId(HashMap<String, Object> map) throws SQLException;
	
	public void deleteRegPsn (SchRegDto dto) throws SQLException;
	
	//public void updateApprSpkList(ApprSpkDto apprSpkList) throws SQLException;
	
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameNotInReg(HashMap<String, Object> map) throws SQLException;
	
	// public List<HashMap<String, Object>> selectApprSchDtlList(int schId) throws SQLException;

	//public List<HashMap<String, Object>> selectApprSchList(HashMap<String, Object> map) throws SQLException;
	
	//public int selectApprSchCnt (HashMap<String, Object> map) throws SQLException;
	
	public int selectApprSchCntBySchId(int schId) throws SQLException;
	 
	public int selectApprSpkCntBySchId(int schId) throws SQLException;
	
	//public void insertApprSpk(HttpServletRequest req, int schId) throws SQLException;
	
	public int selectSchRegCntBySchIdPsnId(HashMap<String, Object> map)throws SQLException;
	
	public SendDto selectSendBySendId(int sendId) throws SQLException;
	
	public List<CodeDto> getCodeList(CodeVo code) throws SQLException;
	
	public void updateSchReg(SchRegDto dto) throws SQLException;
	
	// public String selectDtlListBySchMbr(HashMap<String, Object> map) throws SQLException;
	
	public String selctPsnSendListBySchId(int schId) throws SQLException;
	
	public HashMap<String, Object> selectSchRegBySchIdPsnId(HashMap<String, Object> map)throws SQLException;
	
	public List<HashMap<String, Object>> selectPsnByPsnIdforAdd (int psnId) throws SQLException;

	public String selectPsnRcptListBySchId (int schId) throws SQLException;

	public List<HashMap<String, Object>> selectSendPsnRcptListBySchId (int schId) throws SQLException;
	
	public List<SchSchDto> selectSchListByGbDt(HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchBySchId(HashMap<String, Object> map) throws SQLException;
	
	public int selectSchCntByGbDt (HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchSendInfoByGbDt(HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectSchSendYnList(HashMap<String, Object> map) throws SQLException;
	
	public int selectSchSendYnCnt(HashMap<String, Object> map) throws SQLException;
	
	public int selectSchSendInfoCntByGbDt (HashMap<String, Object> map) throws SQLException;
	
	public String selectPsnBySendId(int send_id) throws SQLException;

	public SchSchDto selectSchBySchId(int schId) throws SQLException;
	
	public void insertSchDtlMbr(SchSchDto schSch, SchDtlDto schDtlList, 
			SchMbrDto schMbrList)throws SQLException;

	public void updateSchDtlMbr(HttpServletRequest request, SchSchDto schSch, SchDtlDto schDtlList, 
			SchMbrDto schMbrList)throws SQLException;
	
	public void copySchIntoTmp(SchSchDto schSch, SchDtlDto schDtlList, 
			SchMbrDto schMbrList)throws SQLException;
	
	public void deleteSchDtlMbr(int sch_id) throws SQLException;
	
	public void insertSendPsn(SendDto send, SendPsnDto sendPsn,  HttpServletRequest request) throws SQLException;
	
	public String selectPsnList(int id, String type) throws SQLException;

	public List<HashMap<String, Object>> selectNonApprSchTitle(HashMap<String, Object> map) throws SQLException;
	
	public HashMap<String, Object> selectSchDtlMbr(int sch_id) throws SQLException;

	public List<HashMap<String, Object>> selectPsnHosInfo (HashMap<String, Object> map) throws SQLException;
	public List<HashMap<String, Object>> selectSchTitle (HashMap<String, Object> map) throws SQLException;
	public List<HashMap<String, Object>> autoSearchSchBySchId (HashMap<String, Object> map) throws SQLException;
	public List<HashMap<String, Object>> selectAllPsnHosInfo (HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameMbr (HashMap<String, Object> map) throws SQLException;
	
	public List<HashMap<String, Object>> selectGrpByGrpName (HashMap<String, Object> map) throws SQLException;

	public void insertSchReg(SchRegDto schReg)throws SQLException; 
}
