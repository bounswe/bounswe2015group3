<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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

<div>
    <div>
        <a href="${contextPath}">Go Back</a>

        <br/>
        <br/>

        <form action="${contextPath}/user/update" method="post">
            <input type="hidden" name="id" value="${user.id}">

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" name="email" placeholder="Email" value="${user.email}">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" name="password" placeholder="Password" value="">
            </div>
            <button type="submit">Submit</button>
        </form>
    </div>
</div>

</body>
</html>