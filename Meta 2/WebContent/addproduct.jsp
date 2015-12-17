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
		<c:when test="${RMIBean.projectDetails.get('Products')!=null}">
			<c:forEach items="${RMIBean.projectDetails.get('Products')}"
				var="value">
				<h3>- ${value.get("DescProduct")}</h3>

			</c:forEach>
		</c:when>
		<c:otherwise>
			<h3>Este projecto não tem níveis extra</h3>
		</c:otherwise>
	</c:choose> 
	
	<h3>Novo Tipo de Produto</h3>
	<s:form action='addProduct' method='post'> 
		<input type="hidden" name="option" value="${session.RMIBean.newProjectID}">
		Descrição:
		<input type='text' name='productType'><br>
	</s:form>


</body>
</html>