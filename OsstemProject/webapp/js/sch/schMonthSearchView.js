jQuery(document).ready(function() {
	
	//처음 로딩 - > 파라미터가 있을때
	schgbChange(jQuery('#sch_gb_01 option:selected').val(),jQuery('#hidsch_gb_02').val(),"go");
});

window.onload = function () {
	$("div .cld_txt01").each(function(){
		$(this).parent().addClass("dday");
	})
}

function movePage(url){
	$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method='+ url);
	$("#frm2").submit();
}

function moveSchDetail(schId){
	$("#schId").val(schId);
	$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=schDetailView');
	$("#frm1").submit();
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

//구분1,2 변경했을때마다 조회
function divChange(val){
	var url = WEB_ROOT+"/sch/sch.do?method=schMonthSearchView";
	location.href = url + "&schd_ymd="+jQuery('#schd_ymd').val()+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+val;
}

//월 변경할때 마다 조회
function changeMonth(){
	var url = WEB_ROOT+"/sch/sch.do?method=schMonthSearchView";
	var params = {};
	var date = jQuery('#year').val()+jQuery('#month').val()+"01";
	
	// 년월일(YYYYMMDD)
	params["schd_ymd"] = date;
	
	location.href = url + "&schd_ymd="+date+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+jQuery('#sch_gb_02 option:selected').val();
}

//이전달 조회
function prevDate(date){

	$("#schd_ymd").val(date);
	$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method=schMonthSearchView');
	$("#frm2").submit();
}

//다음달 조회
function nextDate(date){

	$("#schd_ymd").val(date);
	$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method=schMonthSearchView');
	$("#frm2").submit();
}




