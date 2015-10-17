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
            <th>Email</th>
            <th>Password</th>
            <th>Edit</th>
            <th>Delete</th>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}" varStatus="roop">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.password}</td>
                    <td><a href="${contextPath}/user/edit?id=${user.id}">Edit</a></td>
                    <td><a href="${contextPath}/user/delete?id=${user.id}">Delete</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <a href="${contextPath}/user/new">New User</a>
    </div>
</div>

</body>
</html>