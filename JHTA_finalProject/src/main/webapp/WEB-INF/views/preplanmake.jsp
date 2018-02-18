<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<!-- Bootstrap -->
<link rel="stylesheet"
	href="/resources/common/bootstrap/css/bootstrap.min.css">
<script src="/resources/common/jquery-1.12.0.min.js"></script>
<script src="/resources/common/bootstrap/js/bootstrap.min.js"></script>

<!-- 공통부분 -->
<script src="/resources/common/util.js"></script>
<script src="/resources/common/common.js"></script>
<link rel="stylesheet" href="/resources/common/common.css" />

<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/planmake.css" />
<script type="text/javascript"
	src="http://apis.daum.net/maps/maps3.js?apikey=01547b2af7b0b23180b63ef3365457ea&libraries=services,clusterer"></script>
<script src="/resources/js/preplanmake.js"></script>
<!-- datepicker -->
<!-- sort -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

</head>
<body>

	<div class="container">
		<!-- 헤더 --><input type="hidden" name="user" value="${LOGIN_USER}"/>
		<div id="header" class="row">
			<div class="navbar-header">
				<a id="logo" class="navbar-brand" href="/main.tm"><img
					src="../resources/common/img/bi1.png" /></a>
			</div>
			<div class="nav navbar-nav">
				<span class="navbar">여행할 도시와 각 여행일정을 선택하세요</span>
			</div>
			<div class="pull-right">
				<!-- Trigger the modal with a button -->
				<a type="button" class="btn btn-warning" data-toggle="modal"
					data-target="#myModal">작성하기</a> <a id="close" href="/main.tm"
					class="btn btn-warning">닫기</a>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="sty_posi_rela">

			<div class="panel panel-default firstsidebar">
				<div class="panel-heading">지역</div>
				<div id="cityboxes" class="panel-body"></div>
			</div>

			<!-- map -->
			<div id="map"></div>

			<div class="panel panel-default pull-left secondsidebar">
				<div class="panel-heading" style="height: 37px;">여행도시</div>

				<div id="selcityboxes" class="panel-body"
					style="position: relative;">
					<ul id="sortable">

					</ul>
				</div>

			</div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">상세 정보를 입력하세요</h4>
				</div>
				
				<div class="modal-body">
					<div class="form-group">
						<input type='hidden' name='userno' value="${LOGIN_USER.no}" />
					</div>
					<div class="form-group">
						<label>출 발 일:</label>
						<input type="text" id="datepicker" name="date" value="" />
					</div>
					<div class="form-group">
						<label>여행제목:</label> 
						<input type="text" name="titles" value="" />
					</div>
				</div>
				<div class="modal-footer">
					<a type="button" class="btn btn-default" id="save">시작하기</a>
					<a type="button" class="btn btn-default" data-dismiss="modal">닫기</a>
				</div>
			</div>

		</div>
	</div>

</body>
</html>