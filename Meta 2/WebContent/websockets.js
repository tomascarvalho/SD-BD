var websocket = null;

window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
	connect('ws://' + window.location.host + '/websocket/ws');
	document.getElementById("chat").focus();
}

function connect(host) { // connect to the host websocket
	if ('WebSocket' in window)
		websocket = new WebSocket(host);
	else if ('MozWebSocket' in window)
		websocket = new MozWebSocket(host);
	else {
		writeToHistory('Get a real browser which supports WebSocket.');
		return;
	}

	websocket.onopen = onOpen; // set the event listeners below
	websocket.onclose = onClose;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;
}

function onOpen(event) {
	
}

function onClose(event) {
	
}

function onMessage(message) { // print the received message
	
}

function onError(event) {
	
}

function doSend() {
	
}

