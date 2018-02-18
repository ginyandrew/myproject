/*
$(function(){
	$('.collapse').click(function(){
		  $(this).hide;
		  return true;
	});
});
*/


// 버턴 누르면 아래로 지역명이 나오는코드입니다.
function serachlist(){
	var searchData = {};
	$('button.btn-collapse').click(function(event){
		//	event.preventDefault();
		$('.collapse').collapse('hide');
		//$(this).collapse('show');
	});
	
	$(".citys-open").click(function(event) {
		var cityNo = $(this).attr("id").replace(/city-/, "");
		searchData.cityNo = cityNo;
		
		// 검색 기능 수행
		searchPlan(cityNo);
		
	});
	
	// 도시 상세 버튼을 눌렀을 때 해당 도시의 정보를 가지는 쿼리문을 수행
	$("li .city-open").click(function(event) {
		var cityNo = $(this).attr("id").replace(/city-/, "");
		searchData.cityNo = cityNo;
		
		// 검색 기능 수행
		searchPlan(cityNo);
		
	});
	
	
}

function searchPlan(cityNo) {
	// searchData에서 찾을 정보를 뽑는다
	
	// 찾을 정보를 이용하여 Ajax를 이용하여 정보를 찾는다.
	$.ajax({
		url: 'search.tm',
		type: 'GET',
		data: {cityNo:cityNo},
		dataType: "json",
		success: function(result) {
			// 정보를 받아왔을 떄 수행할 작업
			// 화면에 도시 이미지 및 링크를 뿌리는 거
			var planList = result.planList;
			var cityimg = result.cityimg;
			var $divData=$("<div class='plan-result'></div>");
			$.each(planList, function(index, data) {
				// 이쁘게 뿌리세요
				// 이게 맞는지 모르겠습니다.
				//$(".boxlist").after().append("<h6>"+index +" : " + data+"<h6>");
				//2016-02-04 BACKUP
				//$(".boxlist").append("<p></p>"+data.no+"<p></p>")
				
				var leaveDate 	 = data.leaveDate.substring(0,8);	// 날짜 XXXX-XX-XX로 자르기
				var cuttingtitle = data.title.substring(0,6);
				var imgpath ="..\\resources\\img\\city\\";
				/* 20150211로 정지됩니다.
				var planlist ="";
					planlist += "<div class='row class='col-lg-3' style='float: left'>";
					planlist += '<img src="'+imgpath+ cityimg.fileName + '" height="200" width="200"/>';
					planlist += "<div style='float: left' class='col-lg-3'>"+data.no+"<br>"+cuttingtitle+"<br>"+leaveDate+"</br>"+"</div>";
					planlist += "</div>";
				*/	
				
				/*20160211 새로이 추가됩니다.*/
				var planlist ="";
				planlist += "<div class='col-lg-4'>";
				planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
				planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
				planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
				planlist += "</div>";
				// 상위 추가한걸 모두 append 시킵니다.
				$divData.append(planlist);
				
				// 추가적으로 발생하는 이벤트. [2016 02-12]
				// -> 날짜선택  period
				// 버턴중에 period가 붙은 것을 찾아서.. 그것을 누르면 해당것만 보이게
				$("button.btn-period").click(function(event) {
					var period = $(this).attr("id").replace(/period-/, "");
					
					var $divData= $("div.plan-result");
					
					// 현재 검색된 결과를 일단 초기화
					$divData.empty();
					$.each(planList, function (index, data) {
						
						// data.period의 값을 확인
						var dataPeriod = data.period;
						
						var planlist ="";
						// period의 값이 찾는 범위의 값인지 확인
						if (dataPeriod >= period && dataPeriod <= period+2) {
							
							// 찾는 범위의 값이면 화면에 다시 표시하고, 아니라면 화면에서 감춘다
							planlist += "<div class='col-lg-4'>";
							planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
							planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
							planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
							planlist += "</div>";
						}
						
						$divData.append(planlist);
						
					});
				});
				
				
				
				// -> 여행시기 leaveDate [시작일 기준으로]
				$("button.btn-seasonality").click(function(event) {
					// 버튼 아이디중에서 seasonality- 를 잘라내서 seasonality 에 넣는다.
					var seasonality = $(this).attr("id").replace(/seasonality-/, "");
					var $divData= $("div.plan-result");
					$divData.empty();
					//var planlist ="";
					
					$.each(planList, function (index, data) {
						// 년도 빼버리고 월만 가져와서 month에 저장!
						var month  = data.leaveDate.substring(4,6);
						/*또 잘라요!*/
						var leaveDate 	 = data.leaveDate.substring(0,8);	// 날짜 XXXX-XX-XX로 자르기
						var cuttingtitle = data.title.substring(0,6);		// 타이틀도 잘라서 표시합니다
						
						switch (seasonality) {	// 계절의 id중 spring,summer,autumn,winter 을선택하기위한 스위치!
						case "spring" : 		// spring 버튼을 누르면
							switch(month) {		// 3월~5월을 또 스위치로 나눕니다!
							case "03" :
							case "04" :
							case "05" :var planlist ="";	// 3~5월이면 아래께 출력됩니다.!
										   planlist += "<div class='col-lg-4'>";
										   planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
										   planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
										   planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
										   planlist += "</div>";
										   $divData.append(planlist);	
							}
							// 봄
							break;
							
						case "summer" :	// summer 버턴을 누르면
							switch(month) {	// 6월~8월까지 만
							case "06" :
							case "07" :	// 아래께 출력됩니다.
							case "08" :var planlist ="";
								           planlist += "<div class='col-lg-4'>";
								           planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
								           planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
								           planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
								           planlist += "</div>";
								           $divData.append(planlist);	 
							}
							// 여름
							break;
							
						case "autumn" : 	// 가을버턴
							switch(month) {	// 해당 월에 대한것만 출력됩니다.
							case "09" :
							case "10" :
							case "11" :var planlist ="";
										   planlist += "<div class='col-lg-4'>";
										   planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
										   planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
										   planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
										   planlist += "</div>";
										   $divData.append(planlist);	
							}
							
							// 가을
							break;
							
						case "winter" :		// 겨울이면
							switch(month) {
							case "01" :		// 해당 월수의 것만 출력됩니다.
							case "02" :
							case "12" :	var planlist ="";
										    planlist += "<div class='col-lg-4'>";
										    planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
										    planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
										    planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
										    planlist += "</div>";
										    $divData.append(planlist);	
							}
							// 겨울
							break;	
						}
					});
				});	
				
				// -> 여행테마 themeCode
				$("button.btn-thema").click(function(event) {
					
					var themas = $(this).attr("id").replace(/thema-/, "");
					var $divData= $("div.plan-result");
					$divData.empty();
					
					$.each(planList, function (index, data) {
						
						/*또 잘라요!*/
						var leaveDate 	 = data.leaveDate.substring(0,8);	// 날짜 XXXX-XX-XX로 자르기
						var cuttingtitle = data.title.substring(0,6);		// 타이틀도 잘라서 표시합니다
						var themaCodes = data.themeCode;					// 테마 코드를 가져옵니다..
						
						if(themas == themaCodes) {
							
							var planlist ="";
						    	planlist += "<div class='col-lg-4'>";
						    	planlist += "<a href='/plan/detail.tm?no="+data.no+"' class='thumbnail' style='text-decoration:none !important'>";
						    	planlist += '<img src="'+imgpath+ cityimg.fileName + '" width="500" height="400"/><br>';
						    	planlist += "<div style='float: left' class='col-lg-10'>"+data.themeCode+"<br>"+leaveDate+"<br>"+cuttingtitle+"</br>"+"</div>";
						    	planlist += "</div>";
						    	$divData.append(planlist);	
						}
							                    
					});
					
				});
				
			});
			$(".boxlist").empty().append($divData);
		}
	});
	
}