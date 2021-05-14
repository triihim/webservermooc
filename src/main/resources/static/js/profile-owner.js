// POST CREATION
var feedPostTextarea = document.querySelector("#feed-post-textarea");
var feedPostButton = document.querySelector("#feed-post-button");

function createPost() {
    function onSuccess(data) {
        feedPostButton.disabled = false;
        feedPostTextarea.value = "";
        fetchFeed();
        showNotification("Post created", "success");
    }

    function onError(error) {
        console.error(error);
        feedPostButton.disabled = false;
    }

    if(feedPostTextarea.value.length > 0) {
        feedPostButton.disabled = true;
        var url = context + "api/posts";
        var jsonString = JSON.stringify({ 
            content: feedPostTextarea.value
        });
        var request = new Request(url, "POST", "application/json", jsonString);
        sendRequest(request, onSuccess, onError);
    }
}

feedPostButton.addEventListener("click", createPost);

feedPostTextarea.addEventListener("keydown", function(e) {
    if(e.keyCode === 13) { // enter
        e.preventDefault();
        feedPostButton.click();
    }
});