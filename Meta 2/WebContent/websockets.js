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
}

function onClose(event) {

}

function onMessage(message) { // print the received message
	alert(message.data);
}

function onError(event) {

}

function doSend() {
	if (document.getElementById("amt").value != null  & document.getElementById("projID").value != null) {
		var amount = document.getElementById("amt").value;
		var projectId = document.getElementById("projID").value;
	}
	if (amount != "") {
		var dataOfPledge = "" + amount + "/" + projectId;
		websocket.send(dataOfPledge);
	}

}