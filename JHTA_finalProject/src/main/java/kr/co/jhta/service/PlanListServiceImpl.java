package kr.co.jhta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;

@Service
public class PlanListServiceImpl implements PlanListService {

	@Autowired
	CityDAO cityDao;
	@Autowired
	PlanDAO planDao;
	@Autowired
	DestDAO destDao;
	@Autowired
	UserDAO userDao;
	
	@Autowired
	PlanDetailService planDetailService;
	
	@Override
	public List<CityVO> getCityByStateCode(int code) {
		return cityDao.getCityByStateCode(code);
	}

	@Override
	public List<StateCodeVO> getStates() {
		return cityDao.getStateCodes();
	}
	
	@Override
	public List<PlanVO> getPlanDataByCityNo(int cityNo) {

		// 도시 번호로 일정을 찾는 기능 수행
		List<PlanVO> planList = planDao.getPlanByCityNo(cityNo);
		
		// 찾은 일정에 모자란 정보 채우기
		for(PlanVO plan: planList) {
			int planNo = plan.getNo();
			// 일정 상세 정보를 찾는다
			List<PlanDetailVO> details = planDao.getPlanDetailByPlanNo(planNo);
			
			// 일정 상세 정보를 채운다
			for(PlanDetailVO detail: details) {
				int detailDestNo = detail.getDest().getNo();
				DestVO dest = destDao.getDestByNo(detailDestNo);
				int detailCityNo = dest.getCity().getNo();
				CityVO city = cityDao.getCityByNo(detailCityNo);
				List<CategoryCodeVO> categoryList = destDao.getCategoryByDestNo(detailDestNo);
				StateCodeVO state = cityDao.getStateCodeByCode(city.getStateCode().getCode());
				city.setStateCode(state);
				dest.setCity(city);
				dest.setCategoryList(categoryList);
				detail.setDest(dest);
				detail.setPlanNo(planNo);
			}
			
			// 사용자 정보 설정
			int userNo = plan.getUser().getNo();
			plan.setUser(userDao.getUserByNo(userNo));
			
			// 일정 상세 정보를 넣는다
			plan.setDetails(details);			
		}		
		
		return planList;
	}	
	
	// 유저 번호에 맞는 플랜 가져오기 - 주호 -
	@Override
	public List<PlanVO> getPlanDataByUserNo(int userNo) {
		
		return planDao.getPlanDataByUserNo(userNo);
	}
	
	// 신이가 쓰는 이미지 파일과 도시 번호(?) 한장 가져옵니다
	@Override
	public CityImgVO getImageForPlanList(int cityNo) {
		List<CityImgVO> imgList = cityDao.getCityImgByCityNo(cityNo);
		CityImgVO img = imgList.size()==0?null:imgList.get(0);
		
		return img;
	}
	
	
	// 2016 02-12 GelAllPlanList
	@Override
	public List<PlanVO> getAllPlans() {
		// 플랜의 기본 정보 가져오기
		List<PlanVO> planList = planDao.getAllPlanList();
		
		for(PlanVO plan: planList) {
			int planNo = plan.getNo();
			
			// 플랜의 상세정보 설정하기
			plan.setDetails(planDetailService.getPlanDetailDataByPlanNo(planNo));
		}
		
		return planList;
	}
}
