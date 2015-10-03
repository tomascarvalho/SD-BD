<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Idea</title>
</head>

<c:if test="${newIdeaCheck==null || newIdeaCheck!=1}">
	<c:redirect url="/NewIdeaServlet" />
</c:if>
<c:set var="newIdeaCheck" scope="session" value="0" />

<body>
<h2 align="center">New Idea</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Create a new Idea!</h3>
<jsp:useBean id="error" class="ibroker.MessageBean" scope="session"/>
<jsp:getProperty name="error" property="message" />
<jsp:setProperty name="error" property="message" value="" />
<br/>
<br/>
<form action="/IdeaBroker/NewIdeaServlet" method="POST" >
<table border="0" cellspacing="10" align="center">
<tr><td>Title of the new idea:</td>
<td><input type="text" name="title" style="width:200px;"/></td></tr>
<tr><td>Description of the new idea:</td>
<td><textarea type="text" name="desc" rows="4" cols="50"></textarea></td></tr>
<tr><td valign="top">Choose the topics to relate with the new idea:</td>
<td>
<c:forEach var="x" items="${topics}">
<input type="checkbox" name="topics" value="${x.id}">${x.name}<br/>
</c:forEach>
</td></tr>
<tr><td colspan="2">The new idea will be divided in 10.000 shares</td></tr>
<tr><td>Price per share</td><td><input type="text" name="price" style="width:200px;"/></td></tr>
<tr><td/></td><td><input type="submit" value="Submit"></td></tr>
</table>
</form>
</center>
</body>
</html>