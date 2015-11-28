<%@page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@taglib prefix="s"
uri="http://www.springframework.org/tags" %> <%@taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"
	scope="application" />
<c:set var="requestURI" value="${pageContext.request.requestURI}"
	scope="application" />

<!DOCTYPE html>
<html class="no-js">
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
<script type="text/javascript"
	src="http://w.sharethis.com/button/buttons.js"></script>
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
							<li><i class="icon fa fa-home"> </i> <a href="${contextPath}/">Home</a>
							</li>

							<li><a href="#"><i class="icon fa fa-user"> </i>
									Profile</a></li>
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
	<!-- Event Form -->
	<br />
	<section class="background clearfix newsection">
		<div class="container">
			<div class="border-cover">
				<div class="background-content clearfix">
					<h2 class="title">
						Search Result for: <b> ${query} </b>
					</h2>
				</div>
			</div>
		</div>
	</section>

	<!-- Events -->
	<section class="events newsection">
		<div class="container">
			<div class="row">
				<div class="col-md-8">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h2>Events</h2>
							<hr />
							<c:forEach var="event" items="${events}" varStatus="roop">
								<div class="top-ppost">
									<div class="date">
										<p>
											${event.date}
										</p>
									</div>
									<div class="content">
										<h4 class="title">
											<a href="${contextPath}/event/view?id=${event.id}">${event.name}</a>
										</h4>
										<a href="#" class="meta"><i class="icon fa fa-map-marker"></i>${event.location}</a>
									</div>
								</div>
								<br />
							</c:forEach>							
						</div>
					</aside>
				</div>

				<!-- col-md-3 -->
				<div class="col-md-4">
					<aside id="aside" class="aside-bar-style-two clearfix">

						<div class="widget clearfix">
							<h3 class="title">Groups</h3>
							<div class="top-ppost">
								<div class="date">
									<p>
										<span><i class="icon fa fa-group"> </i></span>GROUP
									</p>
								</div>
								<div class="content">
									<h4 class="title">
										<a href="#">Kare Block Orta Düzey Tenis Oyuncuları </a>
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
										<a href="#">Liseden beri masa tenisi oynayanlar </a>
									</h4>
								</div>
							</div>
						</div>
						<br/>
					</aside>
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<h3 class="title">People</h3>
							<c:forEach var="user" items="${users}" varStatus="roop">
								<div class="top-ppost">
									<div class="date">
										<p>
											<span><i class="icon fa fa-user"> </i></span>
										</p>
									</div>
									<div class="content">
										<h4 class="title">
											<a href="${contextPath}/user/view?id=${event.id}">${user.name}
												${user.surname} </a>
										</h4>
									</div>
								</div>
								<hr />
								<br />
							</c:forEach>
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
