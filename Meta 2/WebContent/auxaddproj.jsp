<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="websockets.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>
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

	<s:form action="newLevel" method="post">
		<input type="hidden" name="option" value="${session.RMIBean.newProjectID}" />
		<s:submit value="Adicionar Nível Extra" />
	</s:form>
<h1>${session.RMIBean.newProjectID}</h1>
	<s:form action="newReward" method="post">
		<input type="hidden" name="option" value="${session.newProjectID}" />
		<s:submit value="Adicionar Recompensa" />
	</s:form>
	
	<s:form action="newProduct" method="post">
		<input type="hidden" name="option" value="${session.RMIBean.newProjectID}" />
		<s:submit value="Adicionar Tipo de Producto" />
	</s:form>

</body>
</html>