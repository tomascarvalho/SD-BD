<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html>
	<head>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	</head>
	
	<body>
		<c:choose> 
			<c:when test = "${session.tumblr_logged == true}">
				<p> Processing...</p>
						
					<s:form id="cena" action="tumblrRedirect" method="post" style="visibility:hidden;">
						<input type = "hidden" id="token" name="token" value = "" />
						<button type = "submit" class="btn btn-block btn-social btn-tumblr">
						</button>
					</s:form>
				
					<script type="text/javascript">


					function gup( name, url ) {
					  if (!url) url = location.href;
					  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
					  var regexS = "[\\?&]"+name+"=([^&#]*)";
					  var regex = new RegExp( regexS );
					  var results = regex.exec( url );
					  return results == null ? null : results[1];
					}
					
					var tok = gup('oauth_verifier', null);
					
					console.log(tok);

					$(document).ready(function() {
						$("#token").val(tok);
					    $("#cena").submit();
					});</script>
			</c:when>
			<c:when test= "${tumblr_logged != true}">
				<p> ALGO SE PASSOU </p>
			</c:when>
		</c:choose>
	</body>
	

</html>



