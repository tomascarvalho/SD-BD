<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WatchList</title>
</head>

<c:if test="${watchListCheck==null || watchListCheck!=1}">
	<c:redirect url="/WatchListServlet" />
</c:if>
<c:set var="watchListCheck" scope="session" value="0" />

<body>
<h2 align="center">WatchList</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>My Watch List!</h3>
<br/>
<table border="0" align="center" cellspacing="10" >
<tr><td>Name of Idea</td><td>Description of Idea</td><td>Idea's Author</td><td>Last Price</td><td>More info.</td></tr>
<c:forEach var="x" items="${ideas}">
<tr><td>${x.name}</td><td>${x.description}</td><td>${x.author}</td><td id="idea${x.id}lastprice">${x.lastPrice}</td><td><a href="/IdeaBroker/IdeaServlet?id=${x.id}">More</a></td></tr>
</c:forEach>
</table>
</center>
</body>
</html>