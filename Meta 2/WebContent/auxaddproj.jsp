<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>
</head>
<body>

	<s:form action="newLevel" method="post">
		<input type="hidden" name="projectId" value="${session.RMIBean.newProjectID}" />
		<s:submit value="Adicionar Nível Extra" />
	</s:form>

	<s:form action="newReward" method="post">
		<input type="hidden" name="projectId" value="${session.RMIBean.newProjectID}" />
		<s:submit value="Adicionar Recompensa" />
	</s:form>
	
	<s:form action="newProduct" method="post">
		<input type="hidden" name="projectId" value="${session.RMIBean.newProjectID}" />
		<s:submit value="Adicionar Tipo de Producto" />
	</s:form>

</body>
</html>