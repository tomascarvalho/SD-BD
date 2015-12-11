<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FundStarter</title>
</head>
<body>

	<s:form action="addProject" method="post">
	
		Nome do Projecto:
		<input type="text" name="name">
		<br>
		Descrição:
		<input type="text" name="projDescription">
		<br>
		Valor Pretendido:
		<input type="text" name="goalValue">
		<br>
		Data Limite:
		<input type="text" name="limitDate">
		<br> 
		Tipo de Produto:
		<input type="text" name="productType">
		<br> 
		<s:submit value="Criar"/>
	</s:form>

</body>
</html>