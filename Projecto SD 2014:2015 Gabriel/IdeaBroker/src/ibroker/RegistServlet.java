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

/**
 * Servlet implementation class RegistServlet
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
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
		HttpSession session = request.getSession(true);
		DBAccess rmi;
		
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
		
		RequestDispatcher dispatcher;
		if(count == 0) {
			rmi.setData("INSERT INTO users (username,pass,bal) VALUES (?, ?, 1000000)", username, password);
			dispatcher = request.getRequestDispatcher("/login.jsp");
		} else { // user already exits
			MessageBean error = new MessageBean();
			error.setMessage("Username already in use!");
			session.setAttribute("error", error);
			dispatcher = request.getRequestDispatcher("/regist.jsp");
		}
		dispatcher.forward(request, response);
	}

}
