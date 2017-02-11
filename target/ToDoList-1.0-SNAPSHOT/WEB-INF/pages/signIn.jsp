<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<html>
<head>
</head>
<%
    UserService userService = UserServiceFactory.getUserService();
%>
<body><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></body>