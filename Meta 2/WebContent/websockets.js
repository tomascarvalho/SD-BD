var websocket = null;

window.onload = function() { // URI = ws://localhost:8080/FundStarterWeb/websockets
	connect('ws://' + window.location.host + '/FundStarterWeb/ws');
	document.getElementById("teste").focus();
}

function connect(host) { // connect to the host websocket
	if ('WebSocket' in window)
		websocket = new WebSocket(host);
	else if ('MozWebSocket' in window)
		websocket = new MozWebSocket(host);
	else {
		alert('Get a real browser which supports WebSocket.')
		return;
	}

	websocket.onopen = onOpen;
	websocket.onclose = onClose;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;
	
	console.log("Cheguei ao fim da função");
}

function onOpen(event) {
	document.getElementById("teste").innerHTML = "CARALHO";
}

function onClose(event) {
	
}

function onMessage(message) { 
	
}

function onError(event) {
	console.log("isto está a dar um erro e eu não percebo porque");
}

function doSend() {
	
}

