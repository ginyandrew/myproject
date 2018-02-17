package com.revature.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.dao.ImgDao;
import com.revature.dao.ImgDaoImpl;
import com.revature.dao.MemberDao;
import com.revature.dao.MemberDaoImpl;
import com.revature.dao.RequestDao;
import com.revature.dao.RequestDaoImpl;
import com.revature.vo.MemberVo;
import com.revature.vo.RequestVo;

public class ServiceCommon {

	MemberDao memberDao = new MemberDaoImpl();
	RequestDao rDao = new RequestDaoImpl();
	ImgDao imgDao = new ImgDaoImpl();
	String viewPage;

	public String loginValidation(HttpServletRequest req, HttpServletResponse resp) {

		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		HttpSession session = req.getSession();

		int ifExist = memberDao.ifMemberExist(id);
		if (ifExist > 0) {
			int ifRightPwd = memberDao.ifRightPwd(id, pwd);
			if (ifRightPwd == 1) {

				// store info into session
				session.setAttribute("m_id", id);
				session.setAttribute("m_pwd", pwd);
				// log in success
				MemberVo vo = memberDao.getMemberById(id);
				session.setAttribute("m_no", vo.getNo());

				String lv = vo.getLv();
				if (lv.equals("0")) {
					// init page No as 1 
					int pageNo = 1;				//init pageNo as 1 when log-in
					List<RequestVo> rVos = rDao.getRequests(pageNo);
					//session.setAttribute("rVos", rVos);
					req.setAttribute("rVos", rVos);
					req.setAttribute("ifRExist", rVos.size());
					viewPage = "views/mng/rqListViewForMng.jsp?no=" + vo.getNo() + "&id=" + vo.getId();
				} else {
					// int empNo = Integer.parseInt(req.getParameter("no")); //empNo
					int ifRExist = memberDao.ifRequestExistByEmployeeNo(vo.getNo());
					req.setAttribute("ifRExist", ifRExist);

					List<RequestVo> vos = new ArrayList<RequestVo>();
					if (ifRExist > 0) {
						vos = memberDao.getRequestsByEmployeeNo(vo.getNo());
					} else {
						vos.add(new RequestVo());
					}
					req.setAttribute("rVos", vos);
					viewPage = "views/emp/rqListViewForEmp.jsp?no=" + vo.getNo() + "&id=" + vo.getId();
				}
			} else {
				// wrong password
				viewPage = "views/common/msg.jsp?error_code=3";
			}
		} else {
			// id doesn't exist
			viewPage = "views/common/msg.jsp?error_code=4";
		}
		return viewPage;
	}


	public String joinValidation(HttpServletRequest req, HttpServletResponse resp) {
		MemberVo mVo = new MemberVo();
		mVo.setId(req.getParameter("id"));
		mVo.setEmail(req.getParameter("email"));
		mVo.setPwd(req.getParameter("pwd"));
		mVo.setLv(req.getParameter("lv"));

		int cnt = memberDao.ifMemberExist(mVo.getId());
		// int cnt = 0;
		if (cnt > 0) {
			// id already exist.
			viewPage = "views/common/msg.jsp?error_code=1";
		} else {
			// success to join in.
			memberDao.insertMember(mVo);
			viewPage = "views/common/msg.jsp?error_code=2";
		}
		return viewPage;
	}

}
