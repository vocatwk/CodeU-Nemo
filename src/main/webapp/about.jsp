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

<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <%@ include file="WEB-INF/view/navbar.jsp" %>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <div id="aboutChat">
      <h1>About Nemo Chat</h1>
     This is a chat app created by Team Nemo. It features:
      <li> a profile pages for users where they can update their About Me's, upload profile pictures, and view their sent messages.</li>
      <li> private and public conversations.</li>
      <li> allows you to add/remove users from a conversation.</li>
      <li> a notification page that allows you to see when messages and conversations are being made that you are member of.</li>
      <li> an Activity Feed that allows users to see when new users join, create
  conversations, send messages, and update their About Me's.</li>
      <li> has a dynamic search feature that allows users to search for other users.</li>
      <li> a Bot Framework that allows developers to contribute their own Bots. </li>
      <li> NemoBot, a bot that functions as an assistant.</li>
      <li> ConversationStatBot, a bot that provides conversation statistics.</li>
  </div>
<br>
  <div id="bioTitle">
    <div style="text-align:center;">
       <h4> Team Nemo </h4>
       </div>
  </div>

  <div class="bio">
    <p class="label success new-label"><span class="nameVer">Veronica T Woldehanna</span>
    <span class="add-label">I'm from Ethiopia. I'm a rising junior at Columbia University majoring in Computer
      Science and minoring in Statistics.</p>
      <br>
    <p class="label success new-label"><span class="nameJas">Jasmine Chau</span>
    <span class="add-label">I'm from California.I'm a rising sophmore at Middlebury College and I am majoring in Computer Science.
      I love baking and memes.</p>
      <br>
    <p class="label success new-label"><span class="nameMemo">Guillermo M. Leal Gamboa</span>
    <span class="add-label">I'm a rising sophomore at the University of Southern California majoring in Computer Science.
      My hobbies include anything soccer related.</p>
  </div>

</body>
</html>
