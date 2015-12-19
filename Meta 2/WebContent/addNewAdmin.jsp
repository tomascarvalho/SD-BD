<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="${pageContext.request.contextPath}/css/shop-homepage.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/alterar.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/mudancas.css" rel="stylesheet" type="text/css"/>
<title>FundStarter</title>
<style>
	h2{
		padding-top: 50px;
		padding-left: 30px;
	}
</style>


	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
<c:choose>
		<c:when test="${session.logged !=true}">
			<s:form id ="cena" action='redirectLogIn' method='post'> 
				
				<button type = "submit" class="btn btn-block btn-social btn-tumblr">
							</button>
			</s:form>
			<script>$(document).ready(function() {
					    $("#cena").submit();
					});</script>
		</c:when>
</c:choose>

	<h2>Novo Admin associado!</h2>
</body>
</html>