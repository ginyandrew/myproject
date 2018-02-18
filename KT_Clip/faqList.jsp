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
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/common.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
		
		$(document).ready(function() {
			$(".menuTab li a").on('click' , function(){
				console.log($(this).attr('id'));
				var faqPart = $(this).attr('id');
				 
				$("#faqPart").val(faqPart);
				$("#ajaxform").submit();
			});
		})
		
		$(function(){			
			$('.faqList').find('li > a').on('click', function(e){
				e.preventDefault();
				
				if($(this).closest('li').hasClass('open')){
					$(this).closest('li').removeClass('open');
				}
				else{
					$('.faqList').find('li').removeClass('open');
					$(this).closest('li').addClass('open');
				}
			});
		});
		
	</script>
</head> 
<body>
<!-- 자주 묻는 질문 : s -->

<!-- menuTab : s -->
<form name="ajaxform" id="ajaxform" method="POST" action="faqList.do">
	<input type="hidden" id="faqPart" name="faqPart" value="${faqPart }">
</form> 

<ul class="menuTab">
	<li <c:if test="${faqPart eq 'GNR'}"> class="on" title="이용안내" </c:if>><a href="#" id="GNR">이용안내</a></li>	 
	<li <c:if test="${faqPart eq 'BEF'}"> class="on" title="카드/혜택" </c:if>><a href="#" id="BEF">카드/혜택</a></li>
	<li <c:if test="${faqPart eq 'SVC'}"> class="on" title="주요 기능" </c:if>><a href="#" id="SVC">주요 기능</a></li>
	<li <c:if test="${faqPart eq 'ETC'}"> class="on" title="기타" </c:if>><a href="#" id="ETC">기타</a></li>
</ul>
<!-- menuTab : se-->

<!-- boardList : s -->
<div class="boardList faqList">
	<ul>
		<!-- 변수 part가 null이거나 GNR이면 이용안내, BEF 카드/혜택  / SVC 주요 기능 / UA 사용자인증 / ETC 기타 를 로드한다.-->
		<c:forEach var='faqlist' items='${faqList }'>			
			<li>
				<a href="#" id="<c:out value='${faqlist.idx}'/>">
					<strong class="subject"><c:out value='${faqlist.title}'/></strong>
				</a>
				<div class="faqCont">
					<pre style="white-space:pre-line;">
					<c:out value='${faqlist.answer}'/>
					</pre>
				</div>
			</li>			
		</c:forEach>
	</ul>
</div>
<!-- boardList : e -->

<!-- 자주 묻는 질문 : e -->
</body>
</html>