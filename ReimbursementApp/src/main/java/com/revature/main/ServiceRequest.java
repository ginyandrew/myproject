package com.revature.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.revature.dao.ImgDao;
import com.revature.dao.ImgDaoImpl;
import com.revature.dao.MemberDao;
import com.revature.dao.MemberDaoImpl;
import com.revature.dao.RequestDao;
import com.revature.dao.RequestDaoImpl;
import com.revature.vo.ImgVo;
import com.revature.vo.RequestVo;

public class ServiceRequest {

	MemberDao mDao = new MemberDaoImpl();
	RequestDao rDao = new RequestDaoImpl();
	ImgDao imgDao = new ImgDaoImpl();

	String viewPage;

	public   void viewRequestListForMng(HttpServletRequest req, HttpServletResponse resp) {
		
		int pageNo = 1;				// temporary (need to change)
		List<RequestVo> rVos = rDao.getRequests(pageNo);
		req.setAttribute("rVos", rVos);
		req.setAttribute("ifRExist", rVos.size());
	}
	
	public String viewRequestListForEmp(HttpServletRequest req, HttpServletResponse resp) {

		HttpSession session = req.getSession();
		int empNo = (Integer) session.getAttribute("m_no");
		// int empNo = Integer.parseInt(req.getParameter("m_no")); //empNo
		int ifRExist = mDao.ifRequestExistByEmployeeNo(empNo);
		req.setAttribute("ifRExist", ifRExist);

		List<RequestVo> vos = new ArrayList<RequestVo>();
		if (ifRExist > 0) {
			vos = mDao.getRequestsByEmployeeNo(empNo);
		} else {
			vos.add(new RequestVo());
		}
		req.setAttribute("rVos", vos);
		viewPage = "views/emp/rqListViewForEmp.jsp";

		return viewPage;
	}

	public String requestUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession session = req.getSession();
		// (1) pictures of Receipts update
		String directory = "C:\\GitRepos\\1801-Jan22-java\\batch-source\\ReimbursementApp\\src\\main\\webapp\\img";
		int maxSize = 1024 * 1024 * 2;
		String encoding = "UTF-8";

		MultipartRequest multiRequest = new MultipartRequest(req, directory, maxSize, encoding,
				new DefaultFileRenamePolicy());

		// (1) Request update
		int m_no = (Integer) session.getAttribute("m_no");
		String purpose = multiRequest.getParameter("purpose");
		int amount = Integer.parseInt(multiRequest.getParameter("amount"));
		int request_no = mDao.insertRequest(new RequestVo(amount, purpose, m_no));

		String name1 = multiRequest.getOriginalFileName("receipt_img");
		String savedfileName = multiRequest.getFilesystemName("receipt_img"); // this file name should be saved
		rDao.insertImg(savedfileName, request_no); // save the file with request no.

		viewPage = "/rqListViewForEmp.do";
		return viewPage;
	}

	public void getaRequestInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession();

		// requestNo >> get requestVo + ImgVos
		int request_no = Integer.parseInt(req.getParameter("request_no"));

		RequestVo vo = rDao.getRequestByRNo(request_no);
		List<ImgVo> iVos = rDao.getImgByRNo(request_no);
		session.setAttribute("rVos", vo); // request info
		session.setAttribute("imgCnt", iVos.size());
		session.setAttribute("iVos", iVos); // imgs' info
		// session.setAttribute("imgPath2", "img/" + iVos.get(1).filename); // imgs'
		// info
	}

}