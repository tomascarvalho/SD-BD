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
    <!-- Latest compiled and minified CSS -->
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link href="${pageContext.request.contextPath}/css/shop-homepage.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/alterar.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/mudancas.css" rel="stylesheet" type="text/css"/>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js">
    </script>

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

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
				<input type="text" name="amount">
				<input type="hidden" name="projectID"
					value=${session.listedProjectID}>
				<s:submit value="Pledge to Project" />
			</s:form>


			<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#enviarMensagem">Enviar Mensagem</button>
			<div class="modal fade" id="enviarMensagem" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="enviarMensagem">Enviar Mensagem</h4>
                    </div>
                    <div class="modal-body">
                        <s:form class = "input-group" action="sendMessage" method="post">
                            <div class="input-group">
                                <span class="input-group-addon yellow1 alinhar" id="basic-addon1">Mensagem:</span>
                                <input type="text" class="form-control" placeholder="Mensagem" aria-describedby="basic-addon1" name= "message">
                                <input type="hidden" name="projectID" value=${session.listedProjectID}>
                            </div>
                            <br>
                            
                            <div class="row">
                                <div class="col-md-0 col-md-offset-9 btn">
                                    <div class="btn-group" role="group">
                                        <input type="submit" value="Doar">
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