package ibroker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;

/**
 * Servlet implementation class AddToWatchListServlet
 */
@WebServlet("/AddToWatchListServlet")
public class AddToWatchListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToWatchListServlet() {
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
		String id = (String) request.getParameter("id");
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		
		int id_idea = Integer.parseInt(id);
		
		rmi.setData("INSERT INTO watchlist (id_user, id_idea) VALUES (?, ?)", user.getId(), id_idea);
		
		response.sendRedirect("/IdeaBroker/IdeaServlet?id=" + id);
	}

}
