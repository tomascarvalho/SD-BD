<c:if test="${online==null || online==false}">
	<c:redirect url="/login.jsp" />
</c:if>

<c:if test="${user.getUsername()==null || user.getUsername()==''}">
	<c:redirect url="/login.jsp" />
</c:if>