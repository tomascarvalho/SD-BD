<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>
</head>
<body>

	<c:choose>
		<c:when test="${RMIBean.storedProjects == 'actual' }">
			<h1>Projectos Actuais</h1>
		</c:when>
		<c:otherwise>
			<h1>Projectos Antigos</h1>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${RMIBean.projects == null}">
			<p>No Projects to present</p>
		</c:when>
		<c:otherwise>
			<c:forEach items="${RMIBean.projects }" var="value">

				<h3>Item: ${value.get("Titulo") }</h3>
				<br>
			</c:forEach>
		</c:otherwise>
	</c:choose>





</body>
</html>