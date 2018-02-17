<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Request List of an Employee For Manager</title>
<link rel="stylesheet" href="../../styles/common.css" />
<link rel="stylesheet" href="../../styles/manager.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="../../scripts/mng/empRqListViewForMng.js"></script>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">

				<div class="row">
					<nav class="navbar navbar-inverse">
						<div class="container-fluid">
							<div class="navbar-header">
								<a class="navbar-brand" href="#">Welcome, Manager001!</a>
							</div>
							<ul class="nav navbar-nav">
								<li class="active"><a href="#">Pending: 3</a></li>
								<li><a href="#">Approved: 4</a></li>
								<li><a href="#">Denied: 10</a></li>
							</ul>
							<div class="btn-toolbar">
								<button class="btn btn-default navbar-btn pull-right">Log-Out</button>
								<button class="btn btn-primary navbar-btn pull-right">My Account</button>
							</div>
						</div>
					</nav>

				</div>

				<div class="row">
						<a class="btn btn-primary pull-left" role="button" href="rqSubmitForEmp.jsp"> Employee ID : Emp1002 </a>
						<a class="btn btn-primary pull-right" role="button" href="rqSubmitForEmp.jsp"> View Employees </a>
						<a class="btn btn-primary pull-right" role="button" href="rqSubmitForEmp.jsp"> View Requests </a>
						<!-- <button class="btn btn-danger navbar-btn pull-right ">Submit Request</button> -->
				</div>
				<br>
				<div class="row  ">
					<table class="table table-hover table-bordered text-center">
						<thead>
							<tr>
								<th class="text-center" style="width:10%">No.</th>
								<th class="text-center" style="width:50%">Title</th>
								<th class="text-center" style="width:20%">Status</th>
								<th class="text-center" style="width:20%">Date</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							<tr>
								<td>1</td>
								<td>Business Trip 100$</td>
								<td>Approved</td>
								<td>2017-10-10</td>
							</tr>
							
							 
						</tbody>

					</table>
				</div>
				<div class="row text-center">
					<div class="pagination">
						<a href="#">&laquo;</a>
						<a href="#">1</a>
						<a class="active" href="#">2</a>
						<a href="#">3</a>
						<a href="#">4</a>
						<a href="#">5</a>
						<a href="#">6</a>
						<a href="#">&raquo;</a>
					</div>
				</div>
			</div>
			<div class="col-md-1"></div>
		</div>
	</div>
</body>
</html>
