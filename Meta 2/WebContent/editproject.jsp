<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FundStarter</title>
</head>
<body>

<p>Modo Admin</p>

	<s:form action="listDetails" method="post">
		<s:submit value="Ver Detalhes" />
		<input type= "hidden" name="option" value="${newProjectID}">
		
	</s:form>

	<s:form action="newReward" method="post">
		<s:submit value="Adicionar Recompensa" />
		<input type= "hidden" name="option" value="${newProjectID}">
	</s:form>
	
	<s:form action="showRewards" method="post">
		<s:submit value="Remover Recompensa" />
		<input type= "hidden" name="option" value="${ProjectID}">
	</s:form>
	
	<s:form action="cancelarProj" method="post">
		<s:submit value="Cancelar Projecto" />
		<input type= "hidden" name="option" value="${newProjectID}">
	</s:form>

</body>
</html>