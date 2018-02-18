function formatDate(stringDate) {
	var date = "";
	date += stringDate.substring(0,4);
	date += "-";
	date += stringDate.substring(4,6);
	date += "-";
	date += stringDate.substring(6,8);

	return date;
	//날짜를   format하는 함수 
}
WebFontConfig = {
	    custom: {
	        families: ['Nanum Gothic'],
	        urls: ['http://fonts.googleapis.com/earlyaccess/nanumgothic.css']
	    }
	  };
	  (function() {
	    var wf = document.createElement('script');
	    wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
	      '://ajax.googleapis.com/ajax/libs/webfont/1.4.10/webfont.js';
	    wf.type = 'text/javascript';
	    wf.async = 'true';
	    var s = document.getElementsByTagName('script')[0];
	    s.parentNode.insertBefore(wf, s);
	  })();

function getUserPlanList() {

	var userNo 		= $("input:hidden[name='userNo']").val();
	$.ajax({
		url  : "planlist.tm",
		type : 'GET',
		data : {destNo: userNo
				
		},
		dateType : 'json',
		success :function(data){

			var planList = data.planList;

			planList.length||$("#planlist").html($("<p class='text-center' style>일정이 없습니다.</p>").css("color", "gray"));
			$.each(planList, function(index,item){

				var planTitle = item.title;
				// 출발일과 도착일을 받아온다(문자열)
				var leaveDate 	= item.leaveDate.substring(0,8);
				var arriveDate	= item.arriveDate.substring(0,8);
				// 문자열 형태로 변환 및 조합한다
				// "yyyy-MM-dd ~ yyyy-MM-dd"
				var plandate = "";
				plandate += "<div class='rows "+item.no+"' id='plan-"+item.no+"'>";
				plandate += "<div class='col-md-5' style='font-size: 15px;'>"+planTitle+"</div>";
				plandate += 
					"<div class='col-md-7'><a href='/plan/planmake?no="+item.no+"'><a type='button' id='choice-"+item.no+"' " +
					"class='btn btn-primary btn-sm pull-right choice' " +
					"href='/plan/detail.tm?no="+item.no+"' >선택</a></div>";
				plandate += "<p class='travelDate'><span class='leaveDate'>"+formatDate(leaveDate)+"</span> ~ <span class='arriveDate "+item.no+"'>"+formatDate(arriveDate)+"</span></p>";
				plandate += "</div>";

				//$("#plandate").text(plandate);
				$("#planlist").append(plandate);
				/*$('#success').click(function(event){
					event.preventDefault();

					var $travleTitle = $("#trable-title")
					var $startDate	= $("#date");
					 $.ajax({
						 url : "addPlan.tm",
						 type: "GET",
						 data: {
							 title : title,
							 date  : date 
						},

					 })
				});*/
			});
			
			// 모달의 선택버튼을 누를 경우 다음 모달로 넘어가고 그안의 시작날짜를 나타낸다.

			$('button.choice').click(function(){

				$('#addPlan-day').empty();

				var no = $(this).attr("id").replace(/choice-/ , "");
				var date = $("#plan-"+no).find(".travelDate").text().replace(/ ~.*$/, "");
				$('#addPlan-day').append("<div class='addDate "+no+" col-md-3'>"+date+"</div>");
				$('#mybtn-day').off('click');
				$('#mybtn-day').click(function(){

					var date = $("#addPlan-day div.addDate:last").text();

					$.ajax({
						url : "planDate.tm",
						type: "GET",
						data: {date : date,

						},
						datatype: "json",
						success : function(result){

							var date = result.date;


							$('#addPlan-day').append($("<div class='addDate col-md-3'></div>").html(date));
							/*$('#mybtn-day').click(function(){
								$.
							})*/
						}
					})	
				})
			});
			$('#closebtn').click(function(){
				/*var $arriveDate = $('.addDate:last-child').text();
				$('.arriveDate '+planList.no).text($arriveDate);*/
				
				getUserPlanList();
				return true;
			});
		}
	});
}
$(function addPlanlist(){
	$('#mybtn').click(function(){
	$('#success').click(function(data){
		var userNo 		= $("input:hidden[name='userNo']").val();
		var destNo 		= $("input:hidden[name='destNo']").val();
		var cityNo		= $("input:hidden[name='cityNo']").val();
		var title 		= $('#travle-title').val();
		var leaveDate 	= $('#date').val();
		$.ajax({
			url : "/plan/NewPlan.tm",
			data: {
				userNo 		: userNo,
				destNo 		: destNo,
				title  		: title,
				leaveDate	: leaveDate,
				cityNo		: cityNo,
				period		: "1",
				themeCode   : "1",
				periodss	: "1"
			},
			type: "POST",
			datatype: "json",
			success			:function(data){
				window.location.href = "/plan/NewPlan.tm";
			}
				// 유저번호/출발일/여행제목/테마=1/기간=1/도시번호/periodss=1
		})
	})
	});
})
$(function(){
	$('#close-btn' ).on('click', function(){
		$('#planlist').empty();
	});
	$('#xbtn').on('click', function(){
		$('#planlist').empty();
	});
		
})

$(function() {
	
	//map
	infodestMapinit();

	// 일정에 담기 모달이 열릴 때 실행됨
	$("#myModal").on("shown.bs.modal", function() {
		getUserPlanList();
	});
	



	$('div input[name="btn"]').click(function(event){
		$('div input[name="btn"]').removeClass("active");
		$(this).addClass("active");

	});

	
	$('#button-copy').on('click', function(event) {
		var userNo 		= $("input:hidden[name='userNo']").val();
		var destNo 		= $("input:hidden[name='destNo']").val();
		$.ajax({
			url : "addClip.tm",	
			type: "GET",
			data: {
				userNo : userNo,
				destNo : destNo
			},
			dataType: "json",
			success: function(ajaxResult){
				var result = ajaxResult.result;
				if(result == "success") {
					var destName = $("input:hidden[name='destName']").val();
					alert('클립보드에 담기: ' + destName); 
				} else {
					alert('클립보드에 담기: ' + result);
				}
			}	
		})
		
		
		
	});


//	일정에 넣기 모달적용 후 안에 있는 달력
	$( "#date" ).datepicker({
		dateFormat: 'yy-mm-dd'
	});


	$('#btnfont').click(function(event){

		// 입력값 확인 및 예외처리
		var userNo 		= $("input:hidden[name='userNo']").val();
		var destNo = $("input:hidden[name='destNo']").val();
		var data = $('div #text').val();
		var rating = $("input[name='btn']").filter(".active").attr("data-rate");
		if(!rating) {
			alert("하나이상 눌러주세요~");
			return;
		}

		// 확인이 끝났으면 서버에 정보를 넣는 ajax 실행
		$.ajax({

			url : "saveReview.tm",
			type : 'POST',
			data : {destNo	:destNo,
				userNo	:userNo,
				rating	:rating,
				data	:data

			},
			dataType : 'json',
			success :function(data){


				// 넣는 것을 성공했을 때
				getReview();

			}
		})
	});
	var userNo 		= $("input:hidden[name='userNo']").val();
	var destNo		= $("input:hidden[name='destNo']").val();

	getReview();
	function getReview() {
		$.ajax({
			url 			: "reviewlist.tm",
			type			: "POST",
			data			: { 
				destNo : destNo 
			},
			success 		: function(data){

				// 현재 작성되어 있는 값을 지운다.
				$('div.review').empty();

				var reviewList = data.review;
				

				$.each(reviewList, function(index, item){
					var name 			= item.user.name;
					var reviewUserNo 	= item.user.no 
					var reviewNo		= item.no;
					var regDate 		= item.regDate;
					var formatRegDate	= regDate.substring(0,8);
					var description 	= item.data;
					var $review = $("<div></div>").attr("id", reviewNo).addClass("reviewlist").css("border", "1px solid black");
					if(reviewUserNo == userNo) {
						$review.append($("<button id ='erase' type='button' class='btn btn-default btn-xs'></button>").append($("<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>")));
					}
					$review.append($("<label id='author' class='form-controler' for='comment'>작성자 : <span class='username'>"+name+"</span></label>"))
					.append($("<label id='regdate' class='form-controler' for='comment'>작성일 : "+formatRegDate+"</label>"))
					if(item.rating=="G"){
						var $rating = $("<label><img src='/resources/common/img/chart_good.png' style ='margin-top : 5px;'>좋아요!!</label>");
						
					} else if (item.rating=="A"){
						var $rating = $("<label><img src='/resources/common/img/chart_normal.png'>괜찮아요~</label>");
						
					} else if (item.rating=="P"){
						var $rating = $("<label><img src='/resources/common/img/chart_bad.png' style ='margin-top : 5px;'>별로에요~</label>");
						
					}
					$review.append($rating);
					$review.append($("<div class='review-control form-control'>"+description+"</div>"));
					$('div.review').append($review);
					
					var id = $review.attr('id');
					// 삭제 요청
					$('#erase').click(function(){
						window.location.href = "deleteReview.tm?destNo="+destNo+"&reviewNo="+id;
					})
				});
			
		
			}
		});
	}


})

function makeChart() {


	/*
	 * 원형 차트를 불러오기 위한 
	 */

	var theme1 = parseInt($('input[name="themeCode3"]').val());
	var theme2 = parseInt($('input[name="themeCode2"]').val());
	var theme3 = parseInt($('input[name="themeCode6"]').val());
	var theme4 = parseInt($('input[name="themeCode5"]').val());
	var theme5 = parseInt($('input[name="themeCode1"]').val());
	var theme6 = parseInt($('input[name="themeCode4"]').val());


	$.jqplot ('graph', [[['단체여행', 12], 
	                     ['커플여행', 24], 
	                     ['가족여행', 10], 
	                     ['비지니스', 8],
	                     ['친구와함께',33],
	                     ['나홀로여행',13]]], 
	                     { 
		seriesDefaults: {
			//원형으로 렌더링
			renderer: $.jqplot.PieRenderer,
			//원형상단에 값보여주기(알아서 %형으로 변환)
			rendererOptions: {
				showDataLabels: true
			}
		},
		//우측 색상별 타이틀 출력
		legend: { show:true, location: 'e' }
	});
}

