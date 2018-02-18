$(function(){
	// 자동검색 기능 연결
	setAutocomplete("#input-search");
	
	$("li.theme-list").click(function(event) {
		$("li.theme-list").removeClass("active");
		$(this).addClass("active");
		setThemeArea($(this).attr("data-code"));
	})
	
	// 테마 메인을 눌렀을 때 이벤트 따로 정의?
	/*$("li.theme-title").click(function(event) {
	 *
		$("li.theme-list")
	});*/
	
	init();
	
	$("button#theme-search-btn").click(function(event) {
		var cityNo = $("select[name='cityNo']").val();
		var code = $("select[name='code']").val();
		var keyword = $("input:text[name='keyword']").val();
		
		if(!cityNo.length){
			alert("도시를 선택하세요");
			return;
		}
		if(!code.length){
			alert("카테고리를 선택하세요");
			return;
		}
		
		// 검색결과 요청하기
		$.ajax({
			url: "list/search.tm"
			,type: "GET"
			,data: {
				cityNo: cityNo
				,code: code
				,keyword: keyword
			},dataType: "json"
			,success: function(result) {
				var data = result.result;
				$("div#theme-search-result-area").empty();
				if(!data.length) {
					// 검색 결과가 업서양ㅁ
					$("div#theme-search-result-area").append($("<p class='text-center'></p>").append("결과가 없어요").css({
						"margin-top": "30px"
						,"font-size": "20px"
						,"color": "#888"						
					}));
					
				} else {
					
					$.each(data, function(index, item) {
						var $a = $("<a></a>").addClass("search-dest-img").attr("href", "dest.tm?destNo="+item.no);
						if(item.mainImgName)	$a.append($("<img />").attr("src", "/resources/img/dest/"+item.mainImgName));
						else					$a.append($("<img />").attr("src", "/resources/img/noimage.jpg"));
						$("div#theme-search-result-area").append($("<div class='img-data' data-toggle='tooltip' title='"+item.name+"' data-placement='top'></div>").append($a));
					});
					
					
				}				
			}
		});
	});
	
	function init() {
		$("li.theme-list:first").trigger('click');
	}
	
	function setThemeArea(code) {
		$("div.theme-img-area").hide();
		$("div#theme-"+code+"-img-area").show();
	}
});