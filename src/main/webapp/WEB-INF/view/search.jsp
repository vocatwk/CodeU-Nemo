<%@ page import="codeu.model.data.User" %>
<%@ page import="java.util.List" %>
<%
List<User> users = (List<User>) request.getAttribute("users");
String searchRequest = (String) request.getAttribute("searchRequest");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Search</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="navbar.jsp" %>

  <div id="container">
    <%
    if (users != null) {
    %>
      <h1>Results for <%= searchRequest %><a href="" style="float: right">&#8635;</a></h1>
      <div id="results">
      <%
        for (User user : users) {
        String userName = user.getName();
      %>
        <a href="/profile/<%= userName %>"><%= userName %></a>
      <%
        }
      %>
    </div>
    <%
    }
    else if (users == null) {
    %>
      <h1>No results for <%= searchRequest %><a href="" style="float: right">&#8635;</a></h1>
    <%
    }
    %>

  </div>

</body>
</html>
