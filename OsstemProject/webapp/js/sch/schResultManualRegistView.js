
$(document).ready(function() {
	
	  
});

function searchTitle(theTitle){
	
	// (1) 제목 자동완성 
	$(theTitle).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(theTitle).val()), "title", '00', function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME,  
						label:item.NAME,			 
						desc:item.ID 
					}
				}))
			})
		},
		focus: function (event, ui){
			// 텍스트에 입력한 순간 밑에 나온 리스트를 키보드로 누르면 기본은 텍스트에 내용을 update하게 되지만 여기선 막았다.
			event.preventDefault();	
		},
		select: function( event, ui ) {
			
		    $(theTitle).val( ui.item.id );	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.
		    var schId = ui.item.desc;
		    $("#schId").val(schId);
		    fillSchInfo(schId);
		    
		}
	})	
	
}
//이름 or 그룹명을 조회 
function autoSearchName(theName){
	
	var type = $(theName).attr('name');		// allName 또는 group 
	
	$(theName).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(theName).val()), type, "00", function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME,  
						label:item.NAME + " - " + item.INFO,			 
						desc:item.ID 
					}
				}))
			})
		},
		focus: function (event, ui){
			event.preventDefault(); 
		},
		select: function( event, ui ) {
			event.preventDefault(); 
			// 제목을 선택해서 기존 일정 정보 불러오기.
			$(theName).val(ui.item.id);
			
			fillPsnInfo(ui.item.desc);
		}
	})
};

//이름, 제목 자동검색
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

//원장 이름 자동검색 결과
function fillPsnInfo (theId){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=selectPsnInfoByPsnId',
		data: {psn_id : theId, sch_id:0 },      
		success: function(data){
			var psn = data.psn;
			$("#psnId").val(psn.psnId);
			$("#phone").val(psn.phone);
			$("#addr").val(psn.addr);
			$("#liceno").val(psn.liceno);
			$("#email").val(psn.email);
			$("#hosname").val(psn.hosname);
		}
	}) 
}

function fillSchInfo(schId){
	 
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=selectSchInfoBySchId',
		data: { sch_id : schId},      
		success: function(data){
			var sch = data.sch;
			$("#place").val(sch.place);
			$("#orgNm").val(sch.orgNm);
			$("#name").val(sch.name);
			$("#date").html(sch.staDate);
			$("#time").html(sch.staHr + ":00 ~ " + sch.finHr + ":00");
			
			var $radio = "";
			
			// 여기서 강연 , 참석 희망여부를 갈라야 한다.
			
			if (sch.gb01 == "01"){
				$radio = 
					"<th scope='row'>강연 희망여부</th>"
					+ "<td>"
					+ "<input id='' type='radio' name='spkyn' value='Y'><label for='' ><span><span></span></span>예</label>"  
					+ "<input id='' type='radio' name='spkyn' value='N' checked><label for=''><span><span></span></span>아니오</label>"
					+ "</td>"
					
					+ "<th scope='row'>참석 희망여부</th>"
					+ "<td>"
					+ "<input id='' type='radio' name='presyn' value='Y'><label for='' ><span><span></span></span>예</label>"  
					+ "<input id='' type='radio' name='presyn' value='N' checked><label for=''><span><span></span></span>아니오</label>"
					+ "</td>";
				
			} else if (sch.gb01=="02"){
				$radio = 
					"<th scope='row'>참석 희망여부</th>"
	                + "<td colspan='3'>"
	                + "<input id='' type='radio' name='presyn' value='Y'><label for='' ><span><span></span></span>예</label>"
	                + "<input id='' type='radio' name='presyn' value='N' checked><label for=''><span><span></span></span>아니오</label>"
	                + "</td>";
			}
			$("#yn").empty();
			$("#yn").append($radio);
		}
	}) 
}

function movePage(){
	$("#movePage").submit();
}

$(function(){
	getSalbonbuLst();
});

// 영업본부리스트 조회
function getSalbonbuLst() {
	$.ajax({
		url: WEB_ROOT + '/sawon/sawon.do?method=getSalBonbuLst'
		, type: 'get'
		, data: {}
		, success: function(data){
			if (data.result == '1') {
				var salBunbuLst = data.data
				var op = $('<option>');
				op.val("");
				op.text("본부를 선택하세요.");
				$('#sel01').append(op);
				for (var i=0 ; i < salBunbuLst.length ; i++) {
					var op = $('<option>');
					op.val(salBunbuLst[i].org_code);
					op.text(salBunbuLst[i].org_name);
					$('#sel01').append(op);
				}
			} else {
				alert('영업본부리스트 조회 실패 ' + '\n' + 'message=' + data.message);
			}
		}
		, error: function(jqXHR, textStatus, errorThrown){
			alert('영업본부리스트 조회요청 실패 ' + '\n' + 'status=' + jqXHR.status);
		}
		, dataType: 'json'
	});
}

// 영업본부의 하위 영업지점 리스트 조회
function getSalJiJumLst(bonbuCd) {
	$.ajax({
		url: WEB_ROOT + '/sawon/sawon.do?method=getSalJiJumLst'
		, type: 'get'
		, data: {orgCd: bonbuCd}
		, success: function(data){
			if (data.result == '1') {
				var salJiJumLst = data.data
				var op = $('<option>');
				op.val("");
				op.text("지점을 선택하세요.");
				$('#sel02').append(op);
				for (var i=0 ; i < salJiJumLst.length ; i++) {
					var op = $('<option>');
					op.val(salJiJumLst[i].org_code);
					op.text(salJiJumLst[i].org_name);
					$('#sel02').append(op);
				}
			} else {
				alert('영업지점 리스트 조회 실패 ' + '\n' + 'message=' + data.message);
			}
		}
		, error: function(jqXHR, textStatus, errorThrown){
			alert('영업지점 리스트 조회요청 실패 ' + '\n' + 'status=' + jqXHR.status);
		}
		, dataType: 'json'
	});
}

// 영업지점의 사원 리스트 조회
function getSawonLstByOrg(jijumCd) {
	$.ajax({
		url: WEB_ROOT + '/sawon/sawon.do?method=getSawonLstByOrg'
		, type: 'get'
		, data: {orgCd: jijumCd}
		, success: function(data){
			if (data.result == '1') {
				var salSawonLst = data.data
				for (var i=0 ; i < salSawonLst.length ; i++) {
					var op = $('<option>');
					op.val(salSawonLst[i].sabun);
					op.text(salSawonLst[i].name);
					$('#sel03').append(op);
				}
			} else {
				alert('영업지점 사원 리스트 조회 실패 ' + '\n' + 'message=' + data.message);
			}
		}
		, error: function(jqXHR, textStatus, errorThrown){
			alert('영업지점 사원 리스트 조회요청 실패 ' + '\n' + 'status=' + jqXHR.status);
		}
		, dataType: 'json'
	});
}

function sel01_onchange(sel){
	$('#sel02 option').remove();
	$('#sel03 option').remove();
	var bonbuCd = $(sel).val();
	if (bonbuCd !== undefined && bonbuCd != ''){
		getSalJiJumLst(bonbuCd);
		getSawonLstByOrg(bonbuCd);
	}
}

function sel02_onchange(sel){
	$('#sel03 option').remove();
	var jijumCd = $(sel).val();
	if (jijumCd !== undefined && jijumCd != ''){
		getSawonLstByOrg(jijumCd);
	} else {
		var bonbuCd = $('#sel01').val();
		getSawonLstByOrg(bonbuCd);
	}
}

function registPsn(){
	
	// 위에 내용이 입력되었는지 먼저 확인하고, 입력되었으면 폼을 보여준다.
	var title = $("#schId").val();
	if(!title){
		alert('기존 일정을 선택한 후 등록해주세요.')
		$("#title").focus();
		return false;
	} else {
		$("#psnForm").css("display","");
		$("#btns").css("display","");
	}
};

function manualRegist(){
	
	// pres
	if(!$("#psnId").val()){
		alert('수신자를 선택하신 후 등록해주세요.');
		$("#psnName").focus();
		return false;
	} else {
		alert('저장 성공');
		// $("#sabun").val($("#sel03").val());
		
		$("#pres_Yn").val($('input[name=presyn]:checked').val());
		if ($("#gb01").val() == "01"){
			$("#spk_Yn").val($('input[name=spkyn]:checked').val());			
		} else {
			$("#spk_Yn").val('N');
		}
		$("#frm1").submit();
	}
	// spk
	// sabun
	// 채우고 frm1 서브밋.
	
	
	
}
 