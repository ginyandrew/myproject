package kr.co.jhta.domain;

// 여행지 카테고리종류 코드VO
public class CategoryCodeVO implements Comparable<CategoryCodeVO> {

	private int code;		//여행지 카테고리종류 번호(PK)
	private String name;	//여행지 카테고리종류 이름(UNN)
	
	public CategoryCodeVO() {}

	public CategoryCodeVO(int code, String name) {
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
		return "CategoryCodeVO [code=" + code + ", name=" + name + "]";
	}
	
	// 성인
	// 중복 방지를 위한 equals 관계 설정
	@Override
	public boolean equals(Object obj) {
		int code = ((CategoryCodeVO)obj).getCode();
		String name = ((CategoryCodeVO)obj).getName();
		return this.code == code && name.equals(this.name);
	}
		
	// 카테고리에 순서를 주기 위한 Comparable의 메소드 구현
	@Override
	public int compareTo(CategoryCodeVO o) {
		return this.code > o.getCode()?1:(this.code == o.getCode()?0:-1);
	}
}
