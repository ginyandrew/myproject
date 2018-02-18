package kr.co.jhta.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.StateCodeVO;

public interface CityDAO {

	//City 테이블
	void addCity(CityVO city);
	List<CityVO> getCity();
	CityVO getCityByNo(int no);
	void deleteCityByNo(int no);
	
	//City_Img 테이블
	void addCityImg(CityImgVO cityImg);
	List<CityImgVO> getCityImg();
	List<CityImgVO> getCityImgByCityNo(int cityNo);
	void deleteCityImgByCityNo(int cityNo);
	
	//StateCode 테이블
	void addStateCode(StateCodeVO code);
	List<StateCodeVO> getStateCodes();
	StateCodeVO getStateCodeByCode(int code);
	void deleteStateCodeByCode(int code);
	
	// 용규
	
	// 진영
	List<Map<String, Object>> getPlaceByName(String placeName);
	List<HashMap<String, Object>> getTopFiveCityNo();
	
	// 성인
	List<CityVO> getRankedCityList(Map<String, Integer> countMap);
	List<CityVO> getCityData();
	CityVO getCityDataByNo(int no);
	List<String> getCityImgFileNameByNo(int no); 
	void updateCity(CityVO city);
	
	// 주호
	
	// 지훈
	
	// 명신
	List<CityVO> getCityByStateCode(int code);
}
