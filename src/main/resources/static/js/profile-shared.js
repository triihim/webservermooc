var followButton = document.querySelector("#follow-button");

function updateFollowingStatus(status) {
    followButton.innerText = status.following ? "Unfollow" : "Follow";
    var photoWrappers = document.querySelectorAll(".album-photo");
    photoWrappers.forEach(function(pEl) {
        var photoId = pEl.dataset.photoid;
        var commentButton = pEl.querySelector(".comment-button");
        var commentField = pEl.querySelector(".comment-field");
        commentButton.disabled = !status.following;
        commentField.disabled = !status.following;
        commentField.placeholder = "Only followers can comment";
    });
}

function handlePostLikeClick(buttonRef, id) {
    buttonRef.disabled = true;

    function onSuccess(data) {
        var json = JSON.parse(data);
        buttonRef.querySelector(".like-badge").innerText = json.likes;
        buttonRef.removeChild(buttonRef.querySelector("i"));
        var thumbHtml = "<i class='bi bi-hand-thumbs-up-fill'></i>";
        buttonRef.innerHTML = thumbHtml + buttonRef.innerHTML;
    }
    function onError(error) {
        buttonRef.disabled = false;
        console.error(error);
        showNotification("Error happened trying to like a post", "danger", JSON.stringify(error));
    }

    var url = context + "api/posts/" + id + "/like";
    var request = new Request(url, "POST");
    sendRequest(request, onSuccess, onError);
}

function handlePostCommentSubmit(inputField, submitButton, postId) {
    function onSuccess(data) {
        var json = JSON.parse(data);
        inputField.value = "";
        submitButton.disabled = false;
        var commentsSection = document.querySelector("[data-postid='" + postId + "'] .comments");
        var commentCount = commentsSection.querySelectorAll(".comment").length;

        if(commentCount >= 10) {
            commentsSection.removeChild(commentsSection.lastElementChild);
        }

        var existingHtml = commentsSection.innerHTML;

        commentsSection.innerHTML = commentHtml(json) + existingHtml;
    }
    function onError(error) {
        submitButton.disabled = false;
        console.error(error);
        showNotification("Error happened trying to comment a post", "danger", JSON.stringify(error));
    }

    if(inputField.value.length > 0) {
        submitButton.disabled = true;
        var url = context + "api/posts/" + postId + "/comment";
        var request = new Request(url, "POST", "application/json", JSON.stringify({ content: inputField.value }));
        sendRequest(request, onSuccess, onError);
    }
}

// NOTICE: Do not call before feed has been fetched.
function enhanceFeedFunctionality() {
    var postWrappers = document.querySelectorAll(".post");
    postWrappers.forEach(function(postEl) {

        var likeButton = postEl.querySelector(".like-button");
        likeButton.addEventListener("click", function() {
            handlePostLikeClick(likeButton, postEl.dataset.postid);
        });

        var commentButton = postEl.querySelector(".comment-button");
        var commentField = postEl.querySelector(".comment-field");
        commentButton.addEventListener("click", function() {
            handlePostCommentSubmit(commentField, commentButton, postEl.dataset.postid);
        });

        commentField.addEventListener("keyup", function(e) {
            if(e.keyCode === 13) { // enter
                e.preventDefault();
                commentButton.click();
            } 
        });

    });

    var timestampEls = document.querySelectorAll(".timestamp");
    timestampEls.forEach(function(tEl) {
        tEl.innerText = formatServerDateTime(tEl.innerText);
    });
}


// NOTICE: do not call before fetching photo album.
function enhancePhotosFunctionality() {

    function deletePhoto(buttonRef, id) {
        function onSuccess(deletedId) {
            buttonRef.disabled = false;
            var deletedPhoto = document.querySelector("[data-photoid='" + deletedId + "']");
            deletedPhoto.parentNode.removeChild(deletedPhoto);
            showNotification("Photo deleted", "success");
        }

        function onError(error) {
            buttonRef.disabled = false;
            console.error(error);
            showNotification("Error happened trying to delete a photo", "danger", JSON.stringify(error));
        }

        if(window.confirm("Delete photo?")) {
            buttonRef.disabled = true;
            var url = context + "api/photos/" + id;
            var request = new Request(url, "DELETE");
            sendRequest(request, onSuccess, onError);
        }
    }

    function likePhoto(buttonRef, id) {
        buttonRef.disabled = true;

        function onSuccess(data) {
            var json = JSON.parse(data);
            buttonRef.querySelector(".like-badge").innerText = json.likes;
            buttonRef.removeChild(buttonRef.querySelector("i"));
            var thumbHtml = "<i class='bi bi-hand-thumbs-up-fill'></i>";
            buttonRef.innerHTML = thumbHtml + buttonRef.innerHTML;
        }

        function onError(error) {
            buttonRef.disabled = false;
            console.error(error);
            showNotification("Error happened trying to like a photo", "danger", JSON.stringify(error));
        }

        var url = context + "api/photos/" + id + "/like";
        var request = new Request(url, "POST");
        sendRequest(request, onSuccess, onError);
    }

    function handlePhotoCommentSubmit(inputField, submitButton, photoId) {
        function onSuccess(data) {
            var json = JSON.parse(data);
            inputField.value = "";
            submitButton.disabled = false;
            var commentsSection = document.querySelector("[data-photoid='" + photoId + "'] .comments");
            var commentCount = commentsSection.querySelectorAll(".comment").length;

            if(commentCount >= 10) {
                commentsSection.removeChild(commentsSection.lastElementChild);
            }

            var existingHtml = commentsSection.innerHTML;

            commentsSection.innerHTML = commentHtml(json) + existingHtml;
        }
        function onError(error) {
            submitButton.disabled = false;
            console.error(error);
            showNotification("Error happened trying to comment a photo", "danger", JSON.stringify(error));
        }

        if(inputField.value.length > 0) {
            submitButton.disabled = true;
            var url = context + "api/photos/" + photoId + "/comment";
            var request = new Request(url, "POST", "application/json", JSON.stringify({ content: inputField.value }));
            sendRequest(request, onSuccess, onError);
        }
    }

    var photoWrappers = document.querySelectorAll(".album-photo");
    photoWrappers.forEach(function(pEl) {
        var photoId = pEl.dataset.photoid;

        var likeButton = pEl.querySelector(".like-button");
        likeButton.addEventListener("click", function() {
            likePhoto(likeButton, photoId);
        });

        var commentButton = pEl.querySelector(".comment-button");
        var commentField = pEl.querySelector(".comment-field");
        commentButton.addEventListener("click", function() {
            handlePhotoCommentSubmit(commentField, commentButton, photoId);
        });

        commentField.addEventListener("keyup", function(e) {
            if(e.keyCode === 13) { // enter
                e.preventDefault();
                commentButton.click();
            } 
        });

        if(isOwner) {
            var deleteButton = pEl.querySelector(".photo-delete-button");
            deleteButton.addEventListener("click", function() {
                deletePhoto(deleteButton, photoId);
            });
        }

    });
}


function toggleBlock(buttonRef, userId) {
    buttonRef.disabled = true;

    function onSuccess(data) {
        buttonRef.disabled = false;
        var json = JSON.parse(data);
        var shieldIcon = buttonRef.querySelector("i");
        if(json.blocked === true) {
            buttonRef.classList.remove("btn-outline-secondary");
            buttonRef.classList.add("btn-outline-danger");
            shieldIcon.classList.remove("bi-shield");
            shieldIcon.classList.add("bi-shield-fill");
            buttonRef.querySelector("span").innerText = "Blocked";
        } else if(json.blocked === false) {
            buttonRef.classList.remove("btn-outline-danger");
            buttonRef.classList.add("btn-outline-secondary");
            shieldIcon.classList.remove("bi-shield-fill");
            shieldIcon.classList.add("bi-shield");
            buttonRef.querySelector("span").innerText = "Block";
        }
    }

    function onError(error) {
        buttonRef.disabled = false;
        console.error(error);
        showNotification("Error happened trying to block a follower", "danger", JSON.stringify(error));
    }

    var url = context + "api/toggle-block/" + userId;
    var request = new Request(url, "POST");
    sendRequest(request, onSuccess, onError);
}

// NOTICE: Do not call before followers are fetched.
function enhanceFollowersFunctionality() {
    var followerWrappers = document.querySelectorAll("#followers [data-userid]");
    followerWrappers.forEach(function(followerEl) {
        var timestamp = followerEl.querySelector(".timestamp");
        timestamp.innerText = formatServerDateTime(timestamp.innerText);
        var followerId = followerEl.dataset.userid;
        var blockButton = followerEl.querySelector(".block-follower-button");
        if(!blockButton) return;
        blockButton.addEventListener("click", function() {
            toggleBlock(blockButton, followerId);
        });
    });
}



// FEED FETCHING

var postsParent = document.querySelector("#posts");

function fetchFeed() {
    function onSuccess(data) {
        postsParent.innerHTML = data;
        enhanceFeedFunctionality();
    }
    function onError(error) {
        console.error(error);
        showNotification("Error happened trying to fetch the feed", "danger", JSON.stringify(error));
    }
    var url = context + "api/feed/" + account + "/html";
    var request = new Request(url, "GET");
    sendRequest(request, onSuccess, onError);
}

fetchFeed();

var feedButton = document.querySelector("#feed-tab");
feedButton.addEventListener("click", fetchFeed);



// FOLLOWERS FETCHING (on demand)

var followersTabButton = document.querySelector("#followers-tab");
var followersTab = document.querySelector("#followers");

function fetchFollowers() {
    function onSuccess(data) {
        followersTab.innerHTML = data;
        enhanceFollowersFunctionality();
    }
    function onError(error) {
        console.error(error);
        showNotification("Error happened trying to fetch the followers", "danger", JSON.stringify(error));
    }
    var url = context + "api/followers/" + account + "/html";
    var request = new Request(url, "GET");
    sendRequest(request, onSuccess, onError);
}

followersTabButton.addEventListener("click", fetchFollowers);



// FOLLOWEES FETCHING (on demand)

var followingTabButton = document.querySelector("#following-tab");
var followingTab = document.querySelector("#following");

followingTabButton.addEventListener("click", function() {

    function onSuccess(data) {
        followingTab.innerHTML = data;
        var timestampEls = document.querySelectorAll(".timestamp");
        timestampEls.forEach(function(tEl) {
            tEl.innerText = formatServerDateTime(tEl.innerText);
        });
    }
    function onError(error) {
        console.error(error);
        showNotification("Error happened trying to fetch the followees", "danger", JSON.stringify(error));
    }
    var url = context + "api/followees/" + account + "/html";
    var request = new Request(url, "GET");
    sendRequest(request, onSuccess, onError);

});


// PHOTOS FETCHING (on demand)

var photosButton = document.querySelector("#photos-tab");
var photosTab = document.querySelector("#photos");

photosButton.addEventListener("click", function() {
    function onSuccess(data) {
        photos.innerHTML = data;
        enhancePhotosFunctionality();
    }
    function onError(error) {
        console.error(error);
        showNotification("Error happened trying to fetch the photo album", "danger", JSON.stringify(error));
    }
    var url = context + "api/photos/" + account + "/html";
    var request = new Request(url, "GET");
    sendRequest(request, onSuccess, onError);
});

