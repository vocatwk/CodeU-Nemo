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
    <h1>Results<a href="" style="float: right">&#8635;</a></h1>

    <div id="results">
      <% 
      if (users == null) {
      %>
        <p>No results for: <%= searchRequest %></p>
      <%
      }
      else {
      %>
        <p>Results for: <%= searchRequest %></p>
      <%
        for (User user : users) {
      %>
          <p><%= user.getName() %></p>
      <%
        }
      }
      %>
    </div>

  </div>

</body>
</html>