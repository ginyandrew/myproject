package kr.co.jhta.domain;

//일정 상세(하나의 일정에 속한 장소 하나)
public class PlanDetailVO {

	private int no;				//일정속 장소번호
	private int day;			//장소 방문일
	private int numbering;		//장소 방문순서
	private int budget;			//해당장소에 쓰일 예산
	private String memo;		//장소에 달린 코멘트
	private int planNo;		//해당 일정번호 (FN)
	private DestVO dest;		//여행지 (FN)
	
	public PlanDetailVO(){}

	public PlanDetailVO(int no, int day, int numbering, int budget, String memo, int planNo, DestVO dest) {
		super();
		this.no = no;
		this.day = day;
		this.numbering = numbering;
		this.budget = budget;
		this.memo = memo;
		this.planNo = planNo;
		this.dest = dest;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getNumbering() {
		return numbering;
	}

	public void setNumbering(int numbering) {
		this.numbering = numbering;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getPlanNo() {
		return planNo;
	}

	public void setPlanNo(int planNo) {
		this.planNo = planNo;
	}

	public DestVO getDest() {
		return dest;
	}

	public void setDest(DestVO dest) {
		this.dest = dest;
	}

	@Override
	public String toString() {
		return "PlanDetailVO [no=" + no + ", day=" + day + ", numbering=" + numbering + ", budget=" + budget + ", memo=" + memo + ", planNo=" + planNo + ", dest=" + dest + "]";
	}
}
