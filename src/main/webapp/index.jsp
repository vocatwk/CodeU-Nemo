<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<% String user = (String) request.getSession().getAttribute("user"); %>
<% Boolean registrationForm = (Boolean) request.getAttribute("registrationForm"); %>

<!DOCTYPE html>
<html>
<head>
  <title>Nemo</title>

  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous"><script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>


  <!-- jQuery first, then Popper.js, then Bootstrap JS -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="/css/main.css">

  <script>

    $(window).ready(function(){
      var registrationForm = <%=registrationForm%>; 
      if(registrationForm){
        showRegister();
      }
    });

    function showRegister(){
      $("#login").hide();
      $("#register").show();
    }

  </script>
</head>
  <body class="bg-primary" id="landing">
    <div class="flex center-container">
      <div class="welcome-text">
        <div class="logo">
          <img src="/logo.png">
        </div>
        Chat with those you love!
        <br>
        Nemo is a free, open source chat platform.
      </div>
        
        <!-- login form -->
        <div id="login" class="login-container" class="rounded">
          <div id="login-content">
            <% if(request.getAttribute("error") != null){ %>
                <h2 style="color:red"><%= request.getAttribute("error") %></h2>
            <% } %>
            <div>Login</div>
            <br>
            <form id="loginForm" action='/login' method="POST">
              <div class="form-group">
                <input class="form-control" name="username" placeholder="Enter username">
              </div>
              <div class="form-group">
                <input type="password" name="password" class="form-control"placeholder="Password">
              </div>
              <button type="submit" class="btn btn-primary" id="loginButton">
                Sign In
              </button>
              <a href="javascript:void(0)" onclick="showRegister()" class="btn btn-light">Register</a>
            </form>
          </div>
        </div>

        <!-- registration form -->
        <div id="register" class="login-container" class="rounded">
          <div id="login-content">
            <% if(request.getAttribute("error") != null){ %>
                <h2 style="color:red"><%= request.getAttribute("error") %></h2>
            <% } %>
            <div>Register a new user</div>
            <br>
            <form action="/register" method="POST">
              <div class="form-group">
                <input type="text" class="form-control" name="username" placeholder="Enter username">
              </div>
              <div class="form-group">
                <input type="password" class="form-control" name="password" placeholder="Password">
              </div>
              <button class="btn btn-primary" type="submit">Register</button>
            </form>
          </div>
        </div>

    </div>
  </body>
</html>
