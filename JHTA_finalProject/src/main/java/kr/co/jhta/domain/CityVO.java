package kr.co.jhta.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

// 도시VO
public class CityVO implements TravelData {

   private int no;               // 도시 번호(PK)
   private String name;         // 도시 이름
   private String lat;            // 도시 위치 위도
   private String lng;            // 도시 위치 경도
   private StateCodeVO stateCode;   // 도시(시도) 카테고리 코드번호(FK, NN)
   private String details;       // 도시 설명 
   
   

   private List<String> imgNameList = new ArrayList<>();	// 여행지 사진명
   
   
	public CityVO() {
	}

	public CityVO(int no, String name, String lat, String lng, StateCodeVO stateCode, String details) {
		super();
		this.no = no;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.stateCode = stateCode;
		this.details = details;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public StateCodeVO getStateCode() {
		return stateCode;
	}

	public void setStateCode(StateCodeVO stateCode) {
		this.stateCode = stateCode;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	// 성인
	/*
	 * 중복된 객체를 처리하기 위해 equals를 재정의함
	 * no가 같으면 같은 객체로 인식함
	 */
	@Override
	public boolean equals(Object obj) {
		int no = ((CityVO)obj).getNo();
		return (this.no==no);
	}

	@Override
	public String toString() {
		return "CityVO [no=" + no + ", name=" + name + ", lat=" + lat + ", lng=" + lng + ", stateCode=" + stateCode + ", details=" + details + "]";
	}

	
	public List<String> getImgNameList() {
		return imgNameList;
	}
	public void setImgNameList(List<String> imgNameList) {
		this.imgNameList = imgNameList;
	}
	
	public String getMainImgName() {
		int size = imgNameList.size();
		return size>0?imgNameList.get((int)(Math.random()*size)):null;
	}
	@JsonIgnore
	public String getRandomImgName() {
		int size = imgNameList.size();
		return size>0?imgNameList.get((int)(Math.random()*size)):null;
	}
	

	//  도시
	/*
	    0100,'서울'
	    0200,'대전'
	    0300,'대구'
	    0400,'부산'
	    0500,'인천'
	    0600,'광주'
	    0700,'울산'
	    0800,'제주'
	    
	    강원도(09)
	    0901,'강릉'
	    0902,'고성'
	    0903,'동해'
	    0904,'삼척'
	    0905,'속초'
	    0906,'양구'
	    0907,'양양'
	    0908,'영월'
	    0909,'원주'
	    0910,'인제'
	    0911,'정선'
	    0912,'철원'
	    0913,'춘천'
	    0914,'태백'
	    0915,'평창'
	    0916,'홍천'
	    0917,'화천'
	    0918,'횡성'
	    
	    경기도(10)
	    1001,'가평'
	    1002,'고양'
	    1003,'과천'
	    1004,'광명'
	    1005,'광주'
	    1006,'구리'
	    1007,'군포'
	    1008,'김포'
	    1009,'남양주'
	    1010,'동두천'
	    1011,'부천'
	    1012,'성남'
	    1013,'수원'
	    1014,'시흥'
	    1015,'안산'
	    1016,'안성'
	    1017,'안양'
	    1018,'양주'
	    1019,'양평'
	    1020,'여주'
	    1021,'연천'
	    1022,'오산'
	    1023,'용인'
	    1024,'의왕'
	    1025,'의정부'
	    1026,'이천'
	    1027,'파주'
	    1028,'평택'
	    1029,'포천'
	    1030,'하남'
	    1031,'화성'
	    
	    경상남도(11)
	    1101,'거제'
	    1102,'거창'
	    1103,'고성'
	    1104,'김해'
	    1105,'남해'
	    1106,'마산'
	    1107,'밀양'
	    1108,'사천'
	    1109,'산청'
	    1110,'양산'
	    1111,'의령'
	    1112,'진주'
	    1113,'진해'
	    1114,'창녕'
	    1115,'창원'
	    1116,'통영'
	    1117,'하동'
	    1118,'함안'
	    1119,'함양'
	    1120,'합천'
	    
	    경상북도(12)
	    1201,'경산'
	    1202,'경주'
	    1203,'고령'
	    1204,'구미'
	    1205,'군위'
	    1206,'김천'
	    1207,'문경'
	    1208,'봉화'
	    1209,'상주'
	    1210,'성주'
	    1211,'안동'
	    1212,'영덕'
	    1213,'영양'
	    1214,'영주'
	    1215,'영천'
	    1216,'예천'
	    1217,'울릉'
	    1218,'울진'
	    1219,'의성'
	    1220,'청도'
	    1221,'청송'
	    1222,'칠곡'
	    1223,'포항'
	    
	    전라남도(13)
	    1301,'강진'
	    1302,'고흥'
	    1303,'곡성'
	    1304,'광양'
	    1305,'구례'
	    1306,'나주'
	    1307,'담양'
	    1308,'목포'
	    1309,'무안'
	    1310,'보성'
	    1311,'순천'
	    1312,'신안'
	    1313,'여수'
	    1314,'영광'
	    1315,'영암'
	    1316,'완도'
	    1317,'장성'
	    1318,'장흥'
	    1319,'진도'
	    1320,'함평'
	    1321,'해남'
	    1322,'화순'
	    
	    전라북도(14)
	    1401,'고창'
	    1402,'군산'
	    1403,'김제'
	    1404,'남원'
	    1405,'무주'
	    1406,'부안'
	    1407,'순창'
	    1408,'완주'
	    1409,'익산'
	    1410,'임실'
	    1411,'장수'
	    1412,'전주'
	    1413,'정읍'
	    1414,'진안'
	    
	    충청남도(15)
	    1501,'공주'
	    1502,'금산'
	    1503,'논산'
	    1504,'당진'
	    1505,'보령'
	    1506,'부여'
	    1507,'서산'
	    1508,'서천'
	    1509,'아산'
	    1510,'예산'
	    1511,'천안'
	    1512,'청양'
	    1513,'태안'
	    1514,'홍성'
	    
	    충청북도(16)
	    1601,'계룡'
	    1602,'괴산'
	    1603,'단양'
	    1604,'보은'
	    1605,'영동'
	    1606,'옥천'
	    1607,'음성'
	    1608,'제천'
	    1609,'진천'
	    1610,'청원'
	    1611,'청주'
	    1612,'충주'
	    1613,'증평'
	*/
	
}
