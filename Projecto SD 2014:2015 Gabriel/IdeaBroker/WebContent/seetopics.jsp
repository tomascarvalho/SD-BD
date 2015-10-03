<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>See Topics</title>
</head>

<c:if test="${seeTopicsCheck==null || seeTopicsCheck!=1}">
	<c:redirect url="/SeeTopicsServlet" />
</c:if>
<c:set var="seeTopicsCheck" scope="session" value="0" />

<body>
<h2 align="center">See Topics</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Topics</h3>
<table border="0" align="center" cellspacing="10">
<tr><td>Name of the Topic</td><td>Creator of the Topic</td><td>More info.</td></tr>
<c:forEach var="x" items="${topics}">
<tr><td>${x.name}</td><td>${x.author}</td><td><a href="/IdeaBroker/TopicServlet?id=${x.id}">More</a></td></tr>
</c:forEach>
</table>

</center>
</body>
</html>