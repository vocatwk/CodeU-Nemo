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
<%@ page import="java.time.Instant" %>
<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <%@ include file="navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <div id="conversationContainer">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/conversations" method="POST">
      <label for="conversationName">New Conversation</label>
      <div class="flex" id="conversationForm">
        <div class="form-group" id="conversationInput">
          <input type="text" class="form-control" id="conversationName" name="conversationTitle" placeholder="Conversation Title">
          <small id="conversationTitleHelp" class="form-text text-muted">Conversations will show up below</small>
        </div>
        <button type="submit" class="btn btn-primary">Create</button>
      </div>
    </form>

    <br>

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
        Instant lastSeen = navBarUser.getLastSeenConversations().get(conversation.getId());
        Integer count = (lastSeen != null) ? conversation.getNewMessagesCount(lastSeen) : null;
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
          <a href="/chat/<%= conversation.getId() %>"> <%= conversation.getTitle() %></a>
    <%
        if(count != null && count != 0){
    %>
          <div class="oval bg-primary"> <div id="number"> <%= count %> </div> </div>
    <% 
        }
    %>
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
</body>
</html>
