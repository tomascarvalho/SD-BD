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
<<<<<<< HEAD
=======

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


<script type="text/javascript" src="websockets.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script type="text/javascript" src="websockets.js"></script>
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
<!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle"  data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

                <a class="navbar-brand" href="mainmenu.jsp">FundStarter</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                
                <ul class="nav navbar-nav navbar-right">
					<li><a href="#campainha"><i class="fa fa-bell"></i></a></li>
					<li class="dropdown">

						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"> ${RMIBean.username}'s Actions <span class="caret"></span></a>

						<ul class="dropdown-menu">
							
							<li><s:form action="callAdd" method="post">
								<button class="list-group-item" type="submit" value="Criar Projecto">Criar Projecto</button>
							</s:form></li>
								<!-- Este botão só pode aparecer quando o user estiver logado -->
							<li><s:form action="CheckSaldo" method="post">
								<button class="list-group-item" type="submit" value="Consultar Saldo">Consultar Saldo </button>
							</s:form></li>
							<li><s:form action="showRewards" method="post">
								<input type = "hidden" name="flag" value="0"></input>
								<button class="list-group-item" type="submit" value="Listar Recompensas">Listar Recompensas </button>
							</s:form></li>
							<li><s:form action="AdminMode" method="post">
								<button class="list-group-item" type="submit" value="Meus Projectos" name="option" value="0">Meus Projectos</button>
							</s:form></li>
							
							<li><s:form action="seeInbox" method="post">
								<button class="list-group-item" type="submit" value="Caixa de Correio">Caixa de Correio </button>
							</s:form></li>
							
							<li><s:form action="logOut" method="post">
								<button class="list-group-item" type="submit" value="Log Out">Log Out </button>
							</s:form></li>
						</ul>
					</li>
				</ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

	<h1>${RMIBean.projectDetails.get("Titulo") }</h1>

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
				<c:when
					test="${RMIBean.projectDetails.containsKey('Levels') == true}">

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
				<c:when
					test="${RMIBean.projectDetails.containsKey('Products') == true}">

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

			<s:form action="pledge" method="post">
				<input type="text" name="amount" id="amt"/>
				<input type="hidden" name="projectID" id="projID"
					value=${session.listedProjectID}/>
				<!--<s:submit value="Pledge to Project"/>-->
				<button type="submit" onClick="sendPledge()">Pledge to Project</button>
			</s:form>


			<button type="button" class="btn btn-warning" data-toggle="modal"
				data-target="#enviarMensagem">Enviar Mensagem</button>
			<div class="modal fade" id="enviarMensagem" tabindex="-1"
				role="dialog" aria-labelledby="exampleModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="enviarMensagem">Enviar Mensagem</h4>
						</div>
						<div class="modal-body">
							<s:form class="input-group" action="sendMessage" method="post">
								<div class="input-group">
									<span class="input-group-addon yellow1 alinhar"
										id="basic-addon1">Mensagem:</span> <input type="text"
										class="form-control" placeholder="Mensagem"
										aria-describedby="basic-addon1" name="message"> <input
										type="hidden" name="projectID" id="IDproject"
										value=${session.listedProjectID}>
								</div>
								<br>

								<div class="row">
									<div class="col-md-0 col-md-offset-9 btn">
										<div class="btn-group" role="group">
											<button type="submit" onClick="sendMsgNot()">Enviar
												Mensagem</button>
										</div>
									</div>
								</div>
							</s:form>
						</div>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>