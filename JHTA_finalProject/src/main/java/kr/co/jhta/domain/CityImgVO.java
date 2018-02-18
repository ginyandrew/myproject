package kr.co.jhta.domain;

//도시 사진 VO
public class CityImgVO {

	private String fileName;//도시 사진 파일명
	private CityVO city;	//도시 사진 번호 (FK, NN)
	
	public CityImgVO(){}

	public CityImgVO(String fileName, CityVO city) {
		super();
		this.fileName = fileName;
		this.city = city;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public CityVO getCity() {
		return city;
	}

	public void setCity(CityVO city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "CityImgVO [fileName=" + fileName + ", city=" + city + "]";
	}
	
	
	
}
