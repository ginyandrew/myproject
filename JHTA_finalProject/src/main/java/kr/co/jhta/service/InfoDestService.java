package kr.co.jhta.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.ClipboardVO;
import kr.co.jhta.domain.DestCategoryVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.ReviewVO;
import kr.co.jhta.domain.UserVO;

public interface InfoDestService {
	// 여행지 
	void addNewDest(DestVO vo);		// 새로운 여행지 추가하기
	List<DestVO> getAllDest();		// 모든 여행지 가져오기
	DestVO searchDestByNo(int no);	// 여행지 번호에 맞는 여행지 검색하기
	void deleteDestByNo(int no);	// 여행지 번호에 맞는 여행지 삭제하기
	
	// Destination_Img 여행지 사진 테이블
	void addNewDestImg(DestImgVO city);	// 새로운 여행지 이미지 추가
	List<DestImgVO> getAllDestImg();	// 모든 여행지 가져오기
	List<DestImgVO> searchDestImgByNo(int no);	// 여행지 번호에 맞는 이미지 가져오기
	void deleteDestImgByNo(int no);		// 여행지 번호에 맞는 이미지 삭제하기
	DestImgVO searchDestImgByDestNo(int destNo); // 여행지 번호에 맞는 이미지를 불러온다.
		
	// Category Code 여행지 카테고리대분류 테이블
	
	List<CategoryCodeVO> getAllCategoryCode();			// 카테고리 목록 불러오기
	CategoryCodeVO searchCategoryCodeByCode(int code);	// 코드에 맞는 카테고리 가져오기 
	void deleteCategoryCodeByCode(int code);			// 코드에 맞는 카테고리 지우기
	List<CategoryCodeVO> getDestCategoryByDestNo(int destNo);	// 
	String searchCategoryNameByDestNo(int destNo);
	Map<String, BigDecimal> searchCountByThemeCode(int destNo);
	void updatePlanArriveDate(PlanVO vo);
	
	void addNewReview(ReviewVO review);				// 새로운 리뷰를 추가
	List<ReviewVO> searchReview();					// 모든 리뷰 가져오기
	ReviewVO serchReviewbyNo(int no);				// 리뷰 번호에 맞는 리뷰 가져오기
	void deleteReviewbyNo(int no);					// 번호에 맞는 리뷰 지우기
	void removeReviewByUserNo(int userNo); 			// 유저 번호를 입력 받아 그 유저가 쓴 번호의 리뷰를 지우기
	List<ReviewVO> searchReviewByDestNo(int destNo);// 여행지 번호에 맞게 리뷰를 출력
	Map<String, BigDecimal> searchEmotionCountByDest(int destNo);
	List<CategoryCodeVO> getCateByDestNo(int destNo);
	
	List<UserVO> searchUserNoByDestNo(int destNo);	// 여행지번호에 관한 유저 번호 찾기.
	List<UserVO> searchUserIdByUserNo(int no);			// 유저 아이디 뽑아오기.
	String searchUserNameByUserNo(int userNo);		// 유저번호를 받으면 유저 name이 나온다.
	Integer getCountByReview(int destNo);			// 여행지에 관한 리뷰 갯수 가져오기
	boolean hasClipBoard(int userNo, int destNo);
	void chingePlanArriveDate(int userNo);
	void addClipBoard(int userNo, int destNo);
	
	
}
