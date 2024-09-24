const ws = connectWebSocket();  // Replace with your actual WebSocket URL

function connectWebSocket()
{
    var ws = new WebSocket('ws://localhost:8080/ws-chat');
    // Replace with your actual WebSocket URL

    ws.onopen =
        function()
        {
            console.log('Connected to WebSocket');
        };

    ws.onmessage = function(event)
    {

        var gptMessageDiv = $('<div></div>').addClass('message-box gpt-message')
            .html(event.data);

        // Append the message to the chat box
        $('.chat-box').append(gptMessageDiv);

        // Scroll the chat box to the bottom
        $('.chat-box').scrollTop($('.chat-box')[0].scrollHeight);
    };

    ws.onclose = function()
    {
        console.log('WebSocket connection closed');
    };

    ws.onerror = function(error)
    {
        console.error('WebSocket error:', error);
    };



    return ws;
}

$(document).ready(function()
{
    // Handle button click to send message
    $('#send-btn').on('click',
        function()
        {
            sendMessage();
        });

    // Handle "Enter" key press to send message
    $('#user-input').on('keydown',
        function(event)
        {
            if (event.key === "Enter")
            {
                event.preventDefault();
                // Prevent the default behavior of the "Enter" key
                sendMessage();
            }
        });

    // Function to send message
    function sendMessage()
    {
        var userInput = $('#user-input').val().trim();
        if (userInput !== '')
        {
            ws.send(userInput);
            // Create new user message element
            var userMessageDiv = $('<div></div>').addClass('message-box user-message')
                .text(userInput);

            // Append the message to the chat box
            $('.chat-box').append(userMessageDiv);

            // Clear the input field
            $('#user-input').val('');

            // Scroll the chat box to the bottom
            $('.chat-box').scrollTop($('.chat-box')[0].scrollHeight);

            // Hide the task buttons
            $('#task-buttons').remove();
        }
    }
});