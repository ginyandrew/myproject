package kr.co.jhta.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

//여행일정
	/*
	테마 코드
	1: 친구와 함께
	2: 커플 여행
	3: 단체 여행
	4: 나홀로 여행
	5: 비즈니스 여행
	6: 가족 여행
	 */
public class PlanVO implements TravelData{

	private int no;					//일정번호
	private String isCompleted;		//완료된 일정/수정중인 일정 여부
	private String leaveDate;		//여행출발일
	private int period;				//여행일정기간
	private String title;			//일정제목
	private int themeCode;			//일정테마
	private int copyCount;			//일정이 스크랩된 횟수
	private UserVO user;			//회원아이디 (FN)
	private List<PlanDetailVO> details; // 일정 상세 리스트
	
	public PlanVO(){}

	public PlanVO(int no, String isCompleted, String leaveDate, int period, String title, int themeCode, int copyCount, UserVO user) {
		super();
		this.no = no;
		this.isCompleted = isCompleted;
		this.leaveDate = leaveDate;
		this.period = period;
		this.title = title;
		this.themeCode = themeCode;
		this.copyCount = copyCount;
		this.user = user;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getThemeCode() {
		return themeCode;
	}

	public void setThemeCode(int themeCode) {
		this.themeCode = themeCode;
	}

	public int getCopyCount() {
		return copyCount;
	}

	public void setCopyCount(int copyCount) {
		this.copyCount = copyCount;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "PlanVO [no=" + no + ", isCompleted=" + isCompleted + ", leaveDate=" + leaveDate + ", period=" + period + ", title=" + title + ", themeCode=" + themeCode + ", copyCount=" + copyCount + ", user="
				+ user + "]";
	}

	// 성인

	public List<PlanDetailVO> getDetails() {
		return details;
	}

	public void setDetails(List<PlanDetailVO> details) {
		this.details = details;
	}
	
	/**
	 * 일정 상세를 날짜별로 분리한 Map을 얻는다.
	 * @return <Integer(Day), PlanDetailVO>로 구성된 Map 
	 */
	public Map<Integer, List<PlanDetailVO>> getPlanDetailMap() {
		Map<Integer, List<PlanDetailVO>> planDetailMap = new HashMap<>();
		if(details == null) {
			return null;
		}
		for(PlanDetailVO detail: details) {
			// 해당 vo의 day를 구함
			int day = detail.getDay();
			
			// 해당 day의 PlanDetailVO가 존재하는지 확인
			if(!planDetailMap.containsKey(day)) {
				// day가 없다면 생성
				planDetailMap.put(day, new ArrayList<PlanDetailVO>());
			}
			
			// 해당 Day의 리스트에 add하기
			List<PlanDetailVO> planDetailList = (List<PlanDetailVO>)planDetailMap.get(day);
			planDetailList.add(detail);
		}
		return planDetailMap;
	}
	
	// yyyyMMdd 형태의 문자열을 날짜로 변환하기 위한 sdf
	//private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	// yyyyMMddHHmmss 형태의 문자열을 날짜로 변환하기 위한 sdf
	private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	

	// 날짜를 Date 타입으로 얻는 메소드
	public Date getLeaveDateToDate() {
		return parseDate(leaveDate);
	}
	public Date getArriveDateToDate() {
		return parseDate(getArriveDate());
	}
	
	/**
	 * 도착일을 계산하는 메소드
	 * @return yyyyMMddHHmmss 타입의 도착 날짜 정보 (String)
	 */
	public String getArriveDate() {
		return yyyyMMddHHmmss.format(addDate(getLeaveDateToDate(), period-1));
	}
	
	/**
	 * yyyyMMddHH24miss형태의 문자열을 날짜로 변환함
	 * @param stringDate 문자열형태의 날짜
	 * @return Date 타입의 날짜
	 */
	private Date parseDate(String stringDate) {
		Date date = null;
		try {
			date = yyyyMMddHHmmss.parse(stringDate);
		} catch (ParseException | NullPointerException e) {
			System.out.println("날짜 변환 오류: " + stringDate);
			date = new Date(0);
		}
		
		return date;
	}
	
	/**
	 * 출발일로부터 일정 날짜가 지난 날짜를 문자열 형태로 구하는 메소드
	 * @param after 더할 날짜
	 * @return String 더해진 날짜
	 */
	public Date getDateInPeriodToDate(int after) {
		return addDate(getLeaveDateToDate(), after);
	}
	
	/**
	 * 문자열 형태의 날짜에 날짜만큼 더한(=지난) 날짜를 구하는 메소드
	 * @param stringDate 문자열 형태의 날짜
	 * @param after 더할 날짜
	 * @return Date 더해진 날짜
	 */
	private Date addDate(Date date, int after) {
		// 날짜 계산을 위한 Calendar 생성
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, after);
		
		return calendar.getTime();
	}
	
	/**
	 * Plan의 해당 날짜에 여행하는 도시 정보들을 얻는다
	 * @param day 해당 날짜
	 * @return 도시 정보의 리스트
	 */
	public List<CityVO> getCitiesOfPlanDetail(int day) {
		List<PlanDetailVO> details = getPlanDetailMap().get(day);
		if(details == null || details.isEmpty())
			return null;
		else{
			List<CityVO> cityList = new ArrayList<>();
			
			// details의 모든 도시를 확인하고 중복되지 않게 추가한다.
			for(PlanDetailVO detail: details) {
				CityVO city = detail.getDest().getCity();
				//TODO: 도시가 중복으로 들어간다면 여기서 해야됨
				if(!cityList.contains(city))
					cityList.add(city);
			}
			
			return cityList;
		}
	}
	
	/**
	 * 속한 PlanDetail들의 예산(budget)의 총합을 구한다.
	 * @return int budget의 총합
	 */
	public int getTotalBudget() {
		int total = 0;
		if(details == null) {
			return 0;
		}
		for(PlanDetailVO detail: details) {
			total += detail.getBudget();
		}
		return total;
	}
	
	/**
	 * 테마 코드에 해당하는 테마를 문자열 형태로 얻는다
	 * @return String 테마
	 */
	public String getThemeCodeToString() {
		switch(themeCode) {
			case 1:
				return "친구와 함께";
			case 2:
				return "커플 여행";
			case 3:
				return "단체 여행";
			case 4:
				return "나홀로 여행";
			case 5:
				return "비즈니스 여행";
			case 6:
				return "가족 여행";
			default:
				return null;
		}
	}
	
	/**
	 * 복사횟수 지정용 생성자
	 * @param no
	 * @param copyCount
	 */
	public PlanVO(int no, int copyCount) {
		 this(no, null, null, -1, null, -1, copyCount, null);
	}
}
