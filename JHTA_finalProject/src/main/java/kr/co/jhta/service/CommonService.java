package kr.co.jhta.service;

import java.util.List;

import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.UserVO;

public interface CommonService {

	UserVO findUser(String email, String password);
	
	void registerNewUser(UserVO user);
	
	List<MessageVO> getRecentMessage(int userNo, int count);
}
