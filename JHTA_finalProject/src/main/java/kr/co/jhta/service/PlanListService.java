package kr.co.jhta.service;

import java.util.List;

import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;

public interface PlanListService {
	
	// 도시 번호 가져오기 입니다
	List<CityVO> getCityByStateCode(int code);
	
	// 모든 State들을 가져옵니다
	List<StateCodeVO> getStates();
	
	// 도시 번호로 일정 리스트를 가져옵니다
	List<PlanVO> getPlanDataByCityNo(int cityNo);
	
	//주호
	List<PlanVO> getPlanDataByUserNo(int userNo);
	
	// 신이
	CityImgVO getImageForPlanList(int cityNo);
	// 플렌리스트 를 가져옵니다. [2016 02 12]
	List<PlanVO> getAllPlans();
}
