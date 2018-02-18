package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;

@Service
public class InfoListServiceImpl implements InfoListService {

	@Autowired
	private PlanDAO planDao;
	@Autowired
	private DestDAO destDao;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private CityDAO cityDao;
	
	@Override
	public List<CityVO> getFamousCityList(int count) {
		
		Map<String, Integer> countMap = new HashMap<>();
		countMap.put("START", 1);
		countMap.put("END", count);
		List<CityVO> cityList = cityDao.getRankedCityList(countMap);
				
		// 값들 마저 설정하기
		for(CityVO city: cityList) {
			List<String> imgNameList = cityDao.getCityImgFileNameByNo(city.getNo());
			city.setImgNameList(imgNameList);
		}
		
		return cityList;
	}
	
	@Override
	public List<CityVO> getAllCityList() {
		List<CityVO> cityList = cityDao.getCityData();
		return cityList;
	}
	
	@Override
	public Map<CategoryCodeVO, List<DestVO>> getThemeDestData() {
		Map<CategoryCodeVO, List<DestVO>> themeDestMap = new TreeMap<>();
		List<CategoryCodeVO> categoryList = destDao.getCategoryCode();
		
		for(CategoryCodeVO category: categoryList) {
			int code = category.getCode();
			// dest list 설정
			List<DestVO> destList = destDao.getDestByCategory(code);
			destList = getSettedDestList(destList);
			themeDestMap.put(category, destList);
		}
		return themeDestMap;
	}
	
	@Override
	public List<DestVO> searchDest(Integer cityNo, Integer categoryCode, String keyword) {
		Map<String, Object> option = new HashMap<>();
		option.put("CITY_NO", cityNo);
		option.put("CODE", categoryCode);
		option.put("KEYWORD", keyword==null?"%%":String.format("%%%s%%", keyword));
		
		List<DestVO> destList = destDao.searchDest(option);
		destList = getSettedDestList(destList);
		
		return destList;
	}
	
	private List<DestVO> getSettedDestList(List<DestVO> destList) {
		for(DestVO dest: destList) {
			int destNo = dest.getNo();
			dest.setImgNameList(destDao.getDestImgFileNameByNo(destNo));
			dest.setCategoryList(destDao.getCategoryByDestNo(destNo));
		}
		return destList;
	}
}
