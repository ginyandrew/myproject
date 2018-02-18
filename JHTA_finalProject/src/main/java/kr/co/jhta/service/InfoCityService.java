package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.Criteria;
import kr.co.jhta.domain.CriteriaReview;

public interface InfoCityService {

	//특정 도시 사진파일번호들 출력
	List<CityImgVO>  getTheCityImgsByCityNo(int cityNo);
	
	//특정 도시 상세정보 출력
	CityVO getCityInfoByCityNo(int cityNo);
	
	//상위 카테고리 모두 출력
	List<CategoryCodeVO> getUpperCategoryCode();
	
	//상위 카테고리 번호를 주면 하위 카테고리 뽑아오기
	List<CategoryCodeVO> getLowerCategoryCode(int upperCateCode);
	
	//특정 도시번호, 카테고리 번호를 넣으면 해당 도시의 4개 여행지 출력
	List<Map<String, Object>> getFourDestData(Criteria c);
	
	//도시의 여행지의 페이지네이션
	int getTotalRows(Criteria c);
	int getTotalReviewRows(CriteriaReview cReview);
	
	//특정 도시번호를 넣으면 해당 도시의 리뷰 3개씩 출력
	List<HashMap<String, Object>> getReviewsByCityNo(CriteriaReview cReview);
}

