package kr.co.jhta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

//운영자가 사용자에게 보내는 메세지
public class MessageVO {

	private int no;					//메시지 번호
	private String sendTime;		//메시지를 보낸 시간
	private String data;			//메시지 내용 
	private UserVO user;			//메시지를 받은 회원아이디 (FN)
	
	public MessageVO(){}

	public MessageVO(String data, int userNo) {
		super();
		this.no = 0;
		this.sendTime = null;
		this.data = data;
		this.user = new UserVO();
		this.user.setNo(userNo);
	}
	
	public MessageVO(int no, String sendTime, String data, UserVO user) {
		super();
		this.no = no;
		this.sendTime = sendTime;
		this.data = data;
		this.user = user;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@JsonIgnore
	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "MessageVO [no=" + no + ", sendTime=" + sendTime + ", data=" + data + ", user=" + user + "]";
	}

	
	
	
	
	
}
