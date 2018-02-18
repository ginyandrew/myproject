
// msg을 send할 기존 일정의 제목을 조회 (이름/그룹과 분리한 이유는 select에서 수행할 기능이 다르기 때문.)
function autoSearchTitle(theTitle){
	
	
	$(theTitle).autocomplete({
		
		source: function(request, response){
			autoSearch(encodeURIComponent($(theTitle).val()), "title", "00", function(data){
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
			event.preventDefault(); 
		},
		select: function( event, ui ) {
			// 제목을 선택해서 기존 일정 정보 불러오기.
		    $(theTitle).val(ui.item.id);

		    var theID =ui.item.desc;
		    $("#sch_id").val(theID);			// 해당 sch_id 뽑아내기		
		    
		    autoSearch( theID, "sch", "00", function(result){
		    	var data = result[0];			// 해당 sch_id의 상세일정내용 가져오기
		    	fillTopTable(data);
			});
		}
	})
}

// 이름 or 그룹명을 조회 
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

			// 제목을 선택해서 기존 일정 정보 불러오기.
			$(theName).val(ui.item.id);
			
			getPsnInfo( type , ui.item.desc, function(result){	// allName 또는 group이 간다.
				$.each(result, function(index, item){
					
					if ($("#psnList tr").length < 1){
						$("#psnList").append($(item));
					}  else {
						$(item).insertAfter($("#psnList tr:last"));						
					}
					
					countTr();
				});
			});
		    
		}
	})
}


// (1) 이름, 그룹 자동완성 >> 여기서 psnId, grpId 를 가져옴.
var autoSearch = function (keyword, theType, mbr_gb, callback){
	
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=searchName',
		data: { input : keyword, type : theType, mbrGB : mbr_gb },      
		success: function(data){
			autoSearchResult = data.model.keys
			callback(autoSearchResult);
		}
	}) 
}

// (2) 1번에서 찾은 psnId로 해당 psn의 정보 가져오기. (무조건 리스트)
var getPsnInfo = function (theType, theId, callback){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=selectPsnList',
		data: { type : theType, id : theId },      
		success: function(data){
			callback(data);
		}
	}) 
}

function countTr(){
	var totalRcpCnt = $("#psnList tr").length;
	$("#totalCnt").text(totalRcpCnt);
}
// 테이블의 발송대상자 1명씩 삭제 
function deleteTr(theTr){
	
	alert('해당 줄을 삭제합니다.');
	$(theTr).parent().parent().remove();
	countTr();
}

function movePage(){
	$("#movePage").submit();
}

function formSubmit(){
	
	var check= true;
	if ($("#psnList tr").length < 1){
		alert('한 명 이상의 발송대상자를 추가해주세요.');
		check = false;
		return false;
	}
	if (check){
		var checkConfirm = confirm("정말 메세지를 발송하시겠습니까?");
		
		if(checkConfirm){

			$("#psnList tr").each(function(i){
				$(this).find('input').attr("name","sendPsnList["+i+"].psn_id");
			});
			$("#form1").submit();
		
		} else {
			alert("입력이 취소됩니다.");
			return false;
		}
	} else {
		return false;
	}
}
 
function registList(){
	
	// 위에 내용이 입력되었는지 먼저 확인하고, 입력되었으면 폼을 보여준다.
	var schNo = $("#sch_id").val();
	if(!schNo){
		alert('제목 중 기존 일정을 하나 선택하고 등록해주세요.')
		$("#title").focus();
		return false;
	} else {
		$("#listForm").css("display","");
	}
};

function uploadFile(theFile){
	var url="selectPsnListBySendId";
	var fileTarget = $(theFile);
		
	if(window.FileReader){  // modern browser
		var filename = fileTarget[0].files[0].name;
	} 
	else {  // old IE
		var filename = fileTarget.val().split('/').pop().split('\\').pop();  // 파일명만 추출
	}
	// 추출한 파일명 삽입
	fileTarget.siblings('.upload-name').val(filename);
	  
}

function switchCk(type){
	$("#searchName").attr('name', type);
}
 
function getHour(date){
	var d = new Date(date);
	var hh = d.getHours();
	return hh;
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}


function changeSchGb01(slt){

	if (slt == '01'){
		$("#sch_gb_02").empty().append(
		  '<option value="01" selected="selected">연구회</option>'
		+ '<option value="02">보수교육</option>'		
		+ '<option value="03">Faculty Seminar</option>'
		+ '<option value="04">수요화상</option>'
		+ '<option value="05">Osstem meeting</option>'); 
		
	} else if (slt == '02'){
		$("#sch_gb_02").empty().append(
		  '<option value="01" selected="selected">Basic</option>'
		+ '<option value="02">Advanced</option>'		
		+ '<option value="03">Master</option>'
		+ '<option value="04">One-day Course</option>');
		 
	}
}



function fillTopTable(data){
	// 제목을 가져와서 선택하는 순간
	// 해당 ID를 보내서 해당 강의의 sch 정보를 불러온다.
	 $("#place").val(data.PLACE);
	 $("#org_nm").val(data.ORG_NM);
	 $("#name").val(data.NAME);
	 $("#sch_gb_01").val(data.SCH_GB_01);
	 
	 changeSchGb01(data.SCH_GB_01);
	 $("#sch_gb_02").val(data.SCH_GB_02);
	 
	 var theDate = formatDate(data.SCH_STA_DT);
	 $("#sch_sta_dt").val(theDate);
	 
	 var staTm = getHour(data.SCH_STA_DT)
	 var finTm = getHour(data.SCH_FIN_DT);
	 
	 $("#sch_sta_time").val(staTm);
	 $("#sch_fin_time").val(finTm);
}


/*function imgDown(){
	$("img").css("display","");
}*/

function imgClose(){
	$("img").css("display","none");
}
