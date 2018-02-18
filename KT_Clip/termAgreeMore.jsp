<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
	<meta charset="utf-8" /> 
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"> 
	<meta name="format-detection" content="telephone=no">
	<title>CLiP</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/cv2/css/common.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/common.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/const.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/cv2/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript">
    $(function(){			
		// 이용약관 동의 아코디언
		$('.boxAgree .agreeList .btnAgree').on('click', function(e){
			e.preventDefault();
			if($(this).closest('.agreeList').hasClass('open')){
				$(this).closest('.agreeList').removeClass('open');
			}
			else{
				$(this).closest('.agreeList').addClass('open');
			}
		})
	});

	// 서버 Url
    const APPSVR = "<c:out value="${appSvr}"/>";
    
 	// 개인정보 수집/이용 동의(필수) 상세 URL 및 정보
	const PRIV_AGREE_TYPE = "<c:out value="${agreePrivInfo.agreeType}"/>";
	const PRIV_AGREE_DISPLAY_YN = "<c:out value="${agreePrivInfo.agreeDisplayYn}"/>";
	const PRIV_AGREE_VERSION = "<c:out value="${agreePrivInfo.agreeVersion}"/>";
	const PRIV_AGREE_URL = "<c:out value="${agreePrivInfo.agreeUrl}"/>";
	
	// Clip 이용약관(필수) 상세 URL
	const TERM = "<c:out value="${agreeTermUrl}"/>";
	
	// 위치정보 이용동의(필수) 상세 URL
	const MERC = "<c:out value="${agreeMercUrl}"/>";
	
	// 사용자 약관동의 정보
	const USER_AGREE = "<c:out value="${userAgreeInfo.agree}"/>";
	const USER_PRIV = "<c:out value="${userAgreeInfo.agreePri}"/>";
	const USER_PRIV_VER = "<c:out value="${userAgreeInfo.agreePriVer}"/>";
	const USER_AGREE_USE = "<c:out value="${userAgreeInfo.agreeUse}"/>";
	const USER_AGREE_CHG_DATE = "<c:out value="${userAgreeInfo.agreeChgDate}"/>";
	const USER_BCCARD = "<c:out value="${userAgreeInfo.agreeBccard}"/>";
	
	const USER_THIRD_BCPAY = "<c:out value="${userThirdPartyAgreeInfo.agreeBcpay}"/>";
	const USER_THIRD_MTIC = "<c:out value="${userThirdPartyAgreeInfo.agreeMtic}"/>";
	const USER_THIRD_BCKOCK = "<c:out value="${userThirdPartyAgreeInfo.agreeBckock}"/>";
	
	// BC스탬프 이용약관 동의(선택) 상세 URL
	const MEMBUSE = "<c:out value="${memberAgreeUrl}"/>";
	
	var OS_PLATFROM = getOS();
	
	$(document).ready(function() {
		 
		if(USER_AGREE != "Y") {
			var appendHtml =
				'<span class="inputCheckbox">'
					+'<input type="checkbox" id="ck02" name="chk" onclick="checkAgree();">'
					+'<label for="ck02">정보/광고 수신동의 (선택)</label>'
				+'</span>'
				+'<div class="agreeArea">'
					+'<ul class="listStyle01">'
						+'<li>전송할 정보/광고의 내용 : 신규 멤버십/쿠폰/신용카드 연계 혜택 안내, 광고/이벤트 안내, 다양한 혜택 소개 및 안내 제공</li>'
						+'<li>전송방법 : 이벤트, 혜택 제공에 대한 문자메시지(SMS, MMS 등), 이메일(E-mail), 모바일 어플리케이션 Push 메시지</li>'
						+'<li>철회안내 : 정보/광고 수신 동의 철회를 원활 시 환경설정에서 철회 가능</li>'
						+'<li>수집/보유하는 신청인의 개인 정보 및 이용행태 분석들을 활용하여 신청인에게 상기의 방법으로 정보/광고를 전송할 수 있습니다.</li>'
						+'<li>신청인에게 발송되는 광고성 정보는 관련 법의 규정을 준수하여 발송됩니다.</li>'
					+'</ul>'
					+'<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>'
				+'</div>';
				
				$("#appendAgreeAd").append(appendHtml);
		} else {
			$("#appendAgreeAd").hide();
		}
		
		if(USER_THIRD_BCKOCK != "Y") {
			
			var appendHtml =
				'<div class="agreeList">'
					+'<span class="inputCheckbox" style="margin-right:0;">'
						+'<input type="checkbox" id="ck03" name="chk" onclick="checkAgree();">'
						+'<label for="ck03" style="margin-right:25px" onclick="termsDetail(\'MEMBUSE\');">BC스탬프 이용약관 동의 (선택)</label>'
						+'<a href="#" class="btnLink" onclick="termsDetail(\'MEMBUSE\');return false;"></a>'
					+'</span>'
				+'</div>'
		
				+'<div class="agreeList">'
					+'<span class="inputCheckbox" style="margin-right:0;">'
						+'<input type="checkbox" id="ck04" name="chk" onclick="checkAgree();">'
						+'<label for="ck04" style="margin-right:25px" onclick="termsDetail(\'BCK_AGR\');">BC스탬프 개인정보 수집/이용 동의 (선택)</label>'
						+'<a href="#" class="btnLink" onclick="termsDetail(\'BCK_AGR\');return false;"></a>'
					+'</span>'
				+'</div>'
		
				+'<div class="agreeList open">'
					+'<span class="inputCheckbox">'
						+'<input type="checkbox" id="ck05" name="chk" onclick="checkAgree();">'
						+'<label for="ck05"> BC스탬프 개인정보 제3자 제공에 대한 동의 (선택)</label>'
					+'</span>'
					+'<div class="agreeArea">'
						+'<div class="tblData">'
							+'<table>'
								+'<caption>BC스탬프 개인정보 제3자 제공에 대한 동의 (선택)</caption>'
								+'<colgroup>'
									+'<col style="width:18%">'
									+'<col style="width:auto">'
									+'<col style="width:30%">'
									+'<col style="width:16%">'
								+'</colgroup>'
								+'<thead>'
									+'<tr>'
										+'<th scope="col">제공받는자</th>'
										+'<th scope="col">목적</th>'
										+'<th scope="col">항목</th>'
										+'<th scope="col">보유기간</th>'
									+'</tr>'
								+'</thead>'
								+'<tbody>'
									+'<tr>'
										+'<td class="aLeft">비씨카드㈜</td>'
										+'<td class="aLeft">BC스탬프 서비스 가입 및 이용</td>'
										+'<td class="aLeft">동일인 식별번호(CI), 이동전화 번호, 이동전화 단말 OS 정보</td>'
										+'<td class="aLeft">서비스 가입기간(가입일~해지일)</td>'
									+'</tr>'
								+'</tbody>'
							+'</table>'
						+'</div>'
						+'<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>'
					+'</div>'
				+'</div>';
			$("#appendBcStamp").append(appendHtml);
		} else {
			$("#appendBcStamp").hide();
		}
		
		// 폼 로드시 '동의' 버튼 이벤트 제거
		//$("#btnOk").removeAttr("onclick");
		
	});
	
	
	
	/* 약관 전체동의 클릭시 처리 */
	function checkAll() {
		
		if($("#ck_all").prop("checked")) {
			$("input[type=checkbox]").prop("checked",true);
		} else {
			$("input[type=checkbox]").prop("checked",false);
		}
		
		checkAgree();
	}
	
	/* 각 항목의 체크박스 또는 Title 클릭시 버튼 처리 */
	function checkAgree() {
		
		var chkMustAgreeCnt = 0;
		var chkAll = 0;
		
		$("input[name=chk]").each(function(index) {
			
			// 필수 체크 항목 카운트
			if(index < 1) {
				
				if($(this).prop("checked") == true) {
					chkMustAgreeCnt++;
				}
			}
			
			// 6번째 체크 항목(개인정보 제3자 제공에 대한 동의)에 대한 하위 체크박스 처리
			if(index == 5) {
				
				if($(this).prop("checked") == true) {
					
					$("#appendTbody").children("tr").each(function(){
						
						$(this).children("td").children("span").children("input").prop("checked",true);
					});
				} else {
					$("#appendTbody").children("tr").each(function(){
						
						$(this).children("td").children("span").children("input").prop("checked",false);
					});
				}
			}
			
			// 전체 체크 항목 카운트
			if($(this).prop("checked") == true) {
				
				chkAll++;
			}
		});
		
		// 필수 항목 모두 체크되었을경우 '동의' 버튼 이벤트 추가
		if(chkMustAgreeCnt == 1) {
			$("#btnOk").removeAttr("onclick");
			$("#btnOk").removeClass("disabled");
			$("#btnOk").attr("onclick","complete();return false;");
			
		} else {
			$("#btnOk").removeAttr("onclick");
			$("#btnOk").addClass("disabled");
			//$("#btnOk").attr("onclick","doAlert();");
		}
		
		// 모든 체크박스 선택시 '약관 전체동의' 체크박스 체크
		if(chkAll == $("input[name=chk]").length){
			$("#ck_all").prop("checked",true);
		} else {
			$("#ck_all").prop("checked",false);
		}
	}
	
	// 하위 체크박스 클릭시 상위 체크박스 처리
	function childCheckAgree() {
		
		var childAllCnt = $("#appendTbody").children("tr").length;
		var childChkCnt = 0;
		
		$("#appendTbody").children("tr").each(function(){
			
			if($(this).children("td").children("span").children("input").prop("checked") == true) {
				childChkCnt++;
			}
		});
		
		if(childChkCnt > 0) {
			$("#ck06").prop("checked", true);
			
		} else {
			$("#ck06").prop("checked", false);
			
		}
		
		var chkAll = 0;
		$("input[name=chk]").each(function(index) {
			
			// 전체 체크 항목 카운트
			if($(this).prop("checked") == true) {
				
				chkAll++;
			}
		});
		
		// 모든 체크박스 선택시 '약관 전체동의' 체크박스 체크
		if(chkAll == $("input[name=chk]").length){
			$("#ck_all").prop("checked",true);
		} else {
			$("#ck_all").prop("checked",false);
		}
	}
	
	/* 각 항목의 체크박스 또는 Title 클릭시 상세 페이지 URL을 App 으로 호출 */
	function termsDetail(idx) {
		
		// 제목을 누르면 >> termsDetail >> 호출url완성 찌르고 
		
		if ( idx == "USE" ) {
			/** MoCa Wallet 이용약관 **/
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('서비스이용약관')+"&url=" + APPSVR + TERM;
			console.log("termsDetail(USE) targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		} else if ( idx == "COL" ) {
			/** 개인정보 수집/이용 **/
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('개인정보수집이용동의')+"&url=" + APPSVR + "/app_nw/terms/mocawallet/clip_privacy_pop_201606.htm";
			console.log("termsDetail(COL) targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		} else if ( idx == "ADV" ) {
			/** 위치정보이용 **/
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('위치정보이용동의')+"&url=" + APPSVR + MERC;
			console.log("termsDetail(ADV) targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		} else if ( idx == "MEMBUSE" ) {
			/** 멤버십 약관 상세보기 **/
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('약관 상세보기')+"&url=" + MEMBUSE;
			console.log("termsDetail(MEMBUSE) targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		} else if ( idx == "BCK_AGR" ) {
			/** BC스탬프 개인정보 수집/이용 동의 약관 상세보기 **/
			var targetUrl = "webtoapp://newWindowPopup?title="+encodeURIComponent('BC스탬프 개인정보수집이용 동의')+"&url=" + APPSVR + "/app_nw/terms/mocawallet/clip_bcstamp_agree_pop_201606.htm";
			console.log("termsDetail(BCK_AGR) targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		}
		
	}
		
		/* 확인 버튼 클릭시 처리 */
        function complete() {
        	
			var agreeString = "";
			
			var agreePri = ""; // 개인정보 수집/이용 동의(필수)
			var agreeAd = ""; // 정보/광고 수신동의(선택)
			var agreeBcStamp = ""; // BC스탬프 이용약관 동의(선택), BC스탬프 개인정보 수집/이용 동의(선택), BC스탬프 개인정보 제3자 제공에 대한 동의(선택)
			
			agreePri = $("#ck01").prop("checked")==true?"Y":"N";
			agreeAd = $("#ck02").prop("checked")==true?"Y":"N";
			
			// BC스탬프 관련 약관 3개 모두 체크시 'Y', 하나라도 빠지면 'N' 
			agreeBcStamp = $("#ck03").prop("checked")==true && $("#ck04").prop("checked")==true && $("#ck05").prop("checked")==true?"Y":"N";
			
			console.log("agreePri : " + agreePri);
			console.log("agreeAd : " + agreeAd);
			console.log("agreeBcStamp : " + agreeBcStamp);
			
			agreeString = "agreePri:" + agreePri + "|agreeAd:" + agreeAd + "|agreeBcStamp:" + agreeBcStamp;
			
			$("#reqAgreeForm input[name=agree]").val(agreeString);
			
			// 동의
            $.ajax({      
                type:"POST",  
                url:"${pageContext.request.contextPath}/cv2/wv/common/updateTermAgreeMore.do",
                dataType:"json",
                data:$("#reqAgreeForm").serialize(),           
                success:function(data){
                	
                    console.log(data);
                    
                    if(data > 0) {
                    	 var targetUrl = "webtoapp://termAgreeMore?return=ok&custId=<c:out value="${custId}"/>&agree="+agreeString;
                         console.log("doAgree() targetUrl : " + targetUrl);
                         $(location).attr("href",targetUrl);
                    } else {
                    	alert("동의 처리 실패했습니다.");
                    }
                },   
                error:function(e){  
                    //console.log(e.responseText);  
                    alert("동의 처리 실패했습니다.");
                }  
            });
        }
        
        /* 취소 버튼 클릭시 처리 */
        function doCancel() {
            var targetUrl = "webtoapp://termAgreeMore?return=";
            
            if( $("input:checkbox[id='ck01']").is(":checked") ){
            	// 필수약관 선택 후 취소 클릭하는 경우
            	targetUrl = targetUrl + "mandatoryCancel";
            } else {
            	targetUrl = targetUrl + "cancel";            	
            }
            console.log("doCancel() targetUrl : " + targetUrl);
            $(location).attr("href",targetUrl);
        }
	</script>
</head> 
<body>
<!-- 추가 약관 동의 : s -->

<!-- layerPop : s -->
<div class="layerPop">
    <!-- popHeader : s -->
    <header class="popHeader">
        <strong class="title">추가 약관 동의</strong>
    </header>
    <!-- popHeader : e -->
    <!-- popContent : s -->
    <div class="popContent">
        <!-- scrollArea : s -->
        <div class="scrollArea bottom01">
            <!-- boxAgree : s -->
	<div class="boxAgree">
		<!-- agreeList : s -->
		<div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck01" name="chk" onclick="checkAgree();">
				<label for="ck01" style="margin-right:25px">개인정보 수집/이용 동의 (필수)</label>
				<!-- <label for="ck02"></label>
				<label class="termText" onclick="termsDetail('COL');">개인정보 수집/이용 동의 (필수)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('COL');return false;"></a>
			</span>
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<p class="mt5">※ CLiP 서비스 제공에 필요함으로 동의를 해 주셔야 서비스를 이용할 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div id="appendAgreeAd" class="agreeList open"></div>
		<!-- agreeList : e -->


		<div id="appendBcStamp"></div>
		

		<!-- <p class="txt">CLiP 이용약관에 동의한 회원 중 본인인증을 받지 않은 회원은  ‘CLiP 앱 삭제’, ‘단말기 기변’ 등의 경우에 직접 등록 멤버십카드, 등록한 신용/체크카드, 발급한 쿠폰 등의 정보가 삭제됩니다. <br>위 정보를 유지하시려면 본인인증이 필요합니다. </p> -->

		<!-- agreeList : s -->
		<div class="agreeList all">
			<span class="inputCheckbox">
					<input type="checkbox" id="ck_all" onclick="checkAll();">
					<label for="ck_all">약관 전체동의(선택항목 포함)</label>
				</span>
		</div>
		<!-- agreeList : e -->

		<!-- btnArea : s -->
		<div class="btnArea">
			<a href="#" class="btn btnLineRed" onclick="doCancel();return false;">
				<span>취소</span>
			</a>
			<a href="#" class="btn btnRed disabled" id="btnOk">
				<span>확인</span>
			</a>
		</div>
		<!-- btnArea : e -->
	</div>
	<!-- boxAgree : e -->
        </div>
        <!-- scrollArea : e -->
    </div>
    <!-- popContent : e -->
    <a href="#" class="btnClose" onclick="doCancel();">팝업 닫기</a>
</div>
<!-- layerPop : e -->
<form name="reqAgreeForm" id="reqAgreeForm" method="post">
    <input type="hidden" name="custId" value="<c:out value="${custId}"/>"/>
    <input type="hidden" name="agree"/>
</form>

<!-- 추가 약관 동의 : e -->
</body>
</html>