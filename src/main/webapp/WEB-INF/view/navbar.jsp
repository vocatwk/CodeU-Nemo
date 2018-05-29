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
  <a href="/activityfeed">Activity Feed</a>
</nav>