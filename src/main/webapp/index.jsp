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
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App - Nemo</title>
  <%@ include file="WEB-INF/view/navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <% if(user != null) {%>
      <div id="container">
      <div
        style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

        <h1>CodeU Chat App - Nemo</h1>
        <h2>Welcome!</h2>

        <ul>
          <li>Go to the <a href="/conversations">conversations</a> page to
              create or join a conversation.</li>
          <li>View the <a href="/about.jsp">about</a> page to learn more about the
              project.</li>
        </ul>
      </div>
    </div>
  <% } else {%>

    <div> Welcome to Nemo! <div>
    <a href="/login">Login or register</a> to get started.
  
    <%-- TODO more marketing info --%>
  <% } %>


    <div class="flex center-container">
      <div class="welcome-text">
        <div class="logo">Logo goes here</div>
        Chat with those you love!
        <br>
        Nemo is a free, open source chat platform.
      </div>
      <div class="login-container" class="rounded">
        <div id="login-content">
          <div>Login</div>
          <br>
          <form>
            <div class="form-group">
              <input type="email" class="form-control" placeholder="Enter email">
            </div>
            <div class="form-group">
              <input type="password" class="form-control"placeholder="Password">
            </div>
            <button type="submit" class="btn btn-primary" id="loginButton">
              Sign In
            </button>
            <button type="submit" class="btn btn-light">Register</button>
          </form>
        </div>
      </div>
    </div>

  </body>
</html>
