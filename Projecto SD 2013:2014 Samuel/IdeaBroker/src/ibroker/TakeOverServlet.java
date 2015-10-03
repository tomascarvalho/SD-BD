package ibroker;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.PackBean;
import beans.UserBean;

/**
 * Servlet implementation class TakeOverServlet
 */
@WebServlet("/TakeOverServlet")
public class TakeOverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeOverServlet() {
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
		UserBean user = (UserBean) session.getAttribute("user");
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		ArrayList<PackBean> packs = rmi.getPack("SELECT p.*, a.title FROM packs p, (SELECT ideas.title AS title, packs.id AS id FROM ideas, packs WHERE ideas.id = packs.id_idea)a WHERE id_idea = ? AND p.id = a.id ORDER by price", Integer.parseInt(id));
		
		int lastPrice, idIdea = Integer.parseInt(id);
		
		lastPrice = rmi.getInteger("SELECT price AS return_value FROM history WHERE id_idea = ? ORDER BY id DESC LIMIT 1", idIdea);
		
		for (int i = 0; i < packs.size(); i++) {
			if (lastPrice != 0) packs.get(i).setLastPrice(lastPrice);
			BuyService.buy(packs.get(i), packs.get(i).getShares(), user, rmi);
		}
		
		rmi.setData("DELETE FROM watchlist WHERE id_idea = ?", idIdea);
		rmi.setData("UPDATE ideas SET pos = 5 WHERE id = ?", idIdea);
		
		response.sendRedirect("/IdeaBroker/HallOfFameServlet");
	}

}
