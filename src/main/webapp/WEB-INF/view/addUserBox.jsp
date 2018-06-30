<!-- Add user modal -->
<div id="addUserBox">

  <!-- modal content -->
  <div id="addUserContent">

    <span class="close">&times;</span>
    <p> Add people to this conversation! </p>
    <div id ="searchDiv">
      <input onkeyup="fetchResults('userResult','userSearchBar')" type="text" autocomplete="off" placeholder="Search for Users. . ." name="searchRequest" id="userSearchBar">
      <div id="userResult">
        <!-- resultItem divs will go here -->
      </div>
    </div>

  </div>

</div>
