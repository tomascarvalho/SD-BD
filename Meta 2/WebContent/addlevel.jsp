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

	<c:choose>
		<c:when test="${RMIBean.projectDetails.get('Levels')!=null}">
			<c:forEach items="${RMIBean.projectDetails.get('Levels')}"
				var="value">
				<h3>- ${value.get("DescNivel")}</h3>

			</c:forEach>
		</c:when>
		<c:otherwise>
			<h3>Este projecto não tem níveis extra</h3>
		</c:otherwise>
	</c:choose>
	
	<h3>Novo Nível</h3>
	<s:form action='addLevel' method='post'> 
		<input type="hidden" name="option" value="${session.RMIBean.newProjectID}">
		Descrição:
		<input type='text' name='levelDesc'><br>
		Valor:
		<input type='text' name='valueDesc'><br>
		<s:submit value='Adicionar Nível'/>
	</s:form>


</body>
</html>