<!DOCTYPE html>
<%@page import="com.revature.vo.MemberVo"%>
<%@page import="com.revature.dao.MemberDao"%>
<%@page import="com.revature.dao.MemberDaoImpl"%>
<html>
<head>
<meta charset="UTF-8">
<title>Validate Registration a new Employee</title>
<link rel="stylesheet" href="../styles/common.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="../scripts/login.js"></script>
</head>
<body>
	<%
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MemberDao memberDao = new MemberDaoImpl();

		int ifExist = memberDao.ifMemberExist(id);
		if (ifExist >0 ) {
			int ifRightPwd = memberDao.ifRightPwd(id, pwd);
			if (ifRightPwd  == 1){
				// store info into session
				session.setAttribute("id", id);
				session.setAttribute("pwd", pwd);
				// log in success
				MemberVo vo = memberDao.getMemberById(id);
				String lv = vo.getLv();
				if (lv.equals("0")){
					response.sendRedirect("/ReimbursementApp/rqListViewForMng.do?no="+ vo.getNo() + "&id=" + vo.getId());
					
				} else {
					response.sendRedirect("/ReimbursementApp/rqListViewForEmp.do?no="+ vo.getNo() + "&id=" + vo.getId());
					
				}
			} else {
				// wrong password
				%>
				<script>
					alert("The password doesn't match.");
					history.go(-1);
				</script>
				<%
			}
		}  else {
			// id doesn't exist
			%>
			<script>
				alert("this Id doesn't exist.");
				history.back();
			</script>
			<%	
		}
	%>
		 <%-- <c:forEach var="vo" items="${memberVo }">
							<tr>
								<td>${vo.no }</td>
								<td>${vo.id }</td>
								<td>${vo.email }</td>
								<td>${vo.pwd }</td>
							</tr>
							</c:forEach>  --%>
</body>
</html>