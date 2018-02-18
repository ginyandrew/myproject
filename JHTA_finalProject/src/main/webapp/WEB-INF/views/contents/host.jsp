<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/host.css" />
<script src="/resources/common/jquery.number.min.js"></script>
<script src="/resources/js/host.js"></script>

<div id="wrap">
	<div class="container">
		<div class="row">
			<div class="page-header">
				<h1 id="data-title">관리자 페이지</h1>
			</div>
			<div class="col-xs-2 col-sm-2 col-md-2">
				<div class="panel panel-default">
					<div class="panel-heading"><h4 class="text-center">관리자 메뉴</h4></div>
					<div class="panel-body">
						<button id="get-user" class="btn btn-default get-btn">사용자 정보</button>
						<button id="get-city" class="btn btn-default get-btn">도시 정보</button>
						<button id="get-dest" class="btn btn-default get-btn">여행지 정보</button>
						<button id="get-plan" class="btn btn-default get-btn">일정 정보</button>
					</div>
				</div>
			</div>
			<div class="col-md-10 col-sm-10 col-xs-12">
				<div id="button-area" style="height: 40px;"></div>
				<table id="data-table" class="table table-bordered table-striped table-fixed">
					<thead>
						<!-- <tr>
							<th style="width: 50px;">No</th>
							<th style="width: 160px;">Name</th>
							<th>Email</th>
							<th style="width: 50px;">Gender</th>
							<th style="width: 160px;">RegDate</th>
							<th style="width: 100px;">Point</th>
							<th style="width: 50px;">수정</th>
							<th style="width: 50px;">삭제</th>
						</tr> -->
					</thead>
					<tbody>
						<!-- <tr>
							<td>99999999</td>
							<td>고길동</td>
							<td>Ko.h@gmail.com</td>
							<td>남</td>
							<td>2012.00.12 01:23:45</td>
							<td>99999999</td>
							<td><button class="btn btn-xs btn-default">수정</button></td>
							<td><input type="checkbox" name="remove" /></td>
						</tr> -->
					</tbody>
				</table>
			</div>
		</div>
		
		<!-- 사용자 추가 Modal -->
		<div id="user-add-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>사용자 추가</p>
					</div>
					<div class="modal-body">
						<form id="user-add-form" method="post" class="form-group col-xs-offset-1 col-xs-10" action="user/add.tm">
							<label>Email</label>
		 					<input type="email" class="form-control" name="email" />
		 					<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>Password</label>
		 					<input type="password" class="form-control" name="password" />
		 					<label>Gender</label>
		 					<select class="form-control" name="gender">
		 						<option value='M'>남</option>
		 						<option value='F'>여</option>
		 					</select>
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>추가</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 사용자에게 쪽지 보내기 Modal -->
		<div id="user-message-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>쪽지 보내기</p>
					</div>
					<div class="modal-body">
						<form id="user-message-form" method="post" class="form-group col-xs-offset-1 col-xs-10" action="sendmsg.tm">
							<label>User</label>
							<input type="text" class="form-control" id="usernamelist" readonly="readonly" />
							<input type="hidden" name="usernolist" />
							<label>Message</label>
		 					<textarea class="form-control" rows="10" name="message"></textarea>
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>보내기</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 사용자 수정 Modal -->
		<div id="user-modify-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>사용자 수정</p>
					</div>
					<div class="modal-body">
						<form id="user-modify-form" method="post" class="form-group col-xs-offset-1 col-xs-10" action="user/modify.tm">
							<label>No</label>
							<input type="text" class="form-control" name="no" readonly="readonly" />
							<label>Email</label>
		 					<input type="email" class="form-control" name="email" />
		 					<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>Gender</label>
		 					<select class="form-control" name="gender">
		 						<option value='M'>남</option>
		 						<option value='F'>여</option>
		 					</select>
		 					<label>Point</label>
		 					<input type="number" class="form-control" name="point" />
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>수정</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 도시 추가 Modal -->
		<div id="city-add-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>도시 추가</p>
					</div>
					<div class="modal-body">
						<form id="city-add-form" method="post" class="form-group col-xs-offset-1 col-xs-10"
							action="city/add.tm" enctype="multipart/form-data">
		 					<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>Lat</label>
		 					<input type="text" class="form-control" name="lat" />
		 					<label>Lng</label>
		 					<input type="text" class="form-control" name="lng" />
		 					<label>State</label>
		 					<select class="form-control" name="stateCode.code">
		 						<c:forEach var="stateCode" items="${stateCodeList }">
		 							<option value="${stateCode.code }">${stateCode.name }</option>
		 						</c:forEach>
		 					</select>
		 					<label>Details</label>
		 					<textarea class="form-control" name="details"></textarea>
		 					<!-- 이미지 -->		 					
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-danger pull-right do-btn'>추가</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 도시 수정 Modal -->
		<div id="city-modify-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>도시 수정</p>
					</div>
					<div class="modal-body">
						<form id="city-modify-form" method="post" class="form-group col-xs-offset-1 col-xs-10"
							action="city/modify.tm" enctype="multipart/form-data">
							<label>No</label>
		 					<input type="text" class="form-control" name="no" readonly="readonly" />
		 					<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>Lat</label>
		 					<input type="text" class="form-control" name="lat" />
		 					<label>Lng</label>
		 					<input type="text" class="form-control" name="lng" />
		 					<label>State</label>
		 					<!-- <select class="form-control" disabled="disabled" name="stateCode.code"> -->
		 					<select class="form-control" name="stateCode.code">
		 						<c:forEach var="stateCode" items="${stateCodeList }">
		 							<option value="${stateCode.code }">${stateCode.name }</option>
		 						</c:forEach>
		 					</select>
		 					<label>Details</label>
		 					<textarea class="form-control" rows="10" name="details"></textarea>
		 					<!-- 이미지 -->
		 					<label>File(현재 개수: <span class="img-count"></span>)</label>
		 					<input type="file" class="form-control" name="file" />		 					
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>수정</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>		
		
		<!-- 여행지 추가 Modal -->
		<div id="dest-add-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>여행지 추가</p>
					</div>
					<div class="modal-body">
						<form id="dest-add-form" method="post" class="form-group col-xs-offset-1 col-xs-10"
							action="dest/add.tm" enctype="multipart/form-data">
							<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>City</label>
		 					<select class="form-control" name="city.no">
		 						<c:forEach var="city" items="${cityList }">
		 							<option value="${city.no }">${city.name }</option>
		 						</c:forEach>
		 					</select>
		 					<label>Category</label>
		 					<div class="row">
		 						<div class="col-xs-6">
				 					<select class="form-control" name="category">
				 						<c:forEach var="category" items="${categoryList }">
				 							<option value="${category.code }">${category.name }</option>
				 						</c:forEach>
				 					</select>
			 					</div>
			 					<div class="col-xs-6">
				 					<select class="form-control" name="categorysub">
			 							<option value=""></option>
				 						<c:forEach var="category" items="${categoryList }">
				 							<option value="${category.code }">${category.name }</option>
				 						</c:forEach>
				 					</select>
			 					</div>
		 					</div>
		 					<label>Address</label>
		 					<input type="text" class="form-control" name="address" />		
		 					<label>Lat</label>
		 					<input type="text" class="form-control" name="lat" />
		 					<label>Lng</label>
		 					<input type="text" class="form-control" name="lng" />
		 					<label>Contact</label>
		 					<input type="text" class="form-control" name="contact" />
		 					<label>OpenTime</label>
		 					<input type="text" class="form-control" name="openTime" />
		 					<label>Site</label>
		 					<input type="text" class="form-control" name="site" />
		 					<label>Details</label>
		 					<textarea class="form-control" rows="5" name="details"></textarea>
		 					<!-- 이미지 -->
		 					<label>File</label>
		 					<input type="file" class="form-control" name="file" />		
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>추가</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 여행지 수정 Modal -->
		<div id="dest-modify-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>여행지 수정</p>
					</div>
					<div class="modal-body">
						<form id="dest-modify-form" method="post" class="form-group col-xs-offset-1 col-xs-10"
							action="dest/modify.tm" enctype="multipart/form-data">
							<label>No</label>
		 					<input type="text" class="form-control" name="no" readonly="readonly" />
							<label>Name</label>
		 					<input type="text" class="form-control" name="name" />
		 					<label>City</label>
		 					<select class="form-control" name="city.no">
		 						<c:forEach var="city" items="${cityList }">
		 							<option value="${city.no }">${city.name }</option>
		 						</c:forEach>
		 					</select>
		 					<label>Category</label>
		 					<div class="row">
		 						<div class="col-xs-6">
				 					<select class="form-control" name="category">
				 						<c:forEach var="category" items="${categoryList }">
				 							<option value="${category.code }">${category.name }</option>
				 						</c:forEach>
				 					</select>
			 					</div>
			 					<div class="col-xs-6">
				 					<select class="form-control" name="categorysub">
			 							<option value=""></option>
				 						<c:forEach var="category" items="${categoryList }">
				 							<option value="${category.code }">${category.name }</option>
				 						</c:forEach>
				 					</select>
			 					</div>
		 					</div>
		 					<label>Address</label>
		 					<input type="text" class="form-control" name="address" />		
		 					<label>Lat</label>
		 					<input type="text" class="form-control" name="lat" />
		 					<label>Lng</label>
		 					<input type="text" class="form-control" name="lng" />
		 					<label>Contact</label>
		 					<input type="text" class="form-control" name="contact" />
		 					<label>OpenTime</label>
		 					<input type="text" class="form-control" name="openTime" />
		 					<label>Site</label>
		 					<input type="text" class="form-control" name="site" />
		 					<label>Details</label>
		 					<textarea class="form-control" rows="5" name="details"></textarea>
		 					<!-- 이미지 -->
		 					<label>File(현재 개수: <span class="img-count"></span>)</label>
		 					<input type="file" class="form-control" name="file" />		
						</form>
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-primary pull-right do-btn'>수정</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 삭제처리 Modal -->
		<div id="remove-modal" class="modal fade unselectable" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<p class='modal-title'>삭제 확인</p>
					</div>
					<div class="modal-body">
						<h3 class="text-center">선택한 정보를 정말 삭제할까요?</h3>
						<input type="hidden" name="type" value="" />
						<input type="hidden" name="removeNo" value="" />
					</div>
					<div class="clear"></div>
					<div class="modal-footer">
						<button class='btn btn-danger pull-right remove-btn'>삭제</button>
						<button class='btn btn-default pull-right' data-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty setTable }">
<script>
$(function(){$("button#get-"+'${setTable }').trigger("click")});
</script>
</c:if>