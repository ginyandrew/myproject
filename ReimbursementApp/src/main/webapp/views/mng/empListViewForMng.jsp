<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Request List View For Manager</title>
<link rel="stylesheet" href="../../styles/common.css" />
<link rel="stylesheet" href="../../styles/manager.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="../../scripts/mng/empListViewForMng.js"></script>
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
					<div class="col-md-8">
						<div class="form-group" style="width: 20%;">
							<!-- <label for="sel1">Sort By:</label> -->
							<select class="form-control" id="sel1">
								<option>Date</option>
								<option>Status</option>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<a class="btn btn-primary pull-right" role="button" href="rqSubmitForEmp.jsp"> Submit Request </a>
						<!-- <button class="btn btn-danger navbar-btn pull-right ">Submit Request</button> -->
					</div>
				</div>

				<div class="row  ">
					<table class="table table-hover table-bordered text-center">
						<thead>
							<tr>
								<th class="text-center" style="width:10%">No.</th>
								<th class="text-center" style="width:50%">Employee ID</th>
								<th class="text-center" style="width:20%">Total Request Count</th>
								<th class="text-center" style="width:20%">Total Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
							</tr>
							<tr>
								<td>9</td>
								<td>Empl00</td>
								<td>12</td>
								<td>1200</td>
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
