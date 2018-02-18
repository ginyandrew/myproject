<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><tiles:insertAttribute name="title" /></title>
<!-- Bootstrap -->
<script src="/resources/common/jquery-1.12.0.min.js"></script>
<script src="/resources/common/jquery-ui/jquery-ui.min.js"></script>
<script src="/resources/common/bootstrap/js/bootstrap.min.js"></script>

<!-- 공통부분 -->
<link rel="stylesheet" href="/resources/common/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/common/contextmenu/contextMenu.css" />
<link rel="stylesheet" href="/resources/common/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" href="/resources/common/common.css" />
<script src="/resources/common/util.js"></script>
<script src="/resources/common/contextmenu/contextMenu.min.js"></script>
<script src="/resources/common/common.js"></script>

</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<!-- header -->
			<tiles:insertAttribute name="header" />
		</div>
		<div class="row">
			<!-- contents -->
			<tiles:insertAttribute name="contents" />
		</div>
		<div class="row">
			<!-- right-menu -->
			<tiles:insertAttribute name="right-menu" />
		</div>
		<div class="row">
			<!-- footer -->
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
</body>
</html>