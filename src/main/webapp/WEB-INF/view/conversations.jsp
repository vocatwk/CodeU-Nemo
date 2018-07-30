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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <div id="conversationContainer">
    <div id="conversationInput">

      <% if(request.getAttribute("error") != null){ %>
          <h2 style="color:red"><%= request.getAttribute("error") %></h2>
      <% } %>

      <form action="/conversations" method="POST">
        <label for="conversationName">New Conversation</label>
        <div class="form-row align-items-center">
          <div class="col-auto">
            <input type="text" class="form-control mb-2" id="conversationName" name="conversationTitle" placeholder="Conversation Title">
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-primary mb-2">Create</button>
          </div>
        </div>
      </form>

    </div>

    <br>

    <div id="conversationList">

      <label for="card">Conversations</label>

      <%
      Boolean listIsEmpty = true;
      Boolean oneFound = false;
      List<Conversation> conversations =
        (List<Conversation>) request.getAttribute("conversations");
      if(conversations != null){
        for(Conversation conversation : conversations){
          if(!conversation.containsMember(navBarUsername)){
            continue;
          }
          if(!oneFound){
      %>
            <div class="card">
            <table class="table table-bordered" id="conversationsTable">
              <tbody>
      <%
            listIsEmpty=false;
            oneFound=true;
          }
      %>
          <tr><td>
            <a href="/chat/<%= conversation.getId() %>"> <%= conversation.getTitle() %></a></li>
          </td></tr>
      <%
        }
      }
      if(listIsEmpty || conversations == null){
      %>
        <p>Create a conversation to get started.</p>
      <%
      }
      else{
      %>
            </tbody>
          </table>
        </div>
      <%
      }
      %>
    </div>

  </div>
</body>
</html>
