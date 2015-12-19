<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fundstarter</title>

<link href="${pageContext.request.contextPath}/css/shop-homepage.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/alterar.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/mudancas.css" rel="stylesheet" type="text/css"/>
<style>
	p.one {
	    padding-right: 30px;
	    padding-left: 50px;
	}
	
	h1{
		padding-left: 20px;
		padding-bottom: 30px;
	}

</style>

</head>
<body>

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

<!-- <h2>${tamMyProject}</h2>  -->
	<h1>Os Meus Projectos:</h1>
	<c:choose>
		<c:when test="${tamMyProject == 0}">
		
			<h4>Não tem projectos</h4>

		</c:when>
		<c:otherwise>
					<s:form action="editProject" method="post">
			<c:forEach var = "i" begin = "0" end ="${tamMyProject}"> 
				<p class = "one"><input type="radio" name="option" value="${MyProjectIDs[i]}"/> 
				<!-- ${MyProjectIDs[i]}  -->
				${MyProjects[i]} </p>
				<br>
			</c:forEach>
			<s:submit value="Gerir Projecto" />
			</s:form>
		</c:otherwise>
	</c:choose>



<!-- 	<c:choose>
		<c:when test="${tamMyProject>0}">
			<s:form action="editProject" method="post">
			<c:forEach var = "i" begin = "0" end ="${tamMyProject}"> 
				<input type="radio" name="option" value="${MyProjectIDs[i]}"/> 
				${MyProjectIDs[i]}
				${MyProjects[i]}
				<br>
			</c:forEach>
			<s:submit value="Gerir Projecto" />
			</s:form>
		</c:when>
		<c:otherwise>
			<h4>Não tem projectos</h4>
		</c:otherwise>
	</c:choose> -->
	
	<!--  
	<s:form action="seeInbox" method="post">
		<s:submit value="Caixa de Correio"/>
	</s:form>
	-->
	
</body>
</html>