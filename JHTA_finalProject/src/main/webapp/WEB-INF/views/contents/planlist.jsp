<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/planlist.css" />
<script src="/resources/js/planlist.js"></script>
<div class="col-md-12">

	<!-- 여기가 제가하는 곳입니다!-->
	<div class="contents">
		<div class="ground" style="height: 600px; width: 1500px;">
			<!--<p class="text">일정짜기 사진이 뜬다고 하는데요?</p>-->
			<!--<img src="IMG/key_bg_1.jpg" style="height:600px; width:1500px;"/>-->
			<div class="button-packege">
				<button type="button" class="btn btn-primary btn-lg" style="height: 100px; width: 180px;" onclick="location.href='/plan/make.tm'";>일정 만들기</button>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn btn-primary btn-lg" style="height: 100px; width: 180px;" onclick="location.href='/mypage.tm?no='";>마이페이지</button>
			</div>
		</div>

		<h2 style="text-align: center;">여행자들의 일정보기</h2>

		<!--  <table class="show2" style="max-width:900px; width: 100%; margin:0 auto; padding-left:25px;" border="1">-->
		<!-- 보드가 보이는곳입니다 일부러 길이를 높게 조정했습니다.-->
		<div class="panel-group" style="max-width: 900px; width: 100%; margin: 0 auto; padding-left: 25px;" border="1">

			<nav class="navbar navbar-default">
				<div class="panel-heading">
					<div class="panel-title">

						<div class="btn-group">
							<p>지역선택</p>
							<c:forEach var="state" items="${states }">
								<c:choose>
									<c:when test="${state.code lt 9 }">
										<button type="button" id="city-${state.code*100 }" class="btn btn-default btn-collapse citys-open" data-toggle="collapse">${state.name }</button>
									</c:when>
									<c:otherwise>
										<button type="button" class="btn btn-default btn-collapse" data-toggle="collapse" href="#collapse-${state.code }">${state.name }</button>
									</c:otherwise>
								</c:choose>

							</c:forEach>
							<!-- <button type="button" class="btn btn-default">서울</button>
                            <button type="button" class="btn btn-default">부산</button>
                            <button type="button" class="btn btn-default">제주도</button>
                            <button type="button" class="btn btn-default">인천</button>
                            <button type="button" class="btn btn-default">대전</button>
                            <button type="button" class="btn btn-default">대구</button>
                            
                            <button type="button" class="btn btn-default pull-right" data-toggle="collapse" href="#collapse1">펼치기</button> -->
							<!--<a data-toggle="collapse" href="#collapse1">펼치기</a>-->

						</div>
					</div>
				</div>

				<%--1차 백업입니다. 옆으로 바가 생기니깐 삭제할려고합니다. --%>
				<%--<c:forEach var="index" begin="8" end="15">
	       			<div id="collapse-${index +1}" class="panel-collapse collapse"><!--style="overflow-y:scroll -->
	                 	 <div class="well" style="height:150px;overflow-y:scroll">
	                    	<ul class="list-group">
	                        	<ul class="nav navbar-nav">
	                        		<c:forEach var="city" items="${cityMap.get(states.get(index).code) }">
	                        			<li><a href="#">${city.name }</a></li>
	                        		</c:forEach>
	                        	</ul>
	                    	</ul>
	                   </div>
	                </div>
                </c:forEach> --%>

				<c:forEach var="index" begin="8" end="15">
					<div id="collapse-${index +1}" class="panel-collapse collapse">
						<%-- <div class="well" style="height:150px;overflow-y:scroll">--%>
						<div class="well" style="height: 160px;">
							<ul class="list-group nav navbar-nav">

								<c:forEach var="city" items="${cityMap.get(states.get(index).code) }">
									<li><a href="#" id="city-${city.no }" class="city-open">${city.name }</a></li>
								</c:forEach>

							</ul>
						</div>
					</div>
				</c:forEach>

				<%--  <div id="collapse1" class="panel-collapse collapse"><!--style="overflow-y:scroll -->
                   <div class="well" style="height:150px;overflow-y:scroll">
                    <ul class="list-group">
                        <ul class="nav navbar-nav">
							<c:forEach var="city" items="${cityMap }">
								<li><a href="#">${city.name }</a></li>
							</c:forEach>
                            <!-- <li><a href="#">광주</a></li>
                            <li><a href="#">울산</a></li>
                            <li><a href="#">세종특별자치시</a></li>
                            <li><a href="#">강진군</a></li>
                            <li><a href="#">고흥군</a></li>
                            <li><a href="#">과천시</a></li>
                            <li><a href="#">광명시</a></li>
                            <li><a href="#">광주시(경기도)</a></li>
                            <li><a href="#">구리시</a></li>
                            <li><a href="#">군포시</a></li>
                            <li><a href="#">김포시</a></li>
                            <li><a href="#">남양주시</a></li>
                            <li><a href="#">동두천시</a></li>
                            <li><a href="#">부천시</a></li>
                            <li><a href="#">성남시</a></li>
                            <li><a href="#">수원시</a></li>
                            <li><a href="#">시흥시</a></li>
                            <li><a href="#">안산시</a></li>
                            <li><a href="#">안성시</a></li>
                            <li><a href="#">안양시</a></li>
                            <li><a href="#">양주시</a></li>
                            <li><a href="#">양평군</a></li>
                            <li><a href="#">여주시</a></li>
                            <li><a href="#">연천군</a></li>
                            <li><a href="#">오산시</a></li>
                            <li><a href="#">용인시</a></li>
                            <li><a href="#">의왕시</a></li>
                            <li><a href="#">의정부시</a></li>
                            <li><a href="#">이천시</a></li>
                            <li><a href="#">파주시</a></li>
                            <li><a href="#">평택시</a></li>

                            <li><a href="#">포천시</a></li>
                            <li><a href="#">하남시</a></li>
                            <li><a href="#">화성시</a></li>
                            <li><a href="#">강릉시</a></li>
                            <li><a href="#">고성군 (강원도)</a></li>
                            <li><a href="#">동해시</a></li>
                            <li><a href="#">삼척시</a></li>
                            <li><a href="#">속초시</a></li>
                            <li><a href="#">양구군</a></li>

                            <li><a href="#">양양군</a></li>
                            <li><a href="#">영월군</a></li>    
                            <li><a href="#">원주시</a></li>
                            <li><a href="#">인제군</a></li>
                            <li><a href="#">정선군</a></li>
                            <li><a href="#">철원군</a></li>
                            <li><a href="#">춘천시</a></li>
                            <li><a href="#">태백시</a></li>
                            <li><a href="#">평창군</a></li>
                            <li><a href="#">홍천군</a></li>
                            <li><a href="#">화천군</a></li>
                            <li><a href="#">횡성군</a></li>
                            <li><a href="#">괴산군</a></li>
                            <li><a href="#">단양군</a></li>
                            <li><a href="#">보은군</a></li>
                            <li><a href="#">영동군</a></li>
                            <li><a href="#">옥천군</a></li>

                            <li><a href="#">음성군</a></li>
                            <li><a href="#">제천시</a></li>
                            <li><a href="#">진천군</a></li>
                            <li><a href="#">청원군</a></li>
                            <li><a href="#">청주시</a></li>
                            <li><a href="#">충주시</a></li>
                            <li><a href="#">증평군</a></li>
                            <li><a href="#">공주시</a></li>
                            <li><a href="#">금산군</a></li>
                            <li><a href="#">논산시</a></li>
                            <li><a href="#">당진시</a></li>
                            <li><a href="#">보령시</a></li>

                            <li><a href="#">부여군</a></li>
                            <li><a href="#">서산시</a></li>
                            <li><a href="#">서천군</a></li>
                            <li><a href="#">아산시</a></li>
                            <li><a href="#">예산군</a></li>
                            <li><a href="#">천안시</a></li>
                            <li><a href="#">청양군</a></li>
                            <li><a href="#">태안군</a></li>
                            <li><a href="#">홍성군</a></li>
                            <li><a href="#">계룡시</a></li>
                            <li><a href="#">경산시</a></li>
                            <li><a href="#">경주시</a></li>
                            <li><a href="#">고령군</a></li>
                            <li><a href="#">구미시</a></li>
                            <li><a href="#">군위군</a></li>
                            <li><a href="#">김천시</a></li>
                            <li><a href="#">문경시</a></li>
                        
                            <li><a href="#">봉화군</a></li>
                            <li><a href="#">상주시</a></li>
                            <li><a href="#">성주군</a></li>
                            <li><a href="#">안동시</a></li>
                            <li><a href="#">영덕군</a></li>
                            <li><a href="#">영양군</a></li>
                            <li><a href="#">영주시</a></li>
                            <li><a href="#">영천시</a></li>
                            <li><a href="#">예천군</a></li>
                            <li><a href="#">울릉군</a></li>
                            <li><a href="#">울진군</a></li>
                            <li><a href="#">의성군</a></li>
                            <li><a href="#">청도군</a></li>
                            <li><a href="#">청송군</a></li>
                            <li><a href="#">칠곡군</a></li>
                            <li><a href="#">포항시</a></li>
                            <li><a href="#">거제시</a></li>
                            <li><a href="#">거창군</a></li>
                            <li><a href="#">고성군 (경상남도)</a></li>
                            <li><a href="#">김해시</a></li>
                            <li><a href="#">남해군</a></li>
                            <li><a href="#">마산시</a></li>
                            <li><a href="#">밀양시</a></li>
                            <li><a href="#">사천시</a></li>
                            <li><a href="#">산청군</a></li>
                            <li><a href="#">양산시</a></li>
                            <li><a href="#">의령군</a></li>
                            <li><a href="#">진주시</a></li>
                            <li><a href="#">진해시</a></li>
                            <li><a href="#">창녕군</a></li>
                            <li><a href="#">창원시</a></li>
                            <li><a href="#">통영시</a></li>
                            <li><a href="#">하동군</a></li>
                            <li><a href="#">함안군</a></li>
                            <li><a href="#">함양군</a></li>
                            <li><a href="#">합천군</a></li>
                            <li><a href="#">고창군</a></li>
                            <li><a href="#">군산시</a></li>
                            <li><a href="#">김제시</a></li>
                            <li><a href="#">남원시</a></li>

                            <li><a href="#">무주군</a></li>
                            <li><a href="#">부안군</a></li>
                            <li><a href="#">순창군</a></li>
                            <li><a href="#">완주군</a></li>
                            <li><a href="#">익산시</a></li>
                            <li><a href="#">임실군</a></li>
                            <li><a href="#">장수군</a></li>
                            <li><a href="#">전주시</a></li>
                            <li><a href="#">정읍시</a></li>
                            <li><a href="#">진안군</a></li>
                            <li><a href="#">강진군</a></li>
                            <li><a href="#">고흥군</a></li>
                            <li><a href="#">곡성군</a></li>
                            <li><a href="#">광양시</a></li>
                            <li><a href="#">구례군</a></li>
                            <li><a href="#">나주시</a></li>
                            <li><a href="#">담양군</a></li>
                            <li><a href="#">목포시</a></li>
                            <li><a href="#">무안군</a></li>
                            <li><a href="#">보성군</a></li>
                            <li><a href="#">순천시</a></li>

                            <li><a href="#">신안군</a></li>
                            <li><a href="#">여수시</a></li>
                            <li><a href="#">영광군</a></li>
                            <li><a href="#">영암군</a></li>
                            <li><a href="#">완도군</a></li>
                            <li><a href="#">장성군</a></li>
                            <li><a href="#">장흥군</a></li>
                            <li><a href="#">진도군</a></li>
                            <li><a href="#">함평군</a></li>
                            <li><a href="#">해남군</a></li>
                            <li><a href="#">화순군</a></li> -->
                            
                        </ul>
                    </ul>
                    </div>
                </div> --%>

				<div class="panel-heading">
					<div class="btn-group">
						<p>날짜선택</p>
						<button type="button" id="period-1" class="btn btn-default btn-period">1-3일</button>
						<button type="button" id="period-4" class="btn btn-default btn-period">4-6일</button>
						<button type="button" id="period-7" class="btn btn-default btn-period">7-10일</button>
						<button type="button" id="period-11" class="btn btn-default btn-period">11-15일</button>
						<button type="button" id="period-15" class="btn btn-default btn-period">15일 이상</button>
					</div>
				</div>

				<div class="panel-heading">
					<div class="btn-group">
						<p>여행시기</p>
						<button type="button" id="seasonality-spring" class="btn btn-default btn-seasonality">봄</button>
						<button type="button" id="seasonality-summer" class="btn btn-default btn-seasonality">여름</button>
						<button type="button" id="seasonality-autumn" class="btn btn-default btn-seasonality">가을</button>
						<button type="button" id="seasonality-winter" class="btn btn-default btn-seasonality">겨울</button>
					</div>
				</div>

				<!-- 여행테마 를 썼네요-->
				<div class="panel-heading">
					<div class="btn-group">
						<p>여행테마</p>
						<button type="button" id="thema-1" class="btn btn-default btn-thema">친구와함께</button>
						<button type="button" id="thema-2" class="btn btn-default btn-thema">커플여행</button>
						<button type="button" id="thema-3" class="btn btn-default btn-thema">단체여행</button>
						<button type="button" id="thema-4" class="btn btn-default btn-thema">나홀로여행</button>
						<button type="button" id="thema-5" class="btn btn-default btn-thema">비즈니스여행</button>
						<button type="button" id="thema-6" class="btn btn-default btn-thema">가족여행</button>
					</div>
				</div>
			</nav>

		</div>



		<div class="boxlist" style="max-width: 975px; margin: 0 auto; padding-left: 25px;">
		  
		  <c:forEach var="index" items="${getAllPlans }"> <!-- getAllPlans  를 index로  -->
		  	<div class='col-lg-4'>
		  		<a href="/plan/detail.tm?no=${index.no}" class="thumbnail" style="text-decoration:none !important">
			  	<c:choose>
			  		<c:when test="${not empty index.details }">
			  			<img src="/resources//img//dest/${index.details.get(0).dest.mainImgName}"  style= "width:250px; height:200px;"/>
			  		</c:when>
			  		<c:otherwise>
			  			<img src="/resources//img//noimage.jpg"  style= "width:250px; height:200px;"/>
			  		</c:otherwise>
			  	</c:choose>	
		  		<br>
		  		${index.themeCode} <br>${fn:substring(index.leaveDate,0,8)}<br> ${fn:substring(index.title,0,8)}
		  		</a>
		  	</div>
		  </c:forEach>
		
		</div>


	</div>
</div>


<script>
$(function(){
	serachlist();
});

</script>
