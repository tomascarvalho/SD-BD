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
</head>
<body>

	<h1>Projectos Actuais</h1>

	<h2>${connectoToRMIBean.getListProjects}</h2>


	<!-- Não posso user codigo java nos jsp's
	<h1>Projectos Actuais</h1>

	<h2>
		"<%/*try {
				fundstarter.model.ConnectToRMIBean aux = new fundstarter.model.ConnectToRMIBean();
			
				//Send result into generated HTML page with out.print!
				out.print(aux.getListProjects());
			} catch (Exception e) {
				e.printStackTrace();
			}*/%>

	</h2>		 -->
</body>
</html>