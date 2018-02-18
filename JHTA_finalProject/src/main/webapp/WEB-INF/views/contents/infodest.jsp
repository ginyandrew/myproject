<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<script src="/resources/common/jquery-ui/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/common/jquery-ui/jquery-ui.css">
<link class="include" rel="stylesheet" type="text/css"
	href="/resources/common/jqplot/jquery.jqplot.min.css" />
<script src="//ajax.googleapis.com/ajax/libs/webfont/1.4.10/webfont.js"></script>
<script type="text/javascript"
	src="/resources/common/jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript"
	src="/resources/common/jqplot/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"
	src="/resources/common/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script
	src='//cdnjs.cloudflare.com/ajax/libs/zeroclipboard/2.2.0/ZeroClipboard.min.js'></script>
<link rel="stylesheet" href="/resources/css/infodest.css" />
<script src="/resources/js/infodest.js"></script>

<!-- mapKey -->
<script type="text/javascript" src="http://apis.daum.net/maps/maps3.js?apikey=01547b2af7b0b23180b63ef3365457ea&libraries=services,clusterer"></script>

<input type="hidden" name="destNo" value="${param.destNo }" />
<input type="hidden" name="destName" value="${dest.name }" />
<input type="hidden" name="cityNo" value="${dest.city.no }"/>
<c:if test="${not empty themeRate }">
	<input type="hidden" name="themeCode1" value="${themeRate.one }"/>
	<input type="hidden" name="themeCode2" value="${themeRate.two }"/>
	<input type="hidden" name="themeCode3" value="${themeRate.three }"/>
	<input type="hidden" name="themeCode4" value="${themeRate.four }"/>
	<input type="hidden" name="themeCode5" value="${themeRate.five }"/>
	<input type="hidden" name="themeCode6" value="${themeRate.six }"/>
</c:if>
<div class="container" id="destPage">
	<div id="myModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content 일정에 넣기 -->
			<div id="<modal-date></modal-date>" class="modal-content">
				<div class="modal-header">
					<button id="xbtn" type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">일정을 선택해 주세요.</h4>
				</div>
				<div id="planlist" class="modal-body"></div>
				<div class="modal-footer">
					<a href="/plan/make.tm"><button id="mybtn" type="button" class="btn btn-primary"
						data-target="#myModalAdd" data-toggle="modal" href="/plan/make.tm">새로운 일정 추가</button></a>
					<button id="close-btn" type="button" class="btn btn-default" data-dismiss="modal">닫기</button>	
				</div>
			</div>

		</div>
	</div>


	<div class="row">
		<div class="col-md-5">
			<h3>${dest.name }</h3>
			<blockquote class="blockquote-reverse">
				<p>${dest.address }</p>
			</blockquote>
		


		</div>
		<div class="col-md-2"></div>
		<c:if test="${not empty LOGIN_USER }">
			<div class="col-md-6" id="icon">
				<div class="col-md-6"></div>
				<div class="col-md-3">
					<button type="submit" class="btn btn-info btn-lg" id='button-copy'
						data-clipboard-text='${dest.name}'>
						<span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>
					</button>
					<p>클립에담기</p>
				</div>
				<div class="col-md-3">
					<button id="planbtn" type="submit" class="btn btn-info btn-lg modalbtn"
						data-toggle="modal" data-target="#myModal">
						<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
					</button>
					<p>일정에담기</p>

					<!-- Modal -->
				</div>

			</div>
		</c:if>

	</div>	
	<br>
	<div id="myCarousel" class="carousel slide" data-ride="carousel">
		<!-- Indicators -->
		<ol class="carousel-indicators">
<%-- 		<c:choose>
		<c:when test="${not empty destImg }"> --%>
                      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <c:forEach begin="1" end="${destImg.size()-1 }" var="num">
                      <li data-target="#myCarousel" data-slide-to="${num }"></li>   
                      </c:forEach>                
        </ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
					  <c:choose>
					  	<c:when test="${not empty destImg }">
							  <c:forEach var="destImg" items="${destImg}" varStatus="loop">	
							  <c:choose>
							  	<c:when test="${loop.count eq 1 }">
			                      <div class="item active">
			                        <img src="../resources/img/dest/${destImg.fileName }" alt="${destImg }" style="height :87%; width : 87%;margin-left :10px;" >
			                      </div>
							  	</c:when>
							  	<c:otherwise>
							  	<div class="item">
			                        <img src="../resources/img/dest/${destImg.fileName }" alt="${destImg }" style="height :87%; width : 87%;">
			                      </div>
							  	</c:otherwise>
							  </c:choose>
							  </c:forEach>
					  	
					  	</c:when>
					  	<c:otherwise>
					  		<div class="item">
					  			<img src="../resources/img/noimage.jpg">
					  		</div>
					  	</c:otherwise>
					  </c:choose>

		<!-- Left and right controls -->
		<a class="left carousel-control" href="#myCarousel" role="button"
			data-slide="prev"> <span class="glyphicon glyphicon-chevron-left"
			aria-hidden="true"></span> 
		</a> <a class="right carousel-control" href="#myCarousel" role="button"
			data-slide="next"> <span
			class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			
		</a>
	</div>


	<div id="placeinfo" class="row">

		<strong style="font-size : 15px;">&nbsp;&nbsp;간단한 장소설명</strong>
		<p>${dest.details }</p>

	</div>
	<div class="contents">
		<div class="col-md-12" id="description">
			<div class="col-md-1"><strong style="font-size : 15px;">카테고리</strong></div>
			<div class="col-md-5">
				<c:forEach var="category" items="${category }">
					<p>${category.name }</p>
				</c:forEach>
			</div>
			<div class="col-md-3"><strong style="font-size : 15px;">웹사이트</strong></div>
			<div class="col-md-3"><a href="${dest.site }">${dest.site }</a> </div>
		</div>
		<div class="col-md-12">
			<div class="col-md-1"><strong style="font-size : 15px;">전화번호</strong></div>
			<div class="col-md-6">${dest.contact }</div>
		</div>
		<div class="col-md-12">
			<div class="col-md-1"><strong style="font-size : 15px;">오픈시간</strong></div>
			<div class="col-md-6">${dest.openTime }</div>
		</div>
	</div>

	<div class="col-md-12" id="detail">
			<div class="col-md-4">
				테마별 인기도
				<div id="graph"></div>
			</div>
		<div class="col-md-4">
			<div></div>
			<div>
				<span class="chart_rate_cnt" style="color: gray; font-size: 15px;">
					만족도&nbsp;${themeTotal } 명의 평가 </span>


			</div>
			<div class="clear" style="height: 35px;"></div>
			<div class="rows">
				<img src="/resources/common/img/chart_good.png" class="chart_img" /><span
					class="think">좋아요~</span>
				<div class="rate_chart_bar">
					<div class="rate_chart_value" style="width: 1%;" data-id="2">&nbsp;</div>
				</div>

				<div class="rate_chart_value_txt" data-id="2">
					<div class="progress">
						<div class="progress-bar" role="progressbar" aria-valuenow="60"
							aria-valuemin="0" aria-valuemax="100" style="width: ${emotionRate.G }%;">
							${emotionRate.G }%
						</div>
					</div>
				</div>
				<div class="rate_chart_value_txt" data-id="3"></div>
				<div class="clear"></div>
			</div>
			<div>
				<img src="/resources/common/img/chart_normal.png" class="chart_img" /><span
					class="think">괜찮아요~</span>

				<div class="rate_chart_bar">
					<div class="rate_chart_value" style="width: 1%;" data-id="2">&nbsp;</div>
				</div>
				<div class="rate_chart_value_txt" data-id="2">
					<div class="progress">
						<div class="progress-bar" role="progressbar" aria-valuenow="60"
							aria-valuemin="0" aria-valuemax="100" style="width: ${emotionRate.A }%;">
							${emotionRate.A }%</div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
			<div>
				<img src="/resources/common/img/chart_bad.png" class="chart_img" /><span
					class="think">별로에요~</span>
				<div class="rate_chart_bar">
					<div class="rate_chart_value" style="width: 1%;" data-id="1">&nbsp;</div>
				</div>
				<div class="rate_chart_value_txt" data-id="1">
					<div class="progress">
						<div class="progress-bar" role="progressbar" aria-valuenow="60"
							aria-valuemin="0" aria-valuemax="100" style="width: ${emotionRate.P }%;">
							${emotionRate.P }%</div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="col-md-4" id="map">지도자리</div>
	</div>


</div>
<div class="col-md-12">
<div>

</div>
 <c:if test="${not empty LOGIN_USER }">
	<a class="btn btn-default" data-toggle="collapse"
		href="#collapseExample" aria-expanded="false" aria-controls="collapse"
		style="margin-top: 10px; font-size: 30px;"> 
		<strong	style="color:;">리뷰남기기 [클릭!]</strong>
	</a>
 </c:if>		
	<div class="collapse" id="collapseExample"
		style="margin-top: 10px; width: 97%;">
		<div class="well">



			<div class="col-md-12">
				<div class="col-md-2">
					<input id="btngood" name="btn" class="btn btn-default" type="image" data-rate="G"
						src="/resources/common/img/chart_good.png" />좋아요!
				</div>
				<div class="col-md-2">
					<input id="btnsoso" name="btn" class="btn btn-default" type="image" data-rate="A"
						src="/resources/common/img/chart_normal.png" />괜찮아요~
				</div>
				<div class="col-md-2">
					<input id="btnpoor" name="btn" class="btn btn-default" type="image" data-rate="P"
						src="/resources/common/img/chart_bad.png" />별로에요!!
				</div>
				<div class="col-md-6">
					<input id="btnfont" class="btn btn-default btn-lg pull-right"
						type="submit" value="리 뷰 쓰 기" />
				</div>
			</div>
			<div>
				<textarea id="text" rows="6"></textarea>
			</div>

		</div>
	</div>

</div>
<div class="review">

</div>
</div>

<c:if test="${not empty themeRate }">
<script>$(function(){
		makeChart();
	}
);</script>
</c:if>