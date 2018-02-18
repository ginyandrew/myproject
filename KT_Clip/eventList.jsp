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
	
	function getEventList(theRecommend, count, theOs){
		
    	$.ajax({
            type: "GET",
            data:{recommend:theRecommend, pageNo:count, os:theOs},
            url: "eventListByPageNo.do",
            success: function(response) {
            	
            	var theEventList = response.eventList;
            	var items = [];
            	
            	$.each(theEventList, function(index, eachEvent){
            		var $content = 
						'<strong class="subject">'+ eachEvent.title +'</strong>'
						+'<p class="date">'+ eachEvent.beginDay + ' ~ '+ eachEvent.endDay +'</p>';
						
						if(eachEvent.evtIng == 'continue'){
							$content = $content +'<p class="state ing">진행중</p>'
						} else if (eachEvent.evtIng == 'over'){
							$content = $content +'<p class="state eng">종료</p>'
						}
						
            		 var $li =
            		'<li><a href="#" onclick="goDetail('+eachEvent.seq+');return false;">'
            			+'<div class="head">'
						+ $content
						+'</div>'
						+'<div class="thumb">'
						+'<img id="'+ eachEvent.seq + '" onerror="checkIfImgNull('+ eachEvent.seq + ');" src="'+ eachEvent.oldEvtImgUrl +'">'
						+'</div>'	
					+'</a></li>';
            		items.push($li);  
            	});
            	
            	if (!$('.evtList ul li')){
            		$('.evtList ul li').last().after(items.join(''));
            	} else {
            		$('.evtList ul').append(items.join(''));
            	}   

            	var theWidth = $('.evtList ul li a .thumb').first().width();
            	var theHeight = theWidth/2;
            	$('.evtList ul li a .thumb img').css("height", theHeight);
            }
        });
	}
	
	function checkIfImgNull(seq){
		 document.getElementById(seq).src = "${pageContext.request.contextPath}/cv2/images/common/evt_default.png";
	}
	
	function goDetail(idx) {
		   var url = "${pageContext.request.contextPath}/cv2/wv/common/eventDetail.do";
		   url += "?idx=" + idx;
		   var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('이벤트 제목')+"&url=" + url;
		   console.log("goDetail("+idx+") targetUrl : " + targetUrl);
		   $(location).attr("href",targetUrl);
	}	
	
	$(document).ready(function() {

		var count = 1;
		
		var theRecommend = $("#recommend").val();
		var totalPageNo = $("#totalPageNo").val();
		var os = $("#os").val();
		
		getEventList(theRecommend, count, os);
		
		$(window).scroll(function() {
			
			if($(window).scrollTop() == $(document).height() - $(window).height()) {	
		        if( count < totalPageNo ){
					count++
			        getEventList(theRecommend, count, os);
				} 
			}	        
		});     
		
		$(".menuTab li a").on('click' , function(){
			
			var recommend = $(this).attr('id');			 
			// post로 pageNo=0 에 recommend만 보낸다.
			$("#recommend").val(recommend);
			// recommend값, pageNo는 1로 설정해 출력
			$("#ajaxform").submit();
		}); 
	});
		 
		 
		    
	</script>
</head> 
<body class="bgGray">
<!-- 이벤트 목록 : s -->


<!-- menuTab : s -->
<form name="ajaxform" id="ajaxform" method="POST" action="eventList.do">
	<input type="hidden" id="recommend" name="recommend" value="<c:out value='${recommend }'/>">
	<input type="hidden" id="os" name="os" value="<c:out value='${os }'/>">
	<input type="hidden" id="totalPageNo" name="totalPageNo" value="<c:out value='${totalPageNo }'/>">
</form> 

<ul class="menuTab">
	<li <c:if test="${recommend eq 'REC'|| empty recommend }"> class="on" title="현재 페이지" </c:if>><a href="#" id="REC">추천</a></li>
	<li <c:if test="${recommend eq 'NOTREC'}"> class="on" title="현재 페이지" </c:if>><a href="#" id="NOTREC">전체</a></li>
</ul>
<!-- menuTab : e-->
<!-- evtList : s -->
<div class="evtList">
	<ul>
		<!-- evtList : e -->
		<!-- 이벤트 목록 : e -->
	</ul>
</div>

</body>
</html>