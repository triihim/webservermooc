function Request(url, method, contentType, payload) {
    this.url = url;
    this.method = method;
    this.contentType = contentType;
    this.payload = payload;
}

function sendRequest(request, onSuccess, onError) {
    if(!request || !request.method || !request.url) {
        console.error("Invalid request request");
        return;
    }

    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {
        if(xhr.readyState === 4) {
            if(xhr.status >= 200 && xhr.status < 400) {
                if(xhr.responseURL.indexOf("/login") > -1) {
                    // Request was redirected to login page = session has timed out -> refresh page.
                    location.reload();
                }
                onSuccess(xhr.responseText);
            } else {
                xhr.onerror();
            }
        }
    }

    xhr.onerror = function() {
        onError({
            status: xhr.status,
            statusText: xhr.statusText,
            responseText: xhr.responseText
        });
    }

    xhr.open(request.method, request.url);

    if(request.contentType && request.payload) {
        xhr.setRequestHeader("Content-Type", request.contentType);
        xhr.send(request.payload);
    } else {
        xhr.send();
    }
}

function formatServerDateTime(datetime) {
    if(!/\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d*/.test(datetime)) return datetime; // Already formatted.
    var dateOptions = { weekday: "long", year: "numeric", month: "long", day: "numeric" };
    var timeOptions = { hour12: false };
    var date = new Date(datetime + "z");
    return date.toLocaleDateString("en-GB", dateOptions) + " " + date.toLocaleTimeString("en-GB", timeOptions);
}

function showNotification(message, type, details) {
    if(["success", "danger", "warning"].indexOf(type.toLowerCase()) === -1) {
        console.error("Invalid notification type: " + type);
        return;
    }
    var wrapper = document.querySelector("#notification-wrapper");
    var element = document.createElement("div");
    element.classList.add("alert");
    element.classList.add("alert-" + type.toLowerCase());
    
    var html = message.toString();

    if(details) {
        html += "<button class='btn btn-dark m-2 details-button'>Details</button>";
    }

    element.innerHTML = html;
    
    var notification = wrapper.appendChild(element);
    var detailsButton = notification.querySelector(".details-button");
    
    if(detailsButton) {
        detailsButton.addEventListener("click", function() {
            var detailsTemp = document.createElement("textarea");
            document.body.appendChild(detailsTemp);
            detailsTemp.value = details;
            detailsTemp.select();
            detailsTemp.setSelectionRange(0, 99999);
            document.execCommand("copy");
            document.body.removeChild(detailsTemp);
            alert("The following is also copied to your clipboard:\n" + details);
        });
    }

    var duration = type.toLowerCase() === "danger" ? 10000 : 5000;

    setTimeout(function() {
        wrapper.removeChild(notification);
    }, duration);
}
