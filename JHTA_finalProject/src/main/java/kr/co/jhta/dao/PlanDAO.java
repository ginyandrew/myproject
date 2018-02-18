package kr.co.jhta.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.ClipboardVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;


public interface PlanDAO {

	// Plan 일정 테이블
	void addPlan(PlanVO plan);
	List<PlanVO> getPlan();
	PlanVO getPlanByNo(int no);
	void deletePlanByNo(int no);
	
	// Plan Details 일정상세(하나의 일정에 속한 장소 하나) 테이블
	void addPlanDetail(PlanDetailVO plan);
	List<PlanDetailVO> getPlanDetail();
	PlanDetailVO getPlanDetailByNo(int no);
	void deletePlanDetailByNo(int no);

	//views_plan 테이블에 중복댓글방지처리 및 댓글수 조회
	void addviews(Map<String, Integer> views);
	int countReview(int dataNo);
	
	// 용규
	void makeNewPlan(PlanVO plan);
	void addNewPlanDetail(PlanDetailVO plandetail);
	void deletePlanDetailByDayNumbering(PlanDetailVO vo);//planNo, day, numbering이 필요
	void deletePlanDetailByDayPlanNo(PlanDetailVO vo);
	void updatePlanDetail(PlanDetailVO vo);
	void insertPlanDetail(PlanDetailVO vo);
	
	// 진영
	// 하나의 planNo에 해당하는 일정상세, 여행지정보 연결 JOIN 뽑기
	List<HashMap> getPlanDestCityInfoByPlanNo(int planNo);
	
	// 성인
	List<PlanDetailVO> getPlanDetailByPlanNo(int no);
	
	// Plan의 조회수를 가져온다.
	int getViewsByPlanNo(int no);
	
	void updatePlan(PlanVO plan);
	
	// Plan, PlanDetail을 사용하기 위한 시퀀스를 가져온다.
	int getPlanSequence();
	
	// 이미 조회수가 추가됐는지 확인하는 값을 가져온다
	String getAlreadyViewData(Map<String, Integer> views);
	
	// 조회수 정보를 삭제한다.
	void deletePlanViewsByPlanNo(int planNo);
	
	// 일정에 속한 일정 상세 정보들을 (일정 번호로 검색하여) 삭제한다. 
	void deletePlanDetailsByPlanNo(int planNo);
	
	// 주호
	List<PlanVO> getPlanDataByUserNo(int userNo);
	void updatePlanArriveDate(PlanVO vo);
	ClipboardVO getClipBoard(ClipboardVO vo);
	void addClipBoard(ClipboardVO vo);
	void updatePlanArriveDate(int userNo);
	
	// 지훈
	
	// 명신
	List<PlanVO> getPlanByCityNo(int cityNo);
	List<PlanVO> getAllPlanList();	// 모든플렌 뿌리기
}
