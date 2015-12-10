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

	<p>Main Menu</p>

	<s:form action="listProj" method="post">
		<s:hidden name="option" value="0" />
		<s:submit value="Listar Projectos Actuais" />
	</s:form>

	<s:form action="listProj" method="post">
		<s:hidden name="option" value="1" />
		<s:submit value="Listar Projectos Antigos" />
	</s:form>
	
	<!-- Este botão só pode aparecer quando o user estiver logado -->
	<s:form action="CheckSaldo" method="post">
		<s:submit value="Consultar Saldo" />
	</s:form>

</body>
</html>