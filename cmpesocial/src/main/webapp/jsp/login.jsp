<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"
	scope="application" />
<c:set var="requestURI" value="${pageContext.request.requestURI}"
	scope="application" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Cmpe Social</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,900,300italic,400italic,600italic,700italic'
	rel='stylesheet' type='text/css'>
<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
<link rel="apple-touch-icon" href="apple-touch-icon-precomposed.png">
<link rel="shortcut icon" href="favicon.png">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/bootstrap/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstrap/css/jquery.datetimepicker.css">
<link rel="stylesheet" href="${contextPath}/assets/bootstrap/css/main.css">
<script src="${contextPath}/assets/js/vendor/modernizr-2.6.2.min.js"></script>
<script type="text/javascript">var switchTo5x=true;</script>
<!--
    <script type="text/javascript" src="http://w.sharethis.com/button/buttons.js"></script>
    -->
<script type="text/javascript">stLight.options({publisher: "ur-b4964695-8b2f-20dd-2ced-c9f6141de24c", doNotHash: false, doNotCopy: false, hashAddressBar: false});</script>
</head>
<body>

	<!--[if lt IE 7]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->

	<!-- Header -->
	<header class="header-container">
		<!-- Main Header  -->
		<div class="main-header affix">

			<!-- Moblie Nav Wrapper  -->
			<div class="mobile-nav-wrapper">
				<div class="container ">

					<!-- logo  -->
					<div id="logo">
						<a href=""${contextPath}/"><img src="${contextPath}/assets/img/logo.png" alt=""></a>
					</div>

					<div id="sb-search" class="sb-search">
						<form>
							<input class="sb-search-input"
								placeholder="People, Events and more" type="text" name="search"
								id="search"> <input class="sb-search-submit"
								type="submit" value=""> <span class="sb-icon-search"></span>
						</form>
					</div>
					<!-- moblie-menu-icon -->

					<div class="mobile-menu-icon">
						<i class="fa fa-bars"></i>
					</div>

					<!-- Nav -->
					<nav class="main-nav mobile-menu">

						<ul class="clearfix">
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>
	<section class="sub-banner newsection">
		<div class="container">
			<h2 class="title">Sign In</h2>
			<ul class="breadcrumb">
				<li><a href=""${contextPath}/">Home</a></li>
				<li>Sign In</li>
			</ul>
		</div>
	</section>
	<section class="events newsection">
		<div class="container">
			<div class="row">
				<div class="col-md-8">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h2>
								<i class="icon fa fa-lock"> </i> Sign In
							</h2>
							<hr />
							<form method="post" action="${contextPath}/user/login" class="form">
								<div class="response">&nbsp;</div>

								<p>
									<label for="email">Email</label> <input id="email"
										type="email" value="" name="email"
										placeholder="Email Address" class="textflied"> <i
										class="icon fa fa-envelope"></i>
								</p>

								<p>
									<label for="password">Password</label> <input
										id="password" type="password" placeholder="Password"
										name="password" class="textflied"> <i
										class="icon fa fa-lock"></i>
								</p>

								<p>
									<input id="login_remember_me" type="checkbox" checked="checked"
										placeholder="Password" name="login_remember_me">
									Remember Me <a style="float: right;" href="#">Forget
										Password</a>
								</p>
								<button type="submit" name="submit" id="login_submit"
									class="btn btn-success">Login</button>
								</p>
							</form>
						</div>
					</aside>
				</div>
				<div class="col-md-4">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h3 class="title">CMPE Social Life</h3>
							<p>CMPE @ BOUN is a very busy department. It has faculty
								members, undergraduates, graduates, support staff, and alumni.</p>
							<p>There are official and unofficial issues that are
								maintained on a daily basis. With the load and variety of work
								this can be difficult to manage. Furthermore, with all the
								activity going on it is difficult to create and maintain
								meaningful connection with other people and information.</p>
							<p>Not member yet, please register.</p>
							<p>
								<a href="${contextPath}/user/new" class="btn btn-pri btn-full"><i
									class="icon fa fa-edit"> </i> Register</a>
							</p>
						</div>
					</aside>
				</div>
			</div>
		</div>
	</section>
	<!-- Footer -->
	<footer class="main-footer">
		<div class="container">
			<div class="row">
				<div class="widget col-md-3">
					<p>
						<a target="_blank"
							href="https://github.com/bounswe/bounswe2015group3/wiki">
							About US</a>
					<p>
				</div>
				<div class="widget col-md-3"></div>
				<div class="widget col-md-3"></div>
				<div class="widget col-md-3"></div>
			</div>
		</div>
	</footer>
	<script src="js/vendor/jquery-1.10.2.min.js"></script>
	<script src="js/plugins.js"></script>
	<script src="js/main.js"></script>
</body>
</html>