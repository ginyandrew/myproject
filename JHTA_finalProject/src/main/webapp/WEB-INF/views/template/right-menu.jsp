<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 우측 메뉴: 최근 본 일정의 목록을 볼 수 있게 해줌 -->
<span id="right-menu-open" class="unselectable"><span class="glyphicon glyphicon-chevron-left"></span></span>
<div id="right-menu" class="unselectable">
	<div id="right-menu-title" class="text-center">
		<spring:message code="label.history" /> <span id="right-menu-close" class="glyphicon glyphicon-remove"></span>
	</div>
	<div id="right-menu-contents">
		<div class="text-center">
			<c:forEach var="item" varStatus="status" items="${HISTORY.descendingSet() }">
				<c:if test="${status.index lt 3 }">
					<div class="dest-item">
						<a href="${item.url }" class="item-img"> <img src="${item.mainImgName }" width="100" alt="" /></a>
						<p class="text-center">
							<c:choose>
								<c:when test="${item.type eq 'plan' }">
									<a href="${item.url }" class="item-title">[${item.type }] ${item.name }</a>
								</c:when>
								<c:otherwise>
									<a href="${item.url }" class="item-title">[${item.type }] ${item.name }</a>
								</c:otherwise>
							</c:choose>
						</p>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>