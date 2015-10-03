<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Topic</title>
</head>
<body>
<h2 align="center">New Topic</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center>
<br/>
<h3>Create a new Topic!</h3>
<form action="/IdeaBroker/NewTopicServlet" method="POST" >
<table border="0" align="center">
<tr><td>Title of the new topic:</td><td><input type="text" name="title" style="width:200px;"/></td></tr>
<tr><td/></td><td><input type="submit" value="Submit"></td></tr>
</table>
</form>
</center>
</body>
</html>