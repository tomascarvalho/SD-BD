<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transaction History</title>
</head>

<c:if test="${historicCheck==null || historicCheck!=1}">
	<c:redirect url="/HistoricServlet" />
</c:if>
<c:set var="historicCheck" scope="session" value="0" />

<body>
<h2 align="center">Transaction History</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Transaction History</h3>
<br/>
<table border="0" align="center" cellspacing="10" >
<tr><td>Buyer</td><td>Seller</td><td>Idea's Title</td><td>Shares Bought</td><td>Price Per Share</td><td>Total</td></tr>
<c:forEach var="x" items="${historic}">
<tr><td>${x.buyer}</td><td>${x.seller}</td><td>${x.title}</td><td>${x.shares}</td><td>${x.price}</td><td>${x.shares * x.price}</td></tr>
</c:forEach>
</table>
</center>
</body>
</html>