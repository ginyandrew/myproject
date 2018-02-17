<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="UTF-8">
<title>Resolve Reimbursement request by a Manager</title>
<link rel="stylesheet" href="styles/manager.css" />
<link rel="stylesheet" href="styles/common.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="scripts/mng/rqViewForMng.js"></script>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">

				<jsp:include page="../common/header.jsp" />
				<div class="row btn-toolbar">
					<c:choose>
						<c:when test="${rVos.manager_no == 0 }">
							<button class="btn   btn-warning navbar-btn pull-left ">Approve</button>
							<button class="btn   btn-warning navbar-btn pull-left ">Deny</button>
						</c:when>
						<c:otherwise>
						<button  class="btn   btn-success navbar-btn pull-left "><c:out value="${rVos.manager_id}"/></button>
							 
						</c:otherwise>
					</c:choose>
					<a role="button" class="btn btn-primary navbar-btn pull-right" href="/ReimbursementApp/rqListViewForMng.do">Go Back to Request List</a>
				</div>

				<div class="row ">
					<table class="table table-hover table-bordered">
						<thead>
							<tr>
								<th colspan="2">
									Reimbursement Request No.
									<c:out value="${rVos.no}" />
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="2">
									<c:choose>
										<c:when test="${rVos.status == 1}">
											<div class="alert alert-warning">
												<strong>Pending</strong>
											</div>
										</c:when>
										<c:when test="${rVos.status == 2}">
											<div class="alert alert-info">
												<strong>Approved</strong>
											</div>
										</c:when>
										<c:when test="${rVos.status == 3}">
											<div class="alert alert-info">
												<strong>Denied</strong>
											</div>
										</c:when>
										<c:otherwise>error!</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width: 20%">Employee ID</td>
								<td style="width: 80%"><c:out value="${rVos.employee_id}" /></td>
							</tr>
							<tr>
								<td style="width: 20%">Purpose</td>
								<td style="width: 80%">
									<c:out value="${rVos.purpose}" />
								</td>
							</tr>
							<tr>
								<td>Amount</td>
								<td>
									<c:out value="${rVos.amount}" />
									$
								</td>
							</tr>
							<!-- <tr>
								<td>Receipt Image</td>
								<td class="btn-toolbar">
									<button type="button" class="btn btn-primary btn-xs">Img1</button> 
									<button type="button" class="btn btn-primary btn-xs">Img2</button>
									<button type="button" class="btn btn-primary btn-xs">Img3</button>
								</td>
							</tr> -->
							<tr>
								<td colspan="2" class="text-center">
									<c:choose>
										<c:when test="${imgCnt > 0 }">
											<c:forEach items="${ iVos}" var="vo">
												<img style="width: 70%;" src=" <c:url value="${vo.filename }"/> ">
											</c:forEach>
										</c:when>
										<c:otherwise>
											No image is registered on this request.
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>

			</div>
			<div class="col-md-1"></div>
			<br>
			<br>
		</div>
	</div>
	<br>
</body>
</html>
