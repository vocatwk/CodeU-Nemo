<!-- Modal -->
<div class="modal fade" id="setUsersModal" tabindex="-1" role="dialog" aria-labelledby="setUsersModal" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="setUsersModalTitle">Edit the members of this conversation! </h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div id="selectedUserList">
          <!-- list of users selected will go here -->
        </div>
        <div id ="searchDiv">
          <input class="form-control" onkeyup="fetchResults('userResult','userSearchBar')" type="text" autocomplete="off" placeholder="Search for Users. . ." name="searchRequest" id="userSearchBar">
        </div>
        <ul class="list-group" id="userResult">
          <!-- resultItem divs will go here -->
        </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button disabled="true" id= "saveChangesButton" type="button" class="btn btn-primary">Save Changes</button>
      </div>
    </div>
  </div>
</div>
