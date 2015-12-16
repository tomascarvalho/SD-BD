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
	<h1>Rewards:</h1>
	<c:choose>
		<c:when test="${RMIBean.projectRewards == null}">
			<p>No Rewards Found</p>
		</c:when>
		<c:otherwise>
				<c:forEach items="${RMIBean.projectRewards.get('Rewards')}"
					var="tokens">

					<p>Titulo:${tokens.get("TituloRecompensa") }</p>
					<p>Valor:${tokens.get("ValorRecompensa") }</p>

				</c:forEach>

		</c:otherwise>
	</c:choose>





</body>
</html>