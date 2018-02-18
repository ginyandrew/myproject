
$(document).ready(function() {
	
	// (1) 제목 자동완성 
	$("#title").autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($("#title").val()), "title", '00', function(data){
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
			
		    $("#title").val( ui.item.id );	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.
		    var schId = ui.item.desc;
		    $("#schId").val(schId);
		    fillSchInfo(schId);
		    
		}
	})	 
});


//이름 or 그룹명을 조회 
function autoSearchName(theName){
	
	var type = "psnNotInReg";				// 서비스 타입. 
	var schId = $("#schId").val();						// 어느 경우던 무조건 schId는 값이 있다. name은 info로 감.
	
	
	$(theName).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(theName).val()), type, schId, function(data){
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

			// 제목을 선택해서 기존 일정 정보 불러오기.
			$(theName).val(ui.item.id);
			
			fillPsnInfo(ui.item.desc);
		}
	})
};

//이름, 제목 자동검색
var autoSearch = function (keyword, theType, sch_id, callback){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=searchName',
		data: { input : keyword, type : theType, schId : sch_id },      
		success: function(data){
			autoSearchResult = data.model.keys
			callback(autoSearchResult);
		}
	}) 
};

//원장 이름 자동검색 결과
function fillPsnInfo (theId){
	var url = "psn_id="+theId+"&sch_id=0"
	getPsnInfo(url);
}

// Reg에 등록된 기존 원장정보 가져오기.
function updatePsnInfo(psnId){
	var schId = $("#schId").val();
	var url = "psn_id="+psnId + "&sch_id="+schId;
	getPsnInfo (url);
	$("#psnForm").css("display","");
	$("#saveBtn").css("display","");
}

// 삭제할 땐 Reg에서만 삭제한다. 여러명을 한꺼번에 삭제할 수 있음.
function deletePsn (){
	
	var checkConfirm = confirm("정말 삭제하시겠습니까?");

	if(checkConfirm){
		// 다 모은 psn을 대상으로 reg에서 삭제를 한다.
		var sch_id = $("#schId").val();
		var psnArr = [];
		$("input:checkbox[name*='ifDeleteReg']").each(function(){
			if($(this).is(':checked')){
				var psnId = $(this).parent().find('input:eq(1)').val();
				psnArr.push(psnId);
			}
		})
		$("#sch_id").val(sch_id);
		$("#psn_id").val(psnArr.join(","));
		$("#frm2").submit();
	} else {
		return false;
	}
}

function getPsnInfo(url){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=selectPsnInfoByPsnId&'+url,
		success: function(data){
			var psn = data.psn;
			console.log(JSON.stringify(psn));
			$("#psnName").val(psn.name);
			$("#psnId").val(psn.psnId);
			$("#phone").val(psn.phone);
			$("#addr").val(psn.addr);
			$("#liceno").val(psn.liceno);
			$("#email").val(psn.email);
			$("#hosname").val(psn.hosname);
			
			$("input:radio[name*='presyn']").each(function(){
				if((psn.presyn).indexOf($(this).val()) > -1){
					$(this).prop("checked",true);
				}
			})
			
			$("input:radio[name*='restyn']").each(function(){
				if((psn.restyn).indexOf($(this).val()) > -1){
					$(this).prop("checked",true);
				}
			})
			
			if($("#gb01").val() == '01'){
				$("input:radio[name*='spkyn']").each(function(){
					if((psn.spkyn).indexOf($(this).val()) > -1){
						$(this).prop("checked",true);
					}
				})
			}
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
					+ "<input id='' type='radio' name='spkyn' value='Y' checked><label for='' ><span><span></span></span>예</label>"  
					+ "<input id='' type='radio' name='spkyn' value='N'><label for=''><span><span></span></span>아니오</label>"
					+ "</td>"
					
					+ "<th scope='row'>참석 희망여부</th>"
					+ "<td>"
					+ "<input id='' type='radio' name='presyn' value='Y' checked><label for='' ><span><span></span></span>예</label>"  
					+ "<input id='' type='radio' name='presyn' value='N'><label for=''><span><span></span></span>아니오</label>"
					+ "</td>";
				
			} else if (sch.gb01=="02"){
				$radio = 
					"<th scope='row'>참석 희망여부</th>"
	                + "<td colspan='3'>"
	                + "<input id='' type='radio' name='presyn' value='Y' checked><label for='' ><span><span></span></span>예</label>"
	                + "<input id='' type='radio' name='presyn' value='N'><label for=''><span><span></span></span>아니오</label>"
	                + "</td>";
			}
			$("#yn").empty();
			$("#yn").append($radio);
		}
	}) 
}

function addPsn(){

	$("#psnName").val("");
	$("#psnId").val("");
	$("#phone").val("");
	$("#addr").val("");
	$("#liceno").val("");
	$("#email").val("");
	$("#hosname").val("");
	
	$("input:radio[name*='presyn']").each(function(){
		if($(this).val()=='Y'){
			$(this).prop("checked",true);
		}
	})
	
	$("input:radio[name*='restyn']").each(function(){
		if($(this).val()=='Y'){
			$(this).prop("checked",true);
		}
	})
	
	if($("#gb01").val() == '01'){
		if($(this).val()=='N'){
			$(this).prop("checked",true);
		}
	}
	$("#psnForm").css("display","");
	$("#saveBtn").css("display","");
}

//강의신청 - 발송제외 저장하기  
function rest_save(){
	var dataArray = new Array();
	var sch_id = $("#schId").val();
	var restYn = "";

	$("#psnList tr").each(function(index,item){
		var info = $(this).find('td:last input:eq(1)').val();
		if ($(this).find('td:last input:eq(0)').is(":checked")){
			// check 되어있으면 Y
			info = info +",Y";
		} else {
			// 안되어있음 N
			info = info + ",N";
		}
		dataArray.push(info);
	});

	if(dataArray.length == 0){
		alert("발송 및 저장할 대상이 없습니다.")
		return false;
	}else{
		$.ajax({
			url: WEB_ROOT + '/sch/sch.do?method=searchName',
			data: { input : sch_id, 
					type : "updateRest", 
					psnInfo : JSON.stringify(dataArray) },  
			dataType: 'json',
			beforeSend: function() {
				$('#pop_wrapper').loadingOverlay();//로딩중 표시
			},
			complete:function(){
				$('#pop_wrapper').loadingOverlay('remove');
			},
			success : function(res) {
				alert('발송제외 여부 저장 완료');
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("error!! \n" + textStatus + " : " + errorThrown);
			}
		});
	}	
}

$(function(){
	getSalbonbuLst();
});

function movePage(){
	$("#movePage").submit();
}

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
	
	if(!$("#psnId").val()){
		alert('수신자를 선택하신 후 등록해주세요.');
		$("#psnName").focus();
		return false;
	}  else {
		$("#restYn").val($('input[name=restyn]:checked').val());
		$("#pres_Yn").val($('input[name=presyn]:checked').val());
		if ($("#gb01").val() == "01"){
			$("#spk_Yn").val($('input[name=spkyn]:checked').val());			
		} else {
			$("#spk_Yn").val('N');
		}
		$("#frm1").submit();
	}
}
 
 