<%@ page import="codeu.model.data.Bot" %>
<%@ page import="java.util.List" %>
<%
List<Bot> bots = (List<Bot>) request.getAttribute("bots");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Bots</title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <div id="container">
    <h1>Bots
      <a href="" style="float: right">&#8635;</a></h1>

    <% for (Bot bot : bots) { 
      String name = bot.getName();
      String aboutMe = bot.getAboutMe();
    %>
      <div class="jumbotron jumbotron-fluid" id="bots">
        <div class="container">
          <h1 class="display-4"><a href="/profile/<%= name %>"><%= name %></a></h1>
          <p class="lead"><%= aboutMe %></p>
        </div>
      </div>
    <% } %>

  </div>

</body>
</html>