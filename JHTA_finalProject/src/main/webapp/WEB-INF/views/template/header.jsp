<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- 로그인에 실패한 경우 모달 띄우기 -->
<c:choose>
	<c:when test="${msgName eq 'login'}">
	<script>
	$(function(){
		$("div#login-box div.modal-header").append($getAlert("alert-login", '${msg }', '${msgType }'));
		$("div#alert-login").show();
		
		$("div#login-box").modal("show");
	});</script>
	</c:when>
	<c:otherwise>
		<!-- 메시지 처리 -->
		<c:if test="${not empty msg }">
			<script>
			$(function(){
				// 알림창 만들기
				$("div#header").append($getAlert("alert-header", '${msg }', "${not empty msgType?msgType:'success' }"));
				// 알림창 띄우기
				$("div#alert-header").slideDown("slow");
			});</script>			
		</c:if>				
	</c:otherwise>
</c:choose>
<!-- 헤더 -->
<div id="header" class="unselectable">
	<div id="header-row" class="row">
		<div class="col-md-offset-0">
			<div class="navbar-header">
				<a href="/main.tm"><img src="/resources/common/img/bi1.png" style="border:0px" alt="Travel Maker"/></a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span style="margin:25px 0px 20px 0px">
                <a style="color:#333" href="/info/list.tm"><spring:message code="label.spots" /></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <a style="color:#333" href="/plan/list.tm"><spring:message code="label.plan" /></a>
                </span>
				<!-- <li><a href="#">이용방법</a></li> -->
			
			</div>
		</div>
		<div style="margin-top: 30px; float:right">
			<div class="pull-right">
				<div id="user-box">
					<c:choose>
						<c:when test="${not empty LOGIN_USER}">
							<span class="user-info">${LOGIN_USER.name }<spring:message code="label.nim" /></span>
							<span class="glyphicon glyphicon-user"></span>
							<span class="glyphicon glyphicon-bell"></span>
							<div id="msgBox" class="panel panel-primary">
						        <div class="panel-heading">
						            <h4 class="text-center msgbox-title"><spring:message code="label.msgbox" /></h4>
						        </div>
						        <div class="panel-body"></div>
						    </div>
						    <input type="text" class="form-control" id="search-header" name="search-header" />
							<!-- 로그인 유저 정보 -->
							<div id="hidden-user-data">
								<input type="hidden" name="userNo" value="${LOGIN_USER.no }" />
								<%-- <input type="hidden" name="email" value="${LOGIN_USER.email }" /> --%>
								<input type="hidden" name="name" value="${LOGIN_USER.name }" />
								<%-- <input type="hidden" name="gender" value="${LOGIN_USER.gender }" /> --%>
								<%-- <input type="hidden" name="regdate" value="${LOGIN_USER.regdate }" />  --%>
								<input type="hidden" name="point" value="${LOGIN_USER.point }" />
							</div>
						</c:when>
						<c:otherwise>
							<input type="text" class="form-control" id="search-header" name="search-header" />
							<button id="login-btn-header" class="btn btn-default" data-toggle="modal" data-target="#login-box"><spring:message code="label.login" /></button>
							<button id="register-btn-header" class="btn btn-default" data-toggle="modal" data-target="#register-box"><spring:message code="label.register" /></button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${empty LOGIN_USER}">
		<!-- 로그아웃 상태에서만 모달을 만듬 -->
		<!-- Login Modal -->
		<div id="login-box" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class="modal-title"><spring:message code="label.login" /></p>
					</div>
					<div class="modal-body">
						<form id='login-form' method='post' action='/login.tm'>
							<input type='hidden' name='beforepage' value='' /> <input type='text' name='email' class='form-control' placeholder='<spring:message code="label.email" />' />
							<input type='password' name='password' class='form-control' placeholder='<spring:message code="label.password" />' />
						</form>
					</div>
					<div class="modal-footer">
						<button class='btn btn-default dialog-button ok-btn' id='login-btn-dialog'><spring:message code="label.login" /></button>
						<button class='btn btn-default dialog-button' style="margin: 10px 0 0 0" data-dismiss="modal"><spring:message code="label.close" /></button>
					</div>
				</div>
			</div>
		</div>

		<!--  Modal -->
		<div id="register-box" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'><spring:message code="label.register" /></p>
					</div>
					<div class="modal-body">
						<form id='register-form' method='post' action='/register.tm'>
							<!-- <input type='hidden' name='before-page' value='d' /> -->
							<input type='text' name='email' class='form-control' placeholder='<spring:message code="label.email" />' />
							<input type='password' name='password' class='form-control' placeholder='<spring:message code="label.password" />' />
							<input type='password' name='password-check' class='form-control' placeholder='<spring:message code="label.confirmpwd" />' />
							<input type='text' name='name' class='form-control' placeholder='<spring:message code="label.name" />' />
							<label for='male-register-dialog' style='font-size: 20px; margin-left: 70px; margin-right: 70px;'><spring:message code="label.gender" /></label>
							<input type='radio' name='gender' id='male-radio' value='M' checked="checked" /> <span style='margin-right: 70px; font-size: 20px; cursor: pointer;'> <spring:message code="label.male" /></span>
							<input type='radio' name='gender' id='female-radio' value='F' /><span style='font-size: 20px; cursor: pointer;'> <spring:message code="label.female" /></span>
						</form>
					</div>
					<div class="modal-footer">
						<button class='btn btn-default dialog-button ok-btn' id='register-btn-dialog'><spring:message code="label.regConfirm" /></button>
						<button class='btn btn-default dialog-button' style="margin: 10px 0 0 0" id='clear-btn-dialog'><spring:message code="label.regResetAll" /></button>
						<button class='btn btn-default dialog-button' style="margin: 10px 0 0 0" data-dismiss="modal"><spring:message code="label.regClose" /></button>
					</div>
				</div>
			</div>
		</div>
	</c:if>
</div>