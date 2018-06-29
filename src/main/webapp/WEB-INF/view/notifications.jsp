<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.List" %>
<%
  String username = (String) request.getSession().getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Notification</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%@ include file="navbar.jsp" %>
  <p> Hello! This is your notifcation page</p>
  <% request.getAttribute("eventsToShow"); %>
</body>
</html>
