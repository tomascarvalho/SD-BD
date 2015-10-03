package ibroker;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.HistoricBean;
import beans.UserBean;

/**
 * Servlet implementation class HistoricServlet
 */
@WebServlet("/HistoricServlet")
public class HistoricServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoricServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int historicCheck = 1;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		// ir buscar as informaçoes do historico à base de dados
		ArrayList<HistoricBean> historic = rmi.getHistoric("SELECT h.*, a.title FROM history h, (SELECT ideas.title AS title, history.id AS id FROM ideas, history WHERE ideas.id = history.id_idea)a WHERE (user1 = ? OR user2 = ?) AND h.id = a.id", user.getUsername(), user.getUsername());
		
		session.setAttribute("historicCheck", historicCheck);
		session.setAttribute("historic", historic);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/historic.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
