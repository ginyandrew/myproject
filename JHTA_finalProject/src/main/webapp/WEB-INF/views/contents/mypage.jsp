<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/mypage.css" />
<!-- <script src="/resources/js/mypage.js" ></script> -->
<script src="/resources/js/mypageUserInfo.js" ></script>

<div class="col-md-12">
    <!--필요시 헤더를 넣으세요-->
    <!-- Page Content -->
    <div class="container">
		
        <div class="row">
			
			<!-- 마이페이지 왼쪽 메뉴탭 -->
            <div class="col-md-3">
                <p class="lead">MY PAGE</p>
                <div class="my_list-group">
                    <a href="#" id="user-info" class="my_list-group-item active"><spring:message code="label.basicInfo" /></a>
                    <a href="#" id="userPlanList" class="my_list-group-item"><spring:message code="label.myPlan" /></a>
                    <a href="#" id="trip" class="my_list-group-item"><spring:message code="label.clipboard" /></a>
                    <a href="#" id="review" class="my_list-group-item"><spring:message code="label.myReview" /></a>
                </div>
            </div>

			<form>
    				<input type="hidden" value="" id="userNo-form"/>
    				<input type="hidden" value="" id="userPassword-form"/>    				
    		</form>
    		
			<!-- 마이페이지 메인컨텐츠 부분 -->
            <div id="userContentsList" class="mypage_con">
            	<!-- 이하 아래는 다 지워지고 새로갱신됨!! -->
                <div class="thumbnail_mypage">
                
                <!--나의 기본 정보ㆍ일정. width 800px fix 기본정보:start-->
                   
                <!--나의 기본 정보ㆍ일정. width 800px fix 기본정보:end-->
                    <div class="caption-full" id="userInfo-mypage">
                        <h4><a href="#" style="color:#000; font-weight:bold"><spring:message code="label.setInfo" /></a>
                        </h4>
                        <hr>
                      <p>
                        <table class="config_table" width=70%>
			<colgroup>
				<col width=250 />
				<col width=* />
			</colgroup>
			<tr>
				<th>
					<!--이메일 주소--><spring:message code="label.emailAddress" />				</th>
				<td>
					<input type="text" id="email-form" name="email_address" class="t_input" size="50" value="" /><!-- disabled delete-->
				</td>
			</tr>
			<tr>
				<td class="space">&nbsp;</td>
			</tr>
			<tr>
				<th>
					<!--이름--><spring:message code="label.fullName" />				</th>
				<td>
					<input type="text" id="name-form" name="name" class="t_input" value="" size="50" />
				</td>
			</tr>
			<tr>
				<td class="space">&nbsp;</td>
			</tr>
			

			<tr>
				<th><spring:message code="label.gender" /></th>
				<td id="gender-radio">
					<input name="gender" type="radio" value="F" style="vertical-align:middle; margin-top:0"><spring:message code="label.male" />&nbsp;&nbsp;&nbsp;&nbsp;					
					<input name="gender" type="radio" class="gender"  value="M" style="vertical-align:middle; margin-top:0" checked><spring:message code="label.female" />
				</td>
			</tr>
			<tr>
				<td class="space">&nbsp;</td>
			</tr>

			<tr>
				<th><spring:message code="label.password" /></th>
				<td>
				
					<a id="password-Chenge" href="javascript:et_modal('365', '295', '', '0', '/ko/member/change_pw', '0', '1', '0','0');"class="btn mybtn-success" data-toggle="modal" data-target="#myModal">
					<spring:message code="label.resetpwd" /></a>
				</td>
			</tr>
			<tr>
				<td class="space">&nbsp;</td>
			</tr>

			<!-- <tr>
				<th>알림메일 수신</th>
				<td>
					<input name="mail" type="radio" style="vertical-align:middle; margin-top:0" checked>on&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="mail" type="radio" class="mail" style="vertical-align:middle; margin-top:0" value="Y">off
				</td>
			</tr>
			<tr>
				<td class="space">&nbsp;</td>
			</tr>

			<tr>
				<th>뉴스레터 수신</th>
				<td>
					<input name="news" type="radio" style="vertical-align:middle; margin-top:0" checked>on&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="news" type="radio" class="mail" style="vertical-align:middle; margin-top:0" value="Y">off
				</td>
			</tr> -->
			<tr>
				<td class="space">&nbsp;</td>
			</tr>
		</table>

		<!-- 비번변경 모달 -->
	<div class="modal fade" id="myModal" role="dialog">
    	<div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title"><spring:message code="label.resetpwdTitle" /></h4>
        </div>
        <div class="modal-body">
        	<form id="form-changepwd">
	          <input type='password' name='password' class='form-control' placeholder='<spring:message code="label.inputCurpwdMsg" />' style="margin: 10px 0;" />
	          <input type='password' name='newPassword' class='form-control' placeholder='<spring:message code="label.inputNewpwdMsg" />' style="margin: 10px 0;" />
          	</form>
          <input type='password' name='new-password-confirm' class='form-control' placeholder='<spring:message code="label.inputReInputNewpwdMsg" />' />
        </div>
        <div class="modal-footer">
        	<button class="btn btn-primary" id="btn-changepwd"><spring:message code="label.resetpwd" /></button>
          <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="label.close" /></button>  
        </div>
      </div>
      
    </div>
  </div>


		<div class="config_foot">
        <hr>
			<a href="#" id="user-delete" style="color:#666; font-size:12px;"><spring:message code="label.closeAccount" /></a>
            <div class="text-right">
                       <a id="userInfo-button" class="btn save-success" href="#"><spring:message code="label.saveInfo" /></a>
    		 </div>
		</div>
   	 </div>
               
  	 </div>
  	 <!-- 마이페이지 메인컨텐츠 종료  -->
		
              
	</div>
	</div>

    </div>

    </div>

    <!-- /.container -->
	
<hr>

    <!-- /.container -->

    