<nav>
  <% String navBarUser = (String)request.getSession().getAttribute("user"); %>
  <% List<String> adminList = (List<String>) request.getSession().getAttribute("adminList"); %>
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
</nav>
