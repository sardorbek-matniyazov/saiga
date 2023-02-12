'use strict';

const connectionPage = document.querySelector('#connection');
const ordersPage = document.querySelector('#orders-page');
const connectionF = document.querySelector('#connectionF');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');

let stompClient = null;
let username = null;

const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    connectionPage.classList.add('hidden');
    ordersPage.classList.remove('hidden');

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe('/topic/new-order-from-driver', onMessageReceived);
    stompClient.subscribe('/topic/new-order-from-user', onMessageReceived);

    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    console.log(message);

    let messageElement = document.createElement('li');

    const avatarElement = document.createElement('i');
    const avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = '#2196F3';

    messageElement.appendChild(avatarElement);

    const usernameElement = document.createElement('span');
    const usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    const textElement = document.createElement('p');
    const messageText = document.createTextNode(message.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

connectionF.addEventListener('submit', connect, true)
