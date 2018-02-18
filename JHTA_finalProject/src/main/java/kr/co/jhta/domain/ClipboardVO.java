package kr.co.jhta.domain;

//클립보드 (여행지 장바구니에 담기)
public class ClipboardVO {

	private UserVO user;		//회원아이디 (FN)		
	private DestVO dest;		//여행지아이디 (FN)
	
	public ClipboardVO(){}

	public ClipboardVO(UserVO user, DestVO dest) {
		super();
		this.user = user;
		this.dest = dest;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public DestVO getDest() {
		return dest;
	}

	public void setDest(DestVO dest) {
		this.dest = dest;
	}

	@Override
	public String toString() {
		return "ClipboardVO [user=" + user + ", dest=" + dest + "]";
	}

	
	
	
}
