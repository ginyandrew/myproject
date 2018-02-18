$(function () {
	plandetailMapinit();
	
    // 일정 복사 관련
    //$('#input-date').datepicker("setDate", new Date()).datepicker( "option", "dateFormat", "yyyy-mm-dd");
	$('#input-date').datepicker({
	    dateFormat: 'yy-mm-dd'
	  });
	
    $("div#left-menu").css({
        'top': $(window).scrollTop() + 100,
        'left': 0
    });

    /*
     * 왼쪽 내비 관련 이벤트
     */
    $("span.left-day-move").click(function (e) {
    	plandetailMapinit(e);
        var day = $(this).text().trim();
        // 날짜 아이디 얻기
        var dayId = day.replace(/\r?\n|\r/g, "").replace(/ .*$/, "").replace(/D/, "day-");

        // 해당 아이디를 가진 el의 위치로 이동
        $('body').animate({
            'scrollTop': $("div#" + dayId).offset().top
        }, 100);
    });

    /*
     * 스크롤 관련 이벤트
     */
    $(window).scroll(function (event) {
        $("div#left-menu").clearQueue();
        $("div#left-menu").stop();
        $("div#left-menu").animate({
            'top': $(window).scrollTop() + 100
        }, 100);
    });
    $(window).resize(function (event) {
        $("div#left-menu").css({
            'top': $(window).scrollTop() + 100,
            'left': 0
        });
    });
    
    	
    
	$("button#plan-copy-modal-btn").click(function(event) {
    	// 로그인 확인 작업
    	if(!$("input[type='hidden'][name='name']").val()) {    		
    		showAlert("alert-body", "로그인이 필요합니다", "warning");
    		return;
    	} else {
    		$("div#copy-box").modal('show');
    	}
		
	});
		
    $("button#plan-copy-btn").click(function() {    	
    	// 일정 복사 작업
    	var userNo = $("input:hidden[name='userNo']").val();
    	var planNo = $("input:hidden[name='planNo']").val();
    	var title = $("div#copy-box input:text[name='title']").val();
    	var leaveDate = $("div#copy-box input:text[name='leaveDate']").val().replace(/-/g,"") + "000000";
    	var themeCode = $("div#copy-box li.theme-li").filter(".active").attr("id").replace(/theme-/, "");
    	
    	planCopy(userNo, planNo, title, leaveDate, themeCode);
    });
    
    $("button#plan-to-excel-btn").click(function() {
    	// 일정 엑셀로 요청하는 작업
    	var planNo = $("input:hidden[name='planNo']").val();
    	window.location.href= "/plan/excel.tm?no=" + planNo;
    })
    
    // 일정 복사 modal
    $("div#copy-box li.theme-li").click(function(event) {
    	$("div#copy-box li.theme-li").removeClass("active");
    	
    	$(this).addClass("active");
    }); 
    
    // 일정 수정 작업

    function init() {
        // 예산 설정하기
    	// 세션에 설정된 화폐단위를 확인한다.
    	var type = $("input:hidden[name='type']").val();
    	// 화폐단위가 USD나 JPY라면
    	var msg;
    	var totalBudget = $("input:hidden[name='total-budget']").val();
    	var unit = $("input:hidden[name='unit']").val();
    	if(type == "USD" || type == "JPY") {
    		setExchange(function(base, result){
    			// 값 설정하기
    			var rate = result.rates.KRW;
    			var krw = parseInt(totalBudget);
    			
    			// $.number(): https://github.com/customd/jquery-number
    			//msg = "예산: " + unit + " "  + $.number(krw/rate, type=="JPY"?0:1);
    			//$("div#cover-budget").text(msg);
        		$("span#budget").text($.number(krw/rate, type=="JPY"?0:1));
        		$("span#unit").text(unit);
    		}, type);
    	} else {
    		// KRW
    		//msg = "예산: " + unit + " "  + totalBudget;
    		//$("div#cover-budget").text(msg);
    		$("span#budget").text(totalBudget);
    		$("span#unit").text(unit);
    	}
    	
    }
	init();
    
});

function planCopy(userNo, planNo, title, leaveDate, themeCode) {
	$.ajax({
		url: "copy.tm"
		,type: "GET"
		,data: 
			{
				userNo: userNo
				,planNo: planNo
				,title: title
				,leaveDate: leaveDate
				,themeCode: themeCode
			}
		,dataType: "json"
		,success: function(ajaxResult) {
			var result = ajaxResult.result;
			var msg = ajaxResult.msg;
			var type = "danger";
			if(result=="success") {
				var dataNo = ajaxResult.planVO.no;
				msg = "복사 작업에 성공했습니다.";
				msg += " <a href='detail.tm?no="+dataNo+"' class='btn btn-primary btn-sm'>페이지로 이동하기</a>";
				type = "success";
			} else {
				msg = msg||"복사 작업에 실패했습니다.";
			}
			$("div#copy-box").modal('hide');
			showAlert("alert-body", msg, type);
		}
	});
	
}

function planToExcel() {
	
}