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
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstrap/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstrap/css/jquery.datetimepicker.css">
<link rel="stylesheet"
	href="${contextPath}/assets/bootstrap/css/main.css">
<script src="${contextPath}/assets/js/vendor/modernizr-2.6.2.min.js"></script>
<script type="text/javascript">var switchTo5x=true;</script>
<script type="text/javascript">stLight.options({publisher: "ur-b4964695-8b2f-20dd-2ced-c9f6141de24c", doNotHash: false, doNotCopy: false, hashAddressBar: false});</script>
</head>
<body>
	<!-- Header -->
	<header class="header-container">
		<!-- Main Header  -->
		<div class="main-header affix">
			<!-- Moblie Nav Wrapper  -->
			<div class="mobile-nav-wrapper">
				<div class="container ">
					<!-- logo  -->
					<div id="logo">
						<a href="${contextPath}/"><img
							src="${contextPath}/assets/img/logo.png" alt=""></a>
					</div>
					<div id="sb-search" class="sb-search">
						<form method="post" action="${contextPath}/search" class="form">
							<input class="sb-search-input"
								placeholder="People, Events and more" type="text" name="query"
								id="query"> <input class="sb-search-submit"
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
							<li><a href="${contextPath}/user/home"><i
									class="icon fa fa-user"> </i> Profile</a></li>
							<li><a href="${contextPath}/events"><i
									class="icon fa fa-calendar"> </i> Events</a></li>
							<li><a href="${contextPath}/groups"><i
									class="icon fa fa-group"> </i> Groups</a></li>
							<li><a href="${contextPath}/user/logout"><i class="icon fa fa-sign-out"> </i>
									Sign Out</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>
	<section class="sub-banner newsection">
		<div class="container">
			<h2 class="title">Create Event</h2>
		</div>
	</section>
	<section class="events newsection">
		<div class="container">
			<div class="row">
				<div class="col-md-8">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h2>Create Event</h2>
							<hr />
							<form method="post" action="${contextPath}/events/update"
								class="form">
								<input type="hidden" name="id" value="${event.id}">
								<div class="response">&nbsp;</div>
								<p>
									<label for="name">Event Name:</label> <input
										id="name" type="text" value="" name="name"
										placeholder="Event Title" class="textflied">
								</p>

								<p>
									<label>Place:</label> <input type="text" name="location"
										id="location" value="" placeholder="Event Location"
										class="textflied"> <i class="icon fa fa-map-marker"></i>
								</p>

								<p>
									<label>Start Date:</label><br /> <input name="date" id="date"
										placeholder="YYYY/MM/DD HH:MM:SS"
										class="date_timepicker_start"
										style="width: 100%; height: 40px; padding: 0px 10px; border: 1px solid #c2c4cb; border-radius: 2px; outline: none;">

									<i class="icon fa fa-calendar"></i>
								</p>
								
								<p>
									<label>End Date:</label><br /> <input name="end_date" id="end_date"
										placeholder="YYYY/MM/DD HH:MM:SS"
										class="date_timepicker_start"
										style="width: 100%; height: 40px; padding: 0px 10px; border: 1px solid #c2c4cb; border-radius: 2px; outline: none;">

									<i class="icon fa fa-calendar"></i>
								</p>
								
								<p>
									<label for="role">Is periodic?</label> <select
										class="textflied" id="periodic" name="periodic">
										<option value="0">Not Periodic</option>
										<option value="1">Weekly</option>
										<option value="2">Monthly</option>
										<option value="3">Yearly</option>
									</select>
								</p>
								
								<p>
									<label>Event Image Url</label> <input type="text" value=""
										id="photo_url" name="photo_url" placeholder="Link of image"
										class="textflied"> <i class="icon fa fa-link"></i>
								</p> 

								<p>
									<label>Event Description</label>
									<textarea
										style="width: 100%; padding: 0px 10px; border: 1px solid #c2c4cb; border-radius: 2px; outline: none;"
										name="description" id="description" rows="10"></textarea>
								</p>
								<p>
									<label for="role">Who can join?</label> <select
										class="textflied" id="type" name="type">
										<option value="0">Everyone</option>
										<option value="1">Under Graduate</option>
										<option value="2">Graduate</option>
										<option value="3">Faculty Member</option>
										<option value="4">Alumni</option>
										<option value="5">Staff</option>
									</select>
								</p>
								<button type="submit" name="submit" id="submitButton"
									class="btn btn-success">CREATE Event</button>
							</form>
						</div>
					</aside>
				</div>
				<div class="col-md-4">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h3 class="title">Suggested For You</h3>
							<div class="top-ppost">

								<div class="date">
									<p>
										<span><i class="icon fa fa-calendar"> </i></span>EVENT
									</p>
								</div>
								<div class="content">
									<h4 class="title">
										<a href="#">Watching Star Wars in Kuzey Kampus Cinema </a>
									</h4>
								</div>
							</div>
							<hr />
							<div class="top-ppost">
								<div class="date">
									<p>
										<span><i class="icon fa fa-group"> </i></span>GROUP
									</p>
								</div>
								<div class="content">
									<h4 class="title">
										<a href="#">Orta Düzey Tenis Oyuncuları </a>
									</h4>
								</div>
							</div>
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
	<script src="${contextPath}/assets/js/vendor/jquery-1.10.2.min.js"></script>
	<script src="${contextPath}/assets/js/plugins.js"></script>
	<script src="${contextPath}/assets/js/main.js"></script>
</body>
</html>
