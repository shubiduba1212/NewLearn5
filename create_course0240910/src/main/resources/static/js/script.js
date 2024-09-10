// scripts.js

document.addEventListener('DOMContentLoaded', function() {
    const chatBox = document.getElementById('chat-box');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');

    // WebSocket 연결
    const socket = new WebSocket('ws://localhost:8080/ws-chat');

    // WebSocket 메시지 수신 처리
    socket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        const message = data.message;

        // 서버로부터 받은 메시지를 화면에 추가
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', 'bot');
        messageElement.textContent = message;
        chatBox.appendChild(messageElement);
        chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 항상 아래로 유지
    };

    // 전송 버튼 클릭 시 메시지 전송
    sendButton.addEventListener('click', function() {
        sendMessage();
    });

    // 엔터 키를 눌러서 메시지 전송
    messageInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    });

    // 메시지 전송 함수
    function sendMessage() {
        const message = messageInput.value.trim();

        if (message) {
            // 사용자 메시지를 화면에 추가
            const messageElement = document.createElement('div');
            messageElement.classList.add('message', 'user');
            messageElement.textContent = message;
            chatBox.appendChild(messageElement);

            // 메시지를 서버로 전송
            socket.send(JSON.stringify({ message: message }));

            messageInput.value = ''; // 입력창 초기화
            chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 항상 아래로 유지
        }
    }
});
