package kr.co.jhta.service;

import java.util.List;
import java.util.Map;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;

public interface InfoListService {

	List<CityVO> getFamousCityList(int count);
	List<CityVO> getAllCityList();

	Map<CategoryCodeVO, List<DestVO>> getThemeDestData();
	List<DestVO> searchDest(Integer cityNo, Integer categoryCode, String keyword);
}
