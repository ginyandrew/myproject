<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error Message</title>
<link rel="stylesheet" href="styles/common.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="scripts/login.js"></script>
</head>
<body>
	<%
		String error_code = request.getParameter("error_code");
		int code = Integer.parseInt(error_code);

		//RequestDispatcher dispatcher = request.getRequestDispatcher("");
		
		if (code == 1) { // Join - id already exist. go back to login page.
	%>
	<script>
		alert("Same Id already exist. please go back to the login page and try with different ID.");
	</script>
	<%
		
		response.sendRedirect("/ReimbursementApp/login.do");

		} else if (code == 2) { // registered as a memger.
	%>
	<script>
		alert("congratulation! You are now registered as a new member.");
	</script>
	<%
		response.sendRedirect("/ReimbursementApp/login.do");
		} else if (code == 3){
			%>
			<script>
				alert("Wrong Password. please try again.");
			</script>
			<%
			response.sendRedirect("/ReimbursementApp/login.do");
		} else if (code==4){
			%>
			<script>
				alert("The ID doesen't exist. please try again.");
			</script>
			<%
			response.sendRedirect("/ReimbursementApp/login.do");
		}
	%>
	<%-- <div class="container">
		<div class="row text-center">
			<div class="jumbotron" style="background-color: orange !important;">
				<h3><%= error_msg %></h3>
				<p>
				<h3>Please go back to previous page.</h3>

			</div>
		</div>
		
		<div class="row center-block" style="width: 40%;">
			<a role="button" class="btn btn-primary btn-lg center-block" href="/ReimbursementApp/login.do">Log-In</a>
		</div>
	</div> --%>

	<script>
		alert("Congratulation! You are now registered.");
		document.location.href = "login.jsp"
	</script>
	<br>
</body>
</html>
l>
