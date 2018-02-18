// 2.datepicker 수정 
// 3.전달 (post로 담아 plan에 저장처리.... ) 수정
	var cityimgPath = "../resources/img/city/";

    $(function() {
    	
   	    $( "#datepicker" ).datepicker({
   	    	 dateFormat: 'yy-mm-dd'
   	    });
    	
       
        //지도생성
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			mapOption = { 
		        center: new daum.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
		        level: 13 // 지도의 확대 레벨
			};

		// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
		var map = new daum.maps.Map(mapContainer, mapOption); 

		// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
		var bounds = new daum.maps.LatLngBounds();    

		//var defaultBounds = new daum.maps.LatLngBounds(new daum.maps.LatLng(32, 125),new daum.maps.LatLng(38.5, 131));
		
		
	   	//마커생성 위치들
	    $.ajax({
	    	type: "GET",
	        url: "/plan/planmakestatecode.tm",
	        dataType:"json",
	        success: function(data) {
	        	var stateCodeArr = data.stateCode; // "stateCode"가져오기
	        	$.each(stateCodeArr, function(index, item) {
	                $.ajax({ // 각 stateCode별 마커를 찍기위한 좌표 구하기
	                    type: "GET",
	                    url: "/plan/planmakecity.tm",
	                    data:{statecode:item.code},// statecode를 쿼리스트링값으로 가져옮
	                    dataType:"json",
	                    success: function(data) {
	                    	if(index<8) {
		                    	//console.log(data.city[0].name + ":" + data.city[0].lat +","+ data.city[0].lng);
		                    	// 마커 하나를 지도위에 표시합니다 
                    		    // 마커를 생성합니다
							    var marker = new daum.maps.Marker({
							        position: new daum.maps.LatLng(data.city[0].lat, data.city[0].lng),
							        clickable: true,
							        zIndex : 1
							    });
							    	                    	
							    // 마커가 지도 위에 표시되도록 설정합니다
							    marker.setMap(map);
							    marker.setClickable(true);
								 // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다
								
							    var iwContent = '<div style="padding:5px;">'+data.city[0].name+'</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
							        iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

							    // 인포윈도우를 생성합니다
							    var infowindow = new daum.maps.InfoWindow({
							        content : iwContent,
							        removable : iwRemoveable,
							        zIndex : 2
							    });

							    // 마커에 클릭이벤트를 등록합니다
							    daum.maps.event.addListener(marker, 'click', function() {
							    	//inforwindow.close();
							          // 마커 위에 인포윈도우를 표시합니다
							          infowindow.open(map, marker);
							     
							    });
							    
							    // 지도에 마커들이 모두 표시되도록 설정 
	        	            	bounds.extend(new daum.maps.LatLng(data.city[0].lat, data.city[0].lng));
	        	            	
	        	            	//console.log(index + "번째" + bounds);
	        	            	map.setBounds(bounds);
	                    	}
	                    }
	                })
	            })
	          }
	      })
	
  		  var selRoutes = [];
	      //첫번째 사이드 도시표시 부분
	      $.ajax({
	    	  type: "GET",
              url: "/plan/preplanmake.tm",
              dataType:"json",
              success: function(data) {
            	  
            	  $.each(data.cityData, function(index,item){
            	 	 // 존재하는 도시리스트를 출력
            		  var $cityhtml = $getCityList(index,item);
            	 	 
		              $("div#cityboxes").after().append($cityhtml);
            	 	  
		              // 추가된 리스트에 선택된 도시가 추가되는 이벤트 걸기 걸기
	            	  $("div#citybox-"+index+" span.glyphicon-plus").click(function() {
	            		  // 경로의 추가가 필요.좌표넣기
	            		  
	            			
	            			var $travelRoute = $getTravelRoute(item.city, map); 
		            		  $("#sortable").after().append($travelRoute);//selbox로 추가됨.
		            		  reline(map);
		            		  $( "#sortable" ).sortable({
		            	    	    placeholder: "ui-state-highlight",
		            	    	    update: function( ) {
		            	    	    	reline(map);
		            		        }
		            	    	});
		            		  $( "#sortable" ).disableSelection();
		            	    	
   			      	  })
            	 })
              }
	      })
	      
	      $("a#save").click(function(e) {
	    		e.preventDefault(); 
	    		insertPlan();
	    	  	
	      })
	      
	})// 페이지 로딩후 실행되야 되는 function부분..
	function $getCityList(index, item) { //왼쪽에 리스트로 표시
    	var imgName = item.fileName;
  	 	var nameEng = imgName.replace(".jpg", "").replace(imgName[0], imgName[0].toUpperCase());
  	 	var nameMain = imgName.replace(".jpg", "").substr(-4);
  	 	
  	 	if(nameMain != "main") {
	  	 	$div = $("<div id='citybox-"+ index +"'"+"class='citybox row'></div>")
	  	 		.append($("<div class='city'></div>")
	  	 			.append($("<img class='cityimg' src='"+cityimgPath + imgName+"' />")) 
	  	 			).append($("<div class='cityname'></div>")
			  	 		.append($("<div>"+item.city.name +"</div>"))
			  	 		.append($("<div>"+nameEng+"</div>"))
	  	 			).append($("<span class='glyphicon glyphicon-plus'></span>"));
		  	  
			return $div;
    	}
    }
    
    var count = 0;
	function $getTravelRoute(city,map) { 
    	var $div = $('<li id="selbox-'+ (count++) +'" class="selbox" draggable="true" data-value="1" data-city="'+city.no +'" lat="'+city.lat+'" lng="'+city.lng+'"></li>');  
	  	$div.append($('<span class="glyphicon glyphicon-remove stayremove">'+city.name+'</span>').css("cursor", "pointer").click(function(){
	  		$(this).parents("li.selbox").remove();
	  		reline(map);
	  	}))
  		 	.append($('<button id="minus" class="btn btn-default btn-xs">-</button>').click(function(event){
  		 		daysTag = $(this).parents("li.selbox").find("span.daysnum");
  		 		if(parseInt(daysTag.text()) > 1) {
	  		 		staydays = parseInt(daysTag.text())-1;
	  		 		$(this).parents("li.selbox").attr("data-value",staydays);
	  		 		daysTag.text(staydays);
  		 		}
  		 		
  		 	}))
	  		.append($('<p class="staydays"><span class="daysnum">1</span>일</p>'))
	  		.append($('<input type="hidden" name="daynum" data-value="1">'))
	  		.append($('<button id="plus" class="btn btn-default btn-xs">+</button>').click(function(event){
	  			daysTag = $(this).parents("li.selbox").find("span.daysnum");
	  			staydays = parseInt(daysTag.text())+1;
	  			$(this).parents("li.selbox").attr("data-value",staydays);
	  			
  		 		daysTag.text(staydays);
  		 	}));
			
		return $div;
    }
	
	function reline(map) {
		selRoutes= [];
		
  		var $selboxes = $(".selbox");
    	$($("path")).remove();
    	$.each($selboxes, function(index, item){
    		var attrlat = parseFloat($(item).attr("lat"));
    		var attrlng = parseFloat($(item).attr("lng"));
    		selRoutes.push(new daum.maps.LatLng(attrlat, attrlng));
    	})
  							
    	console.log(selRoutes)
		polyline = new daum.maps.Polyline({
		    path: selRoutes, // 선을 구성하는 좌표배열 입니다
		    endArrows: true,
		    strokeWeight: 3, // 선의 두께 입니다
		    strokeColor: '#FF0000', // 선의 색깔입니다
		    strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
		    strokeStyle: 'solid' // 선의 스타일입니다
		});
		polyline.setMap(map);
					    		
		//svg로 화삺표를 그려야 될듯!!// $($("path")[1]).remove() // path 라인경로및 d가 코드값임.. 중복되서 경로를 표시하므로 중간 삭제시 전에것까지 삭제해야 한다.유의...
		//<svg>
		//	 <path id="lineAB" d="M 100 350 l 150 -300" stroke="red" stroke-width="3" fill="none" />
		// M은 시작점 (0,0 기준에서 이동) // l 은 시작점 기준으로 좌표값을 더하여 선을 그린다. l의 처음 선언 이후 한 쌍의 값들은 l의 영향에 해당하게 된다.
		// 자세한 것은  http://www.w3schools.com/svg/svg_path.asp 참조
	}
	
	function insertPlan() {
		var userno = $("input[name='userno']").val();
		var leaveDate = $("input[name='date']").val().replace(/-/g,"") + "000000";
		var title = $("input[name='titles']").val();
		var theme = 1 // $("select[name='theme']").val();
		
		var boxarr = $(".selbox");
		var cityNoarr = [];
		var periodarr = [];
		var period = 0;
		$.each(boxarr, function(index, item){
			cityNoarr.push(parseInt($(item).attr("data-city")));
			periodarr.push(parseInt($(item).attr("data-value")));
			period += parseInt($(item).attr("data-value"));
		})
		var cityNo = cityNoarr.join(",");
		var periodss = periodarr.join(",");
		
		 var $form = $("<form id='makePlan' method='post' action='/plan/NewPlan.tm'></form>")
		 	.append($("<input type='hidden' name='leaveDate' />").val(leaveDate))
		 	.append($("<input type='hidden' name='userNo' />").val(userno)) 
		 	//.append($("<input type='hidden' name='userNo' />").val(101))
		 	.append($("<input type='hidden' name='title' />").val(title))
		 	.append($("<input type='hidden' name='themeCode' />").val(theme))
		 	.append($("<input type='hidden' name='period' />").val(period))
		 	.append($("<input type='hidden' name='cityNo' />").val(cityNo))
		 	.append($("<input type='hidden' name='periodss' />").val(periodss));
		 $form.submit();
		 
				
		 /* $.ajax({
	    	type: "POST",
            url: "/plan/NewPlan.tm",
            data : {
    			leaveDate : leaveDate,
    			title : title,
    			themeCode : theme,
    			period : period,
    			
            	cityNo : cityNo,
            	periodss : periodss,
            	
            	userNo : userno
            },
            dataType:"json",
            success: function(data) {
            	
            }
		})  */
	}