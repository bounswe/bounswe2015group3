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
<script type="text/javascript">
	var switchTo5x = true;
</script>
<script type="text/javascript"
	src="http://w.sharethis.com/button/buttons.js"></script>
<script type="text/javascript">
	stLight.options({
		publisher : "ur-b4964695-8b2f-20dd-2ced-c9f6141de24c",
		doNotHash : false,
		doNotCopy : false,
		hashAddressBar : false
	});
</script>
</head>
<body>
	<!-- Header -->
	<header class="header-container">
		<!-- Main Header  -->
		<div class="main-header affix">
			<!-- Moblie Nav Wrapper  -->
			<div class="mobile-nav-wrapper">
				<div class="container ">
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
	<br />
	<section class="events newsection">
		<div class="container">
			<c:choose>
				<c:when test="${isOwner == true}">
					<div class="widget clearfix">
						<h3>Admin Section</h3>
						<br /> <a href="${contextPath}/group/edit?id=${group.id}"
							class="btn btn-info"><i class="fa fa-pencil-square-o">
							</i> EDIT GROUP </a> <a href="${contextPath}/group/delete?id=${group.id}"
							class="btn btn-danger"><i class="fa fa-times">
							</i> Delete Group </a>
					</div>
				</c:when>
			</c:choose>
		</div>
	</section>
	<section class="background clearfix newsection">
		<div class="container">
			<div class="panel panel-default">
				<c:choose>
					<c:when test="${not empty group.group_url}">
						<div class="panel-thumbnail">
							<img src="${group.group_url}" class="img-responsive">
						</div>
					</c:when>
					<c:otherwise>
						<div class="panel-thumbnail">
							<img src="${contextPath}/assets/img/cover-group-default.png"
								class="img-responsive">
						</div>
					</c:otherwise>
				</c:choose>
				<div class="panel-body">
					<p class="lead">${group.name}</p>
					<p>${group.description}</p>
				</div>
			</div>
		</div>
	</section>
	<br />
	<!-- Events -->
	<section class="events newsection">
		<div class="container">
			<div class="row">
				<div class="blog col-md-8">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget clearfix">
							<div class="well">
								<form class="form-horizontal" method="post"
									action="${contextPath}/group/create/post?id_group=${group.id}">
									<h4>Write Post</h4>
									<div class="form-group" style="padding: 14px;">
										<textarea class="form-control" id="post_text" name="post_text"
											placeholder="Write something.."></textarea>
									</div>
									<button class="btn btn-pri pull-right" type="submit">Post</button>
									<ul class="list-inline">
										<li><a href=""><i class="icon fa fa-upload"></i></a></li>
										<li><a href=""><i class="icon fa fa-camera"></i></a></li>
										<li><a href=""><i class="icon fa fa-map-marker"></i></a></li>
									</ul>
									<br />
								</form>
							</div>
						</div>
					</aside>
					<br />
					<div class="clearfix">
						<c:forEach var="post" items="${posts}" varStatus="roop">
							<div class="event-container clearfix">
								<div class="event clearfix">
									<div class="event-content">
										<ul class="meta">
											<li class="date"><i class="icon fa fa-calendar"></i>
											${fn:substring(post.date, 0, 16)}</li>
											<li><a
												href="${contextPath}/user/home?id=${post.author.id}"><i
													class="icon fa fa-user"></i>by ${post.author.name}
													${post.author.surname}</a></li>
										</ul>
										<p>${post.post_text}</p>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<!-- col-md-3 -->
				<div class="col-md-4">
					<aside id="aside" class="aside-bar-style-two clearfix">
						<br />
						<br />
						<!-- Tag Ekleme basliyor-->
						<div class="widget news">
							<form method="post" action="${contextPath}/group/tag/add">
								<div class="form-group">
									<input type="text" placeholder="Add tag to this group" id="tag" name="tag">
									<input type="hidden" id="id_group" name="id_group" value="${group.id}">
									<button type="submit" class="icon fa fa-tags"></button>
								</div>
							</form>
						</div>
						<!-- Tag Ekleme bitti-->
						<!-- Tag goruntuleme basliyor-->
						<div class="widget tag">

							<h3 class="title">Tags</h3>
							<c:forEach var="tag" items="${tags}" varStatus="roop">
								<a href="${contextPath}/search/tag?query=${tag.tag}">${tag.tag}</a>
							</c:forEach>
						</div>
						<!-- Tag goruntuleme bitti-->
						
						
						<c:choose>
							<c:when test="${isMember == false}">
								<div class="widget clearfix">
									<a href="${contextPath}/group/join?id=${group.id}"
										class="btn btn-success btn-full"> <i
										class="icon fa fa-plus"> </i> Join Group
									</a>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget clearfix">
									<a href="${contextPath}/group/leave?id=${group.id}"
										class="btn btn-danger btn-full"><i
										class="icon fa fa-sign-out"> </i> Leave Group</a>
								</div>
							</c:otherwise>
						</c:choose>
					</aside>
					<br />
					<aside id="aside" class="aside-bar-style-two clearfix">
						<div class="widget">
							<h3 class="title">Organizer</h3>
							<div class="top-ppost">
								<div class="date">
									<p>
										<span><i class="icon fa fa-user"> </i></span>
									</p>
								</div>
								<div class="content">
									<h4 class="title">
										<a href="${contextPath}/user/home?id=${creator.id}">${creator.name} ${creator.surname}</a>
									</h4>
								</div>
							</div>
							<br />
						</div>
						<div class="widget clearfix">
							<h3 class="title">Group Members</h3>
							<c:forEach var="participant" items="${members}" varStatus="roop">
								<div class="top-ppost">
									<div class="date">
										<p>
											<span><i class="icon fa fa-user"> </i></span>
										</p>
									</div>
									<div class="content">
										<h4 class="title">
											<a href="${contextPath}/user/home?id=${participant.id}">
												${participant.name} ${participant.surname}</a>
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
