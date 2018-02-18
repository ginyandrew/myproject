
$(document).ready(function(){
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth();
	var day = d.getDay();
	
	var staDt = new Date(year-1, month, day);
	var finDt = new Date(year+1, month, day);
	
	$("#sch_sta_dt").datepicker({dateFormat:'yy-mm-dd'});
	$("#sch_fin_dt").datepicker({dateFormat:'yy-mm-dd'});

	
	// 검색결과 남기기
	// 1. Faculty / Director
	if ($("#psnIds").val()){					
		$("#mbrId").val($("#psnIds").val());
		$("#mbrName").val($("#mbr_name").val());
	}
	// 2. 일시
	if($("#sta_dt").val()){
		$("#sch_sta_dt").val($("#sta_dt").val());
	} 
	if($("#fin_dt").val()){
		$("#sch_fin_dt").val($("#fin_dt").val());
	}
	// 3. Gb Course
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
});

//기본정보 엑셀 다운
function excelDown(){
	// 이미 총 갯수를 가져와서 바로 엑셀 다운에 넣는다고 생각해보자.
	$("#frm5").attr('action', WEB_ROOT + '/sch/sch.do?method=schSchExcelDown');
	$("#frm5").submit();
}


function removeDt(tempSelect){
	var tempYn = $(tempSelect).val();
	if (tempYn == 'Y'){
		$("#sch_sta_dt").datepicker('setDate', null);
		$("#sch_fin_dt").datepicker('setDate', null);
		// disable 하고 
		$("#sch_sta_dt").prop("disabled",true).css("background", "#dddddd");
		$("#sch_fin_dt").prop("disabled",true).css("background", "#dddddd");
		$("a[name*='setMonth']").css("pointer-events","none");
		// $("#dateRow").css("display","none");
	} else {
		$("#sch_sta_dt").prop("disabled",false).css("background", "");
		$("#sch_fin_dt").prop("disabled",false).css("background", "");
		// $("#dateRow").css("display","");
		$("a[name*='setMonth']").css("pointer-events","");
	}
	
};


function setDate(months){

	var d = new Date();
	var today = setDateFormat(d);
	$("#sch_fin_dt").val(today);		// 종료시간엔 현재시간

	d.setMonth(d.getMonth()-months);
	var start = setDateFormat(d);
	$("#sch_sta_dt").val(start);		// 시작시간엔 months 달 전 만큼 
}

function submitForm(){
	
	if($("#mbrId").val() != ""){
		$("#mbrId").attr("name","psnId");
	}  
	if($("#mbrName").val() == ""){
		$("#mbrId").attr("name","");
	}
	 
	$("#pageNo").val(1);
	$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=schTempletManageView');
	$("#frm1").submit();
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

function autoSearchName(theName){
	
	// var inputName = $(theName).attr('class');	jquery를 이용해 class 이름을 얻어낸다.
	var mbrType = "00";
	
	$(theName).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(theName).val()), 'mbrName', mbrType, function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME  ,  
						label:item.NAME +" - " +item.INFO,			 
						desc:item.ID 
					}
				}))
			})
		},
		focus: function (event, ui){
			event.preventDefault(); 
		},
		autoFocus:false,
		select: function( event, ui ) {
			event.preventDefault(); 
			
		    $(theName).val(ui.item.id);	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.
		    $(theName).next().val(ui.item.desc);
		}
	})
}

var autoSearch = function (keyword, theType, mbr_gb, callback){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=searchName',
		data: { input : keyword, type : theType, mbrGB : mbr_gb },      
		success: function(data){
			autoSearchResult = data.model.keys
			callback(autoSearchResult);
		}
	}) 
};














jQuery(document).ready(function() {
	
	$("#start_date").datepicker({// 연수회 
		dateFormat:"yy.mm.dd",
		showOn: "both",
		buttonImageOnly : true,
	    buttonText          : "Calendar",
	    buttonImage        : WEB_ROOT+"/images/data.gif"
	});
	
	$("#end_date").datepicker({//강연회
		dateFormat:"yy.mm.dd",
		showOn: "both",
		buttonImageOnly : true,
	    buttonText          : "Calendar",
	    buttonImage        : WEB_ROOT+"/images/data.gif"
	});
	
});

//개월 -> 버튼 설정해주기 
function monthInterval(id){
	var cur_date = $("#cur_date").val();
	
	var date = new Date(cur_date.substring(0,4), cur_date.substring(6,4), cur_date.substring(8,6));
	
	date.setMonth(date.getMonth()-(Number(id)+1));
	
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	
	if(month.toString().length < 2 ){
		month = "0"+month;
	}
	if(day.toString().length <2){
		day = "0"+day;
	}
	
	$("#start_date").val(year+"."+month+"."+day);
	$("#end_date").val(cur_date.substring(0,4)+"."+cur_date.substring(6,4)+"."+cur_date.substring(8,6));
}

//년도 -> 버튼 설정해주기 
function yearInterval(id){
	var cur_date = $("#cur_date").val();
	
	var date = new Date(cur_date.substring(0,4), cur_date.substring(6,4), cur_date.substring(8,6));
	
	date.setYear(date.getFullYear()-Number(id));

	var year = date.getFullYear();
	var month = date.getMonth();
	var day = date.getDate();
	
	if(month.toString().length < 2 ){
		month = "0"+month;
	}
	if(day.toString().length <2){
		day = "0"+day;
	}
	
	$("#start_date").val(year+"."+month+"."+day);
	$("#end_date").val(cur_date.substring(0,4)+"."+cur_date.substring(6,4)+"."+cur_date.substring(8,6));
	
}

function searchSch(schId){
	 
	$("#schId").val(schId);
	$("#frm2").submit();
}

function movePage(page){
	searchSchTemmplet(page);
}

function searchSchTemmplet(page) {
	// 페이지 번호 설정
	$('#pageNo').val(page);
	$("#frm1").submit();
}


//탬플릿 등록 버튼 
function templetRegist(){
	var url = WEB_ROOT+"/sch/sch.do?method=schDetailRegistView";
	location.href = url+"&templet_gb="+"templet" ;
}

