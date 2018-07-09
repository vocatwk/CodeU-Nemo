<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="/css/main.css">
      <title> Subscriptions </title>
      <%@ include file="navbar.jsp" %>
  </head>
  <body>
    <p> These are the User's subscriptions </p>
    <%= request.getAttribute("subscriptions") %>
  </body>
</html>
