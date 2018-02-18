<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/infocity.css" />
<!-- 구글 차트 library -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">


$(function(){
	
	  google.charts.load('current', {packages: ['line', 'corechart']});
	  google.charts.setOnLoadCallback(drawChart);
	  
	//////////////////////////////////////환율 정보
	$.ajax({
		type: "GET",
		url:"http://api.fixer.io/latest?base=USD&callback=?",	
		dataType:"jsonp",		
		success: function(data){
						
			//alert(data.rates.KRW);
			//미국 1달러의 한화 금액을 input에 넣기
			$("#krCurrency").val(data.rates.KRW);
			
			
			//(1)왼쪽 외화 변경때
			$("input#frCurrency").unbind().on("keyup",function(event){
				var fCurrency = $(this).val();
				var kCurrency = fCurrency*data.rates.KRW;
				//소수점 없애기 
				var amt = parseFloat(kCurrency);
                $("#krCurrency").val(amt.toFixed(1));
				
				//$("#krCurrency").val(kCurrency);
			})
			
			//(2)오른쪽 한화 변경때
			$("input#krCurrency").unbind().on("keyup",function(event){
				var kCurrency = $(this).val();
				var fCurrency = kCurrency/(data.rates.KRW);
				
				var amt = parseFloat(fCurrency);
                $("#frCurrency").val(amt.toFixed(2));
                
				//$("#frCurrency").val(fCurrency);
			});
			
			
			$("#allFrCurrency").unbind().on("change",function(event){
				//다른 외화로 변경할 때
				var theFrCurrency= $('#allFrCurrency option:selected').val();
				$("#frCurrency").val(1);
				
				$.ajax({
					type: "GET",
					url:"http://api.fixer.io/latest?base="+theFrCurrency+"&callback=?",	
					dataType:"jsonp",		
					success: function(data){
						$("#krCurrency").val(data.rates.KRW);
						
						//(1)왼쪽 외화 변경때
						$("input#frCurrency").unbind().on("keyup",function(event){
							var fCurrency = $(this).val();
							var kCurrency = fCurrency*data.rates.KRW;
							
							var amt = parseFloat(kCurrency);
			                $("#krCurrency").val(amt.toFixed(1));
							//$("#krCurrency").val(kCurrency);
						})
						
						//(2)오른쪽 한화 변경때
						$("input#krCurrency").unbind().on("keyup",function(event){
							var kCurrency = $(this).val();
							var fCurrency = kCurrency/(data.rates.KRW);
							
							var amt = parseFloat(fCurrency);
			                $("#frCurrency").val(amt.toFixed(2));
							//$("#frCurrency").val(fCurrency);
						});
					} 				
				})
				
			});
			
		} 
	})
	////////////////////////////////////// 해당 도시 전년도 평균 기온 (날씨)
	var d = new Date();
	var theLastYear = d.getFullYear() -1;		//현재 년도 기준 작년도
	var theCityLng = $("#cityLng").val();					//해당 도시의 위도,경도
	var theCityLat = $("#cityLat").val();
	function drawChart() {
		
		$.ajax({
	        url: "jsonMonthTemp.tm",
	        type: "GET",
	        data: {lastYear:theLastYear,cityLng:theCityLng,cityLat:theCityLat},	
	        dataType: "jsonp",
	        success: function (data) {
	            var weatherArr = data.weatherData;
	            	            
	            var chartDiv = document.getElementById('chart_div');
	            var data = new google.visualization.DataTable();
	            data.addColumn('number', '월 (Month)');
	            data.addColumn('number', "온도 (Temperature)");
	            data.addColumn('number', "습도 (Humidity)");
	      
	            data.addRows(weatherArr);
	            var materialOptions = {
		             chart: {
		             	title: '올해 기준 이전 해의 평균 온도와 평균 습도'
		             },
		             series: {
			             // Gives each series an axis name that matches the Y-axis below.
			             0: {axis: '온도'},
			             1: {axis: '습도'}
		             },
		             axes: {
			             // Adds labels to each axis; they don't have to match the axis names.
			             y: {
			               	 Temps: {label: '온도 (Celsius)'},
			                 Daylight: {label: '습도'}	                 
		               	 }
		             }
	             };	            
                 function drawMaterialChart() {
	                 var materialChart = new google.charts.Line(chartDiv);
	                 materialChart.draw(data, materialOptions);
                 }             
                 drawMaterialChart();	                  
	        }
	    });	
	}
	
	//////////////////////////////////////해당 도시 현재 날씨
	$.ajax({
	        url: "https://api.forecast.io/forecast/b3c35d762e71470531e8161c4826b528/" + theCityLat + "," + theCityLng,
	        type: "GET",
	        dataType: "jsonp",
	        success: function (data) {
	        	
	        	var weatherIcon = data.currently.icon;
     	
	        	if (weatherIcon.indexOf("clear") >= 0){
					weatherIcon = "clear.gif"; 
				} else if (weatherIcon.indexOf("cloudy") >= 0){
					weatherIcon = "cloudy.gif"; 
				} else if (weatherIcon == "snow"){
					weatherIcon = "snow.gif"; 
				} else if (weatherIcon == "sleet"){
					weatherIcon = "snow.gif"; 
				} else if (weatherIcon == "rain"){
					weatherIcon = "rain.gif"; 
				} else {
					weatherIcon = "clear.gif"; 
				}
	        	
	        	//도시 정보에 현재  
	            $("td#weatherIcon").append("<img src='../resources/common/img/"+ weatherIcon +"' alt='현재날씨아이콘'>");
	            //$weatherPic.append("<img src='../resources/common/img/"+ weatherArr.weatherIcon +"' alt='현재날씨아이콘'>");
	        	
	        	var currentTemp = data.currently.temperature;
	        	var $td = $("td#currentTemp");
	        	$td.append("<span id='currentTempF' STYLE='color: red; font-size: 20pt'>"+ currentTemp +"ºF </span>");
	        	$("#tempFrSwitch").hide();
	        	
	        	$("#tempCcSwitch").unbind().on("click",function(event){
	        		var currentTempFr = $("td#currentTemp span").text().replace("ºF","");     		
	        		var currentTempC = (currentTempFr-32)*5/9;
	        		var amt = parseFloat(currentTempC);
	        		$("td#currentTemp span").empty();
	        		$("td#currentTemp span").text(amt.toFixed(1) + "ºC");
	        		$(this).hide();
	        		$("#tempFrSwitch").show();
	        	});
	        	
	        	$("#tempFrSwitch").unbind().on("click",function(event){
	        		var currentTempC = $("td#currentTemp span").text().replace("ºC","");     		
	        		var currentTempFr = (currentTempC*9/5)+32;
	        		var amt = parseFloat(currentTempFr);
	        		$("td#currentTemp span").empty();
	        		$("td#currentTemp span").text(amt.toFixed(1) + "ºF");
	        		$(this).hide();
	        		$("#tempCcSwitch").show();
	        	});
	        		
	        } 	        
	})
	
	////////////////////////////////////// 해당 도시 여행지 정보
	var theCityNo = ${param.cityNo};	
	$("div#destHiddenValue input#cityNo").val(theCityNo);	
	
	//(0) 공통기능
	function destCommons(data){
		
		
		var destArr = data.allDestData;
		var naviArr = data.allDestNavi;
		var pageArr = data.criteria;
		
		var $destList = $("div#destList");
		
		// 공통-여행지출력
		$.each(destArr, function(index, item){
			var $div = $("<div class='col-md-3'></div>");
			
			var $divImage = $("<div class='row dest-img'></div>");
			$divImage.append("<a href='/info/dest.tm?destNo="+ item.destNo +"'><img id='destimg_"+ item.destNo 
					+"' src='../resources/img/dest/"+ item.fileName + "' alt='" + item.destName + "' style='width:220px;height:204px;'/></a>");
			
			var $divDestInfo = $("<div class='row'></div>");
			$divDestInfo.append("<p> 장소이름:" + item.destName + "</p> ");
			$divDestInfo.append("<p> 클립수:" + item.cnt );
			$divDestInfo.append("<p> 카테고리:" + item.categoryName + "</p>");
			
			$div.append($divImage);
			$div.append($divDestInfo);
			
			$destList.append($div);			
		})
		
		// 공통-페이지네이션 출력		
		var $thePagenation = $("ul#destInfoPagi")
		if(naviArr.totalRows > 0){
			if ( pageArr.pageNo > 1 ){
			    $thePagenation.append("<li id="+ naviArr.prev +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>&lt;</a></li>");
			}	
			for (var i= naviArr.startPage; i<= naviArr.endPage; i++){
				if( i == pageArr.pageNo){
					$thePagenation.append("<li id="+ i +" class='active'><a href='../info/city.tm?cityNo="+ theCityNo +"'>"+ i +"</a></li>");
				} else {
					$thePagenation.append("<li id="+ i +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>"+ i +"</a></li>");
				}
			}
			if ( pageArr.pageNo < naviArr.totalPages ){
			    $thePagenation.append("<li id="+ naviArr.next +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>&gt;</a></li>");
			}
		}
	}
	
	//(1) 여행지 상위카테고리	
	//(1-a) 상위카테고리 클릭 : 하위카테고리, 여행지, 페이지네이션 출력 
	$("li.dropdown a").on("click",function(event){
		event.preventDefault();		
		var theUpperCatecode = $(this).attr("href");	//상위카테고리 코드	
		
		//하위카테고리  
		$.ajax({
			type: "GET",
			url:"jsonLowerCateNo.tm",	
			data : {category:theUpperCatecode},	
			dataType:"json",		
			success: function(data){												 
				var lowerCateArr= data.lowerCateCodes;				 				
				$("div#theLowerCategory").empty();				
				var $div = $("div#theLowerCategory")
				$.each(lowerCateArr, function(index, item){
					$div.append("<button class='btn btn-default' style='margin-left:2px;' id='lowerCate_"+item.code+"'><strong>"+ item.name +"</strong></button>");															
				});
				
				//하위카테고리 클릭
				$("div#theLowerCategory button").on("click",function(event){
					event.preventDefault();
					event.stopPropagation();
					$("div#destList").empty();
					$("ul#destInfoPagi").empty();
					//하위카테고리 번호 찾기
					var lowerCateCode = $(this).attr("id").replace("lowerCate_","");
					$.ajax({
						type: "GET",
						url:"jsonFourDestByCityNo.tm",
						data: {cityNo:theCityNo,category:lowerCateCode},
						dataType:"json",
						success: function(data){
							destCommons(data);
							//하위카테고리의 페이지네이션 클릭
							$("body").unbind().on("click","ul#destInfoPagi li a",function(event){
								event.preventDefault();
								event.stopPropagation();
								$("div#destList").empty();
								$("ul#destInfoPagi").empty();
								var thePageNo = $(this).parent().attr("id");
								$.ajax({
									type: "GET",
									url:"jsonFourDestByCityNo.tm",
									data: {cityNo:theCityNo,pageNo:thePageNo,category:lowerCateCode},
									dataType:"json",
									success: function(data){
										destCommons(data);							
									}				 
								})
							})
						}				 
					})
				})
			}
		});
		
		//상위카테고리의 여행지,페이지네이션 
		$("div#destList").empty();
		$("ul#destInfoPagi").empty();
		$.ajax({
			type: "GET",
			url:"jsonFourDestByCityNo.tm",
			data: {cityNo:theCityNo,category:theUpperCatecode},
			dataType:"json",
			success: function(data){
				destCommons(data);
				//상위카테고리의 페이지네이션 누르기				 
				$("body").unbind().on("click","ul#destInfoPagi li a",function(event){
					event.preventDefault();
					event.stopPropagation();
					$("div#destList").empty();
					$("ul#destInfoPagi").empty();
					var thePageNo = $(this).parent().attr("id");
					$.ajax({
						type: "GET",
						url:"jsonFourDestByCityNo.tm",
						data: {cityNo:theCityNo,pageNo:thePageNo,category:theUpperCatecode},
						dataType:"json",
						success: function(data){
							destCommons(data);							
						}				 
					})
				})
			}
		})	
	})						

	//(2) cityNo 로 여행지, 페이지네이션 출력
	$.ajax({
		type: "GET",
		url:"jsonFourDestByCityNo.tm",
		data: {cityNo:theCityNo},
		dataType:"json",
		success: function(data){
			destCommons(data);				
			//(2-1) 페이지네이션 클릭- cityNo, pageNo로 여행지, 페이지네이션 출력
			 $("body").unbind().on("click","ul#destInfoPagi li a",function(event){
				$("div#destList").empty();
				$("ul#destInfoPagi").empty();
				 event.preventDefault();
				 event.stopPropagation();
				 
				 var thePageNo = $(this).parent().attr("id");
				 				  
				 $.ajax({
					type: "GET",
					url:"jsonFourDestByCityNo.tm",
					data: {cityNo:theCityNo,pageNo:thePageNo},
					dataType:"json",
					success: function(data){
						destCommons(data);
					}				 
				 })				 
			 });			 
		 }
	});	
	
	//////////////////////////////////////해당 도시 리뷰 정보
	//(0) 공통기능
	function cityReviewCommons(data){
		var reviewArr = data.theThreeReviews;
		var naviArr = data.theNaviReview;
		var pageArr = data.criteriaReview;
		
		var $allReview = $("div#cityReviewContent");
		
		// 리뷰 출력
				$.each(reviewArr, function(index, item){
			var $div = $("<div class='row' style='background-color:#f2f2f2;margin:10px; border : solid black 1px;'></div>")
			
			var $reviewInfo = $("<div class='row'></div>");
			
			var $reviewInfoInside = $("<div class='col-md-12'></div>");		
			$reviewInfoInside.append("<div class='row' style='height:10px;'></div>");
			$reviewInfoInside.append("<div class='col-md-3'><strong>작성자 : "+ item.userName +"</strong></div>");
			$reviewInfoInside.append("<div class='col-md-3'> 작성일 : "+item.regDate+"</div>");
			$reviewInfoInside.append("<div class='col-md-2'><img alt='"+ item.rating +"' style='width:50px; height:50px; margin : 3px;' src='/resources/common/img/"+item.rating+"'</div>");
		
			$reviewInfo.append($reviewInfoInside);

			
			var $reviewContents = $("<div class='form-control' style='margin-left: 20px; margin-bottom:10px;width:900px;height:150px;padding:20px 40px 20px 40px;'>"+ item.reviewData +"</div>");
			
			$div.append($reviewInfo);
			$div.append($reviewContents);
			
			$allReview.append($div);
		})

		
		//리뷰의 페이지네이션 출력 
		var $thePagenation = $("ul#cityReviewPagi");
		
		if(naviArr.totalRows > 0){
			if ( pageArr.pageNo > 1 ){
			    $thePagenation.append("<li id="+ naviArr.prev +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>&lt;</a></li>");
			}	
			for (var i= naviArr.startPage; i<= naviArr.endPage; i++){
				if( i == pageArr.pageNo){
					$thePagenation.append("<li id="+ i +" class='active'><a href='../info/city.tm?cityNo="+ theCityNo +"'>"+ i +"</a></li>");
				} else {
					$thePagenation.append("<li id="+ i +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>"+ i +"</a></li>");
				}
			}
			if ( pageArr.pageNo < naviArr.totalPages ){
			    $thePagenation.append("<li id="+ naviArr.next +"><a href='../info/city.tm?cityNo="+ theCityNo +"'>&gt;</a></li>");
			}
		};
	}
	//var theRating = ${param.rating};	
	//(1) 모든 리뷰, 페이지네이션 출력
	$.ajax({
		type:"GET",
		url:"jsonCityReview.tm",
		data: {cityNo:theCityNo},
		dataType:"json",
		success: function(data){
			cityReviewCommons(data);
			
			//모든 리뷰의 페이지네이션 클릭 
			$("body").unbind().on("click","ul#cityReviewPagi li a", function(event){
				
				event.preventDefault();
				event.stopPropagation();
				
				$("div#cityReviewContent").empty();
				$("ul#cityReviewPagi").empty();
				var thePageNo = $(this).parent().attr("id");
				$.ajax({
					type: "GET",
					url:"jsonCityReview.tm",
					data: {cityNo:theCityNo,pageNo:thePageNo},
					dataType:"json",
					success: function(data){
						cityReviewCommons(data);							
					}				 
				})
			})
		}
	})
	
	//(2) rating 별 리뷰 클릭
	$("a.emotion").click(function(event){
		event.preventDefault();
		event.stopPropagation();	
		 
		var theRating = $(this).attr("href");			//rating 평가값 뽑기
		
		$("div#cityReviewContent").empty();
		$("ul#cityReviewPagi").empty();
		$.ajax({
			type: "GET",
			url:"jsonCityReview.tm",
			data: {cityNo:theCityNo,rating:theRating},
			dataType:"json",
			success: function(data){
				cityReviewCommons(data);	
				
				//(2-2) rating 별 리뷰의 페이지네이션 클릭 
				$("body").unbind().on("click","ul#cityReviewPagi li a", function(event){
					event.preventDefault();
					event.stopPropagation();
					$("div#cityReviewContent").empty();
					$("ul#cityReviewPagi").empty();
					var thePageNo = $(this).parent().attr("id");
					$.ajax({
						type: "GET",
						url:"jsonCityReview.tm",
						data: {cityNo:theCityNo,pageNo:thePageNo,rating:theRating},
						dataType:"json",
						success: function(data){
							cityReviewCommons(data);							
						}				 
					})
				})
			}				 
		})
	});
});
</script>
<style>

</style>

<!-- 전체 컨테이너 -->    
<div class="container" style="text-align:center;">
   

    <div class="row">
    	<h2><c:out value="${theCityData.name}"/></h2>
    	<!-- 해당 도시의 위도 경도 정보 -->
    	<input id="cityLat" type="hidden" value="${theCityData.lat}"/>
    	<input id="cityLng" type="hidden" value="${theCityData.lng}"/>
    </div>
    <!-- 상단 사진 및 오른쪽 텍스트 정보-->
    <div id="" class="row" style="height:380px;">
       
        <!-- 도시사진 -->
        <div class="row col-md-6">
            
            <!-- 사진 슬라이드 효과-->
            <div>
             
                  <div id="myCarousel" class="carousel slide" data-ride="carousel">

                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <c:forEach begin="1" end="${imgSize-1 }" var="num">
                      <li data-target="#myCarousel" data-slide-to="${num }"></li>   
                      </c:forEach>                
                    </ol>

                    <!-- Wrapper for slides -->
                    <div class="carousel-inner" role="listbox">
					  
					  <c:forEach var="img" items="${theCityImgData}" varStatus="loop">	
					  <c:choose>
					  	<c:when test="${loop.count eq 1 }">
	                      <div class="item active">
	                        <img src="../resources/img/city/${img.fileName }" alt="${img }" width="460" height="345">
	                      </div>
					  	</c:when>
					  	<c:otherwise>
					  	<div class="item">
	                        <img src="../resources/img/city/${img.fileName }" alt="${img }" width="460" height="345">
	                      </div>
					  	</c:otherwise>
					  </c:choose>
					  </c:forEach>
                    
                    </div>

                    <!-- Left and right controls -->
                    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                      <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                      <span class="sr-only">Next</span>
                    </a>
                  </div>
                </div>    
          </div>           
                                                            
        <!-- 도시 텍스트 정보 -->
        <div class="row col-md-5 col-md-offset-1">
             
            <!--월별평균기온 텍스트 정보-->
            <div class="row">
                <table border="border border-collapse" style="border-color:#BDFFF6;width:100%;height:125px;">                     
                    <tr style="hight:10px;">
                    	<td colspan="4">현재 날씨</td>
                    </tr>
                    <tr style="hight:115px;">                    
	                    <td id="weatherIcon"></td>                        
	                    <td id="currentTemp"><td>
                        <td style="width:30%;">
                            <button id="tempCcSwitch" class="btn btn-primary">ºC</button>
                            <button id="tempFrSwitch" class="btn btn-primary">ºF</button>
                        </td>
                    </tr>                 
                </table>
            </div>
            
            <!--도시 상세정보 -->
            <div class="row" style="height:140px;overflow:scroll;margin-top:10px; ">
                 <p><c:out value="${theCityData.details}"/></p>
            </div>
            
            <!--환율 -->
            <div id="currencyCalculate" class="row" style="height:110px; margin-top:10px;">
                <p>환율계산</p>
                <span class="col-md-3">
	                <select id="allFrCurrency">
	                	<option value="USD" selected="selected">미국 USD</option>
	                	<option value="EUR" >유럽 EUR</option>
	                	<option value="GBP" >영국 GBP</option>
	                	<option value="JPY" >일본 JPY</option>
	                	<option value="CNY" >중국 CNY</option>
	                	<option value="AUD" >호주 AUD</option>
	                	<option value="CAD" >캐나다 CAD</option>
	                	<option value="HKD" >홍콩 HKD</option>
	                </select>
                </span>
                <span class="col-md-3"><input style="width:100%;" type="text" size="6" value="1" id="frCurrency"></span>
                <span class="col-md-1"> = </span>
                <span class="col-md-2">한국 KRW</span>
                <span class="col-md-3"><input style="width:100%;" type="text" size="6" value="" id="krCurrency"></span>              
            </div>
  
      </div>
        
    </div>

   
    <!-- 메뉴 사이 공백 -->
    <div style="height:50px;"></div>
    
   
    <!-- 지역별 카테고리 -->
    <div id="" class="row">
        <p><h2>지역의 추천장소 </h2></p>
        <!-- 상위카테고리 -->
        <nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		      <a class="navbar-brand" href="#">여행지 카테고리</a>
		    </div>		    
		    <ul class="nav navbar-nav">
		      <c:forEach var="upperCategory" items="${theUpperCategories }">		    
		      <li class="dropdown">
		      	<a class="dropdown-toggle" data-toggle="dropdown" href="${upperCategory.code }" >
		      		<c:out value='${upperCategory.name }'/><span class="caret"></span>
		      	</a>
		      	<ul class="dropdown-menu">
		          <div id="theLowerCategory" style="width:400px;height:70px;padding: 20px 0 20px 20px;">
		          </div>
		        </ul>
		      </li>
		      </c:forEach>
		     </ul> 		    
		  </div>
		</nav>   

    </div>
   
    
    <!-- 메뉴 사이 공백 & 숨겨진 값 담는 곳  -->
    <div id="destHiddenValue" style="height:60px;">
    	<input type="hidden" id="cityNo" name="cityNo" value="">
        <input type="hidden" id="pageNo" name="pageNo" value="1">
        <input type="hidden" id="category" name="category" value="0">
    </div>
   
   
   
    <!-- 장소사진 -->
    <div class="row">
        <!-- 여행지 사진 4장씩 -->
        <div id="destList" class="row">        
			<!-- Ajax로 로딩됨 -->                  
        </div>
        
        <!-- 여행지 페이지 네이션 -->
        <div class="text-center">
        	<ul class="pagination row" id="destInfoPagi">
        		<!-- Ajax로 로딩됨 -->
        	</ul>
        </div>
        
    </div>
   
    <!-- 온도그래프 -->
    <div id="chart_div" class="row" style="width:900px;height:400px">
    도시 작년 월별 평균기온 그래프
    </div>


	<!-- 메뉴 사이 공백 -->
    <div style="height:50px;"></div>
   
    <!-- 도시에 대한 리뷰 출력 -->
    <div id="cityReview" class="row">
        
	        <div class="col-md-6">
	        	<h1>리뷰보기 </h1>
	        </div>
	    
	        <div class="col-md-6" id="">
		            <span class="pull-right" style="margin :10px;">
		            	<a href='P' class="emotion"><img src='../resources/common/img/chart_bad.png' alt='P'><strong>별로에요!</strong></a>
		            </span> 
		            <span  class="pull-right" style="margin :10px;">   
		                <a href='A' class="emotion"><img src='../resources/common/img/chart_normal.png' alt='A'><strong>괜찮아요</strong></a>
		            </span>
		            <span class="pull-right" style="margin :10px;">
		                <a href='G' class="emotion"><img src='../resources/common/img/chart_good.png' alt='G'><strong>좋아요!</strong></a>        
		            </span>
	        </div>    	           
    </div>
        
        <div id="cityReviewContent" class="row" style="margin-top : 10px;">             
	        <!-- 리뷰 출력 --> 
	    	<!-- Ajax로 로딩됨 -->
        </div>
        
        <!--리뷰 페이지네이션 -->
        <div class="text-center">
        	<ul class="pagination row" id="cityReviewPagi">
        		<!-- Ajax로 로딩됨 -->
        	</ul>
        </div>
    </div>

</div> 