package kr.co.jhta.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestCategoryVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.ReviewVO;
import kr.co.jhta.domain.StateCodeVO;

@Service
public class PlanMakeServiceImpl implements PlanMakeService {

	@Autowired
	CityDAO cityDao;
	@Autowired
	DestDAO destDao;
	@Autowired
	PlanDAO planDao;

	@Override
	public List<DestImgVO> getDestList() {
		ArrayList<DestImgVO> destImgList = new ArrayList<>();
		
		List<DestVO> destlist = destDao.getDest();
		for(DestVO vo:destlist) {
			
				DestImgVO imgVo = destDao.getDestImgByNo(vo.getNo()).get(0);
				imgVo.setDest(vo);
				destImgList.add(imgVo);
			
		}
		return destImgList;
	}
	
	@Override
	public List<DestImgVO> getDestListByCityNo(int cityNo) {
		ArrayList<DestImgVO> destImgList = new ArrayList<>();
		List<DestVO> destlist = destDao.getDestListByCityNo(cityNo);
		for(DestVO vo:destlist) {
			DestImgVO imgVo = destDao.getDestImgByNo(vo.getNo()).get(0);
			imgVo.setDest(vo);
			destImgList.add(imgVo);
		}
		return destImgList;
	}
	
	@Override
	public List<StateCodeVO> getAllStateCode() {
			return cityDao.getStateCodes();
	}
	
	@Override
	public List<CityVO> getAllCityLatLng() {
		return cityDao.getCity();
	}
	
	@Override
	public List<CityVO> selCityByStateCode(int statecode) {
		StateCodeVO stateCodeVo = cityDao.getStateCodeByCode(statecode);
		List<CityVO> citylist = cityDao.getCityByStateCode(statecode);
		
		for(CityVO vo: citylist) {
			vo.setStateCode(stateCodeVo);
		}
		
		return citylist;
	}
	
	@Override
	public HashMap<String, Object> selDetailDestByNo(int destno) {
		HashMap<String, Object> detailDest = new HashMap<>();
		DestVO destVo = destDao.getDestByNo(destno);
		DestImgVO imgVo = destDao.getDestImgByNo(destno).get(0);
		List<ReviewVO> reviewVo = destDao.getReviewListbyNo(destno);
		
		//DestCategoryVO destCateVo = destDao.getDestCategoryByDestNo(destno); // 카테고리 코드 이름 확인을 위해 연결고리로 사용
		//CategoryCodeVO cateCodeVo = destDao.getCategoryCodeByCode(destCateVo.getCateCode().getCode());
		
		detailDest.put("destVo", destVo);
		detailDest.put("imgVo", imgVo);
		detailDest.put("reviewList", reviewVo);
		//detailDest.put("cateCode", cateCodeVo);
		return detailDest;
	}
	
	@Override
	public List<CityImgVO> cityData() {
		ArrayList<CityImgVO> listImg = new ArrayList<>();		
	    List<CityImgVO> imgList = cityDao.getCityImg();
				
		for(CityImgVO ciVo:imgList) {
			
			CityVO cVo = cityDao.getCityByNo(ciVo.getCity().getNo());
			
			StateCodeVO scVo = cityDao.getStateCodeByCode(cVo.getStateCode().getCode());
			cVo.setStateCode(scVo);
			ciVo.setCity(cVo);
			
			if(!(ciVo.getFileName().contains("main"))){
				listImg.add(ciVo);
			}
		}
		
		return listImg;
	}
	
	@Override
	public List<PlanDetailVO> getPlanDetailbyPlanNo(int planno) {
		
		return planDao.getPlanDetailByPlanNo(planno);
	}
	
	
	@Override
	public void makeNewPlan(PlanVO plan) {
		// 플랜번호 생성
		int planNo = planDao.getPlanSequence();
		plan.setNo(planNo);
	
		// 플랜 정보 저장
		planDao.makeNewPlan(plan);
	
		// 플랜 상세정보 설정
		List<PlanDetailVO> details = plan.getDetails(); 
		for(PlanDetailVO detail: details) {
		// 플랜번호 넣고 더하기
		detail.setPlanNo(planNo);
		
		int planDetailNo = planDao.getPlanSequence();
		detail.setNo(planDetailNo);
		// 도시번호로 임시 여행지 정보 구하기
		int cityNo = detail.getDest().getCity().getNo();
		DestVO dest = destDao.getSampleDestByCityNo(cityNo);
		detail.setDest(dest);
	
		planDao.addPlanDetail(detail);
		}	

	};
	
	@Override
	public List<DestImgVO> getCitylistByUppercatecode(int catecode) {
		// 상위카테고리코드로 하위카테고리코드가져오기
		List<CategoryCodeVO> cclist = destDao.getLowerCategoryCodeByUpperCode(catecode);
		
		ArrayList<DestImgVO> divolist = new ArrayList<>();
		for(CategoryCodeVO ccvo : cclist) {
			if(ccvo==null) {
				continue;
			}
			List<DestCategoryVO> dclist = destDao.getDestCateByCategory(ccvo.getCode());
					
			for(DestCategoryVO dcvo : dclist) {
				if(dcvo==null) {
					continue;
				}
				DestVO dvo = destDao.getDestByNo(dcvo.getDest().getNo());
				CityVO cvo = cityDao.getCityByNo(dvo.getCity().getNo());
				dvo.setCity(cvo);
				dcvo.setDest(dvo);
				
				

				List<DestImgVO> divos = destDao.getDestImgByDestNo(dvo.getNo());
				if(divos == null || divos.size() == 0) {
					continue;
				}
				DestImgVO divo = divos.get(0);
				divo.setDest(dvo);
				divolist.add(divo);
			}
		
			
			
		}
		return divolist;
	}
	
	@Override
	public PlanVO getPlan(int no) {
		return planDao.getPlanByNo(no);
	}
	
	@Override
	public void updatePlan(PlanVO plan) {
						
		planDao.updatePlan(plan);
		
	}
	
	@Override
	public void insertPlanDetail(PlanDetailVO vo) {
		
		planDao.insertPlanDetail(vo);
	
	}
	
	@Override
	public void updatePlanDetail(PlanDetailVO vo) {
		planDao.updatePlanDetail(vo);
		
	}
	
	@Override
	public void removePlanDetailByDay(PlanDetailVO vo) {
		//planDao.deletePlanViewsByPlanNo(vo.getPlanNo());
		planDao.deletePlanDetailByDayPlanNo(vo);
	}
	
	@Override
	public void removePlanDetail(PlanDetailVO vo) {
		planDao.deletePlanDetailByDayNumbering(vo);
	}
	
	
}
