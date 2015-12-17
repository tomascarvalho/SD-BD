<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
	integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ=="
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link href="${pageContext.request.contextPath}/css/shop-homepage.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/alterar.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/mudancas.css"
	rel="stylesheet" type="text/css" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js">
	
</script>

<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"
	integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ=="
	crossorigin="anonymous"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>

</head>
<body>

	<c:choose>
		<c:when test="${RMIBean.myMessages != null}">
			<c:forEach items="${RMIBean.myMessages}" var="messages">
				ID do Projecto:${messages.get("ProjectId")}<br>
				<c:forEach items="${messages.get('ProjectMessages')}" var="question">
					<input type="radio" value="${question.get('MessageID')}"
						name="message" id="megaespecial" >${question.get("Message")}<br>
				</c:forEach>
				<br>
			</c:forEach>
			<button type="button" class="btn btn-warning" data-toggle="modal"
				data-target="#donateReward" onclick="func();">Responder</button>
			<div class="modal fade" id="donateReward" tabindex="-1" role="dialog"
				aria-labelledby="exampleModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="doanteReward">Responder</h4>
						</div>
						<div class="modal-body">
							<s:form class="input-group" action="replyMessage" method="post">
								<div class="input-group">
									<span class="input-group-addon yellow1 alinhar"
										id="basic-addon1">Resposta:</span> <input type="text"
										class="form-control" placeholder="Resposta"
										aria-describedby="basic-addon1" name="reply"> <input
										type="hidden" name="messageID" id="iddacena" value="" >
								</div>
								<br>

								<div class="row">
									<div class="col-md-0 col-md-offset-9 btn">
										<div class="btn-group" role="group">
											<input type="submit" value="Enviar">
										</div>
									</div>
								</div>
							</s:form>
						</div>
					</div>
				</div>
			</div>
		</c:when>
	</c:choose>
	
	<script>

	function func(){
		x = document.getElementById("megaespecial");
		if(x.length == null){
			if(x.checked == true){
				document.getElementById("iddacena").value = x.value;
			}
		}else{
			for (i = 0; i < x.length; i++) { 
				if(x[i].checked == "true") document.getElementById("iddacena").value = x[i].value;
			}
		}
		
	}
	
	</script>

</body>
</html>