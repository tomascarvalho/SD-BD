<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>
</head>
<body>
	<s:form action="signIn" method="post">
		<s:text name="Username:"/>
		<s:textfield name="username"/><br>
		<s:text name="Password"/>
		<s:textfield name="password"/><br>
		<s:submit/>
	</s:form>
</body>
</html>