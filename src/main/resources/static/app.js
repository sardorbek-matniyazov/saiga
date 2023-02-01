let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

const userName = 'user' + Math.floor((Math.random() * 1000) + 1);
const socket = io('http://localhost:9092/new-order', {
    transports: ['polling', 'websocket']
});

function connect() {
    socket.on('connect', function () {
        output('<span class="connect-msg">The client has connected with the server. Username: ' + userName + '</span>');
    });

    socket.on('reconnect_attempt', (attempts) => {
        console.log('Try to reconnect at ' + attempts + ' attempt(s).');
    });
}

function disconnect() {
    if (stompClient !== null) {
        socket.on('disconnect', function () {
            output('<span class="disconnect-msg">The client has disconnected!</span>');
        });
    }
    setConnected(false);
    console.log("Disconnected");
}

function emitToServer() {
    socket.on('new-order', function (data) {
        console.log('Received message', data);
        if (data.userName === "admin") {
            output('<span style="color: darkred" class="username-msg">' + data.userName + ':</span> ' + data.message);
        } else {
            output('<span style="color: purple; border: red"  class="username-msg">' + data.userName + ':</span> ' + data.message);
        }
    });
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        emitToServer();
    });
});

