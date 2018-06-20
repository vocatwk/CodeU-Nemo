<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>
<nav>
  <% String navBarUser = (String)request.getSession().getAttribute("user"); %>
  <% User navUser = UserStore.getInstance().getUser(navBarUser);%>
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
  <a href="/signout" style="float: right"> Sign out </a>
  <%if(navBarUser != null && navUser.getIsAdmin() == true){%>
      <a href="/admin"> Admin Page</a>
  <%}%>
</nav>
