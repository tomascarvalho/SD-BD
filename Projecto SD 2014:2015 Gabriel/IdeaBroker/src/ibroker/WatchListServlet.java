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

import beans.IdeaBean;
import beans.UserBean;

/**
 * Servlet implementation class WatchListServlet
 */
@WebServlet("/WatchListServlet")
public class WatchListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WatchListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int watchListCheck = 1;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		ArrayList<IdeaBean> ideas = rmi.getIdea("SELECT * FROM ideas WHERE id IN (SELECT id_idea FROM watchlist WHERE id_user = ?)", user.getId());
		
		int lastPrice, idIdea;
		IdeaBean aux;
		for (int i = 0; i < ideas.size(); i++) {
			aux = ideas.get(i);
			idIdea = aux.getId();
			lastPrice = rmi.getInteger("SELECT price AS return_value FROM history WHERE id_idea = ? ORDER BY id DESC LIMIT 1", idIdea);
			ideas.get(i).setLastPrice(lastPrice);
		}
		
		session.setAttribute("watchListCheck", watchListCheck);
		session.setAttribute("ideas", ideas);
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("/watchlist.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
