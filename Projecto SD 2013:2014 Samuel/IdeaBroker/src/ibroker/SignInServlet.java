package ibroker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

public class SignInServlet extends HttpServlet {
    private static final long serialVersionUID = -7453606094644144082L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("merda");
        Facebook facebook = new FacebookFactory().getInstance();
        
        facebook.setOAuthAppId("183939421805475", "d6187ee28326aad01b7b028d1414bd1f");
        facebook.setOAuthPermissions("email,publish_stream,read_stream");
        
        request.getSession().setAttribute("facebook", facebook);
        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index, callbackURL.length(), "").append("/IdeaBroker/CallBackServlet");
        response.sendRedirect("/IdeaBroker/index.jsp");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("merda");
        doGet(request,response);
    }
}
