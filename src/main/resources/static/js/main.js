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