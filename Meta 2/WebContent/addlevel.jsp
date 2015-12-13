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

	
	
	<!-- imprime niveis existentes -->
	<c:forEach items="${RMIBean.projectDetails.get('Levels')}" var="value">
		<p>lalalalalalala</p>
		<p>${value.get("DescNivel")}</p>
		
	</c:forEach>
	<h1>É um pisso de todo o tamanha por não saberes que quando um valor é nulo não faz o for</h1>
	<!-- recebe dados -->
	
	<!-- volta a chamar a página -->
</body>
</html>