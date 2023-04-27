var stompClient = null;
var id= "";
var currentId ="";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}


function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/sellerbuyer', function (greeting) {
            getMessages(id);
        });
    });
}

$('#message').keydown(function(e){
    if(e.which==13){
        sendMessage();
    }
});

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    if(id != "") {
        stompClient.send("/app/hello", {}, JSON.stringify({'content': $("#message").val(), 'receiever': id, 'sender': currentId}))
        $('#message').val("");
        getMessages(id);
    }
}


function sendFirstMessage() {
    if(id != "") {
        stompClient.send("/app/hello", {}, JSON.stringify({'content': $("#message1").val(), 'receiever': id, 'sender': currentId}))
        $('#message1').val("");
        window.location="localhost:8080/chat";
    }
}

function getMessages(ido){
    $('#chat').empty()
    id= ido;
    $.ajax({
        url:`http://localhost:8080/chat/messages/${ido}`,
        type: 'GET',
        dataType: 'json', // added data type
        success: function(res) {
            $("#chat").html("");
            currentId= res.currentUserId;
            var html="";
            for(let i=0;i<res.chatMessagesList.length;i++) {

                if (res.chatMessagesList[i].sender.email == res.currentUserEmail) {
                    document.getElementById('chatter').innerHTML= `<p>${res.chatMessagesList[i].sender.firstName}</p>`
                    html += `<li class="you">
                <div class="entete">
                    <span class="status green"></span>
                    <h2>${res.chatMessagesList[i].sender.firstName}</h2>
                    <h3>${res.chatMessagesList[i].createdAt}</h3>
                </div>
                <div class="triangle"></div>
                <div class="message">
                    ${res.chatMessagesList[i].content}
                </div>
                </li>`;
                } else {

                    document.getElementById('chatter').innerHTML= `<p>${res.chatMessagesList[i].receiever.firstName}</p>`
                    html += `<li class="me">
                <div class="entete">
                   <h2>${res.chatMessagesList[i].receiever.firstName}</h2>
                    <h3>${res.chatMessagesList[i].createdAt}</h3>
                    <span class="status blue"></span>
                </div>
                <div class="triangle"></div>
                <div class="message">
                   ${res.chatMessagesList[i].content}
                </div>
            </li>`
                }

            }

            $("#chat").append(html);
            var objDiv = document.getElementById("chat");
            objDiv.scrollTop = objDiv.scrollHeight;
            $("html, body").animate({
                scrollTop: $(
                    'html, body').get(0).scrollHeight
            }, 1000);

        }
    });
}



connect();