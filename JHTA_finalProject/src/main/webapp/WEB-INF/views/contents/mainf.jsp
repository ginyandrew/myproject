<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/main.css" />
<!-- 검색어 자동완성: 중복 include가 되서 주석처리 -->
<!-- <link rel="stylesheet" href="/resources/common/jquery-ui/jquery-ui.min.css">
<script src="/resources/common/jquery-ui/jquery-ui.min.js"></script> -->

  	
<script>
$(function() {
	
	///////////////////////////////////////검색어 자동완성 
	$('#autoValue').autocomplete({
		source : function(request, response) {
			$.ajax({
				url: "jsonAutoList.tm",
				data: { input : request.term }, 	 //사용자가 input에 넣은 값을 controller로 보낸다
				dataType: "json",
				success: function(data){					
					response($.map(data.locations, function(item) {
						return{
							id: item.PLACENO,
							category: item.CATEGORY,
							label: item.PLACENAME,
							value: item.PLACENAME
						}
					}));				
				}
			});
		},	
		minLength : 1,		
	    select: function(event, ui) { 
	    	ui.item ? "Selected : " + ui.item.label: "Nothing select, input was " + this.value    	
	    	event.preventDefault();
	    	if( ui.item.category =='city' ){
	    		location.href="/info/city.tm?cityNo=" + ui.item.id ;
	    	} else {
	    		location.href="/info/dest.tm?destNo=" + ui.item.id;
	    	}
	    }
	})	 
		
});    
  
  
  
</script>

<div class="container"> 
 
        <!-- 상단 사진배경 및 검색입력란 -->                        
        
        <div class="row" id="title-area" style="min-height:400px;border: 1px solid black;">
            <img class="title-image" src="/resources/common/img/title-img.jpg" width="974" height="398">
            <div id="search-area" class="text-center">			
				 
				<div class="ui-widget">
				  <input type="text" id="autoValue" placeholder="도시 또는 여행지 이름을 입력해주세요" class="form-control"/>
				</div>			 
            </div>
        </div>
         
        <!-- 메뉴 사이 공백 -->
        <div class="row" style="height:50px;"></div>           
           
           
           
        <!-- 사용자들의 여행일정 리스트 -->           
        <p class="row"><h3 style="text-align: center;">인기 여행일정</h3></p>  
        <p class="row"><h6 style="text-align: center;">다른 여행자들의 일정을 참고해 나만의 일정을 만들어보세요!</h6></p>            
        <div id="restricted_width" class="row">

            <c:forEach var="bestPlan" items="${bestPlans}">
            <div class="col-md-4">               
                <div style="position:relative; height:296px;">
                    <a href="/plan/detail.tm?no=${bestPlan.planNo}" class="plan-img">
                    	<span> 
		                    <img src="../resources/img/city/${bestPlan.ImgfileName}" alt="도시사진" style="width:310px;height:202px;">		                    
		                    <div id="plan-img-area" >
		                        <span class="pull-left" style="color:white;font-weight:bold;font-size:12pt;"><c:out value="${bestPlan.leaveDate}"/></span>
		                  
		                    </div>
                    	</span>
                     </a>
                    <div class="pull-left" style="position:absolute;top:220px;left:10px;">
                        <p><c:out value='${bestPlan.themeCode}'/></p>
                        <p><c:out value="${bestPlan.destName}"/></p>
                        <p><c:out value="${bestPlan.userName}"/></p>
                    </div>
                </div>                                
            </div>
            </c:forEach>             
        </div>
          
                        
        <!-- 메뉴 사이 공백 -->
        <div class="row" style="height:50px;"></div>  
 
                                    
        <!-- 도시 리스트 랭킹 5위 안에 출력-->                       
        <div class="row">
            <p class="row"  ><h3 style="text-align: center;">인기도시</h3></p>
            <div class="row">

                <!-- 랭킹 1위 도시 -->
                <c:forEach var="topCities" items="${topFiveCity}">
	                <c:if  test="${topCities.RANKING eq 1 }">	               
		                <div class="col-md-6">                  
		                    <a href="/info/city.tm?cityNo=${topCities.NO}">
		                    <img src="../resources/img/city/${topCities.FILENAME}" alt="인기1등도시사진" style="width: 478px;height: 478px;margin: 16px 16px 0 0;position: relative;overflow: hidden;border: 1px solid #222288;"/></a>               	 
		                </div>
					</c:if>
				</c:forEach>
				
                <!-- 랭킹 2~5위 도시 -->  	                  
                <div class="col-md-6">                	 
				 <c:forEach var="topCities" items="${topFiveCity}">
	                 <c:if  test="${topCities.RANKING ne 1 }">					  
	                      <div class="col-md-6">
	                      		<a href="/info/city.tm?cityNo=${topCities.NO}" class="city-img">
	                      		<img src="../resources/img/city/${topCities.FILENAME}" alt="인기2-4위도시사진" style="width: 220px;height: 230px;"/></a>
	                      </div>		                
					</c:if>
				</c:forEach>
				</div>				
            </div>           
        </div>    
        
        
    </div>