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
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/swiper.min.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/swiper.min.js"/></script>
	<script type="text/javascript">
		function goShopDetail(shopId, shopName) {
		
			var targetUrl = "webtoapp://creditCardShopDetail?shopId="+shopId+"&shopName="+encodeURIComponent(shopName);
			console.log("doShopDetail() targetUrl : " + targetUrl);
			$(location).attr("href", targetUrl);
		}
	</script>
</head>
<body class="bgGray">
<!-- 신용체크카드 상세 -카드혜택  : s -->

<!-- boxShow : s -->
<div class="boxShow type02">
	<!-- menuTab : s -->
	<!-- <ul class="menuTab">
		<li class="on" title="현재 페이지"><a href="#">카드혜택</a></li>
		<li><a href="#">가맹점</a></li>
		<li><a href="#">당월내역</a></li>
		<li><a href="#">포인트</a></li>
	</ul> -->
	<!-- menuTab : e-->
	<h2 class="titH2">할인/적립혜택</h2>
	<!-- benefitList : s -->
	<div class="benefitList">
		<ul>
			<c:choose>
				<c:when test="${dsctSaveResult eq null || empty dsctSaveResult }">
					<li>
						<a href="#">
							<div class="info">
								<p class="brand">해당 카드에서 제공하는 할인/적립 혜택이 없습니다.</p>
							</div>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<c:forEach var='dsctSave' items='${dsctSaveResult }'>
						<li>
							<a href="${dsctSave.shopId }" 
							onclick="goShopDetail('<c:out value='${dsctSave.shopId }'/>','<c:out value='${dsctSave.shopName }'/>');return false;">
							<p class="img"><img onerror="this.src='${pageContext.request.contextPath}/cv2/images/_temp/@temp_brand05.png'" 
							src="${dsctSave.host}${dsctSave.imgUrl}" alt="${dsctSave.shopName }"></p>
							<div class="info">
								<p class="brand"><c:out value='${dsctSave.shopName }'/></p>
								<c:if test="${!empty dsctSave.benefitName}">
									<p class="etc"><c:out value='${dsctSave.benefitName }'/></p>
								</c:if>
								<c:if test="${!empty dsctSave.mileageInfo}">
									<p class="etc"><c:out value='${dsctSave.mileageInfo }'/></p>
								</c:if>
							</div>
							</a>
						</li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<!-- benefitList : e -->

	<h2 class="titH2">기타혜택</h2>
	<!-- benefitList : s -->
	<div class="benefitList">
		<ul>
			<c:choose>
				<c:when test="${etcResult eq null || empty etcResult }">
					<li>
						<a href="#">
							<div class="info">
								<p class="brand">해당 카드에서 제공하는 기타 혜택이 없습니다.</p>
							</div>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<c:forEach var='etc' items='${etcResult }'>
						<li>
							<a href="${etc.shopId }" onclick="goShopDetail('<c:out value='${etc.shopId }'/>','<c:out value='${etc.shopName }'/>');return false;">
							<p class="img"><img onerror="this.src='${pageContext.request.contextPath}/cv2/images/_temp/@temp_brand05.png'" 
							src="${etc.host}${etc.imgUrl}" alt="${etc.shopName }"></p>
							<div class="info">
								<p class="brand"><c:out value='${etc.shopName }'/></p>
								<c:if test="${!empty etc.benefitName}">
									<p class="etc"><c:out value='${etc.benefitName }'/></p>
								</c:if>
								<c:if test="${!empty etc.mileageInfo}">
									<p class="etc"><c:out value='${etc.mileageInfo }'/></p>
								</c:if>
							</div>
							</a>
						</li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<!-- benefitList : e -->

	<h2 class="titH2">이벤트</h2>
	<!-- benefitList : s -->
	<div class="benefitList">
		<ul>
			<c:choose>
				<c:when test="${eventResult eq null || empty eventResult }">
					<li>
						<a href="#">
							<div class="info">
								<p class="brand">해당 카드에서 제공하는 이벤트가 없습니다.</p>
							</div>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<c:forEach var='event' items='${eventResult }'>
						<li>
							<a href="${event.shopId }" onclick="goShopDetail('<c:out value='${event.shopId }'/>','<c:out value='${event.shopName }'/>');return false;">
							<p class="img"><img onerror="this.src='${pageContext.request.contextPath}/cv2/images/_temp/@temp_brand05.png'" 
							src="${event.host}${event.imgUrl}" alt="${event.shopName }"></p>
							<div class="info">
								<p class="brand"><c:out value='${event.shopName }'/></p>
								<c:if test="${!empty event.benefitName}">
									<p class="etc"><c:out value='${event.benefitName }'/></p>
								</c:if>
								<c:if test="${!empty event.mileageInfo}">
									<p class="etc"><c:out value='${event.mileageInfo }'/></p>
								</c:if>
								<c:if test="${!empty event.eventBeginDate && !empty event.eventEndDate}">
									<p class="date"> 
									<c:out value='${event.eventBeginDate }'/> ~ <c:out value='${event.eventEndDate }'/> </p>
								</c:if>
								<c:if test="${!empty event.eventValidDays}">
									<p class="etc" style="word-break:break-all;">이벤트 지정일 : <c:out value='${etc.eventValidDays }'/></p>
								</c:if> 	
							</div>
							</a>
						</li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<!-- benefitList : e -->
</div>
<!-- boxShow : e -->

<!-- 신용체크카드 상세 -카드혜택  : e -->
</body>
</html>