package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;

import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.UserVO;

public interface MypageService {

	void updateUser(UserVO user);
	void dropUserByNo(int no);
	
	//목표 : 사용자 번호를 주면 >> 그 사용자가 가진 일정번호, 각 일정속에 있는 도시사진들과 장소이름들을 주는 서비스. 
	List<HashMap<String, Object>> getUserPlanList(int userNo);
	
	// 사용자의 클립보드 정보를 가져와 조합한다
	List<DestVO> getClipboardDataByUserNo(int userNo);
}
