package kr.co.jhta.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

// 여행지 VO
public class DestVO implements TravelData {

   private int no;        	 	//여행지 번호(PK, NN)   
   private String address;   	//여행지 주소
   private String lat;     		//여행지 위치 위도
   private String lng;    	  	//여행지 위치 경도
   private String name;   		//여행지 이름
   private String details;   	//여행지 소개(설명)
   private String contact;   	//여행지 연락처
   private String openTime;		//여행지 영업시간
   private String site;   		//여행지 웹사이트
   private CityVO city;   	//여행지 해당 도시번호(FN, NN)
   
   
   private List<CategoryCodeVO> categoryList;	// 카테고리 정보
   private List<String> imgNameList= new ArrayList<>();	// 여행지 사진명
   
   public DestVO(){}

	public DestVO(int no, String address, String lat, String lng, String name, String details, String contact, String openTime, String site, CityVO city) {
		super();
		this.no = no;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.name = name;
		this.details = details;
		this.contact = contact;
		this.openTime = openTime;
		this.site = site;
		this.city = city;	
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public CityVO getCity() {
		return city;
	}

	public void setCity(CityVO city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "DestVO [no=" + no + ", address=" + address + ", lat=" + lat + ", lng=" + lng + ", name=" + name + ", details=" + details + ", contact=" + contact + ", openTime=" + openTime + ", site=" + site
				+ ", city=" + city + "]";
	}

	// 성인
	public List<CategoryCodeVO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryCodeVO> categoryList) {
		this.categoryList = categoryList;
	}
	
	public List<String> getImgNameList() {
		return imgNameList;
	}
	public void setImgNameList(List<String> imgNameList) {
		this.imgNameList = imgNameList;
	}
	
	public String getMainImgName() {
		return imgNameList.size()>0?imgNameList.get(0):null;
	}
}