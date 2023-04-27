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




// Get the button that opens the modal

$('#myBtn').click(function(){
    $("#myBtn").css("display","block");
});

function closeModal(){
    $('#message1').val("");
    $("#myModal").css("display","none");}

$('#closee').click(function(){
    $('#message1').val("");
    $("#myModal").css("display","none");
});

// When the user clicks on <span> (x), close the modal

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == $("#myModal")) {
        $('#message1').val("");
        $("#myModal").css("display","none");
    }
}

connect();

function showModal(ido){
    id=ido;
    $("#myModal").css("display","block");
}


function sendFirstMessage() {
    var currentUser = document.getElementById("currentUser").innerHTML;
    if(id != "" &&  currentUser != ""  && $('#message1').val() != "") {
        stompClient.send("/app/hello", {}, JSON.stringify({'content': $("#message1").val(), 'receiever': id, 'sender': currentUser}))
        $('#message1').val("");
        setTimeout(()=>{
            window.location.assign("http://localhost:8080/chat");
        },1000);

    }
}
function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/sellerbuyer', function (greeting) {

        });
    });


}