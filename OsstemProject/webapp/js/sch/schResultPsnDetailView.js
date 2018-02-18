
$(document).ready(function() {
	
})


function insertYnRcp(){
	
	alert('응답 여부가 저장됩니다.');
	$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=insertManualYnRsp');
	$("#frm1").submit();
	
};

function movePage(){
	$("#movePage").submit();
}

function deleteRegPsn(){
	
	var checkConfirm = confirm("해당 내역을 삭제하시겠습니까?");
	if(checkConfirm){
		alert("해당 내역이 삭제됩니다.")
		$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=deleteRegPsn');
		$("#frm1").submit();
	} else {
		alert("삭제가 취소됩니다.");
		return false;
	}
}


















/*
 * 


window.onload = function(){
	
	var sawonId = $("#sawonId").val();
	
	if (sawonId != 0){
		
		var bonbuCd = $("#bonbuCd").val();
		var jijumCd = $("#jijumCd").val();
		console.log("본부:"+bonbuCd + "/ 지점:"+jijumCd + "/사원:"+ sawonId);
		
		getSalJiJumLst(bonbuCd);
		$('#sel02 option[value='+ jijumCd +']').attr('selected','selected');

		getSawonLstByOrg(jijumCd);
		$('#sel03 option[value='+ sawonId +']').attr('selected','selected');
	}
} 


//영업본부리스트 조회
function getSalbonbuLst() {
	$.ajax({
		url: WEB_ROOT + '/sawon/sawon.do?method=getSalBonbuLst'
		, type: 'get'
		, data: {}
		, async: false
		, success: function(data){
			if (data.result == '1') {
				var salBunbuLst = data.data
				var op = $('<option>');
				op.val("");
				op.text("본부를 선택하세요.");
				$('#sel01').append(op);
				for (var i=0 ; i < salBunbuLst.length ; i++) {
					if (i==0){
						var op = $('<option selected="selected">');
					} else {
						var op = $('<option>');
					}
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
		, async: false
		, data: {orgCd: bonbuCd}
		, success: function(data){
			if (data.result == '1') {
				var salJiJumLst = data.data
				var op = $('<option>');
				op.val("");
				op.text("지점을 선택하세요.");
				$('#sel02').append(op);
				for (var i=0 ; i < salJiJumLst.length ; i++) {
					if (i==0){
						var op = $('<option selected="selected">');
					} else {
						var op = $('<option>');
					}
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
		, async: false
		, data: {orgCd: jijumCd}
		, success: function(data){
			if (data.result == '1') {
				var salSawonLst = data.data
				for (var i=0 ; i < salSawonLst.length ; i++) {
					if (i==0){
						var op = $('<option selected="selected">');
					} else {
						var op = $('<option>');
					}
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
	console.log($(sel).val());
	if (jijumCd !== undefined && jijumCd != ''){
		getSawonLstByOrg(jijumCd);
	} else {
		var bonbuCd = $('#sel01').val();
		getSawonLstByOrg(bonbuCd);
	}
} 
*/
