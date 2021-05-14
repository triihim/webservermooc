var followButton = document.querySelector("#follow-button");

function onSuccess(data) {
    var status = JSON.parse(data);
    updateFollowingStatus(status);
    fetchFeed();
    fetchFollowers();
}

function onError(error) {
    console.log("Error", error);
}

function fetchStatus() {
    var url = context + "api/following/status/" + account;
    var request = new Request(url, "GET");
    sendRequest(request, onSuccess, onError);
}

function toggleFollowingStatus() {
    var url = context + "api/following/toggle-follow/" + account;
    var request = new Request(url, "POST", "application/json", {});
    sendRequest(request, onSuccess, onError);
}

followButton.addEventListener("click", toggleFollowingStatus);

fetchStatus();