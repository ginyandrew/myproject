package kr.co.jhta.domain;

//여행지 카테고리분류VO
public class DestCategoryVO {

	private DestVO dest;				//	카테고리별 여행지번호 (FK, NN)
	private CategoryCodeVO cateCode;	//	카테고리종류 코드번호 (FK, NN)
	
	public DestCategoryVO() {}

	public DestCategoryVO(DestVO dest, CategoryCodeVO cateCode) {
		super();
		this.dest = dest;
		this.cateCode = cateCode;
	}

	public DestVO getDest() {
		return dest;
	}

	public void setDest(DestVO dest) {
		this.dest = dest;
	}

	public CategoryCodeVO getCateCode() {
		return cateCode;
	}

	public void setCateCode(CategoryCodeVO cateCode) {
		this.cateCode = cateCode;
	}

	@Override
	public String toString() {
		return "DestCategoryVO [dest=" + dest + ", cateCode=" + cateCode + "]";
	}

	
	
}
