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

			<c:choose>
				<c:when test="${RMIBean.projects == null}">
					<p>No Projects to present</p>
				</c:when>
				<c:otherwise>
					<c:when test="${aux==1}">
						<s:form action="listDetails" method="post">
							<c:forEach items="${RMIBean.projects }" var="value">
	
								<input type="checkbox" name="selectedProject" method="post"
									value="${value.get('ID') }">${value.get("Titulo") }
					<br>
							</c:forEach>
							<s:submit value="Show Details" />
						
						</s:form>

					</c:when>
					<c:otherwise>
					<s:form action="listRewards" method="post">
							<c:forEach items="${RMIBean.projects }" var="value">
								<h1>CHEGOU AQUI, SIM</h1>
								<input type="checkbox" name="selectedProject" method="post"
									value="${value.get('ID') }">${value.get("Titulo") }
							<br>
							</c:forEach>
							<s:submit value="Remover Rewards" />
						</s:form>
					</c:otherwise>

				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<h1>Projectos Antigos</h1>

			<c:forEach items="${RMIBean.projects }" var="value">
				<p>${value.get("Titulo") }</p>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</body>
</html>