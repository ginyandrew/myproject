package kr.co.jhta.domain;

//도시에 속한 리뷰의 페이지네이션 
public class CriteriaReview {

	private int pageNo; 			//현재 페이지 번호
	private int rowsPerPage = 3 ;	//한 페이지당 출력값 갯수 
	private int pagesPerBlock = 5; 	//페이지네이션 블록당 출력할 페이지갯수
	private int cityNo;				//전달받은 cityNo
	private char rating; 			//전달받은 리뷰 등급 (G / A / P )
	
	public CriteriaReview(){
		this.pageNo = 1;
	}
	
	public CriteriaReview(int cityNo){
		this.pageNo = 1;
		this.cityNo = cityNo;
	}
	
	public CriteriaReview(int cityNo, int pageNo){
		this.pageNo = pageNo;
		this.cityNo = cityNo;
	}

	public CriteriaReview(int pageNo, int cityNo, char rating) {
		super();
		this.pageNo = 1;
		this.cityNo = cityNo;
		this.rating = rating;
	}
	
	public int getBeginIndex(){		
		return (pageNo -1)* rowsPerPage +1;	
	}
	
	public int getEndIndex(){		
		return pageNo * rowsPerPage;			
	}

	// 지금 요청한 페이지 번호 pageNo를 이용해서 이 페이지가 몇번째 블록에 속하는지 알아내기 위한 값.
	// ceil(pageNo나누기 5)값을 구하면 현재 블록값이 나온다. (7페이지면 7/5 = 1.4 의 ceil값은 2, 즉 2번째 블록이 된다)
	public int getCurrentBlockNo(){		
		return (int)(Math.ceil((double)pageNo/pagesPerBlock));
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		if (pageNo <= 0){					//만약 pageNo가 0이거나 0보다 작으면 페이지수는 1이다.
			this.pageNo = 1;				
		}
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getRowsPerBlock() {
		return pagesPerBlock;
	}

	public void setRowsPerBlock(int rowsPerBlock) {
		this.pagesPerBlock = rowsPerBlock;
	}

	public int getCityNo() {
		return cityNo;
	}

	public void setCityNo(int cityNo) {
		this.cityNo = cityNo;
	}

	public int getPagesPerBlock() {
		return pagesPerBlock;
	}

	public char getRating() {
		return rating;
	}

	public void setRating(char c) {
		this.rating = c;
	}

	@Override
	public String toString() {
		return "Criteria [pageNo=" + pageNo + ", rowsPerPage=" + rowsPerPage + ", pagesPerBlock=" + pagesPerBlock + ", cityNo=" + cityNo + "]";
	}


	
	

}
