package kr.co.jhta.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.controller.MainPageController;
import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.Criteria;
import kr.co.jhta.domain.CriteriaReview;
import kr.co.jhta.domain.DestImgVO;

@Service
public class InfoCityServiceImpl implements InfoCityService {

	//DAO
	@Autowired
	private CityDAO cityDao;
	@Autowired
	private DestDAO destDao;
	@Autowired
	private PlanDAO planDao;
	@Autowired
	private UserDAO userDao;
	
	private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
	
	
	
	//특정 도시 사진들을 전달 
	@Override
	public List<CityImgVO> getTheCityImgsByCityNo(int cityNo) {
		//cityimgvo 다량입고
		List<CityImgVO> cityImg = cityDao.getCityImgByCityNo(cityNo);	 
		return cityImg;
	}
	
	//특정 도시의 상세정보를 전달 
	@Override
	public CityVO getCityInfoByCityNo(int cityNo) {
		
		CityVO city = cityDao.getCityByNo(cityNo);
		return city;
	}
	
	//상위 카테고리 모두 출력
	@Override
	public List<CategoryCodeVO> getUpperCategoryCode() {
		List<CategoryCodeVO> upperCategory = destDao.getUpperCategoryCode();
		return upperCategory;
	}
	
	//상위 카테고리 번호를 주면 하위 카테고리 뽑아오기
	@Override
	public List<CategoryCodeVO> getLowerCategoryCode(int theUpperCateCode) {
		List<CategoryCodeVO> lowerCateCode = destDao.getLowerCategoryCodeByUpperCode(theUpperCateCode);
		return lowerCateCode;
	}
	
	//특정 도시번호를 넣으면 해당 도시의 4개 여행지 출력
	@Override
	public List<Map<String, Object>> getFourDestData(Criteria c) {
		
		List<Map<String, Object>> data = new ArrayList<>();		

		//도시번호 넣어서 모든 여행지 상세정보 얻기
		List<HashMap> destDatas = destDao.getFourDestByCityNo(c);
			
		//도시상세정보 hashmap에 모든 여행지 filename 추가하기		
		for(HashMap theDestData : destDatas){
			HashMap<String, Object> map = new HashMap<>();
			//각 여행지의 번호 찾기

			map.put("destName",(String)theDestData.get("DESTNAME"));
			map.put("cnt", theDestData.get("CNT"));
			map.put("categoryName", theDestData.get("CATEGORYNAME"));
			int theDestNo = ((BigDecimal)theDestData.get("DESTNO")).intValue();
			map.put("destNo", theDestNo);
			//각 여행지의 번호로 사진들 찾기
			List<DestImgVO> destImgs = destDao.getDestImgsByDestNo(theDestNo);
			DestImgVO theDestImg = destImgs.get(0);
			String theDestfileName = theDestImg.getFileName();
			
			//여행지 사진들중 하나만 뽑아 사진파일을 hashMap에 담기			
			map.put("fileName", theDestfileName);
			//각각의 hashMap을 List<HashMap>에 담기
			data.add(map);			
		}		
		return data;
	}
	
	//도시의 여행지의 페이지네이션
	@Override
	public int getTotalRows(Criteria c) {		
		return destDao.countingDest(c);		 
	}
		
	//특정 도시번호를 넣으면 해당 도시의 리뷰 3개씩 출력
	@Override
	public List<HashMap<String, Object>> getReviewsByCityNo(CriteriaReview cReview) {
		
		List<HashMap> threeReviews = destDao.getThreeReviewsByCityNo(cReview);
		
		List<HashMap<String,Object>> data = new ArrayList<>();	
		//3개의 리뷰를 for문으로 풀어 
		for (HashMap theReview : threeReviews){
			
			HashMap<String, Object> container = new HashMap<>();
			
			container.put("regDate",(String)theReview.get("REGDATE"));
			container.put("userName",(String)theReview.get("USERNAME"));
			container.put("reviewData",(String)theReview.get("REVIEWDATA"));
			
			String grade = (String)theReview.get("RATING");
			if(grade.equals("G")){
				container.put("rating", "chart_good.png");	
			} else if(grade.equals("A")){
				container.put("rating", "chart_normal.png");	
			} else if(grade.equals("P")){
				container.put("rating", "chart_bad.png");	
			}
			data.add(container);
		}
		return data;
	}
	
	//도시의 리뷰의 페이지네이션
	@Override
	public int getTotalReviewRows(CriteriaReview cReview) {
		return destDao.countingReview(cReview);
	}
	
}