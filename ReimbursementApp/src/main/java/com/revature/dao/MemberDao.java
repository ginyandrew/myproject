package com.revature.dao;

import java.util.List;

import com.revature.vo.MemberVo;
import com.revature.vo.RequestVo;

public interface MemberDao {

	MemberVo getMemberById(String id);
	int insertMember(MemberVo memberVo);
	int insertRequest(RequestVo requestVo);
	int ifMemberExist(String id);
	int ifRightPwd(String id, String pwd);
	int ifRequestExistByEmployeeNo(int empNo);
	List<RequestVo> getRequestsByEmployeeNo(int empNo);
	 
}
