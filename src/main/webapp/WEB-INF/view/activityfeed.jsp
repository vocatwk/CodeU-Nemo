<%@ page import="codeu.model.data.User" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    #activity {
      background-color: white;
    }
  </style>
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App - Nemo</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/profile/<%=request.getSession().getAttribute("user") %>">
         Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <h1>Activity</h1>

    <p>Here's everything that's happened on the site so far!</p>

    <div id="activity">
      
      <%List<User> users = 
        (List<User>) request.getAttribute("users");
      if (users != null && !users.isEmpty()) {
      %>
      <ul>
        <% 
          for(User user : users) {
          Date date = Date.from(user.getCreationTime());
        %>
        <li><b><%= date %>: </b> 
          <a href="/profile/<%= user.getName() %>">
          <%= user.getName() %></a> joined!</li>
        <%
        }
        %>
      </ul>
      <%
      }
      %>
    </div>

  </div>
  
</body>
</html>
