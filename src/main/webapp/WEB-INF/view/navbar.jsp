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

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <% String currentUser = (String) request.getSession().getAttribute("user"); %>
  <% String navBarUsername = (String)request.getSession().getAttribute("user"); %>
  <% User navBarUser = UserStore.getInstance().getUser(navBarUsername);%>

  <a class="navbar-brand" href="#">
    <img src="/logo.png" width="100" height="45" alt="">
  </a>

  <div class="collapse navbar-collapse justify-content-between" id="navbarNav">
    <div class="navbar-nav">
      <a class="nav-item nav-link" href="/conversations">Conversations <span class="sr-only">(current)</span></a>
      <a class="nav-item nav-link" href="/notifications"> Notifications</a> 
      <a class="nav-item nav-link" href="/activityfeed">Activity</a>
      <a class="nav-item nav-link" href="/bots">Bots</a>
      <a class="nav-item nav-link" href="/about.jsp">About</a>
    </div>

    <div class="navbar-nav">
      <%if(navBarUsername != null && navBarUser.getIsAdmin() == true){%>
          <a class="nav-item nav-link" href="/admin"> Admin Page</a>
      <%}%>
      <%@ include file="searchbar.jsp" %>

      <div class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <i class="fa fa-user"></i>
        </a>

        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
          <a class="dropdown-item" href="/profile/<%=navBarUsername %>">My Profile</a>
          <div class="dropdown-divider"></div>
          <form id="logout" class="form-inline" action="/logout" method="POST">
            <a class="dropdown-item" href="#" onclick="document.getElementById('logout').submit();">Sign Out</a>
          </form>
        </div>
      </div>
    </div>

  </div>

</nav>
