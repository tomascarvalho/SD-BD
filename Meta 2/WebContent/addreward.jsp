<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>

	<link href="${pageContext.request.contextPath}/css/shop-homepage.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/alterar.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/css/mudancas.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="websockets.js"></script>
<style>
	h1 {
		padding-left:50px;
	}
	
	p.one {
		padding-left:50px;
	    padding-bottom:30px;
	    padding-top:30px;
	    
	}

</style>
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
                <ul class="nav navbar-nav">
                    <li>
                        <a href="#">A Minha Conta</a>
                    </li>
                    <li>
                        <a href="#">Os Meus Projectos</a>
                    </li>
                    <li>
                        <a href="#">Mensagens</a>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
					<li><a href="#campainha"><i class="fa fa-bell"></i></a></li>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"> ${Username}'s Actions <span class="caret"></span></a>
						<ul class="dropdown-menu">
							
							<li><s:form action="callAdd" method="post">
								<button class="list-group-item" type="submit" value="Criar Projecto">Criar Projecto</button>
							</s:form></li>
								<!-- Este botão só pode aparecer quando o user estiver logado -->
							<li><s:form action="CheckSaldo" method="post">
								<button class="list-group-item" type="submit" value="Consultar Saldo">Consultar Saldo </button>
							</s:form></li>
							<li><s:form action="AdminMode" method="post">
								<button class="list-group-item" type="submit" value="Meus Projectos" name="option" value="0">Meus Projectos</button>
							</s:form></li>
							
							<li><s:form action="seeInbox" method="post">
								<button class="list-group-item" type="submit" value="Caixa de Correio">Caixa de Correio </button>
							</s:form></li>
							
							<!-- Temos que mandar info de logout......... -->
							<li><a class="btn list-group-item" href="index.jsp">Log Out</a></li>
						</ul>
					</li>
				</ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>
	
	<h1>Inserir dados da Recompensa:</h1>
	
<s:form action="createReward" method="post">
	
		Valor:
		<input type="text" name="valor">
		<br>
		<h1>O que vai ser inserido:</h1>
		
		<input type="hidden" name="option" value="${session.newProjectID}" >
		
		Recompensa:
		<input type="text" name="titulo">
		<br>
		<s:submit value="Adicionar" />
	</s:form>




</body>
</html>