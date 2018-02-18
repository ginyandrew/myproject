<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
	<meta http-equiv="Content-Type" content="text/html" charset="utf-8" /> 
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"> 
	<meta name="format-detection" content="telephone=no">
	<title>CLiP</title>
	<script type="text/javascript"
			 src="http://api.ktgis.com:10080/v3m/olleh/mapAPI.js?key=T2xsZWhNYXBJTjAwNjQ6bVcyMktsMXZlUg==&module=Map,Geocoder">
	</script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/common.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/swiper.min.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/swiper.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/common.js"></script>
	
	<script type="text/javascript">
	</script>
	<script>
	// 하나의 마커만 찍는 곳. (소호 y/n 모두 포함)
	var branch = decodeURIComponent("<c:out value='${branch}'/>");
	var branchObj = JSON.parse(branch);
	 
	var lang = "<c:out value='${longitude}'/>";
	var lat = "<c:out value='${latitude}'/>";
	var shopName = "<c:out value='${shopName}'/>";
	var menuYn  = "<c:out value='${menuYn}'/>";
	
	var defaultIcon = {
 			  url:'${pageContext.request.contextPath}/cv2/images/common/map_ico_df.png', 
 			  size: new olleh.maps.Size(18, 25), 
 			  anchor: new olleh.maps.Point(9, 25)
 	};
 	var bkIcon = {
 			  url:'${pageContext.request.contextPath}/cv2/images/common/map_ico_sel.png',
 			  size: new olleh.maps.Size(28, 40), 
 			  anchor: new olleh.maps.Point(14, 40)
 	};
 	
 	$(document).ready(function() {
 		var myCoord = new olleh.maps.LatLng(lat, lang);		
		
		var mapOptions = { 
			center : myCoord,
			zoom :9, 
			disableDefaultUI:true,
			mapTypeId : "ROADMAP" 
		};   
		
	 	map = new olleh.maps.Map(document.getElementById("canvas_map"), mapOptions);   
	 	
	 	var originHeight = 0;
	 	
	 	if(getBrowser().indexOf("chrome") != -1) {
	 		originHeight = 910;
	 	} else {
	 		originHeight = 1107;
	 	}

	 	var aCoord = branchObj.coord;
	 	var umtkStartPoint = aCoord.indexOf("(");
		var umtkEndPoint = aCoord.indexOf(")");
		var umtkCoords = aCoord.substring(umtkStartPoint+1, umtkEndPoint).split("+");
		var theCoord = new olleh.maps.UTMK(umtkCoords[0], umtkCoords[1]);

		var theDistance = Math.round((theCoord).distanceTo(myCoord));
		if (theDistance <1000 ){
			branchObj.distance = theDistance + " m"
		} else if (theDistance >= 1000){
			branchObj.distance = (theDistance/1000).toFixed(1) + " k";
		}
		// branchObj.distance = Math.round((theCoord).distanceTo(myCoord));
		
		// 마커를 찍고 info를 담는다.
       var marker = new olleh.maps.overlay.Marker({
			position: theCoord,    
			map: map,    
			size: new olleh.maps.Size(21, 30), 
			anchor: new olleh.maps.Point(10, 30),
			icon: defaultIcon,
			cursor:"pointer"
	 	});
		map.setCenter(theCoord); 
	
		if (menuYn == 'y'){
		   	getSohoShopInfo();
	    } else {
	       	$("#distance").text(branchObj.distance); 
			getShopInfo(branchObj);
			marker.setIcon(bkIcon);
	    }	
 	})
 	
	
 	
 	function checkIfNull(code){
			
			if(code == 'menu'){
				$("#sohoMenu").replaceWith('<ul><li>해당 매장의 메뉴정보가 없습니다.</li></ul>')
			}
			if(code == 'info'){
				$("#sohoInfo").replaceWith('<ul><li>해당 매장의 정보가 없습니다.</li></ul>')
			}
		}
 	
 	
 	function getSohoShopInfo(){
		$("#infoBox").empty();
			var infoUrl  = "<c:out value='${info_url}'/>";
			var menuUrl  = "<c:out value='${menu_url}'/>";
	
			var sohoMenu = 'sohoMenu'
			var shopInfoAndMenu = 
			  '<iframe id="sohoMenu" src="'+ infoUrl +'" onerror="checkIfNull(info);"style="overflow:hidden;width:100%;height:500px"></iframe>'
			+ '<br/>'
			+ '<strong class="titSubject">메뉴</strong>'
			+ '<iframe id="sohoInfo" src="'+ menuUrl + '" onerror="checkIfNull(menu);"style="overflow:hidden;width:100%;height:1000px"></iframe>';	
		
		$("#infoBox").append(shopInfoAndMenu);
	 
 	}
 	
 	function getShopInfo(aBranch){

 		if(aBranch.branchName == null || aBranch.branchName == 'null' || aBranch.branchName == ''){
			$("#shopName").html(shopName);
		} else {
			$("#shopName").html(shopName + "&nbsp;"+ aBranch.branchName);
		}
		 
		if(aBranch.addr == null ||aBranch.addr == 'null' || aBranch.addr ==''){
			$("#addr").text("해당 매장의 등록된 주소정보가 없습니다.");
		} else {
			aBranch.addr = aBranch.addr.replace(/\+/g,' ');
			$("#addr").text(aBranch.addr);
		}
		
		if(aBranch.tel == null || aBranch.tel == 'null' || aBranch.tel == ''){
			$("#contact").append('<li class="tel">해당 매장의 등록된 전화번호가 없습니다.</li>'); 
		} else {
			
			var eachTel= "";
			var tels = aBranch.tel;
			
			var items =[];
			var telItems=[];
			items = tels.split(";");
			for (var i=0; i< items.length ; i++){
				telItems[i] = '<li class="tel"><a href="tel:'+ items[i] +'"> 전화: '+ items[i] + '</a></li>'
			}
			$("#contact").append(telItems.join(""));
		} 
	}
 	
	</script>
</head>
<body>
<div class="boxMap">
	<div id="canvas_map" style="height:100%"></div>
</div>
<div class="boxShow infoStore" id="infoBox">
	<strong class="titSubject" id="shopName"></strong>
	
	<ul class="listStyle02">
	<li id="addr"><!-- addr info --></li>
	</ul>
	<br/>
	<ul class="listStyle02">
	<li id="contact"> <!-- tel info --></li>
	</ul>
	
	<p class="distance"><span id="distance"></span></p>
</div>
</body>
</html>