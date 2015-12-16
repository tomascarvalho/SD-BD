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
		<c:when test="${RMIBean.myMessages != null}">
			<c:forEach items="${RMIBean.myMessages}" var="messages">
				ID do Projecto:${messages.get("ProjectId")}<br>
				<c:forEach items="${messages.get('ProjectMessages')}" var="question">
					<input type="radio" value="${question.get('MessageID')}">${question.get("Message")}<br>
				</c:forEach>
				<br>
			</c:forEach>
		</c:when>
	</c:choose>


</body>
</html>