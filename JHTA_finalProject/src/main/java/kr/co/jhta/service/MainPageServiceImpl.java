package kr.co.jhta.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.jhta.controller.MainPageController;
import kr.co.jhta.dao.CityDAO;
import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CityImgVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.UserVO;

@Service
public class MainPageServiceImpl implements MainPageService{
	
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
	
	
	//도시,장소 자동완성검색기능
	@Override
	public List<Map<String, Object>> searchPlacesByName(String placeName) {		
		List<Map<String, Object>> places = cityDao.getPlaceByName(placeName);			 
		return places;
	}
	
	
	//인기 순위 상위3개의 plan 이 포함하는 정보를 찾는 service 
	@Override
	public List<Map<String, Object>> GetTopThreePlan() {
		 		
		List<Map<String, Object>> data = new ArrayList<>();
		
		//상위3개 planNo 뽑기
		List<PlanVO> topPlans = destDao.getTopThreePlanNo();
		for(PlanVO plans : topPlans){
	
			HashMap<String, Object> map = new HashMap<>();
			HashSet<String> destNameSet = new HashSet<>();
			HashSet<String> cityNameSet = new HashSet<>();
			HashSet<String> themeSet = new HashSet<>();
			
			String themeName = null;
			String imgFileName = null;
			
			//상위일정3개에서 일정 하나당 정보- (공통)사용자이름,여행일,기간,여행테마, (다중) 여행지 이름들, 도시이름들
			List<HashMap> bestPlans = planDao.getPlanDestCityInfoByPlanNo(plans.getNo());
		
				// map 하나에 plan 하나의 정보 담기
				for (HashMap plan :bestPlans){
					destNameSet.add((String) plan.get("DESTNAME"));
					cityNameSet.add((String) plan.get("CITYNAME"));
					map.put("leaveDate",plan.get("LEAVEDATE"));
					map.put("period",plan.get("PERIOD"));
					map.put("userName", plan.get("USERNAME"));
					map.put("planNo", plan.get("PLANNO"));
					
					int cityNo = ((BigDecimal)plan.get("CITYNO")).intValue();
					CityVO city = cityDao.getCityByNo(cityNo);
					List<CityImgVO> cityImg = cityDao.getCityImgByCityNo(city.getNo());
					CityImgVO theCityImg= cityImg.get(0);
					imgFileName = theCityImg.getFileName();
					
					int themeCode = ((BigDecimal)plan.get("THEMECODE")).intValue();
						if(themeCode == 1){
							themeName = "친구와 함께";
						} else if (themeCode == 2){
							themeName = "커플 여행";
						} else if (themeCode == 3){
							themeName = "단체 여행";
						} else if (themeCode == 4){
							themeName = "나홀로 여행";
						} else if (themeCode == 5){
							themeName= "비지니스 여행";
						} else if (themeCode == 6){
							themeName = "가족 여행";
						}					
				}
				map.put("ImgfileName", imgFileName);
				map.put("cityName", StringUtils.collectionToCommaDelimitedString(cityNameSet));
				map.put("destName", StringUtils.collectionToCommaDelimitedString(destNameSet));							
				map.put("themeCode", themeName);
							
				data.add(map);
		}
		return data;	
	}

	
	//인기 순위 상위1~5위 도시 no,name,cityImg 정보를 찾는다
	@Override
	public List<HashMap<String, Object>> getTopFiveCity() {
		
		//5개 도시정보가 5줄로 나온다. (1줄당 hashmap 하나)
		List<HashMap<String, Object>> fiveCity = cityDao.getTopFiveCityNo();
		return fiveCity;
	}
	
	//멤버가 이미 같은 날짜에 포인트를 적립했는지 체크한다
	@Override
	public int checkPoint(String selectedDate, int userNo) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("thedate", selectedDate);
		userInfo.put("userno", userNo);
		int whetherUserHasPoint = userDao.checkUserPointByMsg(userInfo);	
		//같은 날짜에 적립했는지,확인후 >> 아니면 이게 오늘 날짜인지 아닌지 확인한다. 오늘 날짜면 0 아니면 1을 보낸다.
		if(whetherUserHasPoint > 0){
			return whetherUserHasPoint;
		} else {
			Date date = Calendar.getInstance().getTime();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    String today = (String)sdf.format(date);
		    if (today.equals(selectedDate)){
		    	return 0;
		    } else {
		    	return 1;
		    }			 
		}		
	}
	
	//특정 회원에게 메세지 보내고 포인트 적립하기	
	@Override
	public void addPointMsgByCalendar(String selectedDate, int userNo) {
		
		MessageVO message = new MessageVO();			
		UserVO user = new UserVO();
		
		user.setNo(userNo);
		message.setUser(user);		
		String msgData = selectedDate + "일에 100 포인트가 적립되었습니다.";
		message.setData(msgData);
		String sentDate = selectedDate.replaceAll("-", "") + "000000";
		message.setSendTime(sentDate);
		
		userDao.addPointOnUser(userNo);
		
		userDao.addMessage(message);		
	}
	
	//특정 회원의 클립보드, 리뷰수,일정수 반환
	@Override
	public Map<String, Object> getUserRecord(int userNo) {
		
		System.out.println("유저넘버는 뭐지?" + userNo);
		
		int planCnt = userDao.getPlanCntByUserNo(userNo);
		int clipCnt = userDao.getClipCntByUserNo(userNo);
		int reviewCnt = userDao.getReviewCntByUserNo(userNo);
		UserVO user = userDao.getUserByNo(userNo);
		String userName = user.getName();
		int userPoint = user.getPoint();
		
		Map<String, Object> map = new HashMap<>();
		map.put("planCnt", planCnt);
		map.put("clipCnt", clipCnt);
		map.put("reviewCnt", reviewCnt);
		map.put("userName", userName);
		map.put("userPoint", userPoint);
		
		return map;
	}
	
}