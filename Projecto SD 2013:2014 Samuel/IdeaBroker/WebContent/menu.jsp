<table cellspacing="10" border="0" align="center">
<tr>
<td><a href="/IdeaBroker/">Home</a></td>
<td><a href="/IdeaBroker/ProfileServlet">Profile</a></td>
<td><a href="/IdeaBroker/WalletServlet">Wallet</a></td>
<td><a href="/IdeaBroker/HistoricServlet">Historic</a></td>
<td><a href="/IdeaBroker/WatchListServlet">Watch List</a></td>
<td><a href="/IdeaBroker/newtopic.jsp">New Topic</a></td>
<td><a href="/IdeaBroker/SeeTopicsServlet">See Topics</a></td>
<td><a href="/IdeaBroker/NewIdeaServlet">New Idea</a></td>
<td><a href="/IdeaBroker/HallOfFameServlet">Hall Of Fame</a></td>
<td>
<form action="/IdeaBroker/SearchServlet" method="POST">
<input type="text" name="query" style="width:150px;"/>
<input type="submit" value="Search">
</form>
</td>
<td><a href="/IdeaBroker/LogoutServlet">Logout</a></td>
</tr>
</table>

<style type="text/css">
body {
	background : #9CEEFF url('fundo.png') repeat-x;
	font-family: arial, "lucida console", sans-serif;
	}
table {
	background-color:rgba(255,255,255,0.5);
	}
</style>

<script type="text/javascript">
var websocket;

window.onload = function() { // execute once the page loads
    initialize();
    document.getElementById("menu").focus();
}

function initialize() { // URI = ws://10.16.0.165:8080/chat/chat
    connect('ws://' + window.location.host + '/IdeaBroker/DataWebSocketServlet');
}

function connect(host) { // connect to the host websocket servlet
    if ('WebSocket' in window){
        websocket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket(host);
	} else {
        return;
    }

    websocket.onopen    = onOpen; // set the event listeners below
    websocket.onclose   = onClose;
    websocket.onmessage = onMessage;
    websocket.onerror   = onError;
}

function onOpen(event) {
}

function onClose(event) {
}

function onMessage(message) { // print the received message
	var got = message.data;
	var data = got.split("#");
	if(data[0]=="NOT") {
		alert(data[1]);
	} else {
		document.getElementById("idea"+data[1]+"lastprice").innerHTML = data[2];
	}
}

function onError(event) {
}

</script>