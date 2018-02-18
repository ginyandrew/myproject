$(function() {
	$("button#get-user").click(function() {
		$("#data-title").text($(this).text());
		getUser("NO", "DESC");
	});
	
	$("button#get-city").click(function() {
		$("#data-title").text($(this).text());
		getCity();
	});
	
	$("button#get-dest").click(function() {
		$("#data-title").text($(this).text());
		getDest();
	});

	$("button#get-plan").click(function() {
		$("#data-title").text($(this).text());
		getPlan();
	});
	
	$(".do-btn").click(function() {
		$(this).parents(".modal-content").find("form").submit();
	});
	
	$(".remove-btn").click(function() {
		var type = $(this).parents(".modal-content").find("[name='type']").val();
		var removeNo = $(this).parents(".modal-content").find("[name='removeNo']").val();
		window.location.href = type + "/remove.tm?data=" + removeNo;
	});
});

function getPlan() {
	$.ajax({
		url: "plan.tm"
		,type: "GET"
		,dataType: "json"
		,success: function(result) {
			var data = result.plans.reverse();
			
			clearTable();

			makePlanTable(data);
		}
	});
}

function getDest() {
	var headData =
		[
		 	{name: "No", width: "50px"}
		 	,{name: "Name", width: "100px"}
		 	,{name: "City", width: "100px"}
		 	,{name: "Address"}
		 	,{name: "Lat", width: "100px"}
		 	,{name: "Lng", width: "100px"}
		 	/*,{name: "수정", width: "50px"}
		 	,{name: "삭제", width: "50px"}*/
		];
	$.ajax({
		url: "dest.tm"
		,type: "GET"
		,dataType: "json"
		,success: function(result) {
			var data = result.dests.reverse();
			//var nameArray = ["no", "name", "city", "address", "lat", "lng", "details", "contact", "openTime", "site"];
			/*var cityList = result.cityList;
			var $cityList = $("<select></select>").attr("name", "city.no");
			$.each(cityList, function(index, item) {
				$cityList.append($("<option></option>").val(item.no).text(item.name));
			});*/
			clearTable();
			//makeTable("dest", headData, bodyData, nameArray, $cityList);
			makeDestTable(data);
		}
	});	
}

function getCity() {
	var headData =
		[
		 	{name: "No", width: "50px"}
		 	,{name: "Name", width: "100px"}
		 	,{name: "Lat", width: "100px"}
		 	,{name: "Lng", width: "100px"}
		 	,{name: "State", width: "50px"}
		 	,{name: "Details"}
		 	/*,{name: "수정", width: "50px"}
		 	,{name: "삭제", width: "50px"}*/
		];
	$.ajax({
		url: "city.tm"
		,type: "GET"
		,dataType: "json"
		,success: function(result) {
			var data = result.cities;

			clearTable();
			makeCityTable(data);
		}
	});	
}
function getUser(orderBy, orderType) {
	var headData =
		[
		 	{name: "No", width: "50px"}
		 	,{name: "Name", width: "160px"}
		 	,{name: "Email"}
		 	,{name: "Gender", width: "50px"}
		 	,{name: "RegDate", width: "160px"}
		 	,{name: "Point", width: "100px"}
		 	/*,{name: "수정", width: "50px"}
		 	,{name: "삭제", width: "50px"}*/
	    ];
	
	$.ajax({
		url: "user.tm"
		,type: "GET"
		/*,data: {
			orderBy: orderBy||"NO",
			orderType: orderType||"DESC"
		}*/
		,dataType: "json"
		,success: function(result) {
			var data = result.users.reverse();
				/*[
				 	{
				 		no: 99999999
					 	,name: "고길동"
					 	,email: "kk@naver.com"
					 	,gender: "남"
					 	,regdate: "1234.12.34 01:23:45"
					 	,point: 99999999
				 	}
				 ]*/
			//var nameArray = ["no", "name", "email", "gender", "regdate", "point"];
			clearTable();
			//makeTable("user", headData, bodyData, nameArray);
			makeUserTable(data);
		}
	});	
}

function clearTable() {
	$("div#button-area").empty();
	$("table#data-table thead").empty();
	$("table#data-table tbody").empty();
}

function makeUserTable(data) {
	var $btnArea = $("div#button-area");
	var $thead = $("table#data-table thead");
	var $tbody = $("table#data-table tbody");
	var type = "user";
	
	// 상단 추가/삭제 버튼
	var $delBtn = $("<button class='btn btn-default pull-right' id='delete-modal-btn' disabled='disabled'>선택정보 삭제</button>")
		.click(function(event) {
		var removeIndex = getCheckedIndex();
		if(!removeIndex.length)	return;

		// TODO:
		var removeNo = [];
		$.each(removeIndex, function(index, item){
			removeNo.push($tbody.find("tr#data-"+item+" td:first").text());
		});
		
		$("div#remove-modal .modal-body input[name='type']").val("user");
		$("div#remove-modal .modal-body input[name='removeNo']").val(removeNo);
		
		$("div#remove-modal").modal("show");
	});
	
	var $addBtn = $("<button class='btn btn-default pull-right' id='add-btn'>추가</button>").click(function(event) {
		$("div#user-add-modal").modal("show");
	});
	
	var $selectAllBtn = $("<button class='btn btn-default pull-right' id='selectall-btn'>모두 선택</button>").click(function(event) {
		$tbody.find("input:checkbox").each(function(event){
			this.checked = true;
		});
		
		$delBtn.removeAttr("disabled");
		$sendMsgBtn.removeAttr("disabled");
		
	});
	
	var $sendMsgBtn = $("<button class='btn btn-default pull-right' id='sendmsg-btn' disabled='disabled'>쪽지 보내기</button>").click(function(event) {
		var checkedIndex = getCheckedIndex();
		if(!checkedIndex.length)	return;
		
		var checkedUserNo = [];
		var checkedUserName = [];
		$.each(checkedIndex, function(index, item){
			checkedUserNo.push($tbody.find("tr#data-"+item+" td:first").text());
			checkedUserName.push($tbody.find("tr#data-"+item+" td:eq(1)").text());
		});
		
		$("div#user-message-modal input[name='usernolist']").val(checkedUserNo);
		$("div#user-message-modal input#usernamelist").val(checkedUserName);
		$("div#user-message-modal").modal("show");
	});
	
	$btnArea.append($delBtn).append($addBtn).append($selectAllBtn).append($sendMsgBtn);
	
	
	// 테이블 헤더
	var $headTr = $("<tr></tr>");
	
	$headTr.append($("<th></th>").text("No").css("width", "50px"))
		.append($("<th></th>").text("Name").css("width", "160px"))
		.append($("<th></th>").text("Email"))
		.append($("<th></th>").text("Gender").css("width", "50px"))
		.append($("<th></th>").text("RegDate").css("width", "160px"))
		.append($("<th></th>").text("Point").css("width", "100px"))
		.append($("<th></th>").text("수정").css("width", "50px"))
		.append($("<th></th>").text("선택").css("width", "50px"));
	$thead.append($headTr);
		
	// 테이블 바디
	$.each(data, function(index, item) {
		if(item.no == 1)	return true;
		
		var $bodyTr = $("<tr></tr>").attr("id", "data-"+index);
		
		// 탈퇴 회원 구분
		var isDropped = item["isDropped"]=='T';
		
		// 사용자의 정보 출력
		$bodyTr.append($("<td></td>").html(item.no))
			.append($("<td></td>").html(item.name))
			.append($("<td></td>").html(item.email))
			.append($("<td></td>").html(item.gender=='M'?'남':(item.gender=='F'?'여':'-')))
			.append($("<td></td>").html(getFormattedDate(item.regdate)))
			.append($("<td></td>").html($.number(item.point)));
		
		if(isDropped) {
			// 탈퇴 회원이라면 테이블의 색상 변경
			$bodyTr.css("background-color", "#ff9999");
			$bodyTr.append($("<td></td>")).append($("<td></td>"));
		} else {
			// 수정 버튼
			var $modifyBtn = $('<button class="btn btn-xs btn-default">수정</button>').click(function(event) {
				var dataLocaId = $(this).parents("tr").attr("id");
				
				var $form = $("form#user-modify-form");
				$form.find("input[name='no']").val($("tr#"+dataLocaId).find("td:eq(0)").text());
				$form.find("input[name='name']").val($("tr#"+dataLocaId).find("td:eq(1)").text());
				$form.find("input[name='email']").val($("tr#"+dataLocaId).find("td:eq(2)").text());
				$form.find("select[name='gender']").val($("tr#"+dataLocaId).find("td:eq(3)").text() == "여"?"F":"M");
				$form.find("input[name='point']").val($("tr#"+dataLocaId).find("td:eq(5)").text().replace(/,/g, ""));
				
				$("div#user-modify-modal").modal("show");
			});
			
			// 체크 박스
			var $checkBox = $('<input type="checkbox" name="select" />').click(function(event){
				if($("table#data-table tbody input[name='select']:checked").length < 1) {
					$("button#delete-modal-btn").attr("disabled", "disabled");
					$("button#sendmsg-btn").attr("disabled", "disabled");
				} else {
					$("button#delete-modal-btn").removeAttr("disabled");
					$("button#sendmsg-btn").removeAttr("disabled");
				}
			});
			$bodyTr.append($("<td></td>").append($modifyBtn))
				.append($("<td></td>").append($checkBox));
		}
		
		
		$tbody.append($bodyTr);
	});
}

function makeCityTable(data) {
	var $btnArea = $("div#button-area");
	var $thead = $("table#data-table thead");
	var $tbody = $("table#data-table tbody");
	var type = "city";
	
	// 상단 추가/삭제 버튼
	var $delBtn = $("<button class='btn btn-default pull-right' id='delete-modal-btn' disabled='disabled'>선택정보 삭제</button>")
		.click(function(event) {
		var removeIndex = getCheckedIndex();
		if(!removeIndex.length)	return;
		
		// TODO:
		var removeNo = [];
		$.each(removeIndex, function(index, item){
			removeNo.push($tbody.find("tr#data-"+item+" td:first").text());
		});
		
		$("div#remove-modal .modal-body input[name='type']").val("city");
		$("div#remove-modal .modal-body input[name='removeNo']").val(removeNo);
		
		$("div#remove-modal").modal("show");
	});
	
	var $addBtn = $("<button class='btn btn-default pull-right' id='add-btn' disabled='disabled'>추가</button>").click(function(event) {
		$("div#city-add-modal").modal("show");
	});
	var $selectAllBtn = $("<button class='btn btn-default pull-right' id='selectall-btn' disabled='disabled'>모두 선택</button>")
		.click(function(event) {
		$tbody.find("input:checkbox").each(function(event){
			this.checked = true;
		});
		
		$delBtn.removeAttr("disabled");		
	});
	
	$btnArea.append($delBtn).append($addBtn).append($selectAllBtn);
	
	
	// 테이블 헤더
	var $headTr = $("<tr></tr>");
	
	$headTr.append($("<th></th>").text("No").css("width", "50px"))
		.append($("<th></th>").text("Name").css("width", "70px"))
		.append($("<th></th>").text("Lat").css("width", "100px"))
		.append($("<th></th>").text("Lng").css("width", "100px"))
		.append($("<th></th>").text("State").css("width", "70px"))
		.append($("<th></th>").text("Details"))
		.append($("<th></th>").text("수정").css("width", "50px"));	
	$thead.append($headTr);
		
	// 테이블 바디
	$.each(data, function(index, item) {
		var $bodyTr = $("<tr></tr>").attr("id", "data-"+index);
		
		// 도시의 정보 출력
		var LENGTH = 22;
		$bodyTr.append($("<td></td>").html(item.no))
			.append($("<td></td>").html(item.name))
			.append($("<td></td>").html(item.lat))
			.append($("<td></td>").html(item.lng))
			.append($("<td></td>").html("<span data-code='"+item.stateCode.code+"'>"+item.stateCode.name+"</span>"))
			.append($("<td></td>").html(!item.details?"-":(item.details.substring(0,LENGTH) + (item.details.length>LENGTH?"...":""))))
			.append($("<input type='hidden' />").attr("name", "hidden-details").val(item.details))
			.append($("<input type='hidden' />").attr("name", "hidden-img-count").val(item.imgNameList.length));
		
			
		// 수정 버튼
		var $modifyBtn = $('<button class="btn btn-xs btn-default">관리</button>').click(function(event) {
			var dataLocaId = $(this).parents("tr").attr("id");
			
			var $form = $("form#city-modify-form");
			
			$form.find("input[name='no']").val($("tr#"+dataLocaId).find("td:eq(0)").text());
			$form.find("input[name='name']").val($("tr#"+dataLocaId).find("td:eq(1)").text());
			$form.find("input[name='lat']").val($("tr#"+dataLocaId).find("td:eq(2)").text());
			$form.find("input[name='lng']").val($("tr#"+dataLocaId).find("td:eq(3)").text());
			$form.find("select[name='stateCode.code']").val($("tr#"+dataLocaId).find("td:eq(4) span").attr("data-code"));
			$form.find("[name='details']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-details']").val());
			var imgCount = $("tr#"+dataLocaId).find("input:hidden[name='hidden-img-count']").val();
			$form.find("span.img-count").text(imgCount?imgCount:"-");
			
			$("div#city-modify-modal").modal("show");
		});
		
		$bodyTr.append($("<td></td>").append($modifyBtn));			
		$tbody.append($bodyTr);
	});
}

function makeDestTable(data) {
	var $btnArea = $("div#button-area");
	var $thead = $("table#data-table thead");
	var $tbody = $("table#data-table tbody");
	var type = "dest";
	
	// 상단 추가/삭제 버튼
	var $delBtn = $("<button class='btn btn-default pull-right' id='delete-modal-btn' disabled='disabled'>선택정보 삭제</button>")
		.click(function(event) {
			var removeIndex = getCheckedIndex();
			if(!removeIndex.length)	return;
			
			// TODO:
			var removeNo = [];
			$.each(removeIndex, function(index, item){
				removeNo.push($tbody.find("tr#data-"+item+" td:first").text());
			});
			
			$("div#remove-modal .modal-body input[name='type']").val("dest");
			$("div#remove-modal .modal-body input[name='removeNo']").val(removeNo);
			
			$("div#remove-modal").modal("show");
	});
	
	var $addBtn = $("<button class='btn btn-default pull-right' id='add-btn'>추가</button>").click(function(event) {
		$("div#dest-add-modal").modal("show");
	});

	$btnArea.append($delBtn).append($addBtn);
	
	
	// 테이블 헤더
	var $headTr = $("<tr></tr>");
	
	$headTr.append($("<th></th>").text("No").css("width", "50px"))
		.append($("<th></th>").text("Name"))
		.append($("<th></th>").text("City").css("width", "70px"))
		.append($("<th></th>").text("Lat").css("width", "100px"))
		.append($("<th></th>").text("Lng").css("width", "100px"))
		.append($("<th></th>").text("관리").css("width", "50px"))
		.append($("<th></th>").text("선택").css("width", "50px"));
	$thead.append($headTr);
		
	// 테이블 바디
	$.each(data, function(index, item) {
		var $bodyTr = $("<tr></tr>").attr("id", "data-"+index);
		
		// 여행지의 정보 출력
		$bodyTr.append($("<td></td>").html(item.no))
			.append($("<td></td>").html(item.name))
			.append($("<td></td>").html("<span data-no='"+item.city.no+"'>"+item.city.name+"</span>"))
			.append($("<td></td>").html(item.lat))
			.append($("<td></td>").html(item.lng))
			.append($("<input type='hidden' />").attr("name", "hidden-address").val(item.address))
			.append($("<input type='hidden' />").attr("name", "hidden-details").val(item.details))
			.append($("<input type='hidden' />").attr("name", "hidden-contact").val(item.contact))
			.append($("<input type='hidden' />").attr("name", "hidden-openTime").val(item.openTime))
			.append($("<input type='hidden' />").attr("name", "hidden-site").val(item.site))
			.append($("<input type='hidden' />").attr("name", "hidden-img-count").val(item.imgNameList.length));
		
		$.each(item.categoryList, function(i, category) {
			$bodyTr.append($("<input type='hidden' />").attr("name", "hidden-category"+ (i>0?""+(i+1):"")).val(category.code));
		});	
			
		// 수정 버튼
		var $modifyBtn = $('<button class="btn btn-xs btn-default">관리</button>').click(function(event) {
			var dataLocaId = $(this).parents("tr").attr("id");
			
			var $form = $("form#dest-modify-form");

			$form.find("input[name='no']").val($("tr#"+dataLocaId).find("td:eq(0)").text());
			$form.find("input[name='name']").val($("tr#"+dataLocaId).find("td:eq(1)").text());
			$form.find("select[name='city.no']").val($("tr#"+dataLocaId).find("td:eq(2) span").attr("data-no"));
			$form.find("input[name='lat']").val($("tr#"+dataLocaId).find("td:eq(3)").text());
			$form.find("input[name='lng']").val($("tr#"+dataLocaId).find("td:eq(4)").text());
			$form.find("input[name='address']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-address']").val());
			$form.find("textarea[name='details']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-details']").val());
			$form.find("input[name='contact']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-contact']").val());
			$form.find("input[name='openTime']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-openTime']").val());
			$form.find("input[name='site']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-site']").val());
			$form.find("select[name='category']").val($("tr#"+dataLocaId).find("input:hidden[name='hidden-category']").val());
			var imgCount = $("tr#"+dataLocaId).find("input:hidden[name='hidden-img-count']").val();
			$form.find("span.img-count").text(imgCount?imgCount:"-");
			
			var subCategory = $("tr#"+dataLocaId).find("input:hidden[name='hidden-category2']").val();
			$form.find("select[name='categorysub']").val(subCategory?subCategory:"");
			
			$("div#dest-modify-modal").modal("show");
		});
		
		// 체크 박스
		var $checkBox = $('<input type="checkbox" name="select" />').click(function(event){
			if($("table#data-table tbody input[name='select']:checked").length < 1) {
				$("button#delete-modal-btn").attr("disabled", "disabled");
			} else {
				$("button#delete-modal-btn").removeAttr("disabled");
			}
		});
		
		$bodyTr.append($("<td></td>").append($modifyBtn))
			.append($("<td></td>").append($checkBox));
			
		$tbody.append($bodyTr);
	});
}

function makePlanTable(data, $ajaxData) {
	var $btnArea = $("div#button-area");
	var $thead = $("table#data-table thead");
	var $tbody = $("table#data-table tbody");
	var type = "plan";
	
	// 상단 추가/삭제 버튼
	var $delBtn = $("<button class='btn btn-default pull-right' id='delete-modal-btn' disabled='disabled'>선택정보 삭제</button>")
		.click(function(event) {
		var removeIndex = getCheckedIndex();
		if(!removeIndex.length)	return;
		
		// TODO:
		var removeNo = [];
		$.each(removeIndex, function(index, item){
			removeNo.push($tbody.find("tr#data-"+item+" td:first").text());
		});
		
		$("div#remove-modal .modal-body input[name='type']").val("plan");
		$("div#remove-modal .modal-body input[name='removeNo']").val(removeNo);
		
		$("div#remove-modal").modal("show");
	});
	
	var $addBtn = $("<button class='btn btn-default pull-right' id='add-btn' disabled='disabled'>추가</button>").click(function(event) {
		
	});

	$btnArea.append($delBtn).append($addBtn);
	
	
	// 테이블 헤더
	var $headTr = $("<tr></tr>");
	
	$headTr.append($("<th></th>").text("No").css("width", "50px"))
		.append($("<th></th>").text("Title"))
		.append($("<th></th>").text("UserName").css("width", "100px"))
		.append($("<th></th>").text("Date").css("width", "200px"))
		.append($("<th></th>").text("Theme").css("width", "100px"))
		.append($("<th></th>").text("Copy").css("width", "50px"))
		.append($("<th></th>").text("상세").css("width", "50px"))
		.append($("<th></th>").text("선택").css("width", "50px"));
	$thead.append($headTr);
		
	// 테이블 바디
	$.each(data, function(index, item) {
		var $bodyTr = $("<tr></tr>").attr("id", "data-"+index);
		
		// 일정의 정보 출력
		$bodyTr.append($("<td></td>").html(item.no))
			.append($("<td></td>").html(item.title))
			.append($("<td></td>").html(item.user.name))
			.append($("<td></td>").html(getFormattedDate(item.leaveDate, "short") + "~" + getFormattedDate(item.arriveDate, "short") + " (" + item.period +"일)"))
			.append($("<td></td>").html(item.themeCodeToString))//"<span data-code='theme-"+item.themeCode+"'>"+item.themeCodeToString+"</span>"))
			.append($("<td></td>").html(item.copyCount))
			//.append($("<input type='hidden' />").attr("name", "hidden-leaveDate").val(item.leaveDate))
			//.append($("<input type='hidden' />").attr("name", "hidden-period").val(item.period))
			//.append($("<input type='hidden' />").attr("name", "hidden-user.no").val(item.user.no))
			// 일정 상세 숨겨서 출력 .append()
		
		// 일정 상세 버튼
		var $planDetailBtn = $('<button class="btn btn-xs btn-default">이동</button>').click(function(event) {
			window.open("/plan/detail.tm?no="+item.no);
		});
		// 수정 버튼
		//var $modifyBtn = $('<button class="btn btn-xs btn-default">수정</button>').click(function(event) {});
		
		// 체크 박스
		var $checkBox = $('<input type="checkbox" name="select" />').click(function(event){
			if($("table#data-table tbody input[name='select']:checked").length < 1) {
				$("button#delete-modal-btn").attr("disabled", "disabled");
			} else {
				$("button#delete-modal-btn").removeAttr("disabled");
			}
		});
		
		$bodyTr.append($("<td></td>").append($planDetailBtn))
			.append($("<td></td>").append($checkBox));
			
		$tbody.append($bodyTr);
	});
}

function getCheckedIndex() {
	var checkedIndex = [];
	$("table#data-table tbody input[name='select']:checked").parents("tr").each(function(index,item){
		var id = $(item).attr("id").replace(/data-/, "");
		checkedIndex.push(id);
	});
	return checkedIndex;
}