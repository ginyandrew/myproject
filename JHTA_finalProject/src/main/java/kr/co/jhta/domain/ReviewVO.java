package kr.co.jhta.domain;

//여행지 리뷰VO
	/*
	평가
	'G': GOOD(좋아요)
	'A': Average(괜찮아요)
	'P': Poor(별로에요)
	*/
public class ReviewVO {

	private int no;			// 리뷰번호	(PK)
	private String regDate;	// 리뷰작성일(NN)
	private String rating;	// 여행지 평가(NN), [Y,N] 
	private String data;	// 리뷰글
	private DestVO dest;	// 리뷰가 쓰인 해당 여행지 번호 (FK, NN)
	private UserVO user;	// 리뷰를 쓴 사용자 번호		(FK, NN)
	
	public ReviewVO() {}

	public ReviewVO(int no, String regDate, String rating, String data, DestVO dest, UserVO user) {
		super();
		this.no = no;
		this.regDate = regDate;
		this.rating = rating;
		this.data = data;
		this.dest = dest;
		this.user = user;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public DestVO getDest() {
		return dest;
	}

	public void setDest(DestVO dest) {
		this.dest = dest;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ReviewVO [no=" + no + ", regDate=" + regDate + ", rating=" + rating + ", data=" + data + ", dest=" + dest + ", user=" + user + "]";
	}

	
	
}
