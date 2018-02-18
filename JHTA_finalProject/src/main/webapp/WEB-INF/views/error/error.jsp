<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TravelMaker:: Error</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="/resources/common/bootstrap/css/bootstrap.min.css">
<script src="/resources/common/jquery-1.12.0.min.js"></script>
<script src="/resources/common/bootstrap/js/bootstrap.min.js"></script>

<!-- 공통부분 -->
<link rel="stylesheet" href="/resources/common/common.css" />
<style>
	div#btn-area button {
		width: 100%;
	}
	span.error-title {
		font-size: 20px;
	}
	p.error-contents {
		font-size: 16px;
	}
</style>
<script>
	$(function() {
		$("button#btn-back").click(function(event) {
			window.history.back();
			event.preventDefault();
		});
		$("button#btn-main").click(function(event) {
			window.location.href = "/main.tm";
		});
		$("button#btn-exit").click(function(event) {
			window.location.href = "http://www.jhta.co.kr";
		});
	});
</script>
</head>
<body>
	<div class="container">
		<div class="text-center">
			<h1 style="font-weight:bold; margin: 20px 0; ">오류가 발생했습니다.</h1>
			<img src="/resources/common/img/deadend.png" alt="" width="500" />	
			</div>
	<div class="panel panel-danger">
		<div class="panel-heading">
			<span class="error-title"><%=exception.getClass().toGenericString() %></span>
		</div>
		<div class="panel-body">
			<p class="error-contents"><%=exception.getMessage() %></p>
		</div>
	</div>
	<div class="row" id="btn-area">
		<div class="col-md-4 col-sm-4 col-xs-12">
			<button id="btn-back" class="btn btn-default">뒤로 돌아가겠습니다</button>
		</div>
		<div class="col-md-4 col-sm-4 col-xs-12">
			<button id="btn-main" class="btn btn-primary">메인으로 돌아가겠습니다</button>
		</div>
		<div class="col-md-4 col-sm-4 col-xs-12">
			<button id="btn-exit" class="btn btn-danger">홈페이지를 나가겠습니다</button>
		</div>
	</div>
	</div>
</body>
</html>