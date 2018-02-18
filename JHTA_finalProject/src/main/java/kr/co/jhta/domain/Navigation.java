package kr.co.jhta.domain;

public class Navigation {

	private int totalRows;			//전체 여행지 갯수
	private int totalPages;			//전체 페이지 갯수
	private int totalBlock;			//전체 페이지 블록 갯수	- block 이란, 전체 페이지가 20개일때 페이지네이션 한줄당 [1][2]..[10] 10개만 보여주기로 했다면, 전체 블록은 2개이다. (20/10=2)
									//이를 위해서 criteria에도 block 값을 추가해주어야 한다. (pegesPerBlock)
	private int startPage;			//시작 페이지 번호
	private int endPage;			//끝 페이지 번호
	private int prev;				//이전 페이지 번호
	private int next;				//다음 페이지 번호 
	
	private Criteria criteria;	//Criteria를 여기에 불러와서 currentBlock, pagesPerBlock값 등을 끌어와 쓴다.
	
	public Navigation(int totalRows, Criteria criteria){
		this.totalRows = totalRows;
		this.criteria = criteria;	
		
		//계산하기
		
		//전체글75개에 5개씩 잘라 1페이지라면 전체 15페이지 라는걸 계산(totalpage=전체 페이지수)
		totalPages = (int)(Math.ceil((double)totalRows/criteria.getRowsPerPage()));
		
		//한 화면에 페이지 3개씩 보여주려면>> 전체 페이지15 나누기 3개 = 5개 블록 을 계산한 것. (totalBlock =전체 블록) 어떤 페이지가 몇 블락에 속한건지도 계산한다.
		totalBlock = (int)(Math.ceil((double)totalPages/criteria.getPagesPerBlock()));
		
		//한 블록에 첫번째가 몇번째 페이지인지 나온다. 한 블록에 3페이지씩 나온다 하면, 2번째 블록은 (2-1)*3+1 하면 4페이지부터 시작. 
		startPage = (int) ((criteria.getCurrentBlockNo()-1)* criteria.getPagesPerBlock() +1);
		
		// totalblock *3 하면 한 블록에 마지막이 몇번째페이지인지 나온다. 2번째 블록의 마지막페이지는 2*3 하면 6페이지임.
		endPage = (int) (criteria.getCurrentBlockNo()* criteria.getPagesPerBlock());
		
		
		if(criteria.getCurrentBlockNo() == totalBlock){
			endPage = totalPages;
		}
		
		//이전 페이지로 이동하는 if문. :현재 페이지가 1보다는 커야 이전페이지로 이동할 수 있다.
		if(criteria.getPageNo() >1 ){		
			prev = criteria.getPageNo() -1;
		}
		
		//다음 페이지로 이동하는 if문. 전체 페이지수보다 작은 페이지로만 이동가능함. 전체가 15페이지인데 16으론 이동할 수는 없다. 
		if(criteria.getPageNo() <totalPages){
			next = criteria.getPageNo() +1;
		}
	}

	
	// get만 만든다. set은 만들지 말 것
	public int getTotalRows() {
		return totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalBlock() {
		return totalBlock;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getPrev() {
		return prev;
	}

	public int getNext() {
		return next;
	}

	public Criteria getCriteria() {
		return criteria;
	}


	@Override
	public String toString() {
		return "Navigation [totalRows=" + totalRows + ", totalPages=" + totalPages + ", totalBlock=" + totalBlock
				+ ", startPage=" + startPage + ", endPage=" + endPage + ", prev=" + prev + ", next=" + next
				+ ", criteria=" + criteria + "]";
	}
}
