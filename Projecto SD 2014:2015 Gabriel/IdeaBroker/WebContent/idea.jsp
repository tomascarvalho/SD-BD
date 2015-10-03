<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Idea</title>
</head>

<c:if test="${param.id==null}">
	<c:redirect url="/SeeTopicsServlet" />
</c:if>
<c:if test="${ideaCheck==null || ideaCheck!=1}">
	<c:redirect url="/IdeaServlet?id=${param.id}" />
</c:if>
<c:set var="ideaCheck" scope="session" value="0" />

<body>
<h2 align="center">Idea</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Idea</h3>
<br/>
Title: ${idea.name}<br/>
Author: ${idea.author}<br/>
Description: ${idea.description}<br/>
<br/>
<jsp:useBean id="error" class="ibroker.MessageBean" scope="session"/>
<jsp:getProperty name="error" property="message" />
<jsp:setProperty name="error" property="message" value="" />
<br/>
<h3>Packs of Shares</h3>
<br/>
<table border="0" align="center" cellspacing="10" >
<tr><td>Owner</td><td>Number of Shares</td><td>Price Per Share</td></tr>
<c:forEach var="x" items="${packs}">
<tr><td>${x.owner}</td><td>${x.shares}</td><td>${x.price}</td></tr>
</c:forEach>
</table>
<br/>
<c:if test="${pos!=5}">
<form action="/IdeaBroker/BuyServlet" method="POST" >
<input type="text" name="numshares" style="width:100px;" placeholder="How many shares?"/>
<input type="hidden" name="id" value="${param.id}"/>
<input type="submit" value="Buy">
</form>
</c:if>
<c:if test="${delete==1}">
<form action="/IdeaBroker/DeleteServlet" method="POST" >
<input type="hidden" name="id" value="${param.id}"/>
<input type="submit" value="Delete Idea">
</form>
</c:if>
<c:if test="${exists==0}">
<form action="/IdeaBroker/AddToWatchListServlet" method="POST" >
<input type="hidden" name="id" value="${param.id}"/>
<input type="submit" value="Add to Watch List">
</form>
</c:if>
<c:if test="${user.getUsername()=='root' && pos!=5}">
<form action="/IdeaBroker/TakeOverServlet" method="POST" >
<input type="hidden" name="id" value="${param.id}"/>
<input type="submit" value="Take Over">
</form>
</c:if>
</center>
</body>
</html>