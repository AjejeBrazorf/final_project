var stompClients = [];
var topics=["traffic","bike","bus"];
var sockets=[];

function connectToAllTopics() {
	console.log('try to connect: ');
	for(i=0;i<topics.length;i++){
		sockets[i] = new SockJS('/chat');
		stompClients[i] = Stomp.over(sockets[i]);
		stompClients[i].connect({}, function (frame) {
			console.log('Connected: ' + frame);
			if(stompClients[i]){
				stompClients[i].subscribe('/topic/'+topics[i], function (response) {
					console.log(response);
					showMessage(JSON.parse(response.body));
				});
			}
		});
	}
}

function disconnect() {
	for(i=0;i<topics.length;i++){
		if (stompClients[i] != null) {
			stompClients[i].disconnect();
		}
	}
	console.log("Disconnected");
}

function sendMessage(text) {
	for(i=0;i<topics.length;i++){
		var message= JSON.stringify({'text': text});
		if(stompClients[i].connected)
			stompClients[i].send("/app/"+topics[i], {},message);
	}
}