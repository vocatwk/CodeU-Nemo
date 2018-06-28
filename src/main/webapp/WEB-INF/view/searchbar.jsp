<script type="text/javascript">
  // Iterates through fetched results
  function fetchResults() {
    var searchBarValue = document.querySelector('#searchBar').value;
    if (searchBarValue.length > 0) {
      fetch('/search?searchRequest=' + searchBarValue, {credentials: "same-origin"})
        .then(
          function(response) {
            if (response.status !== 200) {
              console.log("Looks like there was a problem. Status Code: " + response.status);
              return;
            }
            response.json().then(function(data) {
              if (data != null) {
                // Start with no resultItem divs
                document.querySelector('#result').innerHTML = '';
                for (var user in data) {
                  var userName = data[user].name;
                  var div = document.createElement("div");
                  div.setAttribute("class", "resultItem");
                  a = document.createElement("a");
                  a.href = "/profile/" + userName;
                  a.innerHTML = userName;
                  div.appendChild(a);
                  document.getElementById("result").appendChild(div);
                }
              }
              // Data is null
              else {
                // Clear the resultItem divs
                document.querySelector('#result').innerHTML = '';
              }
            });
          }
        ) 
        .catch(function(err) {
          console.log("Fetch Error :-S", err);
        });
    }
    // Search bar is empty
    else {
      // Clear the resultItem divs
      document.querySelector('#result').innerHTML = '';
    }
  }
</script>

<div id ="searchDiv">
  <input onkeyup="fetchResults()" type="text" autocomplete="off" placeholder="Search for Users. . ." name="searchRequest" id="searchBar">
  <div id="result">
    <!-- resultItem divs will go here -->
  </div>
</div>