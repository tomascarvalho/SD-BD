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
	
	<h1>Cancelar Projecto</h1>
	<h2>Os meus projectos:</h2>
	
	<s:form action="CancelarProj" method="post">
	<%--
	<c:choose>
		 
		<c:when test="${RMIBean.projects == null}">
			<p>No Projects to present</p>
		</c:when>
		<c:otherwise>
		--%>
			<s:form action="listDetails" method="post">
				<c:forEach items="${RMIBean.projects }" var="value">

				<input type="checkbox" name="selectedProject" method="post" value="${value.get('ID') }">${value.get("Titulo") }
				<br>
				</c:forEach>
				<s:submit value="Cancelar Projecto"/>
			
			</s:form>
			<%--
		</c:otherwise>
		</c:choose>
		--%>
	</s:form>
	
</body>
</html>