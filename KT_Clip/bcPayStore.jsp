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
<!-- 신용체크카드 상세 - 가맹점  : s -->

<!-- boxShow : s -->
<div class="boxShow type02">
	<!-- menuTab : s -->
	<!-- <ul class="menuTab">
		<li><a href="#">카드혜택</a></li>
		<li class="on" title="현재 페이지"><a href="#">가맹점</a></li>
		<li><a href="#">당월내역</a></li>
		<li><a href="#">포인트</a></li>
	</ul> -->
	<!-- menuTab : e-->
	<!-- boxShow : s -->
	<div class="boxShow type03">
		<!-- boxBrand : s -->
		<div class="boxBrand mt0">
			<ul>
			<c:choose>
				<c:when test="${storesByCardId eq null || empty storesByCardId}">
					<li>
					<a href="#">
						<div class="info">
							<p class="brand">해당 카드의 가맹점이 없습니다.</p>
						</div>
					</a>
					</li>	
				</c:when>
				<c:otherwise>
					<c:forEach var='store' items='${storesByCardId }' varStatus="status">
					<c:choose>
						<c:when test="${store.shopName eq null || empty store.shopName }">
							<li id="<c:out value='${store.shopId }'/>">
						</c:when>
						<c:otherwise>
							<li id="<c:out value='${store.shopId }'/>">
								<p class="brand" onclick="goShopDetail('<c:out value='${store.shopId }'/>','<c:out value='${store.shopName }'/>');">
								<img onerror="this.src='${pageContext.request.contextPath}/cv2/images/_temp/@temp_brand05.png'" 
								src="${store.host }${store.imgUrl}" alt="${store.shopName }"></p>
								<p class="name">
									<em><c:out value='${store.shopName }'/></em>
								</p>
							</li>
						</c:otherwise>
					</c:choose>
					<c:if test="${(status.index +1) % 4 == 0}">
					</ul><ul>
					</c:if>
					</c:forEach>
				</c:otherwise>
			</c:choose>		 
			</ul>
		</div>
		<!-- boxBrand : e -->
	</div>
	<!-- boxShow : e -->
</div>
<!-- boxShow : e -->

<!-- 신용체크카드 상세 -가맹점  : e -->
</body>
</html>