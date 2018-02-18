package kr.co.jhta.domain;

//사용자
	/*
	'M': 남자
	'F': 여자
	*/
	
	/*
	'T': 탈퇴회원(이 회원의 정보는 출력하면 안됨)
	'F': 그냥 회원
	*/

public class UserVO {

	private int no;				//회원아이디
	private String name;		//회원이름
	private String email;		//회원이메일
	private String password; 	//회원비밀번호
	private String gender; 		//회원성별
	private String regdate; 	//회원가입일
	private String isDropped; 	//탈퇴여부 (y/n)	
	private int point;			//적립포인트
	
	public UserVO() {}

	public UserVO(int no, String name, String email, String password, String gender, String regdate, String isDropped, int point) {
		super();
		this.no = no;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.regdate = regdate;
		this.isDropped = isDropped;
		this.point = point;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getisDropped() {
		return isDropped;
	}

	public void setisDropped(String isDropped) {
		this.isDropped = isDropped;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return "User [no=" + no + ", name=" + name + ", email=" + email + ", password=" + password + ", gender=" + gender + ", regdate=" + regdate + ", isDropped=" + isDropped + ", point=" + point + "]";
	}
	
	

	// 성인
	public UserVO(int no) {
		this.no = no;
		this.point = 0;
	}
}
