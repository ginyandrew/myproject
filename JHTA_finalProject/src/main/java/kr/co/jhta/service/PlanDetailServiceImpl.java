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
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.exception.DataNotFoundException;

@Service
public class PlanDetailServiceImpl implements PlanDetailService {

	@Autowired
	private PlanDAO planDao;
	@Autowired
	private DestDAO destDao;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private CityDAO cityDao;
	
	@Override
	public PlanVO getPlanDataByNo(int no) {
		PlanVO plan = planDao.getPlanByNo(no);
		
		// 에러 처리
		if(plan == null) {
			throw new DataNotFoundException("Plan 정보가 존재하지 않습니다.");
		}
		
		// Plan Detail 정보 가져오기
		List<PlanDetailVO> details = getPlanDetailDataByPlanNo(no);
				
		plan.setDetails(details);
		
		// Plan Comment 정보 가져오기
		
		
		return plan;
	}
	
	@Override
	public List<PlanDetailVO> getPlanDetailDataByPlanNo(int planNo) {
		// Plan Detail 정보 가져오기
		List<PlanDetailVO> details = planDao.getPlanDetailByPlanNo(planNo);
		
		// Plan Detail의 여행지의 도시정보 설정하기
		// 도시 정보 중복 처리를 방지하기 위해 이미 가져온 정보를 DB에서 다시 가져오지 않는다.
		Map<Integer, CityVO> cityData = new HashMap<>();
		for(PlanDetailVO detail: details) {
			// 여행지 정보(사진)을 설정한다
			DestVO dest = detail.getDest();
			dest.setImgNameList(destDao.getDestImgFileNameByNo(dest.getNo()));
			int cityNo = dest.getCity().getNo();
			if(cityData.containsKey(cityNo)) {
				dest.setCity(cityData.get(cityNo));
			} else {
				CityVO city = cityDao.getCityDataByNo(dest.getCity().getNo());
				city.setImgNameList(cityDao.getCityImgFileNameByNo(city.getNo()));
				dest.setCity(city);
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
		
		return details;
	}

	@Override
	public int getViewsByPlanNo(int planNo) {
		return planDao.getViewsByPlanNo(planNo);
	}
	
	@Override
	public void addViews(int planNo, int userNo) {
		Map<String, Integer> viewData = new HashMap<>();
		viewData.put("DATA_NO", planNo);
		viewData.put("USER_NO", userNo);
		
		// 이미 조회정보가 있는지 확인하고
		if(planDao.getAlreadyViewData(viewData)==null){
			// 없으면 조회정보를 추가
			planDao.addviews(viewData);
		}
	};
	
	@Transactional
	@Override
	public void copyPlan(PlanVO newPlan, int planNo) {
		// 기존 Plan 정보를 가져옴
		PlanVO sourcePlan = planDao.getPlanByNo(planNo);

		PlanVO forCopyCounting = new PlanVO(planNo, sourcePlan.getCopyCount()+1);
		
		// 가져오지 못한 값을 채워넣는다
		newPlan.setIsCompleted("T");
		newPlan.setPeriod(sourcePlan.getPeriod());
		List<PlanDetailVO> details = planDao.getPlanDetailByPlanNo(planNo);
		
		int newPlanNo = planDao.getPlanSequence();
		newPlan.setNo(newPlanNo);
		
		// 일정 복사하기
		planDao.addPlan(newPlan);
		
		// 일정 상세 복사하기
		
		for(PlanDetailVO detail: details) {
			// 일정 상세마다 새로운 일정의 번호 부여
			int newPlanDetailNo = planDao.getPlanSequence();
			detail.setNo(newPlanDetailNo);
			detail.setPlanNo(newPlanNo);
			planDao.addPlanDetail(detail);
		}
		
		// 기존 Plan 정보의 복사 횟수를 하나 추가
		planDao.updatePlan(forCopyCounting);
	}
	
	@Transactional
	@Override
	public void deletePlan(int planNo) {
		//int planNo = plan.getNo();
		
		// 조회수 정보 삭제하기
		planDao.deletePlanViewsByPlanNo(planNo);
		
		// PlanComment 정보 삭제하기
		
		
		// PlanDetail 정보 삭제하기
		/*List<PlanDetailVO> details = plan.getDetails();
		for(PlanDetailVO detail: details) {
			int planDetailNo = detail.getNo();
			planDao.deletePlanDetailByNo(planDetailNo);
		}*/
		planDao.deletePlanDetailsByPlanNo(planNo);
		
		// Plan 정보 삭제하기
		planDao.deletePlanByNo(planNo);
	}
}