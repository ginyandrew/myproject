<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
		
		function onResize(obj){
			var window_height = $(window).height();
			var pos = $('#iframe').offset();
			var h = window_height - pos.top;
			$('#iframe').css({
				'top': pos.top +'px',
				"height": (h+10)+"px"
			});
		}
		
		function goDetail(){
			var shopId = "<c:out value='${shopId}'/>";
			var shopName = "<c:out value='${shopName}'/>";
			
			shopName = encodeURIComponent(shopName);
			
			var url = "${pageContext.request.contextPath}/cv2/wv/storeBenefit/store/detail.do"; 
			url += "?shopNm=" +shopName + "&shopId=" + shopId + "&branchCnt=20";
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('매장상세')+"&url=" + url;
			$(location).attr("href", targetUrl);
		};
		 
	</script>
</head> 
<body class="bgGray02">
<!-- 쿠폰 발급완료(바코드 제공) : s -->

<!-- boxShow : s -->
<div class="boxShow grayType01">
	<strong class="titSubject">사용안내</strong>
	<p class="txt04">
	<c:choose>
		<c:when test="${noInfo ne null}">
			<c:out value="${noInfo}"/>	
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${couponInfo.infosource eq 'link'}">
					<iframe src="${couponInfo.memo}" id="iframe" style="width:100%;overflow:hidden;" onload="onResize(this)"></iframe>	
				</c:when>
				<c:otherwise>
					<c:if test="${! empty couponInfo.memo && couponInfo.memo ne null}">
					<pre style="white-space:pre-line; word-wrap:break-word; font-family: Gothic; line-height: 1.5em;">
					<c:out value="${couponInfo.memo}"/></pre>
					</c:if>
					<c:if test="${! empty couponInfo.info && couponInfo.info ne null}">
						<c:choose>
							<c:when test="${fn:contains(couponInfo.info, '<br>') }">
								<c:out value="${couponInfo.info}"/>
							</c:when>
							<c:otherwise>
								<pre style="white-space:pre-line; word-wrap:break-word; font-family: Gothic; line-height: 1.5em;">
								<c:out value="${couponInfo.info}"/></pre>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:otherwise>
			</c:choose>		
		<br/>
		유효기간 : <c:out value="${couponInfo.validDay}"/>
		</c:otherwise>
	</c:choose>
	</p>
	<br/>
	 
	<c:if test="${noShopInfo ne null}">
		<p class="txt04"><c:out value="${noShopInfo}"/></p>
	</c:if>
	<c:if test="${noShopInfo eq null}">
		<a href="#" class="btn btnLineRed mt20" id="searchMap" onclick="goDetail();">
			<span>매장 정보</span>
		</a>
	</c:if>
	 
</div>
<!-- boxShow : e -->


<!-- 쿠폰 발급완료(바코드 제공) : 2 -->
</body>
</html>