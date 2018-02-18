<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<!-- Bootstrap -->
<link rel="stylesheet" href="/resources/common/bootstrap/css/bootstrap.min.css">
<script src="/resources/common/jquery-1.12.0.min.js"></script>
<script src="/resources/common/bootstrap/js/bootstrap.min.js"></script>

<!-- 공통부분 -->
<script src="/resources/common/util.js"></script>
<script src="/resources/common/common.js"></script>
<link rel="stylesheet" href="/resources/common/common.css" />

<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/planmake.css" />
<script type="text/javascript" src="http://apis.daum.net/maps/maps3.js?apikey=01547b2af7b0b23180b63ef3365457ea&libraries=services,clusterer"></script>
<script src="/resources/js/planmake.js"></script>

<!-- datepicker -->
<!-- sort -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
</head>
<body>
	<input type="hidden" name="planTest" value='${planToJson }' />
	<div class="container-fluid">
		<input type="hidden" name="leaveDateToDate" value="${plan.leaveDateToDate }"/>
		<form action="planmake.tm" method="get">
			<input type="hidden" name="planNo" value="${plan.no}"/>
			<input type="hidden" name="plan-days" value="0"/>
		</form>
		<!-- 헤더 -->
		<div id="header" class="row">
			<div class="navbar-header">
				<a id="logo" class="navbar-brand" href="/main.tm"><img src="../resources/common/img/bi1.png" /></a> 
			</div>
			<div class="navbar-brand">
				<span class="navbar">여행할 도시와 각 여행일정을 선택하세요</span>
			</div> 
			<div class="pull-right">
				<a id="modify" type="button" class="btn btn-warning" data-toggle="modal" data-target="#saveModal">저장</a>
				<a id="close" href="" class="btn btn-warning">닫기</a>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="sty_posi_rela">

			<div class="panel panel-default firstsidebar">
				<div class="panel-heading">전체일정</div>
				<div id="dayBoxes" class="panel panel-default">
					<!-- 이 위치에 dayBox가 추가됨. -->
					
					<c:forEach var='day' begin="1" end="${plan.period }">
					<div id="daybox-${day }" class="panel-body daybox">
		   		  		<div class="pull-left datelist">
			   		  		<span class="daynumber">DAY${day }</span>
	  	    				<span class="datelisttext">> <fmt:formatDate value="${plan.getDateInPeriodToDate(day-1)  }" pattern="MM.dd [EEEEEE]" /></span>
	   	    				<span class='glyphicon glyphicon-remove dayremove'></span>
			   	    		<div class="pull-left">	
			   	    			<c:forEach var='numbering' items="${plan.getCitiesOfPlanDetail(day) }">
			   	    			<p class="cityno" data-cityno="${numbering.no }">${numbering.name }</p>
		   	    				</c:forEach>
		   	    			</div>
		   	    		</div>
		   	    	</div>
		   	    	</c:forEach>		
	            </div>
	            
	            <div class="panel-putter">
					<button id="addDay">DAY 추가</button>
				</div>
			</div>


		<div class="panel panel-default pull-left selsidebar">
			<div id="seldestday" class="panel-heading"></div>
			<!--div>경로 최적화 하기</div-->
				
			<div id="seldestboxes" class="panel-body">
					
			<c:forEach var='day' begin="1" end="${plan.period }">
				<!-- seldestbox를 위치-->
				<ul id="sortable" class="sortbox" data-viewlevel="${day }">
					<c:forEach var='number' begin="0" end="${fn:length(plan.planDetailMap[day])-1 }"> 
					
					<c:choose>
						<c:when test="${plan.planDetailMap[day][number].numbering eq 0 }">
						
						</c:when>
						<c:otherwise>
							<div id='seldestbox-${number }' class='seldestbox' lat="${plan.planDetailMap[day][number].dest.lat}" lng='${plan.planDetailMap[day][number].dest.lng}'>
			    		        <input type='hidden' class='selbudget' value='${plan.planDetailMap[day][number].budget }'/>
		    			        <input type='hidden' class='selmemo' value='${plan.planDetailMap[day][number].memo }'/>
			 					<div class='city'>
			 						<img class='cityimg' src='../resources/img/dest/${plan.planDetailMap[day][number].dest.mainImgName }' /> 
			 						<div class='destname' data-dest='${plan.planDetailMap[day][number].dest.no }'>
			 							<div>${plan.planDetailMap[day][number].dest.name }</div>
			  	 						<span class='glyphicon glyphicon-remove destremove'></span>
			 						</div>
		    		        	</div>
		    		        </div>	
						</c:otherwise>
					</c:choose>
					</c:forEach>
				</ul>
   	    	</c:forEach>			 
			</div>
			
			<div class="panel-putter">
				<button id="selesase">일정 비우기</button>
			</div>
		</div>
			
			<div class="panel panel-default pull-left thirdsidebar" >
				<div class="panel-heading" style="height: 37px;">
					<span id="cityname" data-cityno=""></span>
		
					<span class="pull-right">
						<button id="modalbutton" type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#myModal">도시변경</button>
					</span>
				</div>
				<div class="panel panel-default pull-left detailbar" >
					<div id="catelist" class="btn-group catelist">
						<button class="btn btn-default" value="100">음식점</button>
						<button class="btn btn-default" value="200">액티비티</button>
						<button class="btn btn-default" value="300">관광명소</button>
						<button class="btn btn-default" value="400">쇼핑</button>
					</div>
				</div> 
			
				<!-- destbox -->
				<div id="destboxes" class="panel-body" style="position:relative;">
				   
				</div>
			</div>
			
			<!-- map -->
			<div id="map" class="mainmap">

				<div class="panel panel-default pull-left detailbar" >
					<div class="panel-heading" style="height: 37px;">
						지역명
						<span class="pull-right">
							<a class="btn-primary btn-xs">일정추가</a>
							<a id="thirdsidebar-close" class="btn btn-default btn-xs full-right">X</a>
						</span>
					</div>
					<div class="panel-body" style="position:relative;">
						<div>
							<img alt="" src=""/>
						</div>
						<div>
							<span id="detailtext"></span>
							<div>
								<span id="destinfo"></span>
							</div>
						</div>
						<div id="reviews">
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">지역을 선택하세요</h4>
				</div>
				
				<div class="modal-body">
					<div id="citybutton" class="btn-group">
  						<c:forEach var="city" items="${cityData }">
  							
  							<button type="button" class="btn btn-primary" data-dismiss="modal" value="${city.city.no } ">${city.city.name }</button>
  						</c:forEach>
					</div>
		
				</div>
				
			</div>

		</div>
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="saveModal" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title"></h4>
				</div>
				
				<div class="modal-body">
					<div class="form-group">
						<input type='hidden' name='userno' value="${LOGIN_USER.no}" />
					</div>
					<div id="seltheme"class="form-group">
						<label>테마선택</label><br/>
						<button type="button" class="btn btn-primary" data-value="1">친구와 함께</button>
						<button type="button" class="btn btn-primary" data-value="2">커플 여행</button>
						<button type="button" class="btn btn-primary" data-value="3">단체 여행</button><br/>
						<button type="button" class="btn btn-primary" data-value="4">나홀로 여행</button>
						<button type="button" class="btn btn-primary" data-value="5">비즈니스 여행</button>
						<button type="button" class="btn btn-primary" data-value="6">가족 여행</button>
					</div>
					<div id="published" class="form-group">
						<label>공개여부</label>
						<button type="button" class="btn btn-primary active" data-value="F">비공개</button>
						<button type="button" class="btn btn-primary" data-value="T">공개</button>
					</div>
				</div>
				<div class="modal-footer">
					<a type="button" class="btn btn-default" id="saved" data-dismiss="modal">저장</a>
					<a type="button" class="btn btn-default" data-dismiss="modal">닫기</a>
				</div>
				
			</div>

		</div>
	</div>
</body>
</html>