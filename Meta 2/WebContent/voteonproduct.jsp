<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
		<c:when test="${Reward != null}">
			<h4>Ganhou um Recompensa:</h4>
			<p>${session.Reward}</p>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when
			test="${RMIBean.projectDetails.containsKey('Products') == true}">
			<h3>Produtos:</h3>
			<br>
			<s:form action="voteonproduct" method="post">
				<c:forEach items="${RMIBean.projectDetails.get('Products')}"
					var="tokens">

					<input type="radio" value="${tokens.get('DescProduct')}" name="productDescription"> ${tokens.get('DescProduct')}
				<br>

				</c:forEach>

				<s:submit value="Votar" />
			</s:form>
		</c:when>
	</c:choose>



	<h1>Fim da pagina</h1>

</body>
</html>