
$(document).ready(function() {	
	
	$('[name="dates"]').datepicker({dateFormat:'yy-mm-dd', minDate:1})
	$('[name="newdates"]').datepicker({dateFormat:'yy-mm-dd', minDate:1}).datepicker('setDate',new Date());
});

function movePage(){
	$("#movePage").submit();
}

function searchTitle (theTitle){
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
			event.preventDefault();	
			
		    $("#title").val( ui.item.id );	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.
		}
	})	 
}
//AJAX callback - 검색어 결과 
var autoSearch = function (keyword, theType, mbr_gb, callback){
	$.ajax({
		url: WEB_ROOT + '/sch/sch.do?method=searchName',
		data: { input : keyword, type : theType, mbrGB : mbr_gb },  
		beforeSend: function() {
			$('body').loadingOverlay();//로딩중 표시
		},
		complete:function(){
			$('body').loadingOverlay('remove');
		},
		success: function(data){
			autoSearchResult = data.model.keys
			callback(autoSearchResult);
		}
	}) 
}


var autoSearchDtl = function (keyword, callback){
	var drtArr = [];
	$("#director").parent().parent().find('span').each(function(i){
		var eachPsnId = $(this).find('input:eq(0)').val();		// psnId
		var eachName = $(this).text();							// 이름 // 그런데 이 이름이 일치해야 하잖어.
		if (eachName.indexOf(keyword)!== -1){
			var obj = {"NAME":eachName , "ID":eachPsnId}
			drtArr.push(obj);
		}
	})
	callback(drtArr);
}

// 연수회의 각 회차별 Director 자동검색
function autoSearcDtlDir(dtlDirector){
	
	var keyword = $(dtlDirector).val();						// 내가 검색한 이름.

	$(dtlDirector).autocomplete({
		source: function(request, response){
			autoSearchDtl(keyword, function(data){
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

			event.preventDefault();	
			
			$(dtlDirector).val(ui.item.id.trim());	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.

			var result = true;
		    var spanCnt  = $(this).parent().parent().find('span').length;
		    if (spanCnt > 0){
		    	$(this).parent().parent().find('span').each(function(i){
		    		var prevId = $(this).find('input:first').val();	// 이미 입력한 director들 id.
		    		if (prevId != ui.item.desc ){
		    			result = true;		// 새로운 거임.
		    			return true;	
		    		} else {
		    			result = false;		// 이미 있는거임.
		    			return false;
		    		}
		    	});		    	
		    } else {
		    	result = true;
		    }
		    
		    if (!result){
		    	alert('이미 입력한 Director입니다. 다른 Director을 입력해주세요.')
		    	$(this).val("").focus();		    	
		    } else {

		    	var schDtlNo = $(dtlDirector).closest('table').find('input:eq(0)').val();//해당 테이블의 회차
		    	$(this).parent().parent().append("<span class='txt'>"+ ui.item.id 
						+"<button type='button' class='del2' id='' onclick='deleteName(this);'></button>"
						+"<input type='hidden' name='' value="+ ui.item.desc +">" 	// psnId 원장키
						+"<input type='hidden' name='' value='04'>"		// mbrGb 멤버구분 - 04:연수회director 
						+"<input type='hidden' name='' value='"+ schDtlNo +"'></span>") 		// no: 연수회의 dtl은 상세회차번호를 넣어준다.
		    }
		}
	})
}


function fillForm(){
	
	formType = $("#sch_gb_01").val();
	var tempYn = $("#temp_yn").val();
	
	$("#schGb").val(formType);

	// 담당명이 다를 경우 기존 사번 삭제하기.
	var defaultName = $("#defaultName").val();
	var finalName = $("#writerName").val();
	if (finalName != defaultName ){
		$("#writerSabun").val("");
	}
	
	// 주관명이 다를 경우 기존 org_cd 삭제하기.
	var defaultOrg = $("#defaultOrgNm").val();
	var finalOrg = $("#finalOrgNm").val();
	if (defaultOrg != finalOrg ){
		$("#orgCd").val("");
	}
	
	
	if (formType == "01"){
		
		// 강연회 폼값 채우기. - 1. TB_SCH
		
		// (1) 시작,종료날짜
		var gb1Date = $("#gb01Date").val();
		var schStaTime = gb1Date + " "+ $("#sch_sta_time").val() +":00";
		var schFinTime = gb1Date + " "+ $("#sch_fin_time").val() +":00";

		$("#sch_sta_dt").val(schStaTime);		// 강연회 시작시간 
		$("#sch_fin_dt").val(schFinTime);		// 강연회 종료시간 

		// (2) 총 회차수
		$("#sch_no_cnt").val(1);	// 강연회 회차수는 무조건 1.
		// $("#sch_no_cnt").val($("#sch01List").children('tr').length);		// 강연회 회차수
		
		// 2.TB_SCH_DTL
		// (2-1) 각 디테일 날짜들.
		var mbrArr = [];
		
		$("#sch01List tr").each(function(index){

			// 강연회 시작, 종료시간 input에 넣기
			$(this).find('input:eq(0)').attr('name', 'schDtlList['+ index + '].no').val(index+1);
	 
			$(this).find('td:eq(0)').each(function(){
				var dtlStaTime = gb1Date +" "+ $(this).find("select:eq(0)").val() +":"+ $(this).find("select:eq(1)").val();
				$(this).find("input").val(dtlStaTime);
				$(this).find("input").attr('name', 'schDtlList['+index+'].sch_sta_dt');
			})

			$(this).find('td:eq(1)').each(function(){
				var dtlFinTime = gb1Date +" "+ $(this).find("select:eq(0)").val() +":"+ $(this).find("select:eq(1)").val();
				$(this).find("input").val(dtlFinTime);
				$(this).find("input").attr('name', 'schDtlList['+ index+ '].sch_fin_dt');
			})
			
			var subgb = false;
			// 값이 있을때만 회차를 붙여준다. 
			for (var i=2 ; i<5 ; i++){
				$(this).find('td:eq('+ i +')').each(function(){
					if($(this).find('input:eq(0)').val()!=''&&$(this).find('input:eq(0)').val()!=null){
						$(this).find('input:eq(3)').attr('name', 'schMbrList['+ index+ '].no').val(Number(index+1));	//회차넣기
						mbrArr.push($(this));
						subgb = true;
					}
				})
			}

			$(this).find('td:eq(5)').find('input').attr('name', 'schDtlList['+ index+ '].subject');
			
			
			if (subgb){		// 각 회차별로 연자가 한명 이상 있으면 true >> 연자주제를 같이 저장.
				$(this).find('td:eq(6)').find('select').attr('name', 'schDtlList['+ index+ '].sub_gb');
			} else {
				$(this).find('td:eq(6)').append("<input type='hidden' value name='schDtlList["+ index+ "].sub_gb'>")// 없으면 null로 처리.
			}
		
		})
		
		for (var i=0; i<mbrArr.length ; i++){
			var eachMbr = mbrArr[i];
			eachMbr.find('input:eq(1)').attr('name','schMbrList['+ i+ '].psn_id');
			eachMbr.find('input:eq(2)').attr('name','schMbrList['+ i+ '].mbr_gb');
			eachMbr.find('input:eq(3)').attr('name','schMbrList['+ i+ '].no');		// 회차
		}

		
	} else if (formType == "02"){
		// 연수회 폼값 채우기 - 
		// 1. TB_SCH 
		var sch02totalCnt = $("#gb02Cnt").val();
		$("#sch_no_cnt").val(sch02totalCnt);	// 총 회차수 입력.

		
		var dateArr = [];
		var mbrArr = [];
		
		// 2. TB_SCH_DTL
		
		var sch02cnt = $("#sch02List table").length;
		$("#sch02List table").each(function(index){
			
			var eachTbId = $(this).attr('id').substring(2);
			
			if( index < Number(sch02totalCnt)){

				$(this).find('input:eq(0)').attr('name', 'schDtlList['+ index + '].no');
				$(this).find('input:eq(1)').attr('name', 'schDtlList['+ index + '].sch_sta_dt');
				$(this).find('input:eq(2)').attr('name', 'schDtlList['+ index + '].sch_fin_dt');
				$(this).find('tr:eq(0) select:last').attr('name','schDtlList['+ index + '].sub_gb');
				$(this).find('tr:eq(2) td input').attr('name', 'schDtlList['+ index + '].content');

				//TextArea 줄바꿈처리 (엔터키에 <br>키를 추가).
				var eachContent = $(this).find('tr:eq(2) td textarea').val();
				eachContent = eachContent.replace(/\n/g, "<br />");
				$(this).find('tr:eq(2) td input').val(eachContent);
				
				// 연수회각회차 시간들
				if (tempYn == 'Y'){
					var sch02Dt = '9999-12-31';
				} else {
					var sch02Dt = $(this).find('tr:first td:first .date_input input').val();	// 날짜
				}
				
				var sch02stTm = sch02Dt + " " + $(this).find('tr:first td:first select:eq(0)').val() + ":00";	// 출발시간
				var sch02finTm = sch02Dt + " " + $(this).find('tr:first td:first select:eq(1)').val() + ":00";	// 종료시간
				$(this).find('input:eq(1)').val(sch02stTm);	// 시작시간
				$(this).find('input:eq(2)').val(sch02finTm);	// 종료시간
				dateArr.push(sch02stTm);
				dateArr.push(sch02finTm);
			
				$(this).find('tr:eq(1) td span').each(function(){		// 각 회차별 Director
					mbrArr.push($(this));
				});
			}
		})
		
		orderedDates = dateArr.sort(function(a,b){
			return Date.parse(a) > Date.parse(b);
		});
		var earlist = orderedDates[0];					// 가장 이른 시간
		var latest = orderedDates[dateArr.length - 1];	// 가장 늦은 시간 

		$("#sch_sta_dt").val(earlist);		// 강연회 시작시간 
		$("#sch_fin_dt").val(latest);		// 강연회 종료시간 
		
		// 3. TB_SCH_MBR (임시값 생성.)
		 
		$("#sch02drts span").each(function(){
			mbrArr.push($(this))
		})
		$("#sch02fct span").each(function(){
			mbrArr.push($(this));
		})
		for (var i=0;i<mbrArr.length;i++){
			var eachMbr = mbrArr[i];
			eachMbr.find('input:eq(0)').attr('name','schMbrList['+ i+ '].psn_id');
			eachMbr.find('input:eq(1)').attr('name','schMbrList['+ i+ '].mbr_gb');
			eachMbr.find('input:eq(2)').attr('name','schMbrList['+ i+ '].no');
		}
	}
	
}



function checkFormIfValid(){
	var tempYn = $("#temp_yn").val();
	
	var result = true;
	// 공통 - 제목, 장소, 주관, 담당이름이 비면 다시 쓰게끔 한다.
	var title = $("#title").val().trim();
	if( title =="" || title == null){
		alert("제목을 반드시 작성해주세요.");
		$("#title").focus();
		result = false;
		return result;
	} 
	
	var place = $("#place").val().trim();
	if( place =="" || place ==null){
		alert("장소를 반드시 작성해주세요.");
		$("#place").focus();
		result = false;
		return result;
	}  
	
	var orgNm = $("#finalOrgNm").val().trim();
	if( orgNm =="" || orgNm==null){
		alert("주관을 반드시 작성해주세요.");
		$("#finalOrgNm").focus();
		result = false;
		return result;
	}  
	var name = $("#writerName").val().trim();
	if( name =="" || name == null){
		alert("담당을 반드시 작성해주세요.");
		$("#writerName").focus();
		result = false;
		return result;
	}
	
	if (formType == "01"){
		
		if(tempYn == 'N'){
			var schStaDate = Number($("#sch_sta_time").val());
			var schFinDate = Number($("#sch_fin_time").val());
	
			if (schStaDate >= schFinDate){
				alert("강연회 일시 종료시간이 시작시간을 앞서고 있습니다. 다시 시간을 설정해주세요.");
				$("#sch_sta_time").val('00').focus();
				$("#sch_fin_time").val('00');
				result = false;
				return result;
			} 
		}
		
		$("#sch01List tr").each(function(index){
			
			if(tempYn == 'N'){
				// 강연회 각 회차마다
				// 1. 시간 앞뒤 확인 >> 해당줄에 각 시간 4개를 모두 0으로 맞추고 첫번째 시간에 focus.
				var tm1 = new Date(($(this).find('td:eq(0) input')).val()+":00");
				var tm2 = new Date(($(this).find('td:eq(1) input')).val()+":00");
				if (tm1 >= tm2){
					alert( index+1 + "회차의 종료시간이 시작시간보다 앞서고 있습니다. 해당 시간을 다시 설정해주세요.");
					$(this).find("select").val("00");	// 모든 셀렉트를 0으로 조정.
					$(this).find("td:eq(0) select:eq(0)").focus();
					result = false;
					return result;
				}
			}
			// 3. faculty 확정에 텍스트는 있지만 하단에 value가 없는 경우 - 텍스트 지우고 다시 쓰고 엔터로 선택하라고 안내.
			var fctText = $(this).find('td:eq(2) input:eq(0)').val().trim();
			if (fctText != "" && fctText != null){
				if(!$(this).find('td:eq(2) input:eq(1)').val()){
					
					alert( "Faculty 확정 이름은 자동완성 리스트 중 하나를 엔터키 또는 마우스 클릭으로 선택해서 입력해주세요.  "+
							(index+1) +"회차의 Faculty 확정 이름을 다시 입력해주세요. ");
				 result = false;
				 return result;
				}
			} 
			
			// 4. faculty 예비에 텍스트는 있지만 하단에 value가 없는 경우 - 텍스트 지우고 다시 쓰고 엔터로 선택하라고 안내.
			var fctText2 = $(this).find('td:eq(3) input:eq(0)').val().trim();
			if (fctText2 != "" && fctText2 != null){
				if(!$(this).find('td:eq(3) input:eq(1)').val()){
					alert( "Faculty 예비 이름은 자동완성 리스트 중 하나를 엔터키 또는 마우스 클릭으로 선택해서 입력해주세요.  "+
							(index+1) +"회차의 Faculty 예비 이름을 다시 입력해주세요. ");
				 result = false;
				 return result;
				}
			} 
			// 5. Moderator 에 텍스트는 있지만 하단에 value가 없는 경우 - 텍스트 지우고 다시 쓰고 엔터로 선택하라고 안내.
			var mdrText = $(this).find('td:eq(4) input:eq(0)').val().trim();
			if (mdrText != "" && mdrText != null){
				if(!$(this).find('td:eq(4) input:eq(1)').val()){
					alert( "Moderator 이름은 자동완성 리스트 중 하나를 엔터키 또는 마우스 클릭으로 선택해서 입력해주세요.  "+
							(index+1) +"회차의 Moderator 이름을 다시 입력해주세요. ");
				 result = false;
				 return result;
				}
			} 
			
			// 5. 주제가 없는 경우 - focus 맞추고 다시 쓰게 한다.
			var sbj = $(this).find('td:eq(5) input').val().trim();
			if (sbj == "" || sbj == null){
				alert( index+1 + "회차의 주제를 반드시 입력해주세요.");
				$(this).find('td:eq(5) input').focus();
				result = false;
				return result;
			} 
		})
	} else if (formType == "02"){
	
		// 연수회
		// 1. director , faculty가 한 명 이상 있는지 확인할 것.
		if($("#sch02drts span").html() == null || $("#sch02drts span").html() == ""){
			alert("Director의 자동완성 리스트 중 한 명 이상을 마우스로 클릭 또는 엔터키로 선택하여 " +
					"한 명 이상의 Director를 추가해주세요.");
			$("#sch02drts div input").focus();
			result = false;
			return result;
		};
		if($("#sch02fct span").html() == null || $("#sch02fct span").html() == ""){
			alert("Faculty의 자동완성 리스트 중 한 명 이상을 마우스로 클릭 또는 엔터키로 선택하여 " +
			"한 명 이상의 Faculty를 추가해주세요.");
			$("#sch02fct div input").focus();
			result = false;
			return result;
		};
		
		var sch02DtlCnt = $("#sch_no_cnt").val();		// 총 회차수. 이만큼만 체크하는거다.
		
		$("#sch02List table").each(function(index){
			// 2. 각 회차의 시간 종료시간이 빠르면 둘 다 00시로 초기화 시킨 후 첫번째 시간에 포커스.
			if(index < sch02DtlCnt){
				
				if (tempYn == 'N'){
					var tm1 = $(this).find("tr:first td .sel:eq(0)").val();
					var tm2 = $(this).find("tr:first td .sel:eq(1)").val();
					if (tm1 >= tm2){
						var theTab = "ck"+(index+1);
						chgCkTab (theTab);
						$(this).find("tr:first td select:eq(0)").val("00").focus();
						$(this).find("tr:first td select:eq(1)").val("00");
						alert(index+1 + " 회차의 종료시간이 시작시간보다 앞서고 있습니다. 해당 시간을 다시 설정해주세요. ");
						result = false;
						return result;
					}
				}
				// 3. 강의내용이 작성되었는지 확인. 안 되었으면 포커스.
				var content = $(this).find("tr:eq(2) td textarea").val();
				if (content == "" || content ==null){
					var theTab = "ck"+(index+1);
					chgCkTab (theTab);
					alert(index+1 + " 회차의 강의내용을 반드시 작성해주세요.");
					$(this).find("tr:eq(2) td textarea").focus();
					result = false;
					return result;
				}
				
				// 4. 한 회당 최소 한명이상의 director가 있는지 확인. 
				var dtlDrt = $(this).find('tr:eq(1) span').html();
				if (dtlDrt == "" || dtlDrt == null){
					var theTab = "ck"+(index+1);
					chgCkTab (theTab);
					alert(index+1 + " 회차의 Director를 반드시 한 명 이상 추가해주세요.");
					$(this).find("tr:eq(1) td div input").focus();
					result = false;
					return result;
				}
			}
		});
		
	};
	return result;
}

// 폼 입력 이벤트.
function submitForm(type){
	
	// 업데이트시 
	if (type == 'delete'){
		var checkIfDelete = confirm("해당일정을 삭제하시겠습니까?");
		if (checkIfDelete){
			alert('해당 일정의 삭제가 진행됩니다.')
			$("#frm1").attr('action', WEB_ROOT + '/sch/sch.do?method=schDeleteView');
			$("#frm1").submit();
		} else {
			alert("삭제가 취소되었습니다.");
			return false;
		}
		
	} else {
		
		fillForm();
		var theResult = checkFormIfValid();
		if (theResult){

			var checkConfirm = confirm("내용을 입력하시겠습니까? 입력하신 후에는 모든 내용이 초기화됩니다.");
			if(checkConfirm){
				if (type == 'update'){
					alert("기존 일정을 새로운 내용으로 업데이트합니다.");
					$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method=schUpdateView');
					$("#frm2").submit();
				} else if (type == 'copyTmp'){
					alert("기존 일정을 새로운 템플릿으로 저장합니다.");
					$("#temp_yn").val('Y');
					$("#sch_sta_dt").val('9999-12-31');
					$("#sch_fin_dt").val('9999-12-31');
					$("#reg_dt").val("");
					$("#reg_id").val("");
					$("#mod_id").val("");
					
					var gb = $("#sch_gb_01").val();
					if (gb == '01'){
						$("#sch01List tr").each(function(index){
							$(this).find('td:eq(0)').find('input').val("9999-12-31 00:00");
							$(this).find('td:eq(1)').find('').val("9999-12-31 00:00");
						});
					} else {
						$("#sch02List table").each(function(index){
							$(this).find('input:eq(1)').val("9999-12-31 00:00");	// 시작시간
							$(this).find('input:eq(2)').val("9999-12-31 00:00");	// 종료시간
						})
					}
					$("#frm2").attr('action', WEB_ROOT + '/sch/sch.do?method=schCopyToTempletView');
					$("#frm2").submit();
				}  
			} else {
				alert("입력이 취소됩니다.");
				$("#title").focus();
				return false;
			}
		}
	}
	
};


// 연수회 하단 탭 누르면 해당 탭으로 이동.
function chgCkTab (theTab){
	theTab = theTab.substring(2);
	
	// 1. 기존 탭 class 지우고 지금 탭에 on 넣고
	$('#ckList ul li[class*=on]').removeClass();
	$("#ck"+theTab).parent().addClass('on');
	
	// 2. 기존 탭 오른쪽 hide하고 지금 탭 show한다.
	$("#sch02List table:visible").css("display","none");;
	$("#tb"+theTab).css("display","");
	
}


//연수회 회차 탭 선택
function changeTotalCntNo(totalCnt){

	$('#ckList ul li[class*=on]').removeClass();
	for (var i = 1; i <= 50 ; i++ ){
		if (i == totalCnt){
			$("#tb"+i).css("display","");
			$("#ck"+totalCnt).parent().addClass('on');
		} else {
			$("#tb"+i).css("display","none");;			
		}

		if (i <= totalCnt){
			$("#ck"+i).css("display","");;
		} else {
			$("#ck"+i).css("display","none");;
		}
	}
	
}

//구분 1 변경 
function schgb1Change(schGb1){
	
	$("#schGb1").val(schGb1);
	$("#frm1").submit();
}


//강연회 버튼 추가 - row 추가 
function addRow(but){
	var clickedRow = $(but).parent().parent();
	var newrow = $(but).parent().parent().clone();
	newrow.find("input[type=text]").val("")
	newrow.insertAfter(clickedRow);
}

//강연회 버튼 삭제 - row 삭제 
function deleteRow(but){
	
	var cntTr = $("#sch01List tr").length -1;
	if (cntTr < 1){
		alert("더 이상 삭제가 불가능합니다.");
		return;
	}
	$(but).parent().parent().remove();
}

// Director, Faculty 삭제
function deleteName(aName){
	$(aName).parent().remove();
	// $(aName).parent().remove();
	
}

function chgTimeToMilisecond(theTime){
	// theTime >> HH:mm 형식의 텍스트. 이 function은 앞에 yyyy-mm-dd를 구해 더해서 miliSecond로 전환해준다. 
	var eachTm = $("#gb01Date").val() + " " + $(theTime).val()	// 시+분 
	var miliTm = $(theTime).next().val(eachTm);		// hidden에 시간을 넣어두었다.
	return miliTm;
}

function autoSearcDir(director){
	
	// (2) 연수회 Director 자동완성
	$(director).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(director).val()), 'name', '04', function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME  ,  
						label:item.NAME +" - " +item.HOSNAME,			 
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
			
			$(director).val(ui.item.id);	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.
			
		    var result = true;
		    var spanCnt  = $("#sch02drts span").length;
		    if (spanCnt > 0){
		    	$("#sch02drts span").each(function(i){
		    		var prevId = $(this).find('input:first').val();	// 이미 입력한 director들 id.
		    		if (prevId != ui.item.desc ){
		    			result = true;		// 새로운 거임.
		    			return true;	
		    		} else {
		    			result = false;		// 이미 있는거임.
		    			return false;
		    		}
		    	});		    	
		    } else {
		    	result = true;
		    }
		    
		    if (!result){
		    	alert('이미 입력한 Director입니다. 다른 Director을 입력해주세요.')
		    	$(director).val("").focus();		    	
		    } else {
			    $("<span class='txt'>"+ ui.item.id 
						+"<button type='button' class='del2' id='' onclick='deleteName(this);'></button>"
						+"<input type='hidden' name='' value="+ ui.item.desc +">" 	// psnId 원장키
						+"<input type='hidden' name='' value='04'>"		// mbrGb 멤버구분 - 04:연수회director 
						+"<input type='hidden' name='' value='0'></span>") 		// no: 연수회 회차는 무조건 0
						.insertAfter($(director).parent());
		    }
		    // 만약 여기서 이미 동일한 멤버를 또 입력한다고 할 경우
		    // 입력이 안 되게 막아야지.
		    return false;
		}
	})
	
}

function autoSearcFct(faculty){
	
	// (3) 연수회 faculty 자동완성
	$(faculty).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(faculty).val()), 'name', '05', function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME ,  
						label:item.NAME +" - " +item.HOSNAME,			 
						desc:item.ID 
					}
				}))
			})
		},
		minLength:1,
		focus: function (event, ui){
			event.preventDefault();
		},
		select: function( event, ui ) {
			event.preventDefault(); 
			
			$(faculty).val(ui.item.id);	// 자동완성리스트중 하나를 선택해서 엔터를 치면 하는 이벤트.

			var result = true;
			var spanCnt  = $("#sch02fct span").length;
			 if (spanCnt > 0){
		    	$("#sch02fct span").each(function(i){
		    		var prevId = $(this).find('input:first').val();	// 이미 입력한 director들 id.
		    		if (prevId != ui.item.desc ){
		    			result = true;		// 새로운 거임.
		    			return true;	
		    		} else {
		    			result = false;		// 이미 있는거임.
		    			return false;
		    		}
		    	});		    	
		    } else {
		    	result = true;
		    }
			 
			 if (!result){
		    	alert('이미 입력한 Faculty입니다. 다른 Faculty을 입력해주세요.')
		    	$(faculty).val("").focus();		    	
		    } else {
		    	 $("<span class='txt'>"+ ui.item.id 
		    		+"<button type='button' class='del2' id='' onclick='deleteName(this);'></button>"
		    		+"<input type='hidden' name='' value="+ ui.item.desc +">" 	// psnId 원장키
		    		+"<input type='hidden' name='' value='05'>"	// mbrGb 멤버구분 - 05:연수회faculty 
		    		+"<input type='hidden' name='' value='0'></span>")	// no: 연수회 회차는 무조건 0
		    		.insertAfter($(faculty).parent());
		    } 
		}
	})

}

// 강연회 자동완성
function autoSearchName(theName){
	
	var mbrType = $(theName).attr('class').substring(3,5);
	
	$(theName).autocomplete({
		source: function(request, response){
			autoSearch(encodeURIComponent($(theName).val()), 'name', mbrType, function(data){
				response($.map(data, function(item){
					return{
						id: item.NAME  ,  
						label:item.NAME +" - " +item.HOSNAME,			 
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
			
			var nm = ui.item.id;
			$(theName).val(nm);
			
		    if($(theName).next().is("input")){		// 뭔가 내용이 있으면 덮어씌우고  
		    	$(theName).next().val(ui.item.desc);
		    } else {								// 없으면 새로 추가한다.
		    	$("<input type='hidden' name='' value='"+ ui.item.desc +"'>" 	// psn_id 원장키
			    	+"<input type='hidden' name='' value='"+ mbrType +"'>"		// mbr_gb 멤버구분 
			    	+"<input type='hidden' name='' value=''>")		// 회차
		    	.insertAfter($(theName));
		    }
		}
	
		
	})

}































//구분 2 선택시 -> textbox 변경 
function textYn(id){
	if(jQuery('#sch_gb_01 option:selected').val() =="02" && id == "02"){//강연회에서 보수교육은 주관이랑 담당이 변경 가능
		ResetAll();
		jQuery('input[name=busu]').attr("readonly",false);
		jQuery('input[name=damdang]').attr("readonly",false);
	}else{
		ResetAll();
		jQuery('input[name=busu]').attr("readonly",true);
		jQuery('input[name=damdang]').attr("readonly",true);
	}
	
}

//전체 리셋
function ResetAll(){
	$("#frm1").find("input[type=text],textarea").val('');
	$("#frm2").find("input[type=text],textarea").val('');
}

//강연회 기본정보 insert 
function saveLect(){
	var start_date = null;
	var end_date = null;
	
	$("#detailtable tbody tr").not('tr[id^=Workshoptr]').each(function(){
		var fields = $(this).find("input,select").serializeArray();
		
		for (i=0; i<fields.length; i++) { // acceesing data array
			if((fields[i].name == "start_date")){
				start_date =  fields[i].value;
			}
			
			if((fields[i].name == "end_date")){
				end_date =  fields[i].value;
			}
		}
	});
	
	if(start_date >= end_date){
		$("#end_date").focus();
		alert("종료시간이 시작시간보다 작습니다.");
		return false;
	}
	
}
