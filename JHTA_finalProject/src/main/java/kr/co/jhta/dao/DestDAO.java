package kr.co.jhta.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.Criteria;
import kr.co.jhta.domain.CriteriaReview;
import kr.co.jhta.domain.DestCategoryVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.ReviewVO;
import kr.co.jhta.domain.UserVO;

public interface DestDAO {

	// Destination 여행지 테이블
	void addDest(DestVO city);
	List<DestVO> getDest();
	DestVO getDestByNo(int no);
	void deleteDestByNo(int no);
	
	
	// Destination_Img 여행지 사진 테이블
	void addDestImg(DestImgVO city);
	List<DestImgVO> getDestImg();
	List<DestImgVO> getDestImgByNo(int destNo);	
	void deleteDestImgByNo(int no);
	
	// Category Code 여행지 카테고리대분류 테이블
	void addCategoryCode(CategoryCodeVO city);
	List<CategoryCodeVO> getCategoryCode();
	CategoryCodeVO getCategoryCodeByCode(int code);
	void deleteCategoryCodeByCode(int code);
	
	// Destination_Category 여행지 카테고리 테이블
	void addDestCategory(DestCategoryVO city);
	List<DestCategoryVO> getDestCategory();
	DestCategoryVO getDestCategoryByDestNo(int destNo);
	DestCategoryVO getDestCategoryByCategory(int category);
	void deleteDestCategoryByCategory(int category);
	List<CategoryCodeVO> getCateByDestNo(int destNo);
	
	
	// Review 여행지 후기 테이블
	void addReview(ReviewVO vo);
	List<ReviewVO> getReview();
	ReviewVO getReviewbyNo(int no);
	void deleteReviewbyNo(int no);
	void deleteReviewByReviewNo(int reviewNo);
	// 주호
	void deleteReviewByUserNo(int userNo);
	List<ReviewVO> getReviewByDestNo(int destNo);	// 여행번호에 맞는 리뷰 찾아오기
	List<UserVO> getUserNoByDestNo(int destNo); 	// 여행지번호를 넣으면 여행지 리뷰에 남긴 유저 찾기
	UserVO getUserIdByUserNo(int userNo);			// 여행지 않의 유저 번호를 이용한 유저 아이디 찾아오기 
	String getUserNameByUserNo(int userNo);	
	List<DestImgVO> getDestImgByDestNo(int destNo);
	Integer getCountByReview(int destNo);
	Map<String, BigDecimal> getEmotionCountByDest(int destNo);
	Map<String, BigDecimal> getCountByThemeCode(int destNo);
	
	//views_dest~ 테이블에 중복댓글방지처리 및 댓글수 조회
	void addviews(List<HashMap<String, Integer>> views);
	int countReview(int dataNo);

	// 용규
	List<DestVO> getDestListByCityNo(int cityNo);
	List<ReviewVO> getReviewListbyNo(int destNo);
	DestVO getSampleDestByCityNo(int cityNo);
	List<DestCategoryVO> getDestCateByCategory(int category);
	
	
	
	// 진영
	List<PlanVO> getTopThreePlanNo();
	List<HashMap> getTopFourDestByCityNo(int cityNo);
	List<CategoryCodeVO> getUpperCategoryCode();
	List<CategoryCodeVO> getLowerCategoryCodeByUpperCode(int theUpperCateCode);
	List<DestImgVO> getDestImgsByDestNo(int destNo);
	List<HashMap> getFourDestByCityNo(Criteria c);
	int countingDest(Criteria c);
	List<HashMap> getThreeReviewsByCityNo(CriteriaReview cReview);
	int countingReview(CriteriaReview cReview);
	String getCateNameByDestNo(int destNo);
	
	// 성인
	List<CategoryCodeVO> getCategoryByDestNo(int destNo);
	List<String> getDestImgFileNameByNo(int no);
	List<DestVO> getDestByCategory(int code);
	List<DestVO> searchDest(Map<String, Object> option);
	List<DestVO> getDestData();
	void updateDest(DestVO dest);
	void addNewCategoryToDest(Map<String, Integer> categoryData);
	void deleteAllCategoryByDestNo(int destNo);
	void deleteAllImgFileNameByDestNo(int destNo);
	int getDestSequence();
	void addNewDest(DestVO dest);
	
	// 지훈
	
	// 명신
	
}
