<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<!-- <script src="/resources/common/jquery-ui/jquery-ui.js"></script> -->
<script src="/resources/common/jquery.number.min.js"></script>
<script src="/resources/js/plandetail.js"></script>
<!-- <link rel="stylesheet" href="/resources/common/jquery-ui/jquery-ui.css"> -->
<link rel="stylesheet" href="/resources/css/plandetail.css" />
<!-- 지도 키 추가 -->
<script type="text/javascript" src="http://apis.daum.net/maps/maps3.js?apikey=01547b2af7b0b23180b63ef3365457ea&libraries=services,clusterer"></script>
<input type="hidden" name="planNo" value="${param.no }" />
<div id="wrap">
	<div class="container">
		<!-- 전체 제목 -->
		<div id="cover-area">
			<div id="cover-info">
				<p id="cover-writer">${plan.user.name }</p>
				<p id="cover-title">${plan.title }</p>
				<p id="cover-sub1">
					<span class="cover-date">
						<fmt:formatDate value="${plan.leaveDateToDate }" pattern="yyyy.MM.dd" /> ~
						<fmt:formatDate value="${plan.arriveDateToDate }" pattern="yyyy.MM.dd" />
						(${plan.period }
						<c:choose>
							<c:when test="${plan.period eq 1 }"><spring:message code="label.day" />)</c:when>
							<c:otherwise><spring:message code="label.days" />)</c:otherwise>
						</c:choose>
					<%-- ${plan.planDateToString } --%><!-- 2016.01.01~2016.01.03 (3일) -->
					</span> <span class="badge cover-theme">${plan.themeCodeToString }</span>
				</p>
				<div id="cover-sub2">
					<span class="glyphicon glyphicon-map-marker"></span> ${plan.details.size() }
					<span class="glyphicon glyphicon-eye-open"></span> ${views }
					<span class="glyphicon glyphicon-copy"></span> ${plan.copyCount }
				</div>
				<div id="cover-budget">
					<spring:message code="label.budget" />: <span id="unit">￦</span> <span id="budget">${plan.totalBudget }</span>
					<input type="hidden" name="total-budget" value="${plan.totalBudget }">
				</div>
				<div class="clear"></div>
			</div>
		</div>
		<!-- 복사 및 출력 버튼 -->
		<div id="copybtn-area">
			<c:choose>
				<c:when test="${hasAuthority }">
					<button class="btn btn-default" id="plan-modify-btn" onclick="window.location.href='/plan/modify.tm?no=${param.no }'"><span class="glyphicon glyphicon-pencil"></span> <spring:message code="label.modifyPlan" /></button>
					<button class="btn btn-default" id="plan-copy-modal-btn"><span class="glyphicon glyphicon-calendar"></span> <spring:message code="label.copyPlan" /></button>
					<button class="btn btn-default" id="plan-to-excel-btn"><span class="glyphicon glyphicon-list-alt"></span> <spring:message code="label.excel" /></button>
					<button class="btn btn-default" id="plan-delete-modal-btn" data-toggle="modal" data-target="#delete-box"><span class="glyphicon glyphicon-remove"></span> <spring:message code="label.deletePlan" /></button>
				</c:when>
				<c:otherwise>
					<button class="btn btn-default" id="plan-copy-modal-btn"><span class="glyphicon glyphicon-calendar"></span> <spring:message code="label.copyPlan" /></button>
					<button class="btn btn-default" id="plan-to-excel-btn"><span class="glyphicon glyphicon-list-alt"></span> <spring:message code="label.excel" /></button>				
				</c:otherwise>
			</c:choose>
		</div>
		<!-- 내용 -->
		<div class="row">
			<!-- 내용 일정 부분 -->
			<div id="contents-area" class="col-sm-8 col-md-8">
				<div class="row">
					<div class="col-md-12 col-sm-12">
						<!-- Day의 제목 부분 -->
						<c:forEach var="day" begin="1" varStatus="dayStatus" end="${plan.period }">
							<div class="plan-title-area">
								<div id="day-${day }" class="plan-day">DAY${day }</div>
								<div class="plan-desc">
									<p class="plan-date">
										<fmt:formatDate value="${plan.getDateInPeriodToDate(day-1) }" pattern="yyyy.MM.dd (E)"/>
									</p>
									<p class="plan-cities">
										<c:forEach var="city" varStatus="status" items="${plan.getCitiesOfPlanDetail(day) }">
											<a href="/info/city.tm?cityNo=${city.no }">${city.name }</a><c:if test="${!status.last }">,</c:if>
										</c:forEach>
										<%-- ${plan.getCitiesOfPlanDetail(day).name } --%>
									</p>
								</div>
								<div class="clear"></div>
							</div>
						
							<c:forEach var="detail" varStatus="status" items="${plan.planDetailMap.get(day) }">								
								<!-- Day에 해당하는 상세 일정 부분 -->
								<div class="plan-details-area">
									<!-- 일정 순서 -->
									<div class="plan-numbering">
										<span class="numbering">${detail.numbering }</span>
									</div>
									<!-- 해당 여행지의 정보 -->
									<div class="plan-detail">
										<a class="dest-info" href="/info/dest.tm?destNo=${detail.dest.no }">
											<img class="dest-img" src="/resources/img/dest/${detail.dest.mainImgName}" width="90" height="90" alt="">
										</a>
										<div class="dest-info">
											<div class="dest-title">${detail.dest.name }</div>
											<div class="dest-category">
												<c:forEach var="category" items="${detail.dest.categoryList }">
													<span class="badge">${category.name }</span>
												</c:forEach>
											</div>
										</div>
										<div class="dest-move" style="height: 100%;">
											<a class="dest-info" href="/info/dest.tm?destNo=${detail.dest.no }">
												<span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement='top' title="<spring:message code="label.move" />"></span>
											</a>
										</div>
									</div>
								</div>
								<c:if test="${!status.last }">
									<div class="dest-linking">
										<span>↓</span>
									</div>
								</c:if>
							</c:forEach>

							<c:if test="${!dayStatus.last }">
								<div class="dest-linking">
									<span>↓</span>
								</div>
							</c:if>
						</c:forEach>
						
					</div>
				</div>

			</div>
			<div id="map-area" class="col-xs-12 col-sm-4 col-md-4">
				<div id="map"></div>
			</div>
		</div>
	</div>
	
	
	<!-- 일정 복사 Modal -->
	<div id="copy-box" class="modal fade unselectable" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<p class='modal-title'><spring:message code="label.copyPlan" /></p>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<input type="text" name="title" class="form-control" placeholder="Title" />
						<input type="text" name="leaveDate" id="input-date" class="form-control" readonly="readonly" value="2016-02-01"/>
						<hr />
						<div style="width:400px; margin:0 auto;">
							<ul class="list-group" style="width: 200px; float: left">
								<li class="list-group-item theme-li active" id="theme-1"><spring:message code="label.friendTour" /></li>
								<li class="list-group-item theme-li" id="theme-2"><spring:message code="label.coupleTour" /></li>
								<li class="list-group-item theme-li" id="theme-3"><spring:message code="label.groupTour" /></li>
							</ul>
							<ul class="list-group" style="width: 200px; float: left">
								<li class="list-group-item theme-li" id="theme-4"><spring:message code="label.soloTour" /></li>
								<li class="list-group-item theme-li" id="theme-5"><spring:message code="label.businessTour" /></li>
								<li class="list-group-item theme-li" id="theme-6"><spring:message code="label.familyTour" /></li>
							</ul>
							<div class="clear"></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button class='btn btn-primary pull-right' style="margin: 10px 0 0 0" id='plan-copy-btn'><spring:message code="label.copy" /></button>
					<button class='btn btn-default pull-right' style="margin: 10px 0 0 0" data-dismiss="modal"><spring:message code="label.cancel" /></button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 일정 삭제 Modal -->
	<div id="delete-box" class="modal fade unselectable" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<p class='modal-title'><spring:message code="label.deletePlan" /></p>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<p style="font-size: 18px;"><spring:message code="label.deleteMsg" /></p>
					</div>
				</div>
				<div class="modal-footer">
					<a href="delete.tm?no=${param.no }" class='btn btn-default btn-danger' id='plan-delete-btn'><spring:message code="label.delete" /></a>
					<button class='btn btn-default' data-dismiss="modal"><spring:message code="label.cancel" /></button>
				</div>
			</div>
		</div>
	</div>

	<!-- 왼쪽 메뉴(내비): 사용자를 따라다니며 특정 Day로 이동할 수 있게 해줌 -->
	<div id="left-menu" class="unselectable">
		<div id="left-menu-contents" class="text-center">
			<c:forEach var="day" begin="1" end="${plan.period }">
				<p>
					<span class="left-day-move"> D${day } <c:forEach var="city" varStatus="status" items="${plan.getCitiesOfPlanDetail(day) }">${city.name }<c:if test="${!status.last }">,</c:if></c:forEach></span>
				</p>
			</c:forEach>
		</div>
	</div>

</div>
