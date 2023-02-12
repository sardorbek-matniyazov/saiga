'use strict';

const tableBody = document.querySelector('#table-body');

let username = null;

const socket = new SockJS('/ws');
const stompClient  = Stomp.over(socket);

stompClient.connect({}, onConnected, onError);

function onConnected() {
    stompClient.subscribe('/topic/new-order-from-driver', onMessageReceived);
    stompClient.subscribe('/topic/new-order-from-user', onMessageReceived);
}


function onError(error) {
    console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
}

function onMessageReceived(payload) {
    const order = JSON.parse(payload.body);
    console.log(order);
    const messageElement = document.createElement('tr');
    const orderId = document.createElement('td');
    orderId.className = 'table-secondary';
    const messageContent = document.createElement('td');
    messageContent.className = 'table-info';
    const messageSender = document.createElement('td');
    messageSender.className = 'table-info';
    const messageDate = document.createElement('td');
    messageDate.className = 'table-info';
    const messageStatus = document.createElement('td');
    messageStatus.className = 'table-info';

    messageContent.textContent =    order.body.order.id;
    messageSender.textContent  =    order.body.order.fromUser.firstName;
    messageDate.textContent    =    order.body.order.timeWhen;
    messageStatus.textContent  =    order.body.order;

    messageContent.appendChild(orderId);
    messageElement.appendChild(messageContent);
    messageElement.appendChild(messageSender);
    messageElement.appendChild(messageDate);
    messageElement.appendChild(messageStatus);

    tableBody.appendChild(messageElement);
}
