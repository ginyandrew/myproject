package kr.co.jhta.service;

import java.util.List;

import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;

public interface PlanDetailService {

	PlanVO getPlanDataByNo(int no);
	//List<PlanDetailVO> getPlanDetailDataByPlanNo(int no);
	List<PlanDetailVO> getPlanDetailDataByPlanNo(int planNo);
	int getViewsByPlanNo(int planNo);
	
	// 조회수 추가하기
	void addViews(int planNo, int userNo);
	
	// 일정 복사하기
	void copyPlan(PlanVO newPlan, int planNo);
	
	// 일정 삭제하기
	void deletePlan(int planNo);
}
