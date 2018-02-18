jQuery(document).ready(function() {
	
	schgbChange(jQuery('#sch_gb_01 option:selected').val(),jQuery('#hidsch_gb_02').val(),"go");
});

window.onload = function () {
	$("div .cal_year table tbody tr td").each(function(){
		if ($(this).find('a span').text()){
			$(this).addClass("dday");
		}
	})
}

function movePage(url){
	$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method='+ url);
	$("#frm2").submit();
}

// 구분 1 변경시 구분 2 select 
function schgbChange(sch_gb_01,sch_gb_02,gb){
	var selectData;
	var selectValue;
	if(sch_gb_01 == "01"){//강연회 일 경우 
		selectData = new Array("연구회","보수교육","Faculty Seminar","수요화상","Osstem meeting");
		selectValue = new Array("01","02","03","04","05");
		$("#sch_gb_01 option[value='"+sch_gb_01+"']").attr("selected","true");
		
	}else{
		selectData = new Array("Basic","Advanced","Master","One-day Course");
		selectValue = new Array("01","02","03","04");
		$("#sch_gb_01 option[value='"+sch_gb_01+"']").attr("selected","true");
	}
	
	var options = $("#sch_gb_02").prop("options");
	
	for(i = 0;i<$("#sch_gb_02 option").length;i++){
		options[i] = null;
	}
	
	for(j = 0;j<selectData.length;j++){
		options[j] = new Option(selectData[j],selectValue[j])
	}
	
	if(gb == "go"){//처음 로딩 할때
		$("#sch_gb_02 option[value='"+sch_gb_02+"']").attr("selected","true");
	}else{
		divChange("01");//전체 다시 조회(처음꺼로 조회->그룹1에서 Change 가 됬을경우)
	}
}


//구분별 페이지 뷰 변경 - 파라미터 넘기기 
function divChange(gb02){
	var url = WEB_ROOT+"/sch/sch.do?method=schYearSearchView";
	location.href = url + "&cur_year="+jQuery('#year').val()+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+gb02;
}

// 일정 상세 내역으로 이동 
function moveSchDetail(schId){
	$("#schId").val(schId);
	$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=schDetailView');
	$("#frm1").submit();
}

function getPopup(link){

	// 눌리는 순간 가장 먼저 '1개인지' 아닌지를 판단한다.
	var cnt = $(link).find('span').text();
	var theYear = $("#year").val();
	var placeNm = $(link).parent().find('input[name*="placeNm"]').val();
	var theMonth = $(link).parent().find('input[name*="theMonth"]').val();
	if (theMonth < 10){
		theMonth = "0"+theMonth;
	} 
	if (cnt == 1){			// 한개면 바로 이동.
		getSchByPlace(encodeURIComponent(placeNm), 'schByYearPlace',  theMonth, theYear, function(data){
			moveSchDetail(data[0].SCH_ID);
		});
	} else if (cnt > 1){	// 2개 이상이면 팝업.
		getSchByPlace(encodeURIComponent(placeNm), 'schByYearPlace',  theMonth, theYear, function(data){
			var div = $("<div class='wrap_pop'></div>");
			var innerDiv = $("<div class='cal_pop'></div>");
			$.each(data, function(index, item){
				innerDiv.append("<p><a href='' " +
						"onclick='moveSchDetail("+ item.SCH_ID +");return false;'>"+ item.TITLE +"</a></p>")
			});
			
			div.append(innerDiv);
			div.insertAfter($(link));
			
		});
	}
}

$(window).click(function() {
	$("div .wrap_pop").remove();		// 팝업 외에 영역을 누르면 모든 팝업을 지운다.
});

	
var getSchByPlace = function (keyword, theType, theMonth, theYear, callback){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=searchName',
		data: { input : keyword, type : theType, month : theMonth, year : theYear },      
		success: function(data){
			autoSearchResult = data.model.keys
			callback(autoSearchResult);
		}
	}) 
};

//이전 년도 넘기기
function prev_year(year){
	var url =  WEB_ROOT+"/sch/sch.do?method=schYearSearchView";
	var params = {};
	// 년월일
	params["cur_year"] = year;
	location.href = url + "&cur_year="+year+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+jQuery('#sch_gb_02 option:selected').val();
	
}

//다음 년도 넘기기
function next_year(year){
	var url =  WEB_ROOT+"/sch/sch.do?method=schYearSearchView";
	var params = {};
	// 년월일
	params["cur_year"] = year;
	location.href = url + "&cur_year="+year+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+jQuery('#sch_gb_02 option:selected').val();
}
