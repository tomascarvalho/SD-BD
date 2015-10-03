<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="facebook.js"></script>
<title>Profile</title>
</head>

<c:if test="${profileCheck==null || profileCheck!=1}">
	<c:redirect url="/ProfileServlet" />
</c:if>
<c:set var="profileCheck" scope="session" value="0" />

<body>
<h2 align="center">Profile</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>My Profile</h3>
<fb:login-button show-faces="true" width="200" max-rows="1"></fb:login-button><br/>
You have: ${saldo} DEICoins
<br/>
<br/>
<jsp:useBean id="error" class="ibroker.MessageBean" scope="session"/>
<jsp:getProperty name="error" property="message" />
<jsp:setProperty name="error" property="message" value="" />
<br/>
<h3>My Portfolio</h3>
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