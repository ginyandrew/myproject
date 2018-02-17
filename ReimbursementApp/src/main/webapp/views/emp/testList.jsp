<!DOCTYPE html>
<%@page import="com.revature.vo.MemberVo"%>
<%@page import="com.revature.dao.MemberDao"%>
<%@page import="com.revature.dao.MemberDaoImpl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="UTF-8">
<title>Request List View For Emp</title>
<link rel="stylesheet" href="styles/employee.css" />
<link rel="stylesheet" href="styles/common.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>

<script src="scripts/emp/rqListViewForEmp.js"></script>
<body>
	<%
		int no = Integer.parseInt(request.getParameter("no"));
		String userId = request.getParameter("id");
		//MemberDao memberDao = new MemberDaoImpl();
		/* Object vo = request.getAttribute("memberVo");
		MemberVo mVo = (MemberVo) vo; */
	%>
	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">

				<div class="row">
					<nav class="navbar navbar-inverse">
						<div class="container-fluid">
							<div class="navbar-header">
								<a class="navbar-brand" href="#">Welcome, <%=userId%>!
								</a>
							</div>
							<ul class="nav navbar-nav">
								<li class="active"><a href="#">Pending: 3</a></li>
								<li><a href="#">Approved: 4</a></li>
								<li><a href="#">Denied: 10</a></li>
							</ul>
							<div class="btn-toolbar">
								<button class="btn btn-default navbar-btn pull-right">Log-Out</button>
								<button class="btn btn-danger navbar-btn pull-right">My Account</button>
							</div>
						</div>
					</nav>

				</div>

				<div class="row">
					<div class="col-md-8">
						<div class="form-group" style="width: 20%;">
							<!-- <label for="sel1">Sort By:</label> -->
							<select class="form-control" id="sel1">
								<option>Status</option>
								<option>Date</option>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<a class="btn btn-danger pull-right" role="button" href="/ReimbursementApp/rqSubmitForEmp.do"> Submit Request </a>
						<!-- <button class="btn btn-danger navbar-btn pull-right ">Submit Request</button> -->
					</div>
				</div>

				<div class="row  ">
					<table class="table table-hover table-bordered text-center">
						<thead>
							<tr>
								<th class="text-center">No.</th>
								<th class="text-center">Title</th>
								<th class="text-center">Status</th>
								<th class="text-center">Date</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${ifRExist > 0 }">
									<c:forEach items="${rVos }" var="vo" varStatus="loop">
										<tr>
											<td>${loop.index}</td>
											<td class="title" data-reqNo="${vo.no }">${vo.purpose }/${vo.amount }$</td>
											<td>
												<c:choose>
													<c:when test="${vo.status == 1}">Pending</c:when>
													<c:when test="${vo.status == 2}">Approved</c:when>
													<c:when test="${vo.status == 3}">Denied</c:when>
													<c:otherwise>error!</c:otherwise>
												</c:choose>
											</td>
											<td>${vo.day}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="4">No Request History Exist.</td>
									</tr>
								</c:otherwise>
							</c:choose>

							<%-- <c:forEach var="vo" items="${memberVo }">
							<tr>
								<td>${vo.no }</td>
								<td>${vo.id }</td>
								<td>${vo.email }</td>
								<td>${vo.pwd }</td>
							</tr>
							</c:forEach>  --%>
						</tbody>

					</table>
				</div>
				<div class="row text-center">
					<ul class="pagination" style="color: red;">
						<li class="page-item"><a class="page-link" href="#">Previous</a></li>
						<c:forEach var="i" begin="1" end="10">
							<li class="page-item"><a class="page-link" href="#">${i }</a></li>
						</c:forEach>
						<li class="page-item"><a class="page-link" href="#">Next</a></li>
					</ul>
				</div>
			</div>
			<div class="col-md-1"></div>
		</div>
	</div>
</body>

<script>
	window.onload = function() {
		var allTd = document.getElementsByClassName("title");
		for (var i = 0; i < allTd.length; i++) {
			allTd[i].addEventListener('click', viewARequest, false);
		}
	}

	function viewARequest(event) {
		console.log('you click me!');
		console.log(event.target.getAttribute("data-reqNo"));
		var requestNo = event.target.getAttribute("data-reqNo");
		// move to the page of the request.
		window.location.href="/ReimbursementApp/rqViewForEmp.do?reqNo=" + requestNo ;
	}
</script>

</html>
