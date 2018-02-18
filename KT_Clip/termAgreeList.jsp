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
		$(function(){			

		});

	    // 개인정보 취급방침
        const PRIV = "<c:out value="${PRIV}"/>";
        
        // 서비스 이용약관
        const TERM = "<c:out value="${TERM}"/>";
        
        // 서버 Url
        const APPSVR = "<c:out value="${appSvr}"/>";
        
     	// 위치정보 이용동의(필수) 상세 URL
		const MERC = "<c:out value="${MERC}"/>";
		
        /* 각 항목의 체크박스 또는 Title 클릭시 상세 페이지 URL을 App 으로 호출 */
        function termsDetail(idx) {
            if ( idx == "TERM" ) {
                var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('서비스 이용약관')+"&url=" + APPSVR + TERM;
              	console.log("termsDetail(TERM) targetUrl : " + targetUrl);
                $(location).attr("href", targetUrl);
                
            } else if ( idx == "PRIV" ) {
                var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('개인정보 처리방침')+"&url=" + APPSVR + PRIV;
                console.log("termsDetail(PRIV) targetUrl : " + targetUrl);
                $(location).attr("href", targetUrl);
            
            } else if ( idx == "OPENSOURCE" ) {

            	var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('오픈소스 라이선스')+"&url=" + APPSVR + "/app_nw/terms/opensourceLisence.html";
                console.log("termsDetail(OPENSOURCE) targetUrl : " + targetUrl);
                $(location).attr("href", targetUrl);
                
            } else if ( idx == "ADV" ) {
				var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('위치기반서비스 이용약관')+"&url=" + APPSVR + MERC;
				console.log("termsDetail(ADV) targetUrl : " + targetUrl);
				$(location).attr("href",targetUrl);
            }
        }

    </script>
</head> 
<body>
<!-- 이용약관 : s -->

<h2 class="titH2">이용약관</h2>
<!-- boardList : s -->
<div class="boardList">
	<ul>
		<li>
			<a href="#" onclick="termsDetail('TERM');">
				<strong class="subject">서비스 이용약관</strong>
			</a>
		</li>
		<li>
			<a href="#" onclick="termsDetail('PRIV');">
				<strong class="subject">개인정보 처리방침</strong>
			</a>
		</li>
		<li>
			<a href="#" onclick="termsDetail('ADV');">
				<strong class="subject">위치기반서비스 이용약관</strong>
			</a>
		</li>
		<li>
			<a href="#" onclick="termsDetail('OPENSOURCE');">
				<strong class="subject">오픈소스 라이선스</strong>
			</a>
		</li>
	</ul>
</div>
<!-- boardList : e -->

<!-- 이용약관 : e -->
</body>
</html>