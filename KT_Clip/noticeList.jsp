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
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/common.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/const.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
		
	function loadNoticeList(count, theOs){
		
	    	$.ajax({
	            type: "GET",
	            data: {pageNo:count, os:theOs},
	            url: "noticeList/titles.do",
	            success: function(response) {
	            	
	            	var noticeAllList = response.noticeTitles;
	            	var items = [];
	            	$.each(noticeAllList, function(index, eachNotice){
            			
	            		var $noticeTitle = eachNotice.title;
            			
            			if (eachNotice.howLongPosted < 168){			// 등록된지 168시간 안에 것만 new를 띄운다.
            				$noticeTitle = $noticeTitle + '&nbsp;<img src="${pageContext.request.contextPath}/cv2/images/common/ico_new.png" alt="공지 ' + eachNotice.idx + '번">'
	            		}

            			var subject = '<strong class="subject">'+ $noticeTitle +'</strong>'; 
            			
	            		var li = 
	            			'<li><a href="#" onclick="goDetail('+eachNotice.idx+');return false;">'
	            			+ subject 
	            			+ '<p class="date">' + eachNotice.regTm+ '</p>'
	            			+ '</a></li>'
	            		
	            		
	            		items.push(li);
	            	});
	            	if (!$('.boardList ul li')){
	            		$('.boardList ul li').last().after(items.join(''));
	            	} else {
	            		$('.boardList ul').append(items.join(''));
	            	}
	            }
	        });
		}
		
		function goDetail(idx) {
			   var url = "${pageContext.request.contextPath}/cv2/wv/common/noticeDetail.do";
			   url += "?idx=" + idx;
			   var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('공지사항')+"&url=" + url;
			   console.log("goDetail("+idx+") targetUrl : " + targetUrl);
			   $(location).attr("href",targetUrl);
		}	
		
		$(document).ready(function(){			
			
			var noticePageNo = $("#noticePage").val();		 
			var os = $("#os").val();
			var count = 1;		
			
			loadNoticeList(count, os);
			
			$(window).scroll(function() {
				
				if($(window).scrollTop() == $(document).height() - $(window).height()) {
					if( count < noticePageNo ){
						count++
						loadNoticeList(count, os);						
					} 
			    }
			});	
		});
			
		
	</script>
	
</head> 
<body>
<!-- 공지사항 : s -->
<input type="hidden" id="noticePage" name="noticePage" value="<c:out value='${noticePage }'/>">
<input type="hidden" id="os" name="os" value="<c:out value='${os}'/>">
 
<!-- boardList : s -->
<div class="boardList">
	<ul>
	<!-- noticeList : e -->	
		 
	</ul>
</div>
<!-- boardList : e -->

<!-- 공지사항 : e -->
</body>
</html>