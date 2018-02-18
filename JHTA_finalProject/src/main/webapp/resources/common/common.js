$(function() {
	// 자동검색 기능 연결
	setAutocomplete("#search-header");
		
	// bootstrap tooltip 활성화
	$("body").tooltip({ selector: '[data-toggle=tooltip]' });
	
	
	$("div.modal").on("shown.bs.modal", function() {
		$(this).find("input[name='email']").focus();
	});
	
	//$("div.modal input:text, div.modal input:password").keypress(function(event){
	$("div#login-box input:text, div#login-box input:password").keypress(function(event){
		// 엔터키에만 반응
		if(event.which==13){
			$(this).parents("div.modal").find("button.ok-btn").trigger("click");
		}				
	});

	/* 오른쪽 메뉴 관련 설정 */
	// 위에서 오른쪽 버튼까지의 거리
	var RIGHT_MENU_MARGIN_TOP = 50;
	var RIGHT_OPEN_BUTTON_WIDTH = 45;
	var RIGHT_MENU_WIDTH = 160;
	var RIGHT_MENU_CLICKABLE = true;
	var RIGHT_MENU_CLICKABLE_TIMEOUT = 500;
	/*
	 * 우측 메뉴 위치 초기화
	 */
	$("div#right-menu").css({
		top : $(window).scrollTop() + RIGHT_MENU_MARGIN_TOP,
		left : $(window).width()
	});
	$("span#right-menu-open").css({
		top : $(window).scrollTop() + RIGHT_MENU_MARGIN_TOP,
		left : $(window).width() - RIGHT_OPEN_BUTTON_WIDTH
	});
	/*
	 * 오른쪽 메뉴 관련 클릭 이벤트
	 */
	$("span#right-menu-open").click(function(event) {
		if (!RIGHT_MENU_CLICKABLE)
			return;
		$("div#right-menu").css('top', $(window).scrollTop() + RIGHT_MENU_MARGIN_TOP);
		$("div#right-menu").show().animate({
			left : $(window).width() - RIGHT_MENU_WIDTH
		}, 'fast');
		$(this).hide();
	});
	$("span#right-menu-close").click(function(event) {
		$("div#right-menu").animate({
			left : $(window).width()
		}, 'fast').hide('fast');
		$("span#right-menu-open").show();
		// 일정 시간(RIGHT_MENU_CLICKABLE_TIMEOUT)동안 클릭할 수 없게 한다
		// 열기버튼이 사라지는 버그를 막는다
		RIGHT_MENU_CLICKABLE = false;
		setTimeout(function() {
			RIGHT_MENU_CLICKABLE = true;
		}, RIGHT_MENU_CLICKABLE_TIMEOUT);
	});

	/*
	 * 스크롤 관련 이벤트
	 */
	$(window).scroll(function(event) {
		$("span#right-menu-open").clearQueue().stop().animate({
			'top' : $(window).scrollTop() + RIGHT_MENU_MARGIN_TOP
		}, 100);
		$("div#right-menu").clearQueue().stop().animate({
			'top' : $(window).scrollTop() + RIGHT_MENU_MARGIN_TOP
		}, 100);
	});

	$(window).resize(function(event) {
		$("span#right-menu-open").css('left', $(window).width() - RIGHT_OPEN_BUTTON_WIDTH);
		$("div#right-menu").css('left', $(window).width() - RIGHT_MENU_WIDTH);
	});

	$("div#footer a").click(function(event) {
		// 푸터의 a의 기본기능 막기
		event.preventDefault();
	});

	$("button#login-btn-dialog").click(function(event) {
		// 입력값 확인
		if(!checkData("login")) {
			//$(this).parents("div.modal").modal("hide");
			return;
		}
		// 돌아올 경로 지정
		$("input:hidden[name='beforepage']").val(getPath());
		// 로그인 페이지로 보내기
		$("form#login-form").submit();
	});

	$("input:radio +").filter("span").click(function(event) {
		$(this).prev().prop('checked', true);
	});

	$("button#clear-btn-dialog").click(function(event) {
		clearForm($("form#register-form"));
	});
	
	$("button#register-btn-dialog").click(function(event) {
		// 정보 확인
		if (!checkData("register")) {
			//
			return;
		}

		var data = convertToObject($("form#register-form").serializeArray());
		// 회원가입 Ajax
		$.ajax({
			url : "/register.tm",
			type : "POST",
			data : data,
			dataType : "json",
			success : function(data) {
				// 모달 닫기
				$('div#register-box').modal('hide');

				var result = data.result;
				var msg = data.msg;
				if (result) {
					// 모달의 데이터 지우기
					clearForm($("form#register-form"));

					// 알림창 만들기
					var $alertData = $getAlert("alert-header", msg, "success");
					$("div#header").append($alertData);

					// 알림창 띄우기
					$("div#alert-header").slideDown("slow");

					setTimeout(function() {
						$("div#alert-header").slideUp("slow")
					}, 10000);
				}
			}
		});
	});

	// 데이터의 정보를 확인함
	function checkData(type) {
		// 정보 확인 정규식
		var emailExr = /^[0-9a-zA-Z]([\-.\w]*[0-9a-zA-Z\-_+])*@([0-9a-zA-Z][\-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9}$/;
		var pwdExr = /^.{4,20}$/;
		var nameExr = /^.{2,20}$/;

		// alert 메시지
		var msg;
		// 포커싱할 데이터
		var $focusData;
		// 결과값: true일시 작업이 수행됨
		var result = true;
		
		var $divBox = $("div#" + type + "-box");
		var alertId = "alert-" + type;
		var $form = $("form#"+type+"-form");

		// 확인할 데이터
		var data = convertToObject($form.serializeArray());
		
		if(type == "login") {
			if (data.email.length == 0) {
				$focusData = $form.find("input:text[name='email']");
	
				msg = "이메일을 입력하세요." + (msg ? "<br />" + msg : "");
				result = false;
			} else if (data.password.length == 0) {
				$focusData = $form.find("input:text[name='password']");
	
				msg = "비밀번호를 입력하세요." + (msg ? "<br />" + msg : "");
				result = false;
			}			
		} else if(type == "register") {
			// 성별 체크부분
			if (($form.find(":radio").filter(":checked")).size() == 0) {
				msg = "성별을 선택하세요." + (msg ? "<br />" + msg : "");
				result = false;
			}
			
			if (data.name.length == 0) {
				$focusData = $form.find(":text[name='name']");
				msg = "이름을 입력하세요." + (msg ? "<br />" + msg : "");
				result = false;
			} else if(!nameExr.test(data.name)) {
				$focusData = $form.find(":text[name='name']");
				msg = "이름은 2~20자로 입력해야 합니다." + (msg ? "<br />" + msg : "");
				result = false;
			}
	
			if (data.password.length == 0) {
				$focusData = $form.find("input:text[name='password']");
	
				msg = "비밀번호를 입력하세요." + (msg ? "<br />" + msg : "");
				result = false;
			} else if (!pwdExr.test(data.password)) {
				$focusData = $form.find("input:text[name='password']");
				msg = "비밀번호는 4~20자리로 입력해야 합니다." + (msg ? "<br />" + msg : "");
				result = false;
			} else if (data.password != data["password-check"]) {
				$focusData = $form.find("input:text[name='password-check']");
				msg = "비밀번호가 일치하지 않습니다." + (msg ? "<br />" + msg : "");
				result = false;
			}
	
			if (data.email.length == 0) {
				$focusData = $form.find("input:text[name='email']");
	
				msg = "이메일을 입력하세요." + (msg ? "<br />" + msg : "");
				result = false;
			} else if (!emailExr.test(data.email)) {
				$focusData = $form.find("input:text[name='email']");
				msg = "이메일의 형식이 올바르지 않습니다." + (msg ? "<br />" + msg : "");
				result = false;
			}
		} 

		// 이전 알림창이 있으면 지운다
		$("div#"+alertId ).remove();

		// 체크에 걸렸으면 알림창을 띄움
		if(!result) {
			// 알림창을 띄운다
			$divBox.find("div.modal-header").append($getAlert(alertId, msg, "danger"));
			$("div#"+alertId ).slideDown("slow");
			
			// $focusData가 있다면 포커스를 준다. 
			!$focusData||$focusData.focus();
		}
		
		return result;
	}

	/*
	 * 각 다이얼로그 별 X버튼 클릭 시 기능
	 * 
	 * $("span.box-close").click(function(event) { hideDialog(); });
	 * 
	 * 
	 * 각 다이얼로그 별 대화상자 닫기 버튼 클릭 시 기능
	 * 
	 * $("button.close-btn-dialog").click(function(event) { hideDialog(); });
	 */

	$(function() {
		getMyMenu();

		getMessage();
		
		
	});

	function clearForm($form) {
		// var $form =
		// $("form#register-form");//$(this).parents("div.modal-dialog").find("form#register-form");
		// input:text,password 비우기
		$form.find("input:text, input:password").val("");
		// input:radio 초기화(남자로)
		$form.find("input:radio#male-radio").prop('checked', true);
	}
	
	// 푸터 언어 설정하기
	$("select[name='lang']").change(function(event) {
		var lang = $(this).val();
		
		if(lang != "ko" && lang != "en" && lang != "ja" && lang != "zh") {
			// 이상한값이 들어오면 튕기기
			return false;
		}
		
		/* 
		 * 중복 처리를 위한 이전 경로 치환처리
		 * /plan/detail.tm?no=229&lang=en
		 * /plan/list.tm?lang=en
		 */ 
		window.location.href = '/language.tm?beforepage='+getPath().replace(/[\&\?]*lang=.*/g, "")+'&lang='+lang;
	});
	
	// 푸터 환율 단위 설정하기
	$("select[name='exchange']").change(function(event) {
		var exchange = $(this).val();
		
		if(exchange != "KRW" && exchange != "USD" && exchange != "JPY") {
			// 이상한값이 들어오면 튕기기
			return false;
		}
		
		window.location.href = '/exchange.tm?beforepage='+getPath()+'&exchange='+exchange;		
	});
});

//로그인을 시도한 페이지의 경로를 구한다.
function getPath() {
	return location.href.replace("http://" + location.host, "");
}

function $getAlert(id, msg, type) {
	var alertType = type ? "alert-" + type : "";

	var $alertData = $('<div id="' + id + '" class="row"></div>').append($('<div></div>').addClass("alert " + alertType).css({
		'position' : 'relative',
		'width' : '90%',
		'max-width' : '900px',
		'margin' : '0 auto'
	}).html(msg).append($('<span class="pull-right glyphicon glyphicon-remove"></span>').css("cursor", "pointer").click(function(event) {
		$(this).parents("div#" + id).slideUp('fast');
	})));

	// 기본-숨김
	$alertData.hide();

	return $alertData;
}

function showAlert(id, msg, type) {
	// 이전 알림창이 있으면 지운다
	$("div#" + id).remove();
	
	var $alert = $getAlert(id, msg, type);
	$("div#header").append($alert);

	// 알림창 띄우기
	$alert.slideDown("slow");

	setTimeout(function() {
		$alert.slideUp("slow")
	}, 10000);
}

// $({formdata}).serializeArray()로 변환된 데이터를 name:value 형태의 객체로 변환한다.
function convertToObject(arr) {
	var obj = {};
	var len = arr.length;
	for(var i=0; i<len; i++) {
		obj[arr[i].name] = arr[i].value; 
	}
	return obj;
}

function getMyMenu() {
	var username = $("div#hidden-user-data input:hidden[name='name']").val();
	var userNo =  $("div#hidden-user-data input:hidden[name='userNo']").val();
	var menu = [{
        name: '<div style="font-size: 14px; margin: 2px 0;">' + (userNo != 1?'마이페이지':'관리자 페이지') + '</div>',
        fun: function() {
        	window.location.href = (userNo!=1?'/mypage.tm':'/host/main.tm');
        }
    }, {
    	name: '<div style="font-size: 14px; margin: 2px 0;">내 정보</div>',
        fun: function() {
        	window.location.href = '/setting.tm';
        }
    }, {
        name: '<div style="font-size: 14px; margin: 2px 0;">로그아웃</div>',
        fun: function () {
        	window.location.href = '/logout.tm?beforepage='+getPath();
        }
    }];

    //Calling context menu
    $("div#user-box span.glyphicon-user").contextMenu(menu);
	
	
	/*var username = $("div#hidden-user-data input:hidden[name='name']").val();
	// 사용자 드롭다운 메뉴 그리기
	// 참조: http://swisnl.github.io/jQuery-contextMenu/
	$.contextMenu({
		selector : '.glyphicon-user',
		trigger : 'left',
		callback : function(key, options) {
			var href = "/" + key + ".tm";

			if (key == 'my') {
				return;
			} else if (key == 'logout') {
				var beforepage = getPath();
				window.location.href = href + "?beforepage=" + beforepage;
			} else {
				window.location.href = href;
			}
		},
		items : {
			"mypage" : {
				name : "마이페이지"
			},
			"setting" : {
				name : "내 정보"
			},
			"sep1" : "---------",
			"logout" : {
				name : "로그아웃"
			}
		
		 * "quit": {name: "Quit", icon: function($element, key, item){
		 * return 'context-menu-icon context-menu-icon-quit'; }}
		 
		}
	});*/
}

function getMessage() {
	
	var userNo = $("div#hidden-user-data input:hidden[name='userNo']").val();
	if(!userNo)	return;
	$.ajax({
		url: "/message.tm"
		,type: "GET"
		,data: {no: userNo}
		,dataType: "json"
		,success: function(result) {
			var resultMessages = result.messageList;
			
			$.each(resultMessages, function(index, item) {
				$("div#msgBox div.panel-body").append($makeMessageData(item.data, item.sendTime));
			});
			
			function $makeMessageData(msg, stringTime) {
				var $msgData= $("<div></div>");
				
				if(!msg) {
					$msgData.addClass("msgbox-no-item")
						.append("<p>메시지가 없습니다</p>");
				} else {
					$msgData.addClass("msgbox-item")
						.append("<p>"+msg+"</p>")
						.append("<p class='pull-right'>"+getFormattedDate(stringTime)+"</p>")
						.append("<div class='clear'></div>");					
				}				
				
				return $msgData;
			}
			
			/*var menu = [{
		        name: '<h3 id="msgBox-title" style="z-index: 1000; color: black;">쪽지함</h3>',
		        disable: true,
	        	fun: function() {
	        		//window.location.href = '/mypage.tm';
	        	}
			}];
			$.each(resultMessages, function(index, item) {
				// 메시지 드롭다운 메뉴 그리기
				menu.push({
			        name: '<div style="font-size: 14px; margin: 2px 0; style="color: #333;">'+item.data +
			        	'<br /><small>'+getFormattedDate(item.sendTime)+'</small></div>',
			        fun: function() {
			        	//window.location.href = '/mypage.tm';
			        }
				});
			});

			if(menu.length <= 1) {
				// 메시지가 없을 때
				menu.push ({
			        name: '<div style="font-size: 14px; margin: 2px 0; style="color: #333;">메시지가 없어요</div>',
			        disable: true,
		        	fun: function() {
		        		//window.location.href = '/mypage.tm';
		        	}
				});
			} */
					
			//Calling context menu
		    $("div#user-box span.glyphicon-bell").contextMenu("#msgBox");
			
		    // 지우기
		    
			/*$.each(resultMessages, function(index, item) {
				if(index > 0) {
					messageList["sep" + index] = "---------";
				}
				messageList["msg" + (index+1)] = {name: item.data + "\r\n" + getFormattedDate(item.sendTime)};
			});
			
			// 메시지 드롭다운 메뉴 그리기
			$.contextMenu({
				selector : '.glyphicon-bell',
				trigger : 'left',
				callback : function(key, options) {
					// var href = "/" + key + ".tm";
					// window.location.href = href;
				},
				items : messageList
			});*/
			
		}
	});
}
/*
function getFormattedDate(stringTime) {
	var formattedTime; 
	if(stringTime.length == 14) {
		formattedTime = stringTime.substring(0,4);
		formattedTime += "-" + stringTime.substring(4,6);
		formattedTime += "-" + stringTime.substring(6,8);
		formattedTime += " " + stringTime.substring(8,10);
		formattedTime += ":" + stringTime.substring(10,12);
		formattedTime += ":" + stringTime.substring(12,14);
	} else if(stringTime.length == 8) {
		formattedTime = stringTime.substring(0,4);
		formattedTime += "-" + stringTime.substring(4,6);
		formattedTime += "-" + stringTime.substring(6,8);
		formattedTime += " " + stringTime.substring(8,10);		
	} else {
		// 실패
		formattedTime = stringTime;
	}
	return formattedTime;
	
}*/


function getFormattedDate(stringDate, type) {
	if(stringDate.length!=14){
		return stringDate;
	}
	var date = {
		y: stringDate.substr(0,4),
		M: stringDate.substr(4,2),
		d: stringDate.substr(6,2),
		h: stringDate.substr(8,2),
		m: stringDate.substr(10,2),
		s: stringDate.substr(12,2)
	}
	var formattedDate = date.y;
	formattedDate += "-" + date.M;
	formattedDate += "-" + date.d;
	if(type == "short") {
		return formattedDate;
	}
	formattedDate += " " + date.h;
	formattedDate += ":" + date.m;
	formattedDate += ":" + date.s;
	return formattedDate;	
}

function getStringDate(formattedDate) {
	formattedDate.replace(/[\D]/g, "");
}