package kr.co.jhta.domain;

//도시(시도) 카테고리 코드 VO	
	/*
	01, '서울'
	02, '대전'
	03, '대구'
	04, '부산'
	05, '인천'
	06, '광주'
	07, '인천'
	08, '제주'
	09, '강원도'
	10, '경기도'
	11, '경상남도'
	12, '경상북도'
	13, '전라남도'
	14, '전라북도'
	15, '충청남도'
	16, '충청북도'
	*/
public class StateCodeVO {

	private int code;		//도시(시도) 카테고리 코드번호(PK)
	private String name;	//도시(시도) 카테고리 코드이름(UNN)
	
	public StateCodeVO() {}
	
	public StateCodeVO(int code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "StateCode [code=" + code + ", name=" + name + "]";
	}
	
}
