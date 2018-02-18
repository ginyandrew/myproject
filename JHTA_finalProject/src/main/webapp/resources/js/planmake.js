
	var queryString = window.location.search;
    var planno = parseInt(queryString.replace("?no=", ""));
    var destimgPath = "../resources/img/dest/";
	
	var bounds = new daum.maps.LatLngBounds();  
	
	var strBeforeJson = null;
	var plantoJson = null;
	
    $(function() {
    	strBeforeJson = $("[name='planTest']").val();
    	plantoJson = $.parseJSON(strBeforeJson);
    	var daycount = 0;
		var planNo = $("input[name=planNo]").val();
		var plan = null;
		var week = new Array('일', '월', '화', '수', '목', '금', '토');
		
		//지도생성
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		mapOption = { 
	        center: new daum.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
	        level: 6 // 지도의 확대 레벨
		};
		// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
		var map = new daum.maps.Map(mapContainer, mapOption); 
		
		// header부분 이벤트
		$("a#modify").click(function() {
			ajaxgetplan();
		})
		
		$("a#saved").click(function() {
			var themeno = $("div#seltheme button.active").attr("data-value");
			var isCompleted = $("div#published button.active").attr("data-value"); 
			ajaxsavemodify(themeno,isCompleted);
			
		})
		
		$("div#seltheme button").click(function() {
			$("div#seltheme button").removeClass("active");
			$(this).addClass("active");
		})
		
		$("div#published button").click(function() {
			$("div#published button").removeClass("active");
			$(this).addClass("active");
		})
		
		// context(body) 부분 이벤트
		$("button#selesase").click(function(){
			var dayNo = $("#seldestday span").text().substr(3,1);
			var $daySellist = $(this).parents("div.selsidebar").find("ul.sortbox[data-viewlevel="+dayNo+"]");
			
			$($("path")).remove();// 경로를 없애요.
			
    		removedest($daySellist);
    		$daySellist.empty();
		})
		
		$("#thirdsidebar-close").click(function(){
    		$(this).parents("div.thirdsidebar").hide();
    	})
    	
    	$("#citybutton button").click(function(e){
    		e.preventDefault();
    		$("div#destboxes").empty();
    		mapclears(); // 지도의 경로 마커 제거
    		
    		var cateNum = $(this).val();
    		ajaxdestlist(cateNum, map);
    		var cityname = $(this).text();
    		var cityno = $(this).val();
    		$("span#cityname").text(cityname);
    		$("span#cityname").attr("data-cityno", cityno);
    		
    	})
    	
    	$("#catelist button").click(function(e){
    		e.preventDefault();
    		$("div#destboxes").empty();
    		mapclears();
    		    		
    		var cateNum = $(this).val();
    		var cityno = $("span#cityname").attr("data-cityno");
    		ajaxcatelist(cateNum, map, cityno);
    		//bounds = new daum.maps.LatLngBounds(); 
    		mapsellist(map, bounds);
    	})
    	
    	$("span.glyphicon.destremove").click(function(){
    		var $thisPatents = $(this).parents("div.seldestbox");
    		removedest($thisPatents);
    		mapsellist(map, bounds);
    	})
    	
    	$("span.glyphicon.dayremove").click(function(){
    		var daylen = $("div.daybox").length;
    		$(this).parents("div.daybox").remove();
			ajaxremoveday(daylen);
			return false;
    	})

    	var defaultBounds = new daum.maps.LatLngBounds(new daum.maps.LatLng(32, 125),new daum.maps.LatLng(38.5, 131));
		
		$("div.daybox").click(function(){
			mapclears();
			$("div#destboxes").empty();
			
			var dayno =$(this).attr("id").substr(-1);
			if(dayno ==  parseInt($($("ul.sortbox").get(dayno-1)).attr("data-viewlevel"))){
				$("ul.sortbox").hide()
				$("ul[data-viewlevel='"+dayno+"']").show()
			}//로딩되어 선택한 기존 day당 detail이 출력되는 부분
			
			$("div.thirdsidebar").show();
			
			var selcityno = parseInt($(this).find('p.cityno').attr('data-cityno'));
			$("span#cityname").attr("data-cityno", selcityno).text($(this).find('p.cityno').text());
			$("#seldestday").html("<span>"+$(this).find("span.daynumber").text()+ "|" +$(this).find("span.datelisttext").text()+"</span>");
			ajaxdestlist(selcityno, map, dayno);// DB저장데이터 가져와서 뿌리기
			mapsellist(map, bounds);			
			//daycount++;
			
			
		})
		$("div#daybox-1").trigger("click");
		   
	    // day추가버튼 시 효과
		$('#addDay').click(function(){
			var daylen = $("div.daybox").length+1;
			var $leaveDateToDate = $("input[name=leaveDateToDate]").val();
			var date = new Date($leaveDateToDate + (24*60*60*1000)*(daylen-1));
			var dayname = date.getDay();
     	    var month = date.getMonth()+1;
     	    var day = date.getDate();
     	    // 실제 추가되는 부분
     	    
		  	var $div = $('<div id="daybox-'+ daylen +'" class="panel-body daybox"></div>')
		  		.append($('<div class="pull-left"></div>')
	    			.append($('<span class="daynumber">DAY'+daylen+'</p>'))
	    			.append($('<span class="datelisttext">'+ week[day]+'</p>'))
	    			.append($("<span class='glyphicon glyphicon-remove dayremove'></span>")
	    		).append($('<div class="pull-left"></div>')
	    			.append($('<p class="cityno" data-cityno=""></p>'))
	 				.click(function(){
	 					$(this).parents("div.daybox").remove();
	 					ajaxremoveday(daylen);
	 					return false;
	 				})	
	    		).click(function(){
	    			var selcityno = parseInt($(this).find('p.cityno').attr('data-cityno'));
	    			if(selcityno !=null) {
		    			$("div#destboxes").empty();
		    			//$("#map div:nth-child(2) div div:nth-child(5) div").empty()
			  			ajaxdestlist(selcityno, map, day);
		    		}
		  		})
		  	)
	    	$("#dayBoxes").after().append($div);
	    	
	    	ajaxinsertday(daylen);
	    	
	    });

    });
    
    function mapclears() { 
    	$($("path")).remove();
		$("#map > div:nth-child(2) > div > div:nth-child(5)").empty() //아이디를 부여해야 하나?
		
    }
    function ajaxgetplan(){
    	
    	$.ajax({
	   		type: "GET",
	           url: "/plan/plandata.tm",
	              		
	           data:{
	        	   no:planno
	           },
	           dataType:"json",
	           success: function(data) {
	        	   $("div#seltheme button").removeClass("active");
	        	   $("div#seltheme button[data-value="+data.themaCode+"]").addClass("active");
	           }
    	})
    }
    
    function ajaxsavemodify(themeno,isCompleted) {
    	var leaveDate = null; // 수정필요
    	var title = null; // 수정필요
    	$.ajax({
	   		type: "GET",
	           url: "/plan/updateplan.tm",
	              		
	           data:{
	        	   no:planno,
	        	   isCompleted:isCompleted,
	        	   //leaveDate: leaveDate,
	        	   //title: title,
	        	   themeCode:themeno
	        	
	           },
	           dataType:"json",
	           success: function(data) {
	        	   if(isCompleted == "T") {
	        		   window.location.href= "/plan/detail.tm?no=" + planno;
	        	   } else {
	        		   window.location.href= "/main.tm";
	        	   }
	        	}
    	})
    }
    
    function ajaxinsertday(day) {
    	
    	$.ajax({
	   		type: "GET",
	           url: "/plan/insertday.tm",
	           //(PLAN_SEQ.NEXTVAL, #{day}, #{planNo}, #{dest.no}, #{numbering})
	   		
	           data:{
	        	   planNo:planno,
	        	   day:day,
	        	   //이하 임시값... 추후 수정요구
	        	   "dest.no": 1,// 모달처리?
	        	   numbering : 0
	        	},
	           dataType:"json",
	           success: function(data) {
	        	   //console.log(data);
	        	 
	        	}
    	})
    	
    }
    	function ajaxremoveday(day) {
    		$.ajax({
		   		type: "GET",
		           url: "/plan/removeday.tm",
		           data:{
		        	   planNo:planno,
		        	   day:day
		           },
		           dataType:"json",
		           success: function(data) {
		        
		           }
	    	})
    	}
    	
	   function ajaxcatelist(cateNum, map, cityno) {
	    	$.ajax({
		   		type: "GET",
		           url: "/plan/catedata.tm",
		           data:{cateno:cateNum},
		           dataType:"json",
		           success: function(data) {
		        	   //console.log(data);
		        	   var arr = data.destlist;
		        	   var seldestarr = [];
		        	   $.each(arr, function(index,item){
		        		   if(cityno == item.dest.city.no){
		        			   seldestarr.push(item);
		        		   }
		        	 });
		        	   
		        	   
		        	   remarking(seldestarr,map);
		           }
	    	})
    	}
	   function ajaxdestlist(selcityno, map, day){
		   $.ajax({
	   		type: "GET",
	           url: "/plan/destlist.tm",
	           data:{cityno:selcityno},
	           dataType:"json",
	           success: function(data) {   
	           		console.log(data)
	           		var destListarr = data.destList;
	           		remarking(destListarr,map);
	           		mapselLine(map, day);
	           		
	           }
		   })
	   }
	   
	   function mapsellist(map, bounds) {
		  
		   bounds = new daum.maps.LatLngBounds(); 
		   var day = $("div#seldestday span").text().substr(3,1);
		   var arr = $("ul#sortable[data-viewlevel='"+day+"'] div.seldestbox");
		   $.each(arr, function(index, item) {
			   
			   var dest = {
					   no : index+1,
					   name : $(item).find("div.destname div").text(),
					   lat : $(item).attr("lat"),
					   lng : $(item).attr("lng")					   
			   }
			   mapNumMarkerInfowindow(map, dest, bounds);
		   })
		   mapselLine(map, day);
		   bounds = new daum.maps.LatLngBounds(); 
	   }
	   
	   function remarking(arr,map) {
		   $.each(arr, function(index, item){
      			var imgName = item.fileName;
		  	 	
			  	 	$div = $("<div id='destbox-"+ index +"'class='destbox'></div>")
			  	 	
			  	 		.append($("<div class='city'></div>")
			  	 			.append($("<img class='cityimg' src='"+destimgPath + imgName+"' />")) 
			  	 			).append($("<div class='destname' data-dest='"+item.dest.no+"'></div>")
					  	 		.append($("<div>"+item.dest.name +"</div>"))
					  	 		
			  	 			).append($("<span class='glyphicon glyphicon-plus'></span>")
			  	 				.click(function(){
			  	 					addwishdestlist(map, item, imgName);
			  	 					//var destbox = $(this).parents("div.destbox");
			  	 					console.log(item)
			  	 					ajaxinsertdest(item.dest);
			  	 					mapsellist(map, bounds);
			  	 				})		
			  	 			).click(function(){
			  	 				//상세정보팝업. // 마커 변경? 중심이동?
			  	 			});
				  	  
			  	 	$("div#destboxes").after().append($div);
			  	 	
			  	 	mapMarkerInfowindow(map, item.dest, bounds);
      		})
		
      		bounds = new daum.maps.LatLngBounds();  
	   }
	function mapNumMarkerInfowindow(map, dest, bounds) {
		var marker = new daum.maps.Marker({
	        position: new daum.maps.LatLng(dest.lat, dest.lng),
	        clickable: true,
	        zIndex : 2
	    });
	    	                    	
	    // 마커가 지도 위에 표시되도록 설정합니다
	    marker.setMap(map);
	    var iwContent = '<div style="padding:5px;"><span class="numbering">'+dest.no+'</span>'+dest.name+'</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	    iwPosition = new daum.maps.LatLng(dest.lat, dest.lng), //인포윈도우 표시 위치입니다
	    iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
	
	 // 인포윈도우를 생성합니다
	    var infowindow = new daum.maps.InfoWindow({
	    	position : iwPosition, 
	        content : iwContent,
	        removable : iwRemoveable,
	        zIndex : 3
	    });
	
	    infowindow.open(map, marker);
	        
		bounds.extend(new daum.maps.LatLng(dest.lat, dest.lng));
	    
		map.setBounds(bounds);
	}
	
	// 인포윈도우를 여는 만드는 함수입니다 
	function openInfoWindow(map, marker, infowindow) {
	    return function() {
	        infowindow.open(map, marker);
	    };
	}

	// 인포윈도우를 닫는 클로저를 만드는 함수입니다 
	function closeInfoWindow(infowindow) {
	    return function() {
	        infowindow.close();
	    };
	}
	
	function mapMarkerInfowindow(map, dest, bounds) {
		var position = new daum.maps.LatLng(dest.lat, dest.lng)
		var marker = new daum.maps.Marker({
	        position: position,
	        clickable: true,
	        zIndex : 1
	    });
	    	                    	
	    // 마커가 지도 위에 표시되도록 설정합니다
	    marker.setMap(map);
	    var iwContent = '<div style="padding:5px;">'+dest.name+'</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	    iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
	
	 // 인포윈도우를 생성합니다
	    var infowindow = new daum.maps.InfoWindow({
	        content : iwContent,
	        removable : iwRemoveable,
	        zIndex : 2
	    });
	
	    // 마커에 클릭이벤트를 등록합니다
	    daum.maps.event.addListener(marker, 'click', function() {
	    	$("")
	    });
	    
	    daum.maps.event.addListener(marker, 'mouseover', openInfoWindow(map, marker, infowindow));
	    daum.maps.event.addListener(marker, 'mouseout', closeInfoWindow(infowindow));
	    
		bounds.extend(new daum.maps.LatLng(dest.lat, dest.lng));
	    
		map.setBounds(bounds);
	}
		 	
    var seldestRoutes = [];
    function addwishdestlist(map, item, imgName) {
    	var seldayno = $("div#seldestday span").text().replace("DAY","").substr(0,1);
    	var wishcount = $("ul[data-viewlevel="+seldayno+"] div.seldestbox").length;
    	$div = $("<div id='seldestbox-"+ wishcount +"'class='seldestbox' lat='"+item.dest.lat+"' lng='"+item.dest.lng+"'></div>")
    		.append($("<input type='hidden' class='selbudget' value=''/><input type='hidden' class='selmemo' value=''/>"))
	 		.append($("<div class='city'></div>")
	 			.append($("<img class='cityimg' src='"+destimgPath + imgName+"' />")) 
	 			).append($("<div class='destname' data-dest='"+item.dest.no+"'></div>")
	  	 		.append($("<div>"+item.dest.name +"</div>"))
	  	 		
	 			).append($("<span class='glyphicon glyphicon-remove destremove'></span>")
	 				.click(function(){
	 					var $thisParents = $(this).parents("div.seldestbox")
	 					removedest($thisParents);
	 					
	 				})		
	 			).click(function(){
	 				//상세페이지 띄우는 부분
	 				$('#detaildest').show();
			    	var destno = $('#seldestbox-1 .destno').attr('id').replace("destno-","");
			    	$.ajax({
			    		type: "GET",
		                url: "/plan/detaildest.tm",
		                data:{destno:destno},
		                dataType:"json",
		                success: function(data) {
		                	//console.log(data.dest);
		                	$('#detaildestname').text(data.dest.destVo.name);
		                	$('#destimg').attr("src", "../resources/img/seoul/01tour_spot/" + data.dest.imgVo.fileName);
		                	var destInfo = data.dest.cateCode.name;
		                	                	
		                	$('#detailDestInfo').html(destInfo);
		                	$('#detaildestname').text(data.dest.destVo.name);
		                }
			    	})
	 			});
  	  	var dayno = $("div#seldestday span").text().substr(3,1);
	 	$("ul#sortable[data-viewlevel='"+dayno+"']").after().append($div);
	 	mapselLine(map, dayno);
	}
    
    function mapselLine(map, day) {
    	seldestRoutes = [];
	 	var $selboxes = $("ul#sortable[data-viewlevel='"+day+"'] div.seldestbox");
    	$($("path")).remove();
    	$.each($selboxes, function(index, item){
    		var attrlat = parseFloat($(item).attr("lat"));
    		var attrlng = parseFloat($(item).attr("lng"));
    		
    		seldestRoutes.push(new daum.maps.LatLng(attrlat, attrlng));
    	})
    	
    	polyline = new daum.maps.Polyline({
		    path: seldestRoutes, // 선을 구성하는 좌표배열 입니다
		    endArrows: true,
		    strokeWeight: 3, // 선의 두께 입니다
		    strokeColor: '#FF0000', // 선의 색깔입니다
		    strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
		    strokeStyle: 'solid' // 선의 스타일입니다
		});
    	polyline.setMap(map);
    }
    
    function ajaxinsertdest(dest) {
    	
    	var seldayno = $("div#seldestday span").text().replace("DAY","").substr(0,1);
		var len = $("ul#sortable[data-viewlevel="+seldayno+"] div.seldestbox").length;
		var selboxarr = $("ul#sortable[data-viewlevel="+seldayno+"] div.seldestbox");
		$.each(selboxarr, function(index, item){
			//console.log(item)//item.dest.no
			if(dest.no == parseInt($(item).find("div.destname").attr("data-dest")) && len == index+1) {
				$.ajax({
					type: "GET",
					url: "/plan/insertdest.tm",
					data:{
						planNo : planno,
						day: seldayno,
						numbering : index+1,
						"dest.no" : dest.no,
						budget : 0,
						memo : " "
					},
					dataType:"json",
					success: function(data) {
						
					}
				})
				return;
			}
		})
    }
    
    function ajaxupdatedest(numbering){
    	var seldayno = $("div#seldestday span").text().replace("DAY","").substr(0,1);
    	var arr = $("ul[data-viewlevel="+seldayno+"] div.seldestbox")
    	var numarr = []
    	$.each(arr, function(index, item){
    		if(numbering<index+1) {
    			numarr.push(index+1);
    		}
    	})
    	var numarrstr = numarr.join(",");
    	if(numarrstr.length > 0)
    	$.ajax({
    			type: "GET",
    			url: "/plan/updatedestlist.tm",
    			data:{
    				planNo : planno,
    				day: seldayno,
    				numarr : numarrstr    				
    			},
    			dataType:"json",
    			success: function(data) {
    				
    			}
    		})
    }
    
    function removedest($thisParents) {
    	var dayno = $("div#seldestday span").text().substr(3,1);
    	var arr = $("ul#sortable[data-viewlevel="+dayno+"] div.seldestbox");
 		$.each($thisParents,function(index, item){
 			var thisNum = $thisParents.attr("id").substr(-1);
 			$.each(arr, function(index, item){
 				var itemNum = $(item).attr("id").substr(-1);
 				if($thisParents.length == 1 && thisNum == itemNum) {
 		 			ajaxremovedest(index);
 		 			$thisParents.remove();
 				}
 				
 			})
 		})
    }
    
    function ajaxremovedest(numbering){
    	//console.log(numbering)
    	var seldayno = $("div#seldestday span").text().replace("DAY","").substr(0,1);
    	
    	$.ajax({
			type: "GET",
			url: "/plan/removedest.tm",
			data:{
				planNo : planno,
				day: seldayno,
				numbering : numbering+1
			},
			dataType:"json",
			success: function(data) {
				ajaxupdatedest(numbering)
			}
		})
    }
    
    function saveplanmake() {
		// hidden form을 이용한 뎅터 전송    	 
    	$("#save").submit();
    }
    
    function datelist(date) {
    	date.setDate(date.getDate()+1)
    	var month = date.getMonth()+1;
    	var day = date.getDate();
    	return month+"."+day;
    }