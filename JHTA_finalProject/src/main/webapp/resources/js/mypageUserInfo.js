$(function(){
	
	//회원정보 업데이트 
	$.ajax({
		url  : "mypageuserinfo.tm",
		type : 'GET',
		dateType : 'json',
		success :function(data){
			var LOGIN_USER = data.LOGIN_USER;
			$("#userNo-form").val(LOGIN_USER.no);
			$("#userPassword-form").val(LOGIN_USER.password);
			
			$("#email-form").attr("value", LOGIN_USER.email);
			$("#name-form").attr("value", LOGIN_USER.name);
			//남녀확인
			var gender = $("#gender-radio").children();
			(LOGIN_USER.gender == 'F')? gender.first().attr("checked", true) : gender.last().attr("checked", true); 
			
			//탈퇴
			$("#user-delete").attr("href", "/userdelete.tm?no="+LOGIN_USER.no);
			//회원정보수정
			/*$("#userInfo-button").attr("href", "/mypageUpdate.tm?name="+LOGIN_USER.name+" &email="+LOGIN_USER.email+"&gender="+LOGIN_USER.gender+" &point="+LOGIN_USER.point);*/
		}
	})
	
	//탈퇴여부
	$("#user-delete").click(function(event){
		var userDel = confirm("탈퇴하시겠습니까?");
		
		if(!userDel){
			 event.preventDefault();
		}
	});
	
	//회원정보수정
	$("#userInfo-button").click(function(){
		var userDel = confirm("수정하시겠습니까?");
		
		if(!userDel){
			event.preventDefault();
			return;
		}
		
		$("#userInfo-button").attr("href", "/mypageUpdate.tm?no="+$("#userNo-form").val()+"&name="+$("#name-form").val()+" &email="+$("#email-form").val()+"&gender="+$("input[name='gender']:checked").val()+" &point=100");
	});
	
	
	$("#btn-changepwd").click(function(event) {
		// 변경할 비밀번호와 재입력한 값이 일치하는지 확인
		var newPwd = $("input:password[name='newPassword']").val();
		var confirmPwd = $("input:password[name='new-password-confirm']").val();
		if(newPwd != confirmPwd) {
			alert("비밀번호가 같지 않습니다.");
			return false;
		}
		
		// 폼에서 값 읽어오기
		var data = convertToObject($("form#form-changepwd").serializeArray());
		
		// 읽어온 값 ajax로 전송
		$.ajax({
			url: "changepwd.tm",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(result) {
				alert(result.result);
			}
		});
	});
	
	//헤더에서 유저번호 뽑아내기
	var theUserNo = $(":hidden[name='userNo']").val();
	
	$(".my_list-group-item").click(function(event) {
		// 기존에 선택한 값의 선택정보(active) 없애기
		$(".my_list-group-item").removeClass("active");
		
		// 선택한 값만 선택정보(active) 넣기
		$(this).addClass("active");
	});
	
	$("a#userPlanList").on("click",function(event){
		event.preventDefault();
		/*$("#user-info").attr("class", "my_list-group-item");
		$("#trip").attr("class", "my_list-group-item");
		$("#review").attr("class", "my_list-group-item");
		$("#userPlanList").attr("class", "my_list-group-item active");*/
		$.ajax({
			url: "getUserPlanList.tm",
			type: "GET",
			data: {userNo:theUserNo},
			dataType: "json",
			success: function(data) {			
				 
				
				var planlistArr = data.userPlanLists
				
				var $div = $("<div class='container'></div>");			
				var $divrow = $("<div class='row'></div>");
				
				$.each(planlistArr, function(index, item){

					/*
					 * <div class="container">
						   <div class="row">
							
							<div class="col-md-4"> 
								<div style="position:relative; height:296px;">
									<a href="planNo" class="plan-img" id="일정번호">
						            	<img src="../resources/img/city/도시사진파일이름" alt="여행지이름들" style="width:310px;height:202px;">
						            </a>
						        </div>    
						        <div class="pull-left" style="position:absolute;top:220px;left:10px;">               
						            <p>여행지이름들:aaaaaaa</p>                 
						        </div>		 
							</div>
							
							<div class="col-md-4"> 
								<div style="position:relative; height:296px;">
									<a href="planNo" class="plan-img">
						            	<img src="../resources/img/city/여행지사진" alt="도시사진" style="width:310px;height:202px;">
						            </a>
						        </div>    
						        <div class="pull-left" style="position:absolute;top:220px;left:10px;">               
						            <p>장소이름</p>                 
						        </div>		 
							</div>
							
							<div class="col-md-4"> 
								<div style="position:relative; height:296px;">
									<a href="planNo" class="plan-img">
						            	<img src="../resources/img/city/여행지사진" alt="도시사진" style="width:310px;height:202px;">
						            </a>
						        </div>    
						        <div class="pull-left" style="position:absolute;top:220px;left:10px;">               
						            <p>장소이름</p>                 
						        </div>		 
							</div>
						</div> 
						</div>
					 * 
					 */
					var $aPlan = $("<div class='col-md-4'></div>");
					var $aPlanImg = $("<div style='position:relative; height:296px;'></div>");
					var $aPlanName = $("<div class='pull-left' style='position:absolute;top:220px;left:10px;'></div>");
					var $aPlanLink = $("<a href='/plan/detail.tm?no="+ item.planNo +"' class='plan-img' id='"+ item.planNo +"'></a>")	
					var $aPicture = $("<img src='../resources/img/city/"+ item.cityimgName +"' alt='"+ item.userPlanList +"' style='width:310px;height:202px;'>");
					var $aPlaceName = $("<p>여행지이름들:"+ item.userPlanList +" </p>")
						
					$aPlanLink.append($aPicture);
					$aPlanImg.append($aPlanLink);

					$aPlanName.append($aPlaceName);
					
					$aPlan.append($aPlanImg);
					$aPlan.append($aPlanName);
									
					$divrow.append($aPlan);
				})
				$div.append($divrow);
				
				$("div#userContentsList").empty();
				
				var $divContainer = $("div#userContentsList");
				$divContainer.append($div);
			}
		});
	});
	
	$(".my_list-group-item").first().click(function(){
		location.reload();
	})
	
	
	// 클립보드 정보 가져오기
	$("a#trip").click(function(event){
		event.preventDefault();
		// ajax로 데이터 가져오기
		$.ajax({
			url: "/getClipboardList.tm",
			type: "GET",
			data: {userNo: theUserNo},
			dataType: "json",
			success: function(result) {
				// 성공 시 결과 출력하기
				var dataList = result.clipboardList;
				var $div = $("#userContentsList");
				$div.empty();
				$.each(dataList, function(index, item) {
					var imgUrl = "/resources/img/";
					// 이미지가 없으면 대체 이미지를 띄움
					if(item.mainImgName == null){
						imgUrl+="noimage.jpg";
					} else {
						imgUrl+=("dest/"+item.mainImgName);
					}
					$div.append($("<a href='/info/dest.tm?destNo="+item.no+"' style='float:left;margin:0 7px;'></a>").append('<dl></dl>').append($("<dt><img src='"+imgUrl+"' style='width:310px;height:202px;' /></dt>").append('<dd>'+item.name+'</dd>')));
					
				});
			}
		});
		
	});
})