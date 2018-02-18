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
		
	
		// (1) 제목을 누르면 >> 체크가 눌린다 + 호출을 한다. >> 호출만 한다. 
		// (2) 체크를 누르면 >> 체크가 눌린다 + 펼쳐진다 (펼쳐지지 않는다 ) >> 아무일도 안일어난다.
		
		
		// 하지만 제목을 눌러도 
		
		$(function(){			
			// 이용약관 동의 아코디언
			/* $('.boxAgree .agreeList .btnAgree').on('click', function(e){
				e.preventDefault();
				if($(this).closest('.agreeList').hasClass('open')){
					$(this).closest('.agreeList').removeClass('open');
				}
				else{
					$(this).closest('.agreeList').addClass('open');
				}
			}) */
		});
	
		// 서버 Url
        const APPSVR = "<c:out value="${appSvr}"/>";
        
     	// 개인정보 수집/이용 동의(필수) 상세 URL
		const PRIV = "<c:out value="${PRIV}"/>";
		
		// Clip 이용약관(필수) 상세 URL
		const TERM = "<c:out value="${TERM}"/>";
		
		// 위치정보 이용동의(필수) 상세 URL
		const MERC = "<c:out value="${MERC}"/>";
		
		// BC스탬프 이용약관 동의(선택) 상세 URL
		const MEMBUSE = "<c:out value="${MEMBUSE}"/>";
		
		var OS_PLATFROM = getOS();
		
		$(document).ready(function() {
			 
			
			// 폼 로드시 '동의' 버튼 이벤트 제거
			//$("#btnOk").removeAttr("onclick");
			
			// OS 가 Android 일 경우 개인정보 제3자 제공에 대한 동의(선택) 항목에 비씨카드(주) 하위 항목 추가
			if(OS_PLATFROM=="Android") {
				
				/* var appendHtml =
					'<tr>'
						+'<td class="aLeft">비씨카드㈜</td>'
						+'<td class="aLeft">BC카드로 신용카드 발급/등록 요청, 결제 및 승인내역 조회</td>'
						+'<td class="aLeft">동일인 식별정보(CI), 신용카드 상품코드, 신용카드 발급사코드, User 아이디(ID), 신용카드 token 정보</td>'
						+'<td class="aLeft">서비스 가입기간(가입일~해지일)</td>'
						+'<td>'
							+'<span class="inputCheckbox">'
								+'<input type="checkbox" id="ck0602" name="chk" onclick="childCheckAgree();">'
								+'<label for="ck0602">'
								+'<span class="hide_txt">체크</span>'
								+'</label>'
							+'</span>'
						+'</td>'
					+'</tr>';
				
				$("#appendTbody").children("tr").eq(0).html(appendHtml); */
				
				var appendHtml =
						'<td class="aLeft">비씨카드㈜</td>'
						+'<td class="aLeft">BC카드로 신용카드 발급/등록 요청, 결제 및 승인내역 조회</td>'
						+'<td class="aLeft">동일인 식별정보(CI), 신용카드 상품코드, 신용카드 발급사코드, User 아이디(ID), 신용카드 token 정보</td>'
						+'<td class="aLeft">서비스 가입기간(가입일~해지일)</td>'
						+'<td>'
							+'<span class="inputCheckbox">'
								+'<input type="checkbox" id="ck0503" name="chk" onclick="childCheckAgree();">'
								+'<label for="ck0503">'
								+'<span class="hide_txt">체크</span>'
								+'</label>'
							+'</span>'
						+'</td>';
				
				$("#appendTr").append(appendHtml);
			} else {
				$("#appendTr").hide();
			}
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
		
		/* 전체 약관 동의 후 시작하기 */
		function doAllCheckOk() {
			$("input[type=checkbox]").prop("checked",true);
			complete();
		}
		
		/* 각 항목의 체크박스 또는 Title 클릭시 버튼 처리 */
		function checkAgree() {
			
			var chkMustAgreeCnt = 0;
			var chkAll = 0;
			
			$("input[name=chk]").each(function(index) {
				
				// 필수 체크 항목 카운트
				if(index < 3) {
					
					if($(this).prop("checked") == true) {
						chkMustAgreeCnt++;
					}
				}
				
				// 6번째 체크 항목(개인정보 제3자 제공에 대한 동의)에 대한 하위 체크박스 처리
				if(index == 4) {
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
			if(chkMustAgreeCnt == 3) {
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
				$("#ck05").prop("checked", true);
				
			} else {
				$("#ck05").prop("checked", false);
				
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
			
			var agreeUse = ""; // Clip 이용약관(필수)
			var agreePri = ""; // 개인정보 수집/이용 동의(필수)
			var agreeMerc = ""; // 위치정보 이용동의(필수)
			var agreeAd = ""; // 정보/광고 수신동의(선택)
			var agreeBccard = ""; // 개인정보 제3자 제공에 대한 동의(선택)
			var agreeBcPay = ""; // 개인정보 제3자 제공에 대한 동의(선택)
			var agreeBuzbil = ""; // 버즈빌
			var agreeBcStamp = ""; // BC스탬프 이용약관 동의(선택), BC스탬프 개인정보 수집/이용 동의(선택), BC스탬프 개인정보 제3자 제공에 대한 동의(선택)
			
			agreeUse = $("#ck01").prop("checked")==true?"Y":"N";
			agreePri = $("#ck02").prop("checked")==true?"Y":"N";
			agreeMerc = $("#ck03").prop("checked")==true?"Y":"N";
			agreeAd = $("#ck04").prop("checked")==true?"Y":"N";
			agreeBccard = $("#ck0501").prop("checked")==true?"Y":"N";
			
			// Android 의 경우 개인정보 제3자 제공에 대한 동의(선택) 의 하위 비씨카드(주) 항목이 2개
			// IOS 의 경우 개인정보 제3자 제공에 대한 동의(선택) 의 하위 비씨카드(주) 항목이 1개
			if(OS_PLATFROM == "Android") {
				agreeBcPay = $("#ck0502").prop("checked")==true && $("#ck0503").prop("checked")==true?"Y":"N";
				agreeBuzbil = $("#ck0505").prop("checked")==true?"Y":"N";
			} else {
				agreeBcPay = $("#ck0502").prop("checked")==true?"Y":"N";
				agreeBuzbil = $("#ck0505").prop("checked")==true?"Y":"N";
			}
			
			// BC스탬프 관련 약관 3개 모두 체크시 'Y', 하나라도 빠지면 'N' 
			agreeBcStamp = $("#ck07").prop("checked")==true && $("#ck08").prop("checked")==true && $("#ck0504").prop("checked")==true?"Y":"N";
			
			console.log("agreeUse : " + agreeUse);
			console.log("agreePri : " + agreePri);
			console.log("agreeMerc : " + agreeMerc);
			console.log("agreeAd : " + agreeAd);
			console.log("agreeBccard : " + agreeBccard);
			console.log("agreeBcPay : " + agreeBcPay);
			console.log("agreeBuzbil : " + agreeBuzbil);
			console.log("agreeBcStamp : " + agreeBcStamp);
			
			agreeString = "agreeUse:" + agreeUse + "|agreePri:" + agreePri + "|agreeMerc:" + agreeMerc + "|agreeAd:" + agreeAd + "|agreeBccard:" + agreeBccard + "|agreeBcPay:" + agreeBcPay + "|agreeBuzbil:" + agreeBuzbil + "|agreeBcStamp:" + agreeBcStamp;
			
			/* $("input[name=chk]").each(function(index) {
				
				if($(this).prop("checked") == true) {
					agreeString += "Y";
				} else {
					agreeString += "N";
				}
			}); */
			
			
			var targetUrl = "webtoapp://termAgree?return=ok&agree="+ agreeString;
			console.log("complete() targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		}
		
		/* 취소 버튼 클릭시 처리 */
		function doCancel() {
			
			alert("서비스 이용약관에 동의하셔야만 CLiP 서비스를 이용하실 수 있습니다.");
			
			var targetUrl = "webtoapp://termAgree?return=cancel";
			console.log("doCancel() targetUrl : " + targetUrl);
			$(location).attr("href",targetUrl);
		}
		
		function doAlert(){
			alert("모든 필수약관에 동의하셔야만 CLiP 서비스를 이용하실 수 있습니다.");
			return;
		}
	</script>
</head> 
<body>
<!-- 서비스 이용약관 동의 : s -->

<!-- scrollArea : s -->
<div class="scrollArea bottom01">
	<p class="firstCopy">CLiP 서비스에 가입하시면 다양한 카드 연계 혜택, 멤버십, 쿠폰 서비스 등을 이용하실 수 있습니다.</p>
	<!-- btnArea : s -->
	<div class="btnArea top">
		<a href="#" class="btn btnRed" id="btnAll" onclick="doAllCheckOk();">
			<span>전체 약관 동의 후 시작하기</span>
		</a>
	</div>
	<!-- btnArea : e -->
	<!-- boxAgree : s -->
	<div class="boxAgree">
		<!-- agreeList : s -->
		<div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck01" name="chk" onclick="checkAgree();">
				<label for="ck01" style="margin-right:25px">이용약관 (필수)</label>
				<!-- <label for="ck01"></label> 
				<label class="termText" onclick="termsDetail('USE');">CLIP 이용약관 (필수)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('USE');return false;"></a>	
			</span>
			
			<!-- agreeArea : s -->
			<div class="agreeArea">
				CLiP은 다양한 카드 연계 혜택, 멤버십, 쿠폰, 결제, 사진카드, 상품권, 스탬프, NFC등의 서비스를 제공하는 모바일 서비스 입니다.
			</div>
			<!-- agreeArea : e -->
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck02" name="chk" onclick="checkAgree();">
				<label for="ck02" style="margin-right:25px">개인정보 수집/이용 동의 (필수)</label>
				<!-- <label for="ck02"></label>
				<label class="termText" onclick="termsDetail('COL');">개인정보 수집/이용 동의 (필수)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('COL');return false;"></a>
			</span>
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck03" name="chk" onclick="checkAgree();">
				<label for="ck03" style="margin-right:25px">위치정보 이용동의 (필수)</label>
				<!-- <label for="ck03"></label>
				<label class="termText" onclick="termsDetail('ADV');">위치정보 이용동의 (필수)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('ADV');return false;"></a>
			</span>
			<!-- <a href="#" class="btnAgree">열기</a> -->
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<ul class="listStyle01">
					<li>이용목적: 위치기반 주변 혜택 제공(멤버십, 쿠폰, 신용카드 연계 등)</li>
					<li>이용정보: 개인위치 정보 수집</li>
					<li>상호: 주식회사 케이티</li>
					<li>주소: 경기도 성남시 분당구 불정로 90</li>
					<li>전화번호: 100번</li>
				</ul>
				<p class="mt5">※  CLiP 서비스 제공에 필요하므로 동의를 해 주셔야 서비스를 이용할 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck04" name="chk" onclick="checkAgree();">
				<label for="ck04">정보/광고 수신동의 (선택)</label>
				<!-- <label for="ck04"></label>
				<label class="termText">정보/광고 수신동의 (선택)</label> -->
			</span>
			<!-- <a href="#" class="btnAgree">열기</a> -->
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<ul class="listStyle01">
					<li>전송할 정보/광고의 내용 : 신규 멤버십/쿠폰/신용카드 연계 혜택 안내, 광고/이벤트 안내, 다양한 혜택 소개 및 안내 제공</li>
					<li>전송 방법 : 이벤트, 혜택 제공에 대한 문자메시지(SMS, MMS 등), 이메일(E-mail), 모바일 어플리케이션 Push 메시지</li>
					<li>철회 안내 : 정보/광고 수신 동의 철회를 원할 시 환경설정에서 철회 가능</li>
					<li>수집/보유하는 신청인의 개인 정보 및 이용행태 분석들을 활용하여 신청인에게 상기의 방법으로 정보/광고를 전송할 수 있습니다.</li>
					<li>신청인에게 발송되는 광고성 정보는 관련 법의 규정을 준수하여 발송됩니다.</li>
					<li>보유 기간 : 서비스 가입기간(가입일~해지일) </li>
				</ul>
				<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<%-- <div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck05" name="chk" onclick="checkAgree();">
				<label for="ck05">개인정보 제3자 제공에 대한 동의 (선택)</label>
				<!-- <label for="ck05"></label>
				<label class="termText">개인정보 제3자 제공에 대한 동의 (선택)</label> -->
			</span>
			<!-- <a href="#" class="btnAgree">열기</a> -->
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<!-- tblData : s -->
				<div class="tblData">
					<table>
						<caption>개인정보 제3자 제공에 대한 동의(선택)</caption>
						<colgroup>
							<col style="width:18%">
							<col style="width:auto">
							<col style="width:30%">
							<col style="width:16%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제공받는자</th>
								<th scope="col">목적</th>
								<th scope="col">항목</th>
								<th scope="col">보유기간</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="aLeft">비씨카드㈜</td>
								<td class="aLeft">TM, 이메일, SMS 등을 통한 비씨카드㈜의 상품·서비스 안내 및 판매<br>
								비씨카드㈜가 보험대리점 자격으로 행하는 위탁 보험 상품 소개 및 판매, 보험서비스 제공에 활용하거나 보험개발원 전산망의 보험 정보 조회 목적</td>
								<td class="aLeft">성명, 전화번호, 생년월일, 성별, 이메일(E-mail), 동일인 식별번호(CI)</td>
								<td class="aLeft">서비스 가입~탈회 시까지</td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- tblData : e -->
				<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div> --%>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck05" name="chk" onclick="checkAgree();">
				<label for="ck05">개인정보 제3자 제공에 대한 동의 (선택)</label>
				<!-- <label for="ck06"></label>
				<label class="termText">개인정보 제3자 제공에 대한 동의 (선택)</label> -->
			</span>
			<!-- <a href="#" class="btnAgree">열기</a> -->
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<!-- tblData : s -->
				<div class="tblData">
					<table>
						<caption>개인정보 제3자 제공에 대한 동의 (선택)</caption>
						<colgroup>
							<col style="width:18%">
							<col style="width:auto">
							<col style="width:30%">
							<col style="width:16%">
							<col style="width:10%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제공받는자</th>
								<th scope="col">목적</th>
								<th scope="col">항목</th>
								<th scope="col">보유기간</th>
								<th scope="col">동의여부</th>
							</tr>
						</thead>
						<tbody id="appendTbody">
							<tr>
								<td class="aLeft">비씨카드㈜</td>
								<td class="aLeft">TM, 이메일, SMS 등을 통한 비씨카드㈜의 상품·서비스 안내 및 판매<br>
								비씨카드㈜가 보험대리점 자격으로 행하는 위탁 보험 상품 소개 및 판매, 보험서비스 제공에 활용하거나 보험개발원 전산망의 보험 정보 조회 목적</td>
								<td class="aLeft">성명, 전화번호, 생년월일, 성별, 이메일(E-mail), 동일인 식별번호(CI)</td>
								<td class="aLeft">서비스 가입~탈회 시까지</td>
								<td>
									<span class="inputCheckbox" style="margin-right:0;">
										<input type="checkbox" id="ck0501" name="chk" onclick="childCheckAgree();">
										<label for="ck0501">
											<span class="hide_txt">체크</span>
										</label>
									</span>
								</td>
							</tr>
							<tr>
								<td class="aLeft">비씨카드㈜</td>
								<td class="aLeft">이용자의 보유 신용카드 상품명 조회 및 CLiP으로 전달</td>
								<td class="aLeft">동일인 식별번호(CI)</td>
								<td class="aLeft">서비스 가입기간(가입일~해지일)</td>
								<td>
									<span class="inputCheckbox">
										<input type="checkbox" id="ck0502" name="chk" onclick="childCheckAgree();">
										<label for="ck0502">
											<span class="hide_txt">체크</span>
										</label>
									</span>
								</td>
							</tr>

							<tr id="appendTr"></tr>
							<!-- added when it's Android 
							<tr>
								<td class="aLeft">(주)케이지모빌리언스</td>
								<td class="aLeft">휴대폰소액결제 서비스 가입 요청 및 결제</td>
								<td class="aLeft">이동전화 번호, 이동통신사 구분정보, 휴대폰 소액결제 서비스 비밀번호</td>
								<td class="aLeft">서비스 가입기간(가입일~해지일)</td>
								<td>
									<span class="inputCheckbox">
										<input type="checkbox" id="ck0603" name="chk" onclick="childCheckAgree();">
										<label for="ck0603">
											<span class="hide_txt">체크</span>
										</label>
									</span>
								</td>
							</tr> -->

							<tr>
								<td class="aLeft">비씨카드㈜</td>
								<td class="aLeft">BC스탬프 서비스 가입 및 이용</td>
								<td class="aLeft">동일인 식별번호(CI), 이동전화 번호, 이동전화 단말 OS 정보</td>
								<td class="aLeft">서비스 가입기간(가입일~해지일)</td>
								<td>
									<span class="inputCheckbox">
										<input type="checkbox" id="ck0504" name="chk" onclick="childCheckAgree();">
										<label for="ck0504">
											<span class="hide_txt">체크</span>
										</label>
									</span>
								</td>
							</tr>
							
							<tr>
								<td class="aLeft" rowspan="2">㈜ 버즈빌</td>
								<td class="aLeft">맞춤형 광고 제공, 광고 참여 확인 및 KT로 클립포인트 회신</td>
								<td class="aLeft">성별, 연령, 광고 아이디(ID), User 아이디(ID)</td>
								<td class="aLeft" rowspan="2">서비스 가입기간(가입일~해지일)</td>
								<td>
									<span class="inputCheckbox">
										<input type="checkbox" id="ck0505" name="chk" onclick="childCheckAgree();">
										<label for="ck0505">
											<span class="hide_txt">체크</span>
										</label>
									</span>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- tblData : e -->
				<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck07" name="chk" onclick="checkAgree();">
				<label for="ck07" style="margin-right:25px">BC스탬프 이용약관 동의 (선택)</label>
				<!-- <label for="ck07"></label>
				<label class="termText" onclick="termsDetail('MEMBUSE');">BC스탬프 이용약관 동의 (선택)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('MEMBUSE');return false;"></a>
			</span>
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<div class="agreeList">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck08" name="chk" onclick="checkAgree();">
				<label for="ck08" style="margin-right:25px">BC스탬프 개인정보 수집/이용 동의 (선택)</label>
				<!-- <label for="ck08"></label>
				<label class="termText" onclick="termsDetail('BCK_AGR');">BC스탬프 개인정보 수집/이용 동의 (선택)</label> -->
				<a href="#" class="btnLink" onclick="termsDetail('BCK_AGR');return false;"></a>
			</span>
		</div>
		<!-- agreeList : e -->

		<!-- agreeList : s -->
		<%-- <div class="agreeList open">
			<span class="inputCheckbox" style="margin-right:0;">
				<input type="checkbox" id="ck09" name="chk" onclick="checkAgree();">
				<label for="ck09"> BC스탬프 개인정보 제3자 제공에 대한 동의 (선택)</label>
				<!-- <label for="ck09"></label>
				<label class="termText"> BC스탬프 개인정보 제3자 제공에 대한 동의 (선택)</label> -->
			</span>
			<!-- <a href="#" class="btnAgree">열기</a> -->
			<!-- agreeArea : s -->
			<div class="agreeArea">
				<!-- tblData : s -->
				<div class="tblData">
					<table>
						<caption>BC스탬프 개인정보 제3자 제공에 대한 동의 (선택)</caption>
						<colgroup>
							<col style="width:18%">
							<col style="width:auto">
							<col style="width:30%">
							<col style="width:16%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제공받는자</th>
								<th scope="col">목적</th>
								<th scope="col">항목</th>
								<th scope="col">보유기간</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="aLeft">비씨카드㈜</td>
								<td class="aLeft">BC스탬프 서비스 가입 및 이용</td>
								<td class="aLeft">동일인 식별번호(CI), 이동전화 번호, 이동전화 단말 OS 정보</td>
								<td class="aLeft">서비스 가입기간(가입일~해지일)</td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- tblData : e -->
				<p class="mt5">※ 동의를 거부하시는 경우에도 CLiP 서비스는 이용하실 수 있습니다.</p>
			</div>
			<!-- agreeArea : e -->
		</div> --%>
		<!-- agreeList : e -->

		<p class="txt">CLiP 이용약관에 동의한 회원 중 본인인증을 받지 않은 회원은  ‘CLiP 앱 삭제’, ‘단말기 기변’ 등의 경우에 직접 등록 멤버십카드, 등록한 신용/체크카드, 발급한 쿠폰 등의 정보가 삭제됩니다. <br>위 정보를 유지하시려면 본인인증이 필요합니다. </p>

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
<!-- 서비스 이용약관 동의 : e -->
</body>
</html>