<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
	<meta charset="utf-8" /> 
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"> 
	<meta name="format-detection" content="telephone=no">
	<title>CLiP</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/common.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
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
<!-- 공지사항 : s -->

<!-- boardDetail : s -->
<div class="boardDetail">
	<!-- boardHead : s -->
	<div class="boardHead">
		<strong class="subject"><c:out value="${noticeDetail.title}"/>
		<c:if test="${noticeDetail.howLongPosted < 168 }">
			&nbsp;<img src="${pageContext.request.contextPath}/cv2/images/common/ico_new.png" alt="<c:out value="${noticeDetail.title}"/>">	
		</c:if>
		</strong>
		<p class="date"><c:out value="${noticeDetail.regTm}"/></p>
	</div>
	<!-- boardHead : e -->
	<!-- boardContent : s -->
	<div class="boardContent">
		<c:if test="${noticeDetail.msgMode eq 'T' }">
			<pre style="white-space:pre-line; word-wrap:break-word; font-family: Gothic; line-height: 1.5em;">
			<c:out value="${noticeDetail.msg}"/></pre>
		</c:if>
		<c:if test="${noticeDetail.msgMode eq 'W' }">
			<iframe src="${noticeDetail.msg}" id="iframe" style="width:100%;overflow:hidden;" onload="onResize(this)"></iframe>	
		</c:if> 
	</div>
	<!-- boardContent : e -->
</div>
<!-- boardDetail : e -->

<!-- 공지사항 : e -->
</body>
</html>