<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
 
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="format-detection" content="telephone=no">

<title>CLiP</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/cv2/css/common.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/cv2/css/swiper.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/cv2/js/swiper.min.js" /></script>
<script type="text/javascript">

	$(document).ready(function(){
		<c:choose>
			<c:when test="${imgUrl eq null || empty imgUrl}">
			</c:when>
			<c:otherwise>/*		
				$("#cust_id").val('${custId}');
				$("#evtfrm").attr('action', "${imgUrl}")
				console.log("cust_id:"+ $("#cust_id").val());
				console.log("action:"+ $("#evtfrm").attr('action'));
				$("#evtfrm").submit(); */
				window.location='${imgUrl}';
			</c:otherwise>
		</c:choose>
	});
	
	function onResize(obj){		
		var window_height = $(window).height();
		var pos = $('#iframe').offset();
		var h = window_height - pos.top;
		$('#iframe').css({
			'top': pos.top +'px',
			"height": (h+10)+"px"
		});
	}
</script>
</head> 
<body>
	<div class="boardDetail"> 
		<div class="boardContent">
			<li id="noimageId">${noImgUrl }</li>
		<!-- 이벤트 상세 : s -->
			<li>${noImgUrl }</li>
		</div>
	</div>
</body>
</html>