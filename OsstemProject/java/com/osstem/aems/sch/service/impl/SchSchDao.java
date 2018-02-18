package com.osstem.aems.sch.sch.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.osstem.aems.sch.sch.model.ApprSpkDto;
import com.osstem.aems.sch.sch.model.SchDtlDto;
import com.osstem.aems.sch.sch.model.SchMbrDto;
import com.osstem.aems.sch.sch.model.SchRegDto;
import com.osstem.aems.sch.sch.model.SchSchDto;
import com.osstem.aems.sch.sch.model.SendDto;
import com.osstem.aems.sch.sch.model.SendPsnDto;
import com.osstem.aems.stdInfo.psn.model.GrpDto;
import com.osstem.comm.code.model.CodeDto;

/*
 * @author Ginyoung Lee
 * @since 2016. 10. 19.
 * @version 1.0
 * @see
*/
@Repository("SchSchDao")
public class SchSchDao {
	
	@Resource(name="sqlSessionTemplate_aems")
	private SqlSessionTemplate sqlSession;
	
	/*
	 * Delete
	 */
	public void deleteSchSch(int schId)  throws SQLException {
		sqlSession.delete("deleteSchSch", schId);
	}
	
	public void deleteSchMbr(int schId)  throws SQLException {
		sqlSession.delete("deleteSchMbr", schId);
	}
	
	public void deleteRegPsn (SchRegDto dto) throws SQLException {
		sqlSession.delete("deleteRegPsn", dto);
	}
	
	public void deleteSchDtl(int schId)  throws SQLException {
		sqlSession.delete("deleteSchDtl", schId);
	}
	
	public void deleteGrpByGrpId(HashMap<String, Object> map) throws SQLException {
		sqlSession.delete("deleteGrpByGrpId", map);
	}

	public void deleteGrpPsnByGrpId(HashMap<String, Object> map) throws SQLException {
		sqlSession.delete("deleteGrpPsnByGrpId", map);
	}
	
	/*
	 * Update
	 */
	public void updateSchSchBySchId(SchSchDto dto)  throws SQLException {
		sqlSession.update("updateSchSchBySchId", dto);
	}
	
	public void updateSchReg(SchRegDto dto)  throws SQLException {
		sqlSession.update("updateSchReg", dto);
	}
	
	public void updateApprSpkRecmYn(ApprSpkDto dto) throws SQLException {
		sqlSession.update("updateApprSpkRecmYn", dto);
	}
	
	/*
	 * Insert
	 */
	public void insertSchSch(SchSchDto dto) throws SQLException {
		sqlSession.insert("insertSchSch", dto);
	}
	
	public void insertSchReg(SchRegDto dto) throws SQLException {
		sqlSession.insert("insertSchReg", dto);
	}
	
	public void insertSchDtl(SchDtlDto dto) throws SQLException {
		sqlSession.insert("insertSchDtl", dto);
	}
	
	public void insertSchMbr(SchMbrDto dto) throws SQLException {
		sqlSession.insert("insertSchMbr", dto);
	}
	
	public void insertSend(SendDto dto) throws SQLException {
		sqlSession.insert("insertSend", dto);
	}
	
	public void insertSendPsn(SendPsnDto dto) throws SQLException {
		sqlSession.insert("insertSendPsn", dto);
	}
	
	/*
	 * Select
	 */
	public int checkIfSendPsnExist(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("checkIfSendPsnExist", map);
	}
	
	public int selectSchMbrCntBySchId(int schId) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchMbrCntBySchId", schId);
	}
	
	public int selectPsnByLiceNo(int liceNo) throws SQLException {
		return (Integer) sqlSession.selectOne("selectPsnByLiceNo", liceNo);
	}
	
	public int selectRegCntByPsnSchId(HashMap<String, Object> map)throws SQLException {
		return (Integer) sqlSession.selectOne("selectRegCntByPsnSchId", map);
	}
	
	public List<Integer> selectSchNoOverlap() throws SQLException {
		return sqlSession.selectList("selectSchNoOverlap");
	}
	
	public List<Integer> selectPsnByGrpId(int grpId) throws SQLException {
		return sqlSession.selectList("selectPsnByGrpId", grpId);
	}
	
	public HashMap<String, Object> selectPsnAcaByPsnId(int psnId) throws SQLException {
		return sqlSession.selectOne("selectPsnAcaByPsnId", psnId);
	}
	
	public HashMap<String, Object> selectPsnHosByPsnId(int psnId) throws SQLException {
		return sqlSession.selectOne("selectPsnHosByPsnId", psnId);
	}
	
	public HashMap<String, Object> selectPsnByPsnId(int psnId) throws SQLException {
		return sqlSession.selectOne("selectPsnByPsnId", psnId);
	}
	
	public List<Integer> selectRepPsnBySendId(int sendId) throws SQLException {
		return sqlSession.selectList("selectRepPsnBySendId", sendId);
	}
	
	public SendDto selectSendBySendId(int sendId) throws SQLException {
		return sqlSession.selectOne("selectSendBySendId", sendId);
	}
	
	public int selectApprSchCntBySchId(int schId) throws SQLException {
		return (Integer) sqlSession.selectOne("selectApprSchCntBySchId", schId);
	}
	
	public int selectApprSpkCntBySchId(int schId) throws SQLException {
		return (Integer) sqlSession.selectOne("selectApprSpkCntBySchId", schId);
	}
	
	public int selectApprSpkCntBySchIdNo(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectApprSpkCntBySchIdNo", map);
	}
	
	public String selectSawonFromSendByPsnSchId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectOne("selectSawonFromSendByPsnSchId", map);
	}
	
	public String selectAcaNameByCode(String acaCd) throws SQLException {
		return sqlSession.selectOne("selectAcaNameByCode", acaCd);
	}
	
	public List<SchSchDto> selectSchByGbDt(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchByGbDt", map);
	}
	
	public List<SchMbrDto> selectSchMbr01BySchId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchMbr01BySchId", map);
	}
	
	public int selectSchCntByGbDt(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchCntByGbDt", map);
	}
	
	public int checkIfCanDeleteRegPsn(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("checkIfCanDeleteRegPsn", map);
	}
	
	/*public ApprSpkDto selectApprSch(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectOne("selectApprSch", map);
	}*/
	
	public List<HashMap<String, Object>> selectPsnByPsnIdforAdd(int psnId) throws SQLException {
		return sqlSession.selectList("selectPsnByPsnIdforAdd", psnId);
	}
	
	public List<String> selectPsnBySchId(int schId) throws SQLException {
		return sqlSession.selectList("selectPsnBySchId", schId);
	}
	
	public SchSchDto selectSchBySchId(int sch_id) throws SQLException {
		return sqlSession.selectOne("sch-sch.selectSchBySchId", sch_id);
	}
	
	public List<SchDtlDto> selectSchDtlBySchId(int sch_id) throws SQLException {
		return sqlSession.selectList("sselectSchDtlBySchId", sch_id);
	}
	
	public SchDtlDto selectSchDtl(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectOne("selectSchDtl", map);
	}
	
	public List<SchMbrDto> selectSchMbrBySchId(int sch_id) throws SQLException {
		return sqlSession.selectList("selectSchMbrBySchId", sch_id);
	}
	
	public List<HashMap<String, Object>> selectPsnHosInfo(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectPsnHosInfo", map);
	}
	
	public List<HashMap<String, Object>> selectNonApprSchTitle (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectNonApprSchTitle", map);
	}
	
	public List<HashMap<String, Object>> selectAllPsnHosInfoByName(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectAllPsnHosInfoByName", map);
	}
	
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameMbr(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectAllPsnHosInfoByNameMbr", map);
	}
	
	public List<HashMap<String, Object>> selectAllPsnHosInfoByNameNotInReg(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectAllPsnHosInfoByNameNotInReg", map);
	}
	
	public List<HashMap<String, Object>> selectSchTitle(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchTitle", map);
	}
	
	public List<HashMap<String, Object>> selectGrpByGrpName(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectGrpByGrpName", map);
	}
	
	public List<HashMap<String, Object>> autoSearchSchBySchId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("autoSearchSchBySchId", map);
	}
	
	public List<HashMap<String, Object>> selectSchSendInfoByGbDt(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchSendInfoByGbDt", map);
	}
	
	public int selectSchSendInfoCntByGbDt(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchSendInfoCntByGbDt", map);
	}
	
	public List<HashMap<String, Object>> selectPsnBySendId(int send_id) throws SQLException {
		return sqlSession.selectList("selectPsnBySendId", send_id);
	}
	
	public List<HashMap<String, Object>> selectSchInfoGroupBySchId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchInfoGroupBySchId", map);
	}
	
	public HashMap<String, Object> selectPsnBySchIdNoOverlap(int schId) throws SQLException {
		return sqlSession.selectOne("selectPsnBySchIdNoOverlap", schId);
	}
	
	public List<Integer> selectPsnListBySchId(int schId) throws SQLException {
		return sqlSession.selectList("selectPsnListBySchId", schId);
	}
	
	public HashMap<String, Object> selectSchRegBySchIdPsnId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectOne("selectSchRegBySchIdPsnId", map);
	}
	
	public List<HashMap<String, Object>> selectSchRegsBySchIdPsnId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectOne("selectSchRegsBySchIdPsnId", map);
	}
	
	public int selectSchRegCntBySchIdPsnId(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchRegCntBySchIdPsnId", map);
	}
	
	public List<Integer> selctPsnSendListBySchId(int schId) throws SQLException {
		return sqlSession.selectList("selctPsnSendListBySchId", schId);
	}
	
	public List<Integer> selectPsnFromRegBySchId(int schId) throws SQLException {
		return sqlSession.selectList("selectPsnFromRegBySchId", schId);
	}
	
	public List<HashMap<String, Object>> selectSchNoOverlapBySchId(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchNoOverlapBySchId", map);
	}
	
	public List<Integer> selectPsnIdNoOverlapFromReg(int schId) throws SQLException {
		return sqlSession.selectList("selectPsnIdNoOverlapFromReg", schId);
	}
	
	public List<Integer> selectPsnIdNoOverlapFromSend(int schId) throws SQLException {
		return sqlSession.selectList("selectPsnIdNoOverlapFromSend", schId);
	}
	
	 
	public List<HashMap<String, Object>> selectSch01ByMonth (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch01ByMonth", map);
	}
	
	public List<HashMap<String, Object>> selectSch02ByMonth (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch02ByMonth", map);
	}
	
	public List<HashMap<String, Object>> selectSch01ByWeek (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch01ByWeek", map);
	}
	
	public List<HashMap<String, Object>> selectSch02ByWeek (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch02ByWeek", map);
	}
	
	public int selectSchCnt(SchSchDto sch) throws SQLException {
		return(Integer) sqlSession.selectOne("selectSchCnt", sch);
	}
	
	public int selectSch01CntByYear (HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSch01CntByYear", map);
	}
	
	public List<HashMap<String, Object>> selectSch01ByYear (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch01ByYear", map);
	}
	
	public int selectSch02CntByYear (HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSch02CntByYear", map);
	}
	
	public List<HashMap<String, Object>> selectSch02ByYear (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSch02ByYear", map);
	}
	
	public List<HashMap<String, Object>> selectSchByYearPlace (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchByYearPlace", map);
	}
	
	public List<HashMap<String, Object>> selectSchPlaceNmByYear (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchPlaceNmByYear", map);
	}
	
	public List<HashMap<String, Object>> selectSchMbrByYear (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectSchMbrByYear", map);
	}
	
	public int selectSchMbrCntByYear (HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchMbrCntByYear", map);
	}
	
	public int selectSchMbrMaxCntByYear (HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchMbrMaxCntByYear", map);
	}
	
	public int selectSendSchCntBySchId(int schId) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSendSchCntBySchId", schId);
	}
	
	public int selectSchCntBySchIdNTime(int schId) throws SQLException {
		return (Integer) sqlSession.selectOne("selectSchCntBySchIdNTime", schId);
	}
	
	public List<Integer> selectPsnIdNoOverlapFromRegSend(HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectPsnIdNoOverlapFromRegSend", map);
	}
	
	/*public List<HashMap<String, Object>> selectApprCdsByApprId (int apprId) throws SQLException {
	return sqlSession.selectList("selectApprCdsByApprId", apprId);
	}*/
	
	/*public int selectApprSchCntByPsnId(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectApprSchCntByPsnId", map);
	}*/
	
	/*public int selectApprSchCnt (HashMap<String, Object> map) throws SQLException {
	return (Integer) sqlSession.selectOne("selectApprSchCnt", map);
	}*/
	
	/*public List<HashMap<String, Object>> selectApprSchList (HashMap<String, Object> map) throws SQLException {
		return sqlSession.selectList("selectApprSchList", map);
	}*/
	
	/*public String selectApprCdAvgBySchId(int schId) throws SQLException {
		return sqlSession.selectOne("selectApprCdAvgBySchId", schId);
	}*/
	
	/*public int selectApprSpkPsnCntBySchId(HashMap<String, Object> map) throws SQLException {
		return (Integer) sqlSession.selectOne("selectApprSpkPsnCntBySchId", map);
	}*/
	
	/*public void insertApprSpkRecmYn(ApprSpkDto dto) throws SQLException {
	sqlSession.insert("insertApprSpkRecmYn", dto);
	}*/


}
