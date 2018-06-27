<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.store.basic.NotificationStore" %>
<%@ page import="codeu.model.data.Notification"%>
<% List<Notification> notifications = (List<Notification>) request.getAttribute("userNotifications");
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
  <p> Hello User This is your notifcation page</p>
</body>
</html>
