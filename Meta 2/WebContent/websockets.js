var websocket = null;

window.onload = function() {
	connect('ws://' + window.location.host + '/FundStarterWeb/ws');
}

function connect(host) {
	if ('WebSocket' in window)
		websocket = new WebSocket(host);
	else if ('MozWebSocket' in window)
		websocket = new MozWebSocket(host);
	else {
		alert('Get a real browser which supports WebSocket.');
		return;
	}

	websocket.onopen = onOpen;
	websocket.onclose = onClose;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;

	console.log("End of connection function");
}

function onOpen(event) {
	console.log("New WebSocket Connection");
	if(window.location.href == "http://localhost:8080/FundStarterWeb/pledge.action"){
		sendPledgeNot()
	}
}

function onClose(event) {

}

function onMessage(message) { // print the received message
	alert(message.data);
}

function onError(event) {

}

function sendPledgeNot() {

		var amount = document.getElementById("amt").value;
		var projID = document.getElementById("projID").value;
		
		var dataToSend = amount + "/" + projID;
		sendMessage(dataToSend);

}

function sendMsgNot() {

	var projectId = document.getElementById("IDproject").value;
	var dataToSend = "gotMessage/" + projectId;
	sendMessage(dataToSend);

}

function sendMessage(message){
	websocket.send(message);
}