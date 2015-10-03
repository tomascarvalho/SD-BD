<%@ include file="include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<style type="text/css">
body {
	background : #9CEEFF url('nuvem.png') no-repeat;
	background-position: center;
	font-family: arial, "lucida console", sans-serif;
	}
</style>
<body>  
<h2 align="center">Login</h2>
<br/>
<center>
<jsp:useBean id="error" class="ibroker.MessageBean" scope="session"/>
<jsp:getProperty name="error" property="message" />
<jsp:setProperty name="error" property="message" value="" />
<br/>
<br/>
<form action="/IdeaBroker/LoginServlet" method="POST" >
<input type="text" name="username" style="width:150px;" placeholder="Username" autofocus/><br/>
<input type="password" name="password" style="width:150px;" placeholder="Password"/><br/>
<input type="submit" value="Login"> || <a href="regist.jsp">Regist</a>
</form>
<form action="/IdeaBroker/FacebookServlet" method="GET">
<input type="submit" name="face" value="Login with Facebook">
</form>
</center>
</body>
</html>