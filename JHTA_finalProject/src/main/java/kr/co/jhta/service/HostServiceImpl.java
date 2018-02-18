package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;
import kr.co.jhta.domain.UserVO;

@Service
public class HostServiceImpl implements HostService {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private CityDAO cityDao;
	
	@Autowired
	private DestDAO destDao;
	
	@Autowired
	private PlanDAO planDao;
	
	@Autowired
	private PlanDetailService planDetailService;
	
	@Override
	public List<UserVO> getUser() {

		//Map<String, String> orderMap = new HashMap<>();
		//orderMap.put("ORDER_BY", orderBy.toUpperCase());
		//orderMap.put("ORDER_TYPE", orderType.toUpperCase());
		//return userDao.getOrderedUser(orderMap);
		return userDao.getUser();
	}
	
	@Override
	public List<CityVO> getCity() {
		List<CityVO> cityList = cityDao.getCityData();
		for(CityVO city: cityList) {
			int cityNo = city.getNo();
			
			city.setImgNameList(cityDao.getCityImgFileNameByNo(cityNo));			
		}
		
		return cityList;
	}

	@Override
	public List<StateCodeVO> getStateCode() {
		return cityDao.getStateCodes();
	}
	
	@Override
	public List<DestVO> getDest() {
		List<DestVO> destList = destDao.getDestData();
		
		for(DestVO dest: destList) {
			int destNo = dest.getNo();
			// 카테고리 설정
			dest.setCategoryList(destDao.getCategoryByDestNo(destNo));
			
			// 이미지 설정
			dest.setImgNameList(destDao.getDestImgFileNameByNo(destNo));		
			
		}
		return destList;
	}
	
	@Override
	public List<PlanVO> getPlan() {
		// Plan Detail의 여행지의 도시정보 설정하기
		// 도시 정보 중복 처리를 방지하기 위해 이미 가져온 정보를 DB에서 다시 가져오지 않는다.
		Map<Integer, CityVO> cityData = new HashMap<>();
		
		List<PlanVO> planList = planDao.getPlan();
		for(PlanVO plan: planList) {
			int planNo = plan.getNo();
			List<PlanDetailVO> details = planDao.getPlanDetailByPlanNo(planNo);
			
			for(PlanDetailVO detail: details) {
				// 여행지 정보(사진)을 설정한다
				DestVO dest = detail.getDest();
				dest.setImgNameList(destDao.getDestImgFileNameByNo(dest.getNo()));
				int cityNo = dest.getCity().getNo();
				if(cityData.containsKey(cityNo)) {
					dest.setCity(cityData.get(cityNo));
				} else {
					dest.setCity(cityDao.getCityDataByNo(dest.getCity().getNo()));
				}
				//int destNo = detail.getDest().getNo();
				//DestVO dest = destDao.getDestByNo(destNo);
				//int cityNo = dest.getCity().getNo();
				/*CityVO city = cityDao.getCityByNo(cityNo);
				dest.setCity(city);*/
				List<CategoryCodeVO> categoryList = destDao.getCategoryByDestNo(dest.getNo());
				dest.setCategoryList(categoryList);
				detail.setDest(dest);
			}
			plan.setDetails(details);
		}
		
		return planList;
	}
	
	@Override
	public void sendMsg(String[] noArr, String msg) {
		for(String strNo: noArr) {
			Integer no = Integer.parseInt(strNo);
			MessageVO message = new MessageVO(msg, no); 
			
			userDao.addMessage(message);
		}
	}
	
	@Override
	public void dropUser(String[] noArr) {
		for(String strNo: noArr) {
			Integer no = Integer.parseInt(strNo);
			userDao.dropUserByNo(no);
		}
	}
	
	@Override
	public void updateUser(UserVO user) {
		userDao.updateUser(user);
	}
	
	//@Override public void insertNewCity(CityVO city) {cityDao.addCity(city);}
	@Override
	public void updateCity(CityVO city) {
		cityDao.updateCity(city);
	}
	@Override
	public void addNewCityImage(int cityNo, String filename) {
		CityVO city = new CityVO();
		city.setNo(cityNo);
		CityImgVO cityImg = new CityImgVO(filename, city);
		cityDao.addCityImg(cityImg);
	}
	
	@Transactional
	@Override
	public int addDest(DestVO dest) {
		int destNo = destDao.getDestSequence();
		dest.setNo(destNo);
		
		destDao.addNewDest(dest);
		
		// 카테고리 정보 설정
		List<CategoryCodeVO> categoryList = dest.getCategoryList();
		
		for(CategoryCodeVO category: categoryList) {
			int code = category.getCode();
			
			Map<String, Integer> categoryData = new HashMap<>();
			categoryData.put("DEST_NO", destNo);
			categoryData.put("CATE_CODE", code);
			
			destDao.addNewCategoryToDest(categoryData);
		}
		
		return destNo;
	}
	@Override
	public void updateDest(DestVO dest) {
		destDao.updateDest(dest);

		// 카테고리 초기화를 위한 카테고리 삭제
		int destNo = dest.getNo();
		destDao.deleteAllCategoryByDestNo(destNo);
		
		// 카테고리 정보 설정
		List<CategoryCodeVO> categoryList = dest.getCategoryList();
		
		for(CategoryCodeVO category: categoryList) {
			int code = category.getCode();
			
			Map<String, Integer> categoryData = new HashMap<>();
			categoryData.put("DEST_NO", destNo);
			categoryData.put("CATE_CODE", code);
			
			destDao.addNewCategoryToDest(categoryData);
		}
	}
	
	@Override
	public void removeDest(String[] noArr) {
		for(String strNo: noArr) {
			Integer no = Integer.parseInt(strNo);
			
			// 이미지 삭제
			destDao.deleteAllImgFileNameByDestNo(no);
			
			// 카테고리 삭제
			destDao.deleteAllCategoryByDestNo(no);
						
			// 여행지 삭제
			destDao.deleteDestByNo(no);
		}
	}
	
	@Override
	public void addNewDestImage(int destNo, String filename) {
		DestVO dest = new DestVO();
		dest.setNo(destNo);
		DestImgVO destImg = new DestImgVO(filename, dest);
		destDao.addDestImg(destImg);
	}
	
	@Override
	public void removePlan(String[] noArr) {
		for(String strNo: noArr) {
			Integer no = Integer.parseInt(strNo);
			
			planDetailService.deletePlan(no);
		}		
	}
	
	@Override
	public List<CategoryCodeVO> getCategoryList() {
		List<CategoryCodeVO> categoryList = destDao.getCategoryCode();
		return categoryList;
	}
}
