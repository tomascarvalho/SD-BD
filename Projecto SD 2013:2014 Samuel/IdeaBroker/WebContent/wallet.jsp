<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wallet</title>
</head>

<c:if test="${walletCheck==null || walletCheck!=1}">
	<c:redirect url="/WalletServlet" />
</c:if>
<c:set var="walletCheck" scope="session" value="0" />

<body>
<h2 align="center">Wallet</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>My Wallet</h3>
<br/>
<table border="0" align="center" cellspacing="10" >
<tr><td>Name of the Idea</td><td>Number of Shares</td><td>Last Price</td><td>Price Per Share</td><td>Idea's Page</td></tr>
<c:forEach var="x" items="${packs}">
<tr><td>${x.title}</td><td>${x.shares}</td><td id="idea${x.idea}lastprice">${x.lastPrice}</td><td>
<form action="/IdeaBroker/ChangePriceServlet" method="POST" >
<input type="text" value="${x.price}" name="newprice" style="width:30px;" />
<input type="hidden" value="${x.id}" name="id" />
<input type="submit" value="Update">
</form>
</td><td><a href="/IdeaBroker/IdeaServlet?id=${x.idea}">Page</a></td></tr>
</c:forEach>
</table>
</center>
</body>
</html>