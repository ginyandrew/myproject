package kr.co.jhta.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.ClipboardVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.UserVO;

public interface UserDAO {

	// User 회원 테이블
	void addUser(UserVO user);
	List<UserVO> getUser();
	UserVO getUserByNo(int no);
	void deleteUserByNo(int no);
	
	// Message //운영자가 사용자에게 보내는 메세지 테이블
	void addMessage(MessageVO user);
	List<MessageVO> getMessage();
	MessageVO getMessageByNo(int no);
	void deleteMessageByNo(int no);
	
	// Message //운영자가 사용자에게 보내는 메세지 테이블
	void addClipboard(ClipboardVO user);
	List<ClipboardVO> CountClipboardByDestno();
	ClipboardVO getClipboardByUserno(int userNo);
	void deleteClipboardByDestno(int userNo);
	
	// 용규
	
	// 진영
	int checkUserPointByMsg(Map<String, Object> userInfo);
	void addPointOnUser(int userNo);
	int getClipCntByUserNo(int userNo);
	int getPlanCntByUserNo(int userNo);
	int getReviewCntByUserNo(int userNo);
	
	// 성인
	// UserVO getUserByNo(int no);가 안쓰일수도 있음...
	UserVO getUserByEmail(String email);
	
	List<MessageVO> getRankedMessage(Map<String, Integer> map);
	
	List<UserVO> getOrderedUser(Map<String, String> map);
	
	// 회원을 탈퇴상태로 만든다.
	void dropUserByNo(int no);
	// 회원정보를 수정한다.
	void updateUser(UserVO user);	
	
	// 주호
	
	// 지훈
	List<Integer> getUserPlanNo (int userNo);
	List<HashMap>getPlaceDetailName (int planNo);
	List<DestVO> getMyClipboard(int userNo);
	
	// 명신
	
	
}
