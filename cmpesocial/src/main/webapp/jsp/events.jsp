<%@page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="application"/>
<c:set var="requestURI" value="${pageContext.request.requestURI}" scope="application"/>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cmpe Social Life</title>
</head>
<body>

<div>
    <h1>Cmpe Social Life</h1>
    <p></p>
</div>

<div class="row">
    <div>
        <table style="margin-top:10px;">
            <thead>
            <th>ID</th>
            <th>Name</th>
            <th>Date</th>
            <th>UserID</th>
            <th>Location</th>
            <th>Description</th>
            </thead>
            <tbody>
            <c:forEach var="event" items="${events}" varStatus="roop">
                <tr>
                    <td>${event.id}</td>
                    <td>${event.name}</td>
                    <td>${event.date}</td>
                    <td>${event.id_user}</td>
                    <td>${event.location}</td>
                    <td>${event.description}</td>
                    <td><a href="${contextPath}/event/edit?id=${event.id}">Edit</a></td>
                    <td><a href="${contextPath}/event/delete?id=${event.id}">Delete</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <a href="${contextPath}/events/create">Create New Event</a>
    </div>
</div>

</body>
</html>