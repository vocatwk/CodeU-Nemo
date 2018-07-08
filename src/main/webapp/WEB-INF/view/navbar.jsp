<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous"><script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>


<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<nav>
  <% String currentUser = (String) request.getSession().getAttribute("user"); %>
  <% String navBarUsername = (String)request.getSession().getAttribute("user"); %>
  <% User navBarUser = UserStore.getInstance().getUser(navBarUsername);%>
  <a id="navTitle" href="/">CodeU Chat App - Nemo</a>

  <% if(currentUser!=null) { %>
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
  <% } %>
</nav>
