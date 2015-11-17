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
	href="${contextPath}/assets/bootstrap/css/jquery.datetimepicker.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/bootstrap/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${contextPath}/assets/bootstracss/easy-responsive-tabs.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/bootstrap/css/owl.carousel.css">
<link rel="stylesheet" href="${contextPath}/assets/bootstrap/css/main.css">
<script src="${contextPath}/assets/js/vendor/modernizr-2.6.2.min.js"></script>
<script type="text/javascript">var switchTo5x=true;</script>
<script type="text/javascript"
	src="http://w.sharethis.com/button/buttons.js"></script>
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
						<a href="index.htm"><img src="${contextPath}/assets/img/logo.png" alt=""></a>
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
							<li><i class="icon fa fa-home"> </i> <a href="index.html">Home</a>
							</li>

							<li><a href="#"><i class="icon fa fa-user"> </i> Profile</a></li>
							<li><a href="#"><i class="icon fa fa-comments"> </i>
									Messages</a></li>
							<li><a href="#"><i class="icon fa fa-sign-out"> </i>
									Sign Out</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>
	<!-- Sub-banner -->
	<section class="sub-banner newsection">
		<div class="container">
			<h2 class="title">Event Detail</h2>
		</div>
	</section>

	<!-- Events -->
	<section class="events text-left newsection">
		<div class="container">
			<div class="row">
				<!-- col-md-9 -->
				<div class="col-md-9 col-sm-9">
					<!--Event Detail  -->
					<section class="event-detail newsection">
						<h2 class="main-title ">
							${event.name}
						</h2>
						<!-- meta -->
						<ul class="meta clearfix">
							<li class="date"><i class="icon fa fa-calendar"></i> ${event.date}</li>
							<li>${event.location} </li>
						</ul>
						<!-- event-detail-img -->
						<div class="event-detail-img">
							<img src="${contextPath}/assets/img/TableTennis2.gif" alt="">
						</div>
						<h3 class="title">Description</h3>
						<p>${event.description}</p>
						<!-- Social Icon -->
						<div class="social-icon">
							<a href="#" class="facebook fa fa-facebook"></a> <a href="#"
								class="twitter fa fa-twitter"></a> <a href="#"
								class=" googleplus fa fa-google-plus"></a> <a href="#"
								class="vimeo fa fa-vimeo-square"></a> <a href="#"
								class="linkedin fa fa-linkedin"></a>
						</div>
					</section>
					<section class="speaker-event newsection">
						<h2 class="title">Participants of Event</h2>
						<!-- owl slider  -->
						<div class="owl-team">
							<div class="event">
								<div class="eventsimg">
									<img src="${contextPath}/assets/img/prof02.jpg" alt="">
								</div>
								<div class="event-content">
									<h3 class="title">Naqibullah Danishjo</h3>
									<p class="job">Senior CmpE Student</p>
									<p>Some description about me, some more description about
										me</p>

								</div>
								<div class="social-icon">
									<a href="#" class="email fa fa-envelope-o"></a><a href="#"
										class="facebook fa fa-facebook"></a><a href="#"
										class="fa linkedin fa-linkedin"></a><a href="#"
										class="googleplus fa fa-google-plus"></a><a href="#"
										class="twitter fa fa-twitter"></a>

								</div>
							</div>

							<div class="event">
								<div class="eventsimg">
									<img src="${contextPath}/assets/img/prof03.jpg" alt="">
								</div>
								<div class="event-content">
									<h3 class="title">Abdurrahman Can Kurtan</h3>
									<p class="job">Senior CmpE Student</p>
									<p>Some description about me, some more description about
										me</p>

								</div>
								<div class="social-icon">
									<a href="#" class="email fa fa-envelope-o"></a><a href="#"
										class="facebook fa fa-facebook"></a><a href="#"
										class="fa linkedin fa-linkedin"></a><a href="#"
										class="googleplus fa fa-google-plus"></a><a href="#"
										class="twitter fa fa-twitter"></a>
								</div>
							</div>
							<div class="event">
								<div class="eventsimg">
									<img src="${contextPath}/assets/img/prof04.jpg" alt="">
								</div>
								<div class="event-content">
									<h3 class="title">Tuba Topaloğlu</h3>
									<p class="job">Senior CmpE Student</p>
									<p>Some description about me, some more description about
										me</p>

								</div>
								<div class="social-icon">
									<a href="#" class="email fa fa-envelope-o"></a><a href="#"
										class="facebook fa fa-facebook"></a><a href="#"
										class="fa linkedin fa-linkedin"></a><a href="#"
										class="googleplus fa fa-google-plus"></a><a href="#"
										class="twitter fa fa-twitter"></a>
								</div>
							</div>
						</div>
					</section>

					<!-- speakers-tabs -->
				</div>
				<!-- Col-md-3 -->
				<div class="col-md-3 col-sm-3">
					<aside id="aside" class="aside-bar-style-two clearfix">

						<div class="widget border-remove">
							<div id="contact-map" class="map"></div>

							<div class="clearfix">
								<div class="main-example">
									<div class="countdown-container" id="upcomeing-events"></div>
								</div>
							</div>
						</div>

						<div class="widget clearfix">
							<a class="btn btn-pri btn-full">Join Event</a>
						</div>
						<div class="widget">
							<h3 class="title">Organizer</h3>
							<p>
								Organized by <b>Umut Afacan</b>
							</p>

							<a href="#" class="btn btn-black contact-button"><i
								class="button-icon fa fa-envelope-o"></i>Contact the Organizer</a>
							<ul class="social-icon">
								<li class="email"><a href="#"><i
										class=" icon fa fa-user"></i>
										<div class="content">View Profile of EventOrganizer</div></a></li>
								<li class="facebook"><a href="#"><i
										class="icon fa fa-facebook"></i>
										<div class="content">facebook.com/EventOrganizer</div></a></li>
								<li class="twitter"><a href="#"><i
										class=" icon fa fa-twitter"></i>
										<div class="content">twitter.com/EventOrganizer</div></a></li>
							</ul>
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
	<script src="http://maps.googleapis.com/maps/api/js?sensor=false"
		type="text/javascript"></script>
	<script src="${contextPath}/assets/js/plugins.js"></script>
	<script src="${contextPath}/assets/js/main.js"></script>

</body>
</html>