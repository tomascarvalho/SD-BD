<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hall of Fame</title>
</head>

<c:if test="${hallOfFameCheck==null || hallOfFameCheck!=1}">
	<c:redirect url="/HallOfFameServlet" />
</c:if>
<c:set var="hallOfFameCheck" scope="session" value="0" />

<body>
<h2 align="center">Hall Of Fame</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Hall Of Fame!</h3>
<br/>
<table border="0" align="center" cellspacing="10" >
<tr><td>Name of Idea</td><td>Description of Idea</td><td>Idea's Author</td></tr>
<c:forEach var="x" items="${ideas}">
<tr><td>${x.name}</td><td>${x.description}</td><td>${x.author}</td></tr>
</c:forEach>
</table>
</center>
</body>
</html>