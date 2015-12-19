<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="websockets.js"></script>
<title>FundStarter</title>
</head>
<body>
	
	<h1>Balance</h1>
	<h2>${RMIBean.balance}</h2>
		
	<p id="teste"></p>
	<input type="hidden" id="escondido" value="${RMIBean.userID}"/>
	
	<p id="newField"></p>
	
</body>
</html>