<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<nav>
  <% String navBarUser = (String)request.getSession().getAttribute("user"); %>
  <% User user = UserStore.getInstance().getUser(navBarUser);%>
  <a id="navTitle" href="/">CodeU Chat App - Nemo</a>
  <a href="/conversations">Conversations</a>
  <% if(navBarUser != null){ %>
    <a href="/profile/<%=navBarUser %>">
      Hello <%= navBarUser %>!</a>
  <% } else{ %>
    <a href="/login">Login</a>
  <% } %>
  <a href="/about.jsp">About</a>
  <a href="/activityfeed">Activity Feed</a>
  <%if(navBarUser != null && user.getIsAdmin() == true){%>
      <a href="/admin"> Admin Page</a>
  <%}%>
  <%@ include file="searchbar.jsp" %>
</nav>