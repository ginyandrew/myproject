
$(document).ready(function() {
	
	$("#pop_wrapper").draggable();
	$("#pop_wrapper5").draggable();
	
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth();
	var day = d.getDay();
	
	var staDt = new Date(year-1, month, day);
	var finDt = new Date(year+1, month, day);
	$("#sch_sta_dt").datepicker({dateFormat:'yy-mm-dd'});

	d.setMonth(d.getMonth()+1);
	$("#sch_fin_dt").datepicker({dateFormat:'yy-mm-dd'});
	
	// 검색결과 남기기
	// 1. 일시
	if($("#sta_dt").val()){
		$("#sch_sta_dt").val($("#sta_dt").val());
	} 
	if($("#fin_dt").val()){
		$("#sch_fin_dt").val($("#fin_dt").val());
	}
	// 2. Gb Course
	if($("#gb01s").val()){
		var gb01s = $("#gb01s").val().split(",");
		$("input:checkbox[name*='gb101']").each(function(){
			if(gb01s.indexOf($(this).val()) > -1){
				$(this).prop("checked",true);
			}
		})
	}
	if($("#gb02s").val()){
		var gb02s = $("#gb02s").val().split(",");
		$("input:checkbox[name*='gb102']").each(function(){
			if(gb02s.indexOf($(this).val()) > -1){
				$(this).prop("checked",true);
			}
		})
	}

})

function setDate(months){

	var d = new Date();
	var today = setDateFormat(d);
	$("#sch_fin_dt").val(today);		// 종료시간엔 현재시간

	d.setMonth(d.getMonth()-months);
	var start = setDateFormat(d);
	$("#sch_sta_dt").val(start);		// 시작시간엔 months 달 전 만큼 
}

function setDateFormat(today){
	 
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	
	if(dd<10) {
	    dd='0'+dd
	} 
	if(mm<10) {
	    mm='0'+mm
	} 
	today = yyyy + '-' + mm + '-' + dd;
	return today;
}

function searchSch(schIdCnt){
	var cnt = $(schIdCnt).attr("id");
	var schId = $(schIdCnt).attr("name");
	$("#schId").val(schId);
	$("#cnt").val(cnt);
	
	$("#frm2").submit();
}

function submitForm(){
	$("#pageNo").val(1);
	$("#frm1").submit();
}

function movePage(page){
	searchSchTemmplet(page);
}

function searchSchTemmplet(page) {
	// 페이지 번호 설정
	$('#pageNo').val(page);
	$("#frm1").submit();
}

function manualRegist(){
	$("#frm4").submit();
}

//기본정보 엑셀 다운
function excelDown(){
	// 이미 총 갯수를 가져와서 바로 엑셀 다운에 넣는다고 생각해보자.
	$("#frm5").attr('action', WEB_ROOT + '/sch/sch.do?method=schRegExcelDown');
	$("#frm5").submit();
}

function popupSendList(schId){
	
	$.ajax({
		url: WEB_ROOT + "/sch/sch.do?method=selectPsnListBySchId",
		data: { sch_id : schId},
		beforeSend: function() {
			$('body').loadingOverlay();//로딩중 표시
		},
		complete:function(){
			$('body').loadingOverlay('remove');
		},
		success: function(data){
			var result = data.psnList;
			if(document.getElementById("pop_wrapper5") !== null){
				$("#pop_wrapper5").remove();
			}
			$(result).insertAfter($("#btns"));
			
			$("#pop_wrapper5").draggable();
			$("#pop_wrapper").draggable();
		}
	});
}

function movePageToManage(psnId){
	var Ids = $(psnId).attr("id").split(",");
	 
	$("#psn_id").val(Ids[0]);
	$("#sch_id").val(Ids[1]);
	var sawonId = $(psnId).parent().parent().find('td:eq(6)').find('input').val();
	$("#sawonId").val(sawonId);
	
	$("#frm3").submit();
}
function closePopup(){
	
	$("#pop_wrapper5").remove();
}