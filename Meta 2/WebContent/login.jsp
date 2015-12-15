<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="../../favicon.ico"/>
	<title>FundStarter LogIn</title>
	
    <!-- Bootstrap Core CSS -->
    <link href="https://student.dei.uc.pt/~tmdcc/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="https://student.dei.uc.pt/~tmdcc/css/shop-homepage.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="https://student.dei.uc.pt/~tmdcc/css/signin.css" rel="stylesheet">

    <script src="https://student.dei.uc.pt/~tmdcc/js/ie-emulation-modes-warning.js"></script>
</head>
<body>

	<nav class="navbar navbar-inverse navbar-fixed-top mudar" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp">FundStarter</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="login.jsp">Log In</a>
                    </li>
                    <li>
                        <a href="signin.jsp">Sign In</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

	<div class="container">
		<form class="form-signin" action="logIn" method="post">
			<h2 class="form-signin-heading">Please log in</h2>
			<label for="inputEmail" class="sr-only">Username</label>
       		<input name = "username" id="inputEmail" class="form-control" placeholder="Username" required autofocus>	
			<label for="inputPassword" class="sr-only">Password</label>
        	<input name = "password" type="password" id="inputPassword" class="form-control" placeholder="Password" required>	
			<br>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button> 
		</form>
	</div>
</body>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="https://student.dei.uc.pt/~tmdcc/js/ie10-viewport-bug-workaround.js"></script>
</html>