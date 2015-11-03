<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"
	scope="application" />
<c:set var="requestURI" value="${pageContext.request.requestURI}"
	scope="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript"
	src="${contextPath}/assets/bootstrap/js/bootstrap.min.js"></script>
<style>
.error {
	color: #ff0000;
	font-size: 0.9em;
	font-weight: bold;
}

.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
<title>Cmpe Social Life</title>
</head>
<body>
	<div class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="${contextPath}/home">Cmpe Social
					Life</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">Page 1 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#">Page 1-1</a></li>
							<li><a href="#">Page 1-2</a></li>
							<li><a href="#">Page 1-3</a></li>
						</ul></li>
					<li><a href="#">Page 2</a></li>
					<li><a href="#">Page 3</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="${contextPath}/user/new"><span
							class="glyphicon glyphicon-user"></span> Sign Up</a></li>
					<li><a href=${contextPath}/user/login><span
							class="glyphicon glyphicon-log-in"></span> Login</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-lg-5 col-lg-offset-0">
		<div class="well">
			<div class="container">
				<div>
					<h1>Welcome to CMPE Social Life</h1>
					<p>Login to explore the complete features!</p>
				</div>
			</div>
		</div>
	</div>
	<div class="col-lg-6">
		<div class="well">
			<div class="container">
				<div class="row">
					<div class="col-lg-6">
						<form action="${contextPath}/user/update" method="post">
							<input type="hidden" name="id" value="${user.id}">
							<div class="form-group">
								<label for="name">Name:</label> <input type="text"
									class="form-control" placeholder="Your Name" name="name" id="name" value="${user.name}">
							</div>
							<div class="form-group">
								<label for="surname">Surname:</label> <input type="text"
									class="form-control" placeholder="Your Surname" name="surname" id="surname" value="${user.surname}">
							</div>
							<div class="form-group">
								<label for="email">Email address:</label> <input type="email"
									class="form-control" placeholder="Your Email" name="email" id="email" value="${user.email}">
							</div>
							<div class="form-group">
								<label for="password">Password:</label> <input type="password"
									class="form-control" placeholder="Your Password" name="password" id="password">
							</div>
							<div class="form-group">
								<label for="password2">Password Again:</label> <input
									type="password" class="form-control"
									placeholder="Your Password Again" name="password2" id="password2">
							</div>
							<button type="submit" class="btn btn-default">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>