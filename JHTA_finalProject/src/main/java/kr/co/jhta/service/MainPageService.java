package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.PlanVO;

public interface MainPageService {
	
	
	//도시,장소 자동완성검색기능
	List<Map<String, Object>> searchPlacesByName(String placeName);
	
	//인기 상위3위 일정 관련 정보 출력 	
	List<Map<String, Object>> GetTopThreePlan();
	
	//인기 상위1~4위 도시 관련 정보 출력
	List<HashMap<String, Object>> getTopFiveCity();
	
	//달력 포인트적립,메세지저장, or 경고창
	int checkPoint(String selectedDate, int userNo);
	
	//특정 회원에게 메세지 보내고 포인트 적립하기	
	void addPointMsgByCalendar(String selectedDate, int userNo);

	//특정 회원의 클립보드, 리뷰수,일정수 반환
	Map<String, Object> getUserRecord(int userNo);
}
