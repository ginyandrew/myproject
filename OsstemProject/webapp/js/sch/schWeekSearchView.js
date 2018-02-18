jQuery(document).ready(function() {
	schgbChange(jQuery('#sch_gb_01 option:selected').val(),jQuery('#hidsch_gb_02').val(),"go");
    
});

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

//구분별 페이지 뷰 변경 - 파라미터 넘기기 
function divChange(val){
	var url = WEB_ROOT+"/sch/sch.do?method=schWeekSearchView";
	location.href = url + "&year="+jQuery('#year').val() + "&month="+jQuery('#month').val()+"&day="+jQuery('#day').val()+"&sch_gb_01="+jQuery('#sch_gb_01 option:selected').val()+"&sch_gb_02="+val;
}
