<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="websockets.js"></script>
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
	<h1>Rewards:</h1>
	<c:choose>
		<c:when test="${RMIBean.projectRewards == null}">
			<p>No Rewards Found</p>
		</c:when>
		<c:otherwise>

			<c:forEach items="${RMIBean.projectRewards}" var="tokens">
				<h4>${tokens}</h4>
			</c:forEach>
			
			<form action="deleteReward" method="post">
				ID Reward:<input type="text" name="rewardID"/>
				<s:submit value="Eleminar" />
			</form>
		</c:otherwise>
	</c:choose>





</body>
</html>