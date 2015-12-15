<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fundstarter</title>
</head>
<body>
	<c:choose>
		<c:when test="${MyProjects == null}">
			<h4>Não tem projectos</h4>
		</c:when>
		<c:otherwise>
			<s:form action="editProject" method="post">
			<c:forEach var = "i" begin = "0" end ="${tamMyProject}"> 
				<input type="radio" name="option" value="${MyProjectIDs[i]}"/> 
				${MyProjectIDs[i]}
				${MyProjects[i]}
				<br>
			</c:forEach>
			<s:submit value="Gerir Projecto" />
			</s:form>
		</c:otherwise>
	</c:choose>
	
</body>
</html>