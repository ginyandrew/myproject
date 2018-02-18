/*
 *  setExchange()
 *      환율을 구할 때 사용한다.
 *      fn: ajax로 환율을 얻으면 실행할 함수
 *      _base: 대상이 되는 화폐로, 입력하지 않으면 USD
 *  
 *      사용법
 *          setExchange(function(params){...});
 */
function setExchange(fn, _base) {
    var base = _base?_base:'USD';
    var exchangeUrl = 'http://api.fixer.io/latest?base='+base+'&symbols=KRW&callback=?';
    $.ajax({
        url: exchangeUrl,
        //data: {},
        type: "GET",
        dataType: "jsonp",
        success: function (result) {
            fn(base, result);
        }
    });
}

/**
 * 자동 검색이 되게 설정한다
 * @param input의 아이디: #id
 */
function setAutocomplete(id) {
	$(id).autocomplete({
		source : function(request, response) {
			$.ajax({
				url: "/jsonAutoList.tm",
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
}

function setDailyForecastData(latlng, time, fn) {
    
    var url = "https://api.forecast.io/forecast/b3c35d762e71470531e8161c4826b528/" + latlng + "," + time;

    $.ajax({
        url: url,
        type: "GET",
        dataType: "jsonp",
        success: function (data) {
            var dailyData = data.daily.data[0];
            var tempData = {
                time: getTimeToString(new Date(time)),//""+new Date(dailyData.time) + dailyData.time + new Date(time),
                min: getTemperatureC(dailyData.temperatureMin),
                max: getTemperatureC(dailyData.temperatureMax)
            };
            fn(tempData);
        }
    });
}

function setDataToArray(data, array) {
    if(array.length >= 0) {
        array.push(data);
    }
}
function getTimeToString(time) {
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    return y+"/"+m+"/"+d;
}

function getTemperatureC(F) {
    return Math.round(((F - 32) / 9 * 5)*10)/10;
}


/*
 *  다이얼로그 만들기
 *
 *
 */
//var NOW_DIALOG = null;
/* 레이어 팝업 스크립트 시작*/
$(function () {
    //$("body").append($("<div id='dark-background'></div>"));
});

/*function makeDialog(components) {

    var $box = components.box;
    var size = {
        box: {
            width: components.size.width,
            height: components.size.height
        },
        window: {
            width: $(window).width(),
            height: $(window).height()
        }
    };
    $box.width(size.box.width).height(size.box.height);

    $box.css({
        'background-color': components.bgColor ? components.bgColor : 'white',
        'position': 'absolute',
        //'left': getCenterPixel(size),
        //'top': '25%',
        'padding': '0 20px',
        'border-radius': '10px',
        'z-index': 999
    }).addClass('dialog');
    // 센터로 보내기?
    //var $headerBar = components.headerBar;
    var $headerBar = $("<div id='login-box-header' style='width:100%; height:50px;'></div>").append($("<span class='box-close pull-right glyphicon glyphicon-remove'></span>").css({
        'margin-top': '10px',
        'color': '#aaa',
        'font-size': '30px',
        'cursor': 'pointer'
    }));

    var $title = components.title;
    var $contents = components.contents;

    $box.css('display', 'none').append($headerBar).append($title).append($contents);
    $("body").append($box);
}

function getCenterPixel($dialog) {
    var size = {
        box: {
            width: $dialog.width(),
            height: $dialog.height()
        },
        window: {
            width: $(window).width(),
            height: $(window).height()
        }
    };
    var left = (size.window.width - size.box.width) / 2;

    return left + "px";
}

function setDialogToCenter($dialog, left) {
    $dialog.css({
        'left': left,
        'top': '20%'
    });
}

function showDialog(id) {
    NOW_DIALOG = id;
    var $div = $("div#" + id);
    setDialogToCenter($div, getCenterPixel($div));
    $("div#dark-background").fadeIn('fast');
    $div.fadeIn('fast');
}

function hideDialog() {
    var $div = $("div#" + NOW_DIALOG);
    $div.fadeOut('fast');
    $("div#dark-background").fadeOut('fast');
    NOW_DIALOG = null;
}

$(window).resize(function () {

    if (NOW_DIALOG) {
        var $dialog = $("div#" + NOW_DIALOG);
        setDialogToCenter($dialog, getCenterPixel($dialog));
    }
});*/

////////////map 부분////////////////
function infodestMapinit() {
	// 주호부분 지도
	var queryString = window.location.search;
	var destno = parseInt(queryString.replace("?destNo=", ""));
	$.ajax({
		type : "GET",
		url : "/plan/detaildest.tm",
		data : {destno : destno	},
		dataType : "json",
		success : function(data) {

			var destPoint = {
				lat : parseFloat(data.dest.destVo.lat),
				lng : parseFloat(data.dest.destVo.lng)
			} // 장소 위치를 저장

			//지도부분
			var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			mapOption = {
				center : new daum.maps.LatLng(destPoint.lat, destPoint.lng), // 지도의 중심좌표
				level : 3
			// 지도의 확대 레벨
			};
			var map = new daum.maps.Map(mapContainer, mapOption); // 지도 생성

			//마커부분
			var marker = new daum.maps.Marker({ // 마커생성
				position : new daum.maps.LatLng(destPoint.lat, destPoint.lng),
				zIndex : 1
			});
			marker.setMap(map); // 지도에 마커 놓음.

			//인포윈도우 부분
			var iwContent = '<div class="unselectable" style="padding:5px;">' + data.dest.destVo.name + '</div>'; // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
			var infowindow = new daum.maps.InfoWindow({// 인포윈도우 생성
				content : iwContent,
				zIndex : 2
			});
			infowindow.open(map, marker); //인포윈도우 띄움
			map.setZoomable(false);
		}
	})
}

function plandetailMapinit(e) {
	//성인이 부분 지도
	if(e != undefined) {
		var days = $(e.target).parent().index()+1;
	} else {
		var days = $(this).parent().index()+1;
	}
	if(days == 0){
		days = 1;
	}
	var queryString = window.location.search;
	var planno = parseInt(queryString.replace("?no=", ""));
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = { 
		center: new daum.maps.LatLng(35.2, 126.76), // 지도의 중심좌표
		level: 6 // 지도의 확대 레벨
	};
	
	// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
	var map = new daum.maps.Map(mapContainer, mapOption); 
	$.ajax({
		type : "GET",
		url : "/plan/plandest.tm",
		data : {planno : planno	},
		dataType : "json",
		success : function(data) {
			var bounds = new daum.maps.LatLngBounds();// day 수에따라 새로 적용하기 위해 여기에 위치
			var selRoutes = [];
			//지도생성
			$.each(data.planDetailList, function(index, item){
				console.log(item);
				if(item.day == days) {
					var lat = parseFloat(item.dest.lat);
					var lng = parseFloat(item.dest.lng);
					
					var marker = new daum.maps.Marker({
						position: new daum.maps.LatLng(lat, lng),
						zIndex : 1
					});
					marker.setMap(map);
					var iwContent = '<div class="unselectable" style="padding:5px;"><span class="numbering">'+item.numbering+'</span> " ' + item.dest.name + '"</div>'; // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
					//var iwContent = '<div style="background-color: #203341; color: white; width: 24px; height: 24px; line-height: 24px; border-radius: 12px; display: inline-block; text-align: center;"> 여기가 ' + item.dest.name + '입니다.</div>'; // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
					var infowindow = new daum.maps.InfoWindow({// 인포윈도우 생성
						content : iwContent,
						zIndex : 2
					});
					infowindow.open(map, marker); //인포윈도우 띄움
					bounds.extend(new daum.maps.LatLng(lat, lng)); // day에 해당하는 좌표를 등록
					map.setBounds(bounds);
					map.setLevel(map.getLevel()+1);
					
					selRoutes.push(new daum.maps.LatLng(lat, lng));
            		  //console.log(selRoutes);
            		var polyline = new daum.maps.Polyline({
            			path: selRoutes, // 선을 구성하는 좌표배열 입니다
            			endArrows: true,
            			strokeWeight: 3, // 선의 두께 입니다
            			strokeColor: '#FF0000', // 선의 색깔입니다
            			strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
            			strokeStyle: 'solid' // 선의 스타일입니다
            		});

            		// 지도에 선을 표시합니다 
            		polyline.setMap(map);
            		map.setZoomable(false);
            		/*
            		// 커스텀 오버레이를 생성합니다
            		var customOverlay = new daum.maps.CustomOverlay({// 번호넘버링을 표시하기 위한 오버레이.
            		    position: new daum.maps.LatLng(lat, lng),
            		    content: '<span class="numbering">'+item.numbering+'</span>' ,
            		    zIndex : 2,
            		    xAnchor: 3.9, // 좌표중심으로 왼쪽으로 값이 증가 
            		    yAnchor: 3.1  // 좌표중심으로 위쪽으로 값이 증가
            		});

            		// 커스텀 오버레이를 지도에 표시합니다
            		customOverlay.setMap(map);*/
				}
			})
		}
	})
	
}
