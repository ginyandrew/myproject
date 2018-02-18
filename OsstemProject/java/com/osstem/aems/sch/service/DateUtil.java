/**
 * 
 */
package com.osstem.aems.sch.sch.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;



/**
 * 
 * 날짜 처리 유틸
 * 
 * @author 김민지
 *
 */
public class DateUtil {
	/**
	 * String 형태의 날짜값을 Calendar(날짜만)
	 * @param ymd 연월일
	 * @return
	 * @author 석민호
	 */
	public static Calendar calStringToCal(String ymd){
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(ymd.substring(0,4));
		int month = Integer.parseInt(ymd.substring(4, 6));
		int date = Integer.parseInt(ymd.substring(6, 8));
		cal.set(year, month-1, date);
		return cal;
	}

	/**
	 * String 형태의 날짜값을 Date(날짜만)
	 * @param ymd 연월일
	 * @return
	 * @author 석민호
	 */
	public static Date dateStringToDate(String ymd){
		return calStringToCal(ymd).getTime();
	}
	
	/**
	 * 일자만큼 더한 날짜
	 * @param ymd 연월일
	 * @param amount 일자
	 * @return
	 * @author 석민호
	 */
	public static String getYmdaddDay(String ymd,int amount){
		Date dt = DateUtils.addDays(dateStringToDate(ymd),amount);
		return DateFormatUtils.format(dt,"yyyy-MM-dd").replaceAll("-", "");
	}
	
	/**
	 * 개월만큼 더한 날짜
	 * @param ymd 연월일
	 * @param amount 월
	 * @return
	 * @author 석민호/김민지
	 */
	public static String getYmdaddMonth(String ymd,int amount){
		return DateFormatUtils.format(DateUtils.addMonths(dateStringToDate(ymd),amount),"yyyy-MM-dd").replaceAll("-", "");
	}
	

	/**
	 * 선택날짜 월의 리스트를 가지고 옵니다.
	 * @param cur_date 현재일자(연월일)
	 * @return 일자(8자리)
	 * @author 김민지
	 * @throws Exception
	 */
	public static ArrayList<Hashtable<String, String>> getNowMonthDay(int int_select_year,int int_select_month, int int_select_day) throws Exception {
		Calendar day = Calendar.getInstance();			
		day.set(int_select_year,int_select_month-1,int_select_day);
        ArrayList<Hashtable<String, String>> arr_temp = new ArrayList<Hashtable<String, String>>();
        for (int int_i=1; int_i<=day.getActualMaximum(Calendar.DATE); int_i++) {
            Hashtable<String, String> hash_temp = new Hashtable<String, String>();
            day.set(Calendar.DATE, int_i);                        
            hash_temp.clear();
            hash_temp.put("YEAR",Integer.toString(day.get(Calendar.YEAR)));
            hash_temp.put("MONTH",Integer.toString(day.get(Calendar.MONTH)+1));
            hash_temp.put("DATE",Integer.toString(day.get(Calendar.DATE)));
            hash_temp.put("WEEK",Integer.toString(day.get(Calendar.DAY_OF_WEEK)));
            arr_temp.add(hash_temp);	            
        }        
        return arr_temp;
    }
	
	/**
	 * 월간 활동정보 달력에서 앞부분의 빈날짜
	 * @param month_day_list 일자 리스트
	 * @return String 빈날짜 html
	 * @throws Exception -> 김민지
	 */
	public static String strFrontBlank(ArrayList<Hashtable<String, String>> month_day_list) throws Exception{
		StringBuffer sb = new StringBuffer();
		Hashtable<String, String> ht = (Hashtable<String, String>)month_day_list.get(0);
		String week = ObjectUtils.toString(ht.get("WEEK"));
		int day_blank = 0;
		if(week.equals("1")){
			day_blank = 0;
		}else{
			day_blank = Integer.parseInt(week)-1;
		}
		for(int j=0;j<day_blank;j++){
			sb.append("\n <td>");
			sb.append("\n   <div class=\"cld_date\"></div>");
			sb.append("\n </td>");
		}
		return sb.toString();
	}
	
	/**
	 * 월간 활동정보 달력에서 뒤부분의 빈날짜
	 * @param month_day_list 일자 리스트
	 * @return String 빈날짜 html
	 * @throws Exception-> 김민지
	 */
	public static String strEndBlank(ArrayList<Hashtable<String, String>> month_day_list) throws Exception{
		StringBuffer sb = new StringBuffer();
		Hashtable<String, String> ht = (Hashtable<String, String>)month_day_list.get(month_day_list.size()-1);
		String week = ObjectUtils.toString(ht.get("WEEK"));
		int day_blank = 0;
		if(!week.equals("7"))
			day_blank = 7-Integer.parseInt(week);
		
		for(int j=0;j<day_blank;j++){
			sb.append("\n <td>");
			sb.append("\n   <div class=\"cld_date\"></div>");
			sb.append("\n </td>");
		}
		return sb.toString();
	}
	
	/**
	 * 국가별 현재일자(YYYYMMDD)
	 * @param tz_id 타임존
	 * @return String 연월일(8자리)
	 * @throws Exception
	 * @author 석민호
	 */
	public static String getCurYmd() throws Exception{
		
		Calendar cal = Calendar.getInstance();
		String year = ObjectUtils.toString(cal.get(Calendar.YEAR));
		String month = ObjectUtils.toString(cal.get(Calendar.MONTH)+1);
		if(month.length()<2) month = "0"+month;
		String day = ObjectUtils.toString(cal.get(Calendar.DAY_OF_MONTH));
		if(day.length()<2) day = "0"+day;
		return year+month+day;
	}
	
		/**
	 * 국가별 현재시간(YYYYMMDDHHMISE)
	 * @param tz_id 타임존 아이디
	 * @return String 현재시간(연월일시분초 14자리)
	 * @throws Exception
	 * @author 석민호
	 */
	public static String getCurTime() throws Exception{
		Calendar cal = Calendar.getInstance();
		String year = ObjectUtils.toString(cal.get(Calendar.YEAR));
		String month = ObjectUtils.toString(cal.get(Calendar.MONTH)+1);
		if(month.length()<2) month = "0"+month;
		String day = ObjectUtils.toString(cal.get(Calendar.DAY_OF_MONTH));
		if(day.length()<2) day = "0"+day;
		String hour = ObjectUtils.toString(cal.get(Calendar.HOUR_OF_DAY));
		if(hour.length()<2) hour = "0"+hour;
		String minute = ObjectUtils.toString(cal.get(Calendar.MINUTE));
		if(minute.length()<2) minute = "0"+minute;
		String second = ObjectUtils.toString(cal.get(Calendar.SECOND));
		if(second.length()<2) second = "0"+second;
		return year+month+day+hour+minute+second; 
	}
	
	/**
	 * 선택날짜 주간 정보를 가져옵니다.
	 * @param cur_date 현재일자(연월일)
	 * @return 일자(8자리)
	 * @author 김민지
	 * @throws Exception
	 */
	public static String getNowWeekDay(int int_select_year,int int_select_month, int int_select_day) throws Exception {
		 	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String setDate = ""; 
			
	        Calendar cal = Calendar.getInstance();
	        cal.set(int_select_year, int_select_month-1, int_select_day);
	        int yoil = cal.get(Calendar.DAY_OF_WEEK);//요일가져오기 
			 		
			int gap = 0;
			int[] idx = {1, 2, 3, 4, 5, 6, 7};
			for(int i = 0; i < idx.length; i++) {
				if( yoil != 1 && yoil == idx[i] ) {
					gap = i*2-yoil;
					gap = -1-gap;
				}
			}
			cal.add(Calendar.DATE, gap);
			setDate = formatter.format(cal.getTime());
			yoil = cal.get(Calendar.DAY_OF_WEEK);
			
        return setDate.toString();
    }
	
	/**
	 * 선택날짜 주간 리스트를 가지고 옵니다.
	 * @param cur_date 현재일자(연월일)
	 * @return 일자(8자리)
	 * @author 김민지
	 * @throws Exception
	 */
	public static List<HashMap<String, Object>> getNowWeekList(String week_ymd) throws Exception {
		
		List<HashMap<String, Object>> arr_temp = new ArrayList<HashMap<String, Object>>();
		
		for(int i=0;i<7;i++){
    		
    		HashMap<String, Object> ht = new HashMap<String, Object>();
    		
    		String tmp  = DateUtil.getYmdaddDay(week_ymd, i);
    		
    		Calendar cal3 = Calendar.getInstance ();
    		cal3.set(Calendar.YEAR, Integer.parseInt(tmp.substring(0, 4)));
    		cal3.set(Calendar.MONTH, Integer.parseInt(tmp.substring(4, 6))-1);
    		cal3.set(Calendar.DATE, Integer.parseInt(tmp.substring(6)));
        
    		int yoil_int = cal3.get(Calendar.DAY_OF_WEEK);
    		
    		String yoil = null;
    		
    		if(yoil_int == 1){
    			yoil = "일";
    		}else if(yoil_int == 2){
    			yoil = "월";
    		}else if(yoil_int == 3){
    			yoil = "화";
    		}else if(yoil_int == 4){
    			yoil = "수";
    		}else if(yoil_int == 5){
    			yoil = "목";
    		}else if(yoil_int == 6){
    			yoil = "금";
    		}else{
    			yoil = "토";
    		}
    		
    		ht.put("YEAR", tmp.substring(0, 4));
    		ht.put("MONTH", tmp.substring(4, 6));
    		ht.put("DATE", tmp.substring(6));
    		ht.put("WEEK", yoil);
    		ht.put("WEEK_DAY", cal3.get(Calendar.WEEK_OF_MONTH));//몇주차
    		
    		arr_temp.add(ht);
    	}
		
        return arr_temp;
        
    }
	
}