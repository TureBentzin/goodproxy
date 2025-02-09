const webSocket = new WebSocket('ws://befator.befatorinc.de:5000/banking');

function sendMessage(action, value1, value2, value3, value4) {
    const messageJson = {
        "action": action,
        "value1": value1,
        "value2": value2,
        "value3": value3,
        "value4": value4
    };

    const message = JSON.stringify(messageJson);

    console.log("Sending message: " + message);
    webSocket.send(message);
}

webSocket.onopen = function () {
    console.log("WebSocket connection opened.");
    sendMessage("register", "tdr", "tdr", "", "");
};

webSocket.onmessage = function (event) {
    const message = JSON.parse(event.data);
    console.log("Received message:", message);
};

webSocket.onerror = function (event) {
    console.error("WebSocket error:", event);
};
