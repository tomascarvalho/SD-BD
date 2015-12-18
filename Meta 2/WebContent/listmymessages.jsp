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

<link href="${pageContext.request.contextPath}/css/shop-homepage.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/alterar.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/mudancas.css"
	rel="stylesheet" type="text/css" />


</head>
<body>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>

			<a class="navbar-brand" href="mainmenu.jsp">FundStarter</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="#">A Minha Conta</a></li>
				<li><a href="#">Os Meus Projectos</a></li>
				<li><a href="#">Mensagens</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#campainha"><i class="fa fa-bell"></i></a></li>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">#Nome User <span class="caret"></span></a>
					<ul class="dropdown-menu">

						<li><s:form action="callAdd" method="post">
								<button class="list-group-item" type="submit"
									value="Criar Projecto">Criar Projecto</button>
							</s:form></li>
						<!-- Este botão só pode aparecer quando o user estiver logado -->
						<li><s:form action="CheckSaldo" method="post">
								<button class="list-group-item" type="submit"
									value="Consultar Saldo">Consultar Saldo</button>
							</s:form></li>
						<li><s:form action="AdminMode" method="post">
								<button class="list-group-item" type="submit"
									value="Meus Projectos" name="option" value="0">Meus
									Projectos</button>
							</s:form></li>
						<!-- Temos que mandar info de logout......... -->
						<li><a class="btn list-group-item" href="index.jsp">Log
								Out</a></li>
					</ul></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container --> </nav>

	<h1>Debug</h1>
	<h3>MENSAGENS PARA MIM: "${RMIBean.myMessages}"</h3>
	<h3>MENSAGENS RESPONDIDAS: "${RMIBean.myMessages1}"</h3>


	<c:choose>
		<c:when test="${RMIBean.myMessages != null}">
			<c:forEach items="${RMIBean.myMessages}" var="messages">
				ID do Projecto:${messages.get("ProjectId")}<br>
				<c:forEach items="${messages.get('ProjectMessages')}" var="question">
					<input type="radio" value="${question.get('MessageID')}"
						name="message" class="megaespecial">${question.get("Message")}<br>
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
										type="hidden" name="messageID" id="iddacena" value="">
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
		<c:when test="${RMIBean.myMessages1.size() != 0}">
			<h5>Entra aqui</h5>
			<c:forEach items="${RMIBean.myMessages1}" var="messages1">
				<h5>Entra aqui</h5>
				ID do Projecto:${messages1.get('ProjectID')}<br>
				Pergunta:${messages1.get('Pergunta')}
				Resposta:${messages1.get('Resposta')};  
				<br>
			</c:forEach>

		</c:when>

	</c:choose>

	<script>
		function func() {
			x = $(".megaespecial");

			for (i = 0; i < x.length; i++) {
				if (x[i].checked == true)
					document.getElementById("iddacena").value = x[i].value;
			}

		}
	</script>

</body>
</html>