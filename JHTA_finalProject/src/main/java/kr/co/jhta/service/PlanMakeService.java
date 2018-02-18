package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestCategoryVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;

@Service
public interface PlanMakeService {

	public List<DestImgVO> getDestList();
	public List<DestImgVO> getDestListByCityNo(int cityNo);
	
	public List<StateCodeVO> getAllStateCode();
	public List<CityVO> getAllCityLatLng();
	public List<CityVO> selCityByStateCode(int statecode); 
	
	public HashMap<String, Object> selDetailDestByNo(int destno);
	
	
	public List<CityImgVO> cityData();
	
	public List<PlanDetailVO> getPlanDetailbyPlanNo(int planno);
	
	
	public void makeNewPlan(PlanVO plan);
	
	public List<DestImgVO> getCitylistByUppercatecode(int catecode);
	
	public PlanVO getPlan(int no);
	public void updatePlan(PlanVO vo);
	
	public void removePlanDetailByDay(PlanDetailVO vo);
	
	public void insertPlanDetail(PlanDetailVO vo);
	public void updatePlanDetail(PlanDetailVO vo);
	public void removePlanDetail(PlanDetailVO vo);
}
