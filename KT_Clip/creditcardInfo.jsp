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
		/* $(function(){			
			// KT 멤버십 이벤트 스와이프
			var visualSwiper = new Swiper('.leaveSwipe', {
				direction: 'horizontal',
				spaceBetween: 6,
				slidesPerView: 1,
				loop: false,
				pagination : '.leaveSwipe .page',
				paginationType: 'fraction',
				paginationFractionRender: function (swiper, currentClassName, totalClassName) {
				  return '<span class="' + currentClassName + '"></span>' +
						 '<em>/</em>' +
						 '<span class="' + totalClassName + '"></span>';
				}
			});
		});  */
		
		$(document).ready(function() {
			
			
			$(".menuTab li a").on('click' , function(){
				var benefitType = $(this).attr('id');
				$("#benefitType").val(benefitType);
				
				var cardId = "<c:out value='${cardId}'/>";
				$("#cardId").val(cardId);		 
				
				$("#ajaxform").submit();
			});
		});
		
		function goShopDetail(shopId, shopName) {
			var eventUrl = "webtoapp://creditCardShopDetail?shopId="+shopId+"&shopName="+encodeURIComponent(shopName);
			console.log("doShopDetail() targetUrl : " + eventUrl);
			$(location).attr("href", eventUrl);
		}
		
		function goDetail(eventLinkUrl, benefitName){
			var targetUrl = "webtoapp://newWindowPopup?cardEventTitle="+encodeURIComponent(benefitName)+"&cardEventUrl=" + eventLinkUrl;
			console.log("doShopDetail() targetUrl : " + eventLinkUrl);
			$(location).attr("href", targetUrl);
		};
		
	</script>
</head> 
<body class="bgGray">
<!-- 신용체크카드 상세(등록 전) : s -->
<form name="ajaxform" id="ajaxform" method="POST" action="info.do">
	<input type="hidden" id="benefitType" name="benefitType" value="">
	<input type="hidden" id="cardId" name="cardId" value="">
</form> 

<!-- boxShow : s -->
<div class="boxShow type02">
	<!-- menuTab : s -->
	<ul class="menuTab">
		<li <c:if test="${benefitType eq 'dsctSave'|| benefitType eq null }"> class="on" title="현재 페이지" </c:if>><a href="#" id="dsctSave">할인/적립 혜택</a></li>
		<li <c:if test="${benefitType eq 'etc'}"> class="on" title="현재 페이지" </c:if>><a href="#" id="etc">기타 혜택</a></li>
		<li <c:if test="${benefitType eq 'event'}"> class="on" title="현재 페이지" </c:if>><a href="#" id="event">이벤트</a></li>
	</ul>
	<!-- menuTab : e-->
	<!-- benefitList : s -->
	<div class="benefitList">
		<ul>
		<c:choose>
			<c:when test="${benefitList eq null || empty benefitList}">
				<li>
					<a href="#">
						<div class="info">
							<p class="brand">해당 카드에서 제공하는 
							<c:if test="${benefitType eq 'dsctSave'|| benefitType eq null }">할인/적립혜택이</c:if>
							<c:if test="${benefitType eq 'etc'}">기타 혜택이</c:if>
							<c:if test="${benefitType eq 'event'}">이벤트가</c:if>
							없습니다.</p>
						</div>
					</a>
				</li>	
			</c:when>
			<c:otherwise>
				<c:forEach var='benefit' items='${benefitList}'>
					<li>
						<a href="#" id="${benefit.seq }" 
						<c:if test='${benefitType eq "event"}'>
						onclick="goDetail('<c:out value='${benefit.eventLinkUrl }'/>','<c:out value='${benefit.benefitName }'/>');return false;"
						</c:if>
						>
							<p class="img"><img onerror="this.src='${pageContext.request.contextPath}/cv2/images/_temp/@temp_brand05.png'" src="${benefit.host }${benefit.imgUrl }" alt="${benefit.shopName }"></p>
							<div class="info">
								<p class="brand"><c:out value='${benefit.shopName}'/></p>
								
								<c:if test="${!empty benefit.benefitName}">
									<p class="etc"><c:out value='${benefit.benefitName }'/></p>
								</c:if>
								<c:if test="${!empty benefit.mileageInfo}">
									<p class="etc"><c:out value='${benefit.mileageInfo }'/></p>
								</c:if>
								<c:if test="${!empty benefit.eventBeginDate && !empty benefit.eventEndDate}">
									<p class="date"> 
									<c:out value='${benefit.eventBeginDate }'/> ~ <c:out value='${benefit.eventEndDate }'/> </p>
								</c:if>
								<c:if test="${!empty benefit.eventValidDays}">
								<p class="etc" style="word-break:break-all;">이벤트 지정일 : <c:out value='${benefit.eventValidDays }'/></p>
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

<!-- 신용체크카드 상세(등록 전) : e -->
</body>
</html>