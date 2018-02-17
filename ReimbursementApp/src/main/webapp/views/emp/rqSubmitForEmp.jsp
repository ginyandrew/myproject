<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Submit Reimbursement request by an Employee</title>
<link rel="stylesheet" href="styles/employee.css" />
<link rel="stylesheet" href="styles/common.css" />
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="scripts/emp/rqSubmitForEmp.js"></script>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">

				<jsp:include page="../common/header.jsp"></jsp:include>

				<br>
				<form action="requestUpdate.do" method="post" enctype="multipart/form-data">
					<div class="row ">
						<table class="table table-hover table-bordered text-center">
							<thead>
								<tr>
									<th colspan="2">Reimbursement Request Application</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="width: 20%">Purpose</td>
									<td style="width: 80%">
										<select class="form-control" name="purpose">
											<option>Business Trip</option>
											<option>Purchase Office Supply</option>
											<option>Certificate</option>
											<option>Buyer Treatment</option>
											<option>Relocation</option>
											<option>Health Insurance</option>
											<option>ETC</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Amount</td>
									<td>
										<div class="input-group">
											<span class="input-group-addon"><i class="glyphicon glyphicon-usd"></i></span> 
											<input type="text" class="form-control" name="amount">
										</div>
									</td>
								</tr>
								<tr>
									<td>Receipt Image<br/> (only .jpg, .gif, png)<br/><br/>
										<!-- <button type="button" class="btn btn-danger" id="addFile">Add</button> -->
									</td>
									<td>
										<div class="input-group">
											<span class="input-group-addon"><i class="glyphicon glyphicon-remove"></i></span> 
											<input type="file" name="receipt_img" accept="image/*">
										</div>
									</td>
								</tr>

							</tbody>
						</table>
					</div>

					<div class="row  ">
						<div class="btn-toolbar">
							<a class="btn btn-danger pull-right" role="button" href="rqListViewForEmp.do?no=41&id=EMP1"> Go Back to Request List </a> <input type="submit" class="btn btn-danger pull-right" value="Submit Request" role="button">
						</div>
					</div>

				</form>

			</div>
			<div class="col-md-1"></div>
		</div>
	</div>
</body>

<script>
document.getElementById("addFile").addEventListener('click', addFile);
function addFile(event){
	console.log('you clicked me!');
	
}
</script>
</html>
