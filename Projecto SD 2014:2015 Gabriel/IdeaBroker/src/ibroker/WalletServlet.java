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

import beans.PackBean;
import beans.UserBean;

/**
 * Servlet implementation class WalletServlet
 */
@WebServlet("/WalletServlet")
public class WalletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WalletServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int walletCheck = 1;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		ArrayList<PackBean> packs = rmi.getPack("SELECT p.*, a.title FROM packs p, (SELECT ideas.title AS title, packs.id AS id FROM ideas, packs WHERE ideas.id = packs.id_idea)a WHERE id_user = ? AND p.id = a.id", user.getUsername());
		
		int lastPrice, idIdea;
		PackBean aux;
		for (int i = 0; i < packs.size(); i++) {
			aux = packs.get(i);
			idIdea = aux.getIdea();
			lastPrice = rmi.getInteger("SELECT price AS return_value FROM history WHERE id_idea = ? ORDER BY id DESC LIMIT 1", idIdea);
			packs.get(i).setLastPrice(lastPrice);
		}
		
		session.setAttribute("walletCheck", walletCheck);
		session.setAttribute("packs", packs);		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/wallet.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
