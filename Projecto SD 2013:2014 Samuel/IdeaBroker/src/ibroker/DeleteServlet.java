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
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
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
		String id = request.getParameter("id");
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		MessageBean error = new MessageBean("Idea not deleted!");
		int delete = 0;
		delete = rmi.getInteger("SELECT num_av AS return_value FROM packs WHERE id_user = ? AND id_idea = ?", user.getUsername(), Integer.parseInt(id));
		
		if(delete == 100000) {
			rmi.setData("DELETE FROM relation WHERE id_idea = ?", Integer.parseInt(id));
			rmi.setData("DELETE FROM packs WHERE id_idea = ? ", Integer.parseInt(id));
			rmi.setData("DELETE FROM history WHERE id_idea = ? ", Integer.parseInt(id));
			rmi.setData("DELETE FROM watchlist WHERE id_idea = ? ", Integer.parseInt(id));
			rmi.setData("DELETE FROM ideas WHERE id = ?", Integer.parseInt(id));
			error.setMessage("Idea deleted successfuly!");
		}
		
		session.setAttribute("error", error);
		response.sendRedirect("/IdeaBroker/ProfileServlet");
	}

}
