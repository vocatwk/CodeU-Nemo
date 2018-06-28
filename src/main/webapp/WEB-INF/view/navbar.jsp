<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<nav>
  <% String navBarUsername = (String)request.getSession().getAttribute("user"); %>
  <% User navBarUser = UserStore.getInstance().getUser(navBarUsername);%>
  <a id="navTitle" href="/">CodeU Chat App - Nemo</a>
  <a href="/conversations">Conversations</a>
  <% if(navBarUsername != null){ %>
    <a href="/profile/<%=navBarUsername %>">
      Hello <%= navBarUsername %>!</a>
  <% } else{ %>
    <a href="/login">Login</a>
  <% } %>
  <a href="/about.jsp">About</a>
  <a href="/activityfeed">Activity Feed</a>
  <a href="/notifications"> Notifications</a>
  <form action="/logout" method="POST" style="float: right">
      <input type="submit" value="Log out"/>
  </form>
  <%if(navBarUsername != null && navBarUser.getIsAdmin() == true){%>
      <a href="/admin"> Admin Page</a>
  <%}%>
  <%@ include file="searchbar.jsp" %>
</nav>
