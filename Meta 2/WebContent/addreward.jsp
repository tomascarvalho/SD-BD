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

	<s:form action="createReward" method="post">
	
		Valor:
		<input type="text" name="valor">
		<br>
		<h1>O que vai ser inserido:</h1>
		
		<input type="text" name="option" value="${session.newProjectID}" >
		
		Recompensa:
		<input type="text" name="titulo">
		<br>
		<s:submit value="Adicionar" />
	</s:form>


</body>
</html>