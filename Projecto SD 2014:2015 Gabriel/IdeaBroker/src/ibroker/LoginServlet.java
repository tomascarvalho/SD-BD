package ibroker;

import java.io.IOException;
import java.rmi.NotBoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;

/**
 * Servlet implementation class ServletLogin
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LoginServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		DBAccess rmi;
		Boolean online = false;
		HttpSession session = request.getSession(true);
		RequestDispatcher dispatcher;
		
		if ((rmi = (DBAccess)session.getAttribute("rmi")) == null) {
			try {
				rmi = new DBAccess();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.setAttribute("rmi", rmi);
		}
		
		int count = rmi.getInteger("SELECT COUNT(*) AS return_value FROM users WHERE username = ? AND pass = ?", username, password);
		
		if(count == 1) {
			UserBean user = rmi.getUser("SELECT * FROM users WHERE username = ?", username).get(0);
			session.setAttribute("user", user);
			online = true;
			dispatcher = request.getRequestDispatcher("/index.jsp");
		} else { // Login incorrecto
			MessageBean error = new MessageBean();
			error.setMessage("Username or password incorrect!");
			session.setAttribute("error", error);
			dispatcher = request.getRequestDispatcher("/login.jsp");
		}
		session.setAttribute("online", online);
		dispatcher.forward(request, response);
	}

}
