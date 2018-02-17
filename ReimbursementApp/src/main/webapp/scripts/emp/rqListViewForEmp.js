 
window.onload = function(){
	
	var allTd = document.getElementsByClassName("title");
	for (var i = 0; i < allTd.length; i++) {
		allTd[i].addEventListener('click', viewARequest, false);
	}
	viewARequest(event);
}
 
function viewARequest(event) {
	console.log('you click me!');
	alert(event.target.getAttribute("data-reqNo"));
	var requestNo = event.target.getAttribute("data-reqNo");

}
// now need to move to the requestNo screen.

/*<c:forEach var = "i" begin = "1" end = "5">
<tr>
<td colspan="4">Item <c:out value = '${i}'/></td>
</tr>
</c:forEach> --%>

<!-- <div class="row text-center">
<div class="pagination">
<a href="#">&laquo;</a> <a href="#">1</a> <a class="active" href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">5</a> <a href="#">6</a> <a href="#">&raquo;</a>
</div>
</div> */