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
import beans.PackBean;
import beans.UserBean;

/**
 * Servlet implementation class IdeaServlet
 */
@WebServlet("/IdeaServlet")
public class IdeaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IdeaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		int ideaCheck = 1;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		// Ir buscar as informaçoes da udeia e todos os ideias daquela ideia à base de dados
		IdeaBean idea = (IdeaBean) rmi.getIdea("SELECT * FROM ideas WHERE id = ?", Integer.parseInt(id)).get(0);
		ArrayList<PackBean> packs = rmi.getPack("SELECT p.*, a.title FROM packs p, (SELECT ideas.title AS title, packs.id AS id FROM ideas, packs WHERE ideas.id = packs.id_idea)a WHERE id_idea = ? AND p.id = a.id",Integer.parseInt(id));
		int exists = 0;
		int delete = 0;
		int pos = 0;
		
		exists = rmi.getInteger("SELECT COUNT(*) AS return_value FROM watchlist WHERE id_idea = ? AND id_user = ?", Integer.parseInt(id), user.getId());
		delete = rmi.getInteger("SELECT num_av AS return_value FROM packs WHERE id_user = ? AND id_idea = ?", user.getUsername(), Integer.parseInt(id));	
		pos = rmi.getInteger("SELECT pos AS return_value FROM ideas WHERE id = ?", Integer.parseInt(id));
		if(delete == 100000) delete = 1;
		
		session.setAttribute("ideaCheck", ideaCheck);
		session.setAttribute("exists", exists);
		session.setAttribute("delete", delete);
		session.setAttribute("pos", pos);
		session.setAttribute("packs", packs);
		session.setAttribute("idea", idea);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/idea.jsp?id="+id);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
