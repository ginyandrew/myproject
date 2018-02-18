<%@page import="kr.co.jhta.domain.CategoryCodeVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/infolist.css" />
<script src="/resources/js/infolist.js"></script>
<div class="col-md-12">
	<div class="container">
		<div id="title-area">
			<div id="search-area" class="text-center">
				<span class="text-center"><spring:message code="label.search" /></span> <input type="text" id="input-search" class="form-control" name="search-data" placeholder="<spring:message code="label.inputPlaceHolder" />" />
				<!-- <span id="search-left" class="pull-left">최근 검색어: <span class="search-item">홍콩</span>, <span class="search-item">미국</span></span> -->
				<!-- <a href="/plan/make.tm"><span id="search-right" class="pull-right">지도에서 검색></span></a> -->
			</div>
		</div>
		<!-- <div id="title-area">			
			<img class="title-image" src="/resources/common/img/title-img.jpg" width="974" height="398">
			<div id="search-area" class="text-center">
				<span class="text-center" style="">검색</span>
				<input type="text" id="input-search" name="search-data" placeholder="입력하세요" />
			</div>
		</div> -->
		<div class="row">
			<div id="contents-area">
				<div id="famous-city-area">
					<div class="row">
						<p class="title text-center"><spring:message code="label.popCity" /></p>
					</div>
					<div class="img-frame row">
						<c:forEach var="city" items="${famousCityList }">
							<div class="img-data unselectable" data-toggle="tooltip" data-placement='top' title="${city.name }">
								<a href="city.tm?cityNo=${city.no }" class="city-img"> <c:choose>
										<c:when test="${not empty city.mainImgName }">
											<img src="/resources/img/city/${city.mainImgName }" />
										</c:when>
										<c:otherwise>
											<img src="/resources/img/noimage.jpg" />
										</c:otherwise>
									</c:choose>

								</a> <span class="img-name">${city.name }</span>
							</div>
						</c:forEach>
					</div>
				</div>
				<div id="theme-city-area">
					<div class="row">
						<p class="title text-center"><spring:message code="label.themeTour" /></p>
					</div>
					<div id="theme-table" class="row">
						<c:forEach var="theme" items="${themeDestMap.keySet() }">
							<c:if test="${theme.code mod 100 ne 0 }">
								<div id="theme-${theme.code }-img-area" class="theme-img-area">
									<c:forEach var="dest" items="${themeDestMap.get(theme) }">
										<a href="dest.tm?destNo=${dest.no }" class="dest-img" data-toggle="tooltip" title="${dest.name }"> <c:choose>
												<c:when test="${not empty dest.mainImgName }">
													<img src="/resources/img/dest/${dest.mainImgName }" alt="">
												</c:when>
												<c:otherwise>
													<img src="/resources/img/noimage.jpg" />
												</c:otherwise>
											</c:choose>
										</a>
									</c:forEach>
								</div>
							</c:if>
						</c:forEach>
						<div id="theme-list-area">
							<ul class="list-group">
								<c:forEach var="category" items="${themeDestMap.keySet() }">
									<c:choose>
										<c:when test="${(category.code) % 100 != 0 }">
											<li class="list-group-item unselectable theme-list" data-code="${category.code }">▷${category.name }</li>
										</c:when>
										<c:otherwise>
											<li class="list-group-item unselectable theme-title text-center" data-code="${category.code }">${category.name }(${category.code })</li>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</ul>
						</div>
						<div class="clear"></div>
						<div id="theme-selectbox-area">
							<div class="col-xs-3 form-group">
								<select name="cityNo" class="form-control">
									<option value=""><spring:message code="label.city" /></option>
									<c:forEach var="city" items="${cityList }">
										<option value="${city.no }">${city.name }</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-xs-3 form-group">
								<select name="code" class="form-control">
									<option value=""><spring:message code="label.category" /></option>
									<c:forEach var="category" items="${themeDestMap.keySet() }">
										<c:if test="${(category.code) % 100 != 0 }">
											<option value="${category.code }">${category.name }</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="col-xs-3 form-group">
								<input type="text" name="keyword" class="form-control" placeholder="ex)맛집" />
							</div>
							<button class="btn btn-default" id="theme-search-btn"><spring:message code="label.search" /></button>
						</div>
						<div id="theme-search-result-area" class="img-frame row"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>