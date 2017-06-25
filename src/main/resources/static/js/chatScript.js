var stompClient = null;

function connectToTopic(topic) {
	console.log('try to connect: ');
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        $("#spinner").hide();
        stompClient.subscribe('/topic/'+topic, function (response) {
        	console.log(response);
        	showMessage(JSON.parse(response.body));
            $("#spinner").hide();
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage(text) {
	var topic;
    $("#spinner").show();
	if(window.location.pathname=="/cartopic") topic="traffic";
	sendMessageWithTopic(text,topic);
    
}

function sendMessageWithTopic(text, topic) {
	var message= JSON.stringify({'text': text});
    stompClient.send("/app/"+topic, {},message);
}

function showTime(date) {
	var d = new Date(date),
	minutes = d.getMinutes().toString().length == 1 ? '0'+d.getMinutes() : d.getMinutes(),
	hours = d.getHours().toString().length == 1 ? '0'+d.getHours() : d.getHours();
	return hours+':'+minutes;
}

function readMessagesFromServer(messages){
    $("#spinner").show();
	for(var i=0; i< messages.length; i++){
		messages[i].user=messages[i].nickname;
		showMessage(messages[i]);
	}
    $("#spinner").hide();
}

function showMessage(response) {
	var header="";
	var imageSpan="";
	var classUser="";
	
	if(response.user==$("#user").text())
		classUser="right";
				
	var image="http://placehold.it/50/55C1E7/fff&text="+response.user[0].toUpperCase();
	if(response.image!=null){
		image= response.image;
	}
	$("#scrollDown").remove();
	
	$("#chat-messages").append("" +
	    "<div class='message "+classUser+"'>"+
			"<img src='"+ image +"'"+
				"alt='User Avatar' class='img-circle' />"+
			'<div class="bubble" style="background-color: rgba(0, 0, 0, 0.3);"> '+response.message+
				"<span>"+showTime(response.date)+"</span>"+
			"</div>"+
		"</div>"+
		"<div id='scrollDown' style='height:75px'>"
	    );
	
    	$('#chat-messages').animate({
            scrollTop: $("#scrollDown").offset().top
        }, 500);

}



$(function () {
/*	$("form").on('submit', function (e) {
        e.preventDefault();
    });*/
//    $( "#btn-chat" ).click(function() { sendMessage($("#topic").text()); });
});

$(document).bind('keydown',function(e){
    if ($(':focus:not("input")').length){
        $('#btn-input').focus();
    }
});

$(window).bind('beforeunload', function(){
	disconnect();
	});



$(document).ready(function(){
    
    $("#spinner").show();

    
    $("#sendmessage input").focus(function(){
        if($(this).val() == "Send message..."){
            $(this).val("");
        }
    });
    $("#sendmessage input").focusout(function(){
        if($(this).val() == ""){
            $(this).val("Send message...");
            
        }
    });
                 
});


function setSelectionRange(input, selectionStart, selectionEnd) {
	  if (input.setSelectionRange) {
	    input.focus();
	    input.setSelectionRange(selectionStart, selectionEnd);
	  } else if (input.createTextRange) {
	    var range = input.createTextRange();
	    range.collapse(true);
	    range.moveEnd('character', selectionEnd);
	    range.moveStart('character', selectionStart);
	    range.select();
	  }
	}

	function setCaretToPos(input, pos) {
	  setSelectionRange(input, pos, pos);
	}