<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<link href="css/bootstrap.min.css" rel="stylesheet">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Index</title>
<script src="plotly-latest.min.js"></script>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
</head>

<body>

	<div
		class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
		<h5 class="my-0 mr-md-auto font-weight-normal">xCBR Challenge 2022</h5>
		<nav class="my-2 my-md-0 mr-md-3">
			<a class="p-2 text-dark" href="#">Index</a> 
			<a class="p-2 text-dark"href="#">About us</a>
		</nav>
	</div>

	<div
		class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
		Pick the date to forecast:
		<form method="POST" action="ForecastServlet">
			<input type="date" name="date"> <input type="submit">
		</form>
	</div>



</body>
</html>