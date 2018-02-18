
$(document).ready(function() {
	
	 
});

function insertYnRcp(){
	var bool = $('#agree').is(':checked')
	if (!bool){
		alert('약관에 동의해주세요');
	} else {
		var schId = $("#sch_id").val();
		var psnId = $("#psn_id").val();
		var presYn = $("input[name='pres_yn']:checked").val();
		
		var gb01 = $("#gb01").val();
		if (gb01 == '01'){
			var spkYn = $("input[name='hope_spk_yn']:checked").val();
		} else if (gb01 == '02'){
			var spkYn = 'N';
		}
		$.ajax({
			url : WEB_ROOT +'/sch/sch.do?method=insertMobileYn',
			data: { sch_id : schId, psn_id : psnId,
				hope_spk_yn : spkYn, pres_yn : presYn},    
			success : function(res) {
				alert("답변해주셔서 감사합니다.");
				self.close();
			}
		})
	}
	
};

$( document ).ready(function() { 
	
	 $('#overlay_t, .close').click(function(e){ 
	     e.preventDefault(); 
	     $('#popup_layer, #overlay_t').hide(); 
	 }); 
});