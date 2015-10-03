<%@ include file="include.jsp" %>
<%@ include file="verification_login.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
</head>
<body>
<h2 align="center">Bem vindo!</h2>
<br/>
<%@ include file="menu.jsp" %>
<br/>
<center><h3>Olá ${user.username} || Online: ${online}</h3></center>
</body>
</html>