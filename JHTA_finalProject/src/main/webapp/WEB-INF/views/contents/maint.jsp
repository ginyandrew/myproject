<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 고유부분 -->
<link rel="stylesheet" href="/resources/css/main.css" />
<!-- 검색어 자동완성: 중복 include가 되서 주석처리 -->
<!-- <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> -->
<!-- <link rel="stylesheet" href="/resources/common/jquery-ui/jquery-ui.min.css">
<script src="/resources/common/jquery-ui/jquery-ui.min.js"></script> -->


  	
<script>
$(function() {
	
    var theUserNo = $(":hidden[name='userNo']").val();		 	//회원번호  
     
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
	
	///////////////////////////////////////달력의 특정날짜를 클릭했을 때 
	$('input[type="date"]').change(function(){		 
	    var theUserNo = $(":hidden[name='userNo']").val();		 	//회원번호
        //var inputDate = (this.value).replace(/-/g,'');			//클릭한 날짜정보 (YYYYMMDD)
        var inputDate = this.value;									//클릭한 날짜정보 (YYYY-MM-DD)       
        //클릭한 날짜정보와 회원번호를 이용해 메세지가 왔는지 확인하기.        
        $.ajax({
				url: "jsonCalendar.tm",
				data: { selectedDate:inputDate, userNo:theUserNo }, 	 
				dataType: "json",
				success: function(data){	
					 
					var ifUserHasPoint = data.userPoint
					if(ifUserHasPoint < 1){						
						//회원에게 메세지 보내고 포인트 적립하기							
				        $.ajax({
				        	url: "jsonUserPointAndMsg.tm",
							data: { selectedDate:inputDate, userNo:theUserNo}, 	 
							dataType: "json",
							success: function(data){								
								alert("100 포인트가 적립되었습니다.");
								var userUpdatedPoint = data.userUpdatedInfo.userPoint;
								$("#userStoredPoint").empty();
								$("#userStoredPoint").text("총 포인트: "+ userUpdatedPoint);
							}
				        })			        
					} else {
						// Modal로 경고창 보내기
						 $("#myModal2").modal("show",{backdrop: false});
					}			
				}
		});
		
    });
});    
  
  
  
</script>

<div class="container"> 
 
        <!-- 상단 사진배경 및 검색입력란 -->                        
        
        <div class="row" id="title-area" style="min-height:400px;border: 1px solid black;">
        	<img class="title-image" src="/resources/common/img/title-img.jpg" width="974" height="398">
            <div id="search-area" class="text-center" style="background-color: white">			
				 
				<div class="ui-widget">
				  <input type="text" id="autoValue" placeholder="도시 또는 여행지 이름을 입력해주세요" class="form-control"/>
				</div>			 
            </div>
        </div>
           
        <!-- 로그인하면 나오는 회원정보 + 달력 -->          
        <div class="row" style="height:150px;">
           
            <!-- 회원정보 -->
            <div id="userTotalInfo" class='col-md-7' style="height:100%;">
				<table class="table table-hover" style="height:100%;">
				    <tr>
				      <td rowspan="2"><span style="font-size:17pt;">${userTotalInfo.userName}</span>님! </br></br><b>환영합니다</b> </td>
				      <td style="font-size:13pt;">클립보드</td>
				      <td style="font-size:17pt;color:red;font-weight:bold">
				      	<a href="/mypage.tm">
				      ${userTotalInfo.clipCnt}
				      	</a>
				      </td>
				       <td style="font-size:13pt;">리뷰</td>
				       <td style="font-size:17pt;color:red;font-weight:bold"> 
					      	<a href="/mypage.tm">
					      ${userTotalInfo.reviewCnt}		        
					    	</a>
				    	</td>
				    </tr>
				    <tr>
				      <td style="font-size:13pt;">포인트</td>
				      <td><a href="/mypage.tm">
				      <span id="userStoredPoint" style="font-size:17pt;color:red;font-weight:bold">${userTotalInfo.userPoint}</span>
				      </a>
				      </td>
				      <td style="font-size:13pt;">나의 여행일정</td>
				      <td style="font-size:17pt;color:red;font-weight:bold"><a href="/mypage.tm">${userTotalInfo.planCnt}</a></td>
				    </tr>		     
				</table>
            </div>
                       
            <!-- 달력 -->
            <div class="col-md-5">
                <div id="wholeCalander" class='wrap pull-right'>
				     <div class='btn-holder'>				          
				          <span id='currentDate' style="font-size:20pt;padding:10px 0 0 0;">2월</span>
				          <p style="text-align: center;">달력을 클릭해 포인트를 적립하세요!</p>				          
				     </div>   
				     <div id="calendar">
				     	 <input type="date">
				     </div>
				     
				     <!-- 이미 포인트 적립한 경우 경고창 모달-->
				     <!-- Modal -->
					  <div class="modal fade" id="myModal2" role="dialog">
					    <div class="modal-dialog">
					    
					      <!-- Modal content-->
					      <div class="modal-content">
					        <div class="modal-header">
					          <button type="button" class="close" data-dismiss="modal"></button>
					          <h4 class="modal-title">*알림*</h4>
					        </div>
					        <div class="modal-body">
					          <p>Warning!</p>
					          <p><strong>Note:</strong> 이미 적립하였거나 오늘 날짜가 아닙니다. 달력을 확인해주세요 </p>
					        </div>
					        <div class="modal-footer">
					          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					        </div>
					      </div>
					      
					    </div>
					  </div>
					<!-- Modal end -->
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
                    	<img src="../resources/img/city/${bestPlan.ImgfileName}" alt="도시사진" style="width:310px;height:202px;">
                    </a>
                    <div id="plan-img-area" >
                        <span class="pull-left"><c:out value="${bestPlan.leaveDate}"/></span>
                        <span class="pull-right">지도에서 검색</span>
                    </div>
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