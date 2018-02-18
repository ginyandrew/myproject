<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- 푸터 -->
<div id="footer">
	<div class="row">
		<div class="col-md-12">
			<div class="row">
				<div class="col-md-3 col-sm-2 col-xs-12">
					<p class="footer-title">Travel Maker</p>
					<p><a href="/main.tm"><spring:message code="label.main" /></a></p>
					<p><a href="/mypage.tm"><spring:message code="label.mypage" /></a></p>
					<p><a href="/setting.tm"><spring:message code="label.setMyPage" /></a></p>
				</div>
				<div class="col-md-3 col-sm-2 col-xs-12">
					<p class="footer-title"><spring:message code="label.trip" /></p>
					<p><a href="/info/list.tm"><spring:message code="label.spotList" /></a></p>
				</div>
				<div class="col-md-3 col-sm-2 col-xs-12">
					<p class="footer-title"><spring:message code="label.plan" /></p>
					
					<p><a href="/plan/list.tm"><spring:message code="label.planList" /></a></p>
					<p><a href="/mypage.tm"><spring:message code="label.mySchedule" /></a></p>
				</div>
				<div class="col-md-3 col-sm-4">
					<div class="form-group select-box">
						<select name="lang" class="form-control">
							<option value="ko">한국어</option>
							<option value="en" ${LANGUAGE eq 'en'? 'selected="selected"':'' }>English</option>
							<option value="ja" ${LANGUAGE eq 'ja'? 'selected="selected"':'' }>日本語</option>
							<option value="zh" ${LANGUAGE eq 'zh'? 'selected="selected"':'' }>中文</option>
						</select>
					</div>
					<div class="form-group select-box">
						<select name="exchange" class="form-control">
							<option value="KRW">KRW</option>
							<option value="USD" ${EXCHANGE.type eq 'USD'? 'selected="selected"':'' }>USD</option>
							<option value="JPY" ${EXCHANGE.type eq 'JPY'? 'selected="selected"':'' }>JPY</option>
						</select>
						<input type="hidden" name="type" value="${EXCHANGE.type }">
						<input type="hidden" name="unit" value="${EXCHANGE.unit }">
					</div>

				</div>
			</div>
		</div>
		<div id="footer-bottom">Copyright JHTA1508-team2</div>
	</div>
</div>