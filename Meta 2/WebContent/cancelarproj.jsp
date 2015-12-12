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
	<h3>Projecto Cancelado:</h3>
	<h2>${RMIBean.projectDetails.get("Titulo") }</h2>

<!-- 
	<c:choose>
		<c:when test="${RMIBean.projectDetails == null}">
			<p>No Projects Found</p>
		</c:when>
		<c:otherwise>
			<p>Descrição:${RMIBean.projectDetails.get("Descricao")}</p>
			<p>Valor
				Pretendido:${RMIBean.projectDetails.get("ValorPretendido")}</p>
			<p>Valor Actual:${RMIBean.projectDetails.get("ValorActual")}</p>
			<p>Data Limite:${RMIBean.projectDetails.get("DataLimite")}</p>

			<c:choose>
				<c:when
					test="${RMIBean.projectDetails.containsKey('Rewards') == true }">
					<h2>Recompensas:</h2>
					<c:forEach items="${RMIBean.projectDetails.get('Rewards')}"
						var="tokens">

						<p>Titulo:${tokens.get("TituloRecompensa") }</p>
						<p>Valor:${tokens.get("ValorRecompensa") }</p>

					</c:forEach>
				</c:when>
				<c:otherwise>
					<h2>Este projecto não tem recompensas</h2>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${RMIBean.projectDetails.containsKey('Levels') == true}">

					<h2>Níveis Extra:</h2>

					<c:forEach items="${RMIBean.projectDetails.get('Levels')}"
						var="tokens">

						<p>Descrição:${tokens.get("DescNivel") }</p>
						<p>Valor:${tokens.get("ValorNivel") }</p>

					</c:forEach>
				</c:when>
				<c:otherwise>
					<h2>Este projecto não tem níveis extra</h2>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${RMIBean.projectDetails.containsKey('Products') == true}">

					<h2>Produtos:</h2>

					<c:forEach items="${RMIBean.projectDetails.get('Products')}"
						var="tokens">

						<p>Descrição:${tokens.get("DescProduct") }</p>
						<p>Votos:${tokens.get("Contador") }</p>

					</c:forEach>
				</c:when>
				<c:otherwise>
					<h2>Este projecto não tem produtos</h2>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	
	 -->
</body>
</html>