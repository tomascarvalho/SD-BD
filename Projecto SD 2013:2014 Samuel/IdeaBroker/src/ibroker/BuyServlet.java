package ibroker;

import ibroker.DataWebSocketServlet.DataInbound;

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
 * Servlet implementation class BuyServlet
 */
@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BuyServlet() {
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
		String num = request.getParameter("numshares");
		int numShares = Integer.parseInt(num);
		int buyed = 0;
		int done = 0;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		UserBean user = (UserBean) session.getAttribute("user");
		ArrayList<PackBean> packs = rmi.getPack("SELECT p.*, a.title FROM packs p, (SELECT ideas.title AS title, packs.id AS id FROM ideas, packs WHERE ideas.id = packs.id_idea)a WHERE id_idea = ? AND p.id = a.id ORDER by price", Integer.parseInt(id));
		PackBean aux;
		int toBuy = 0;
		int lastPrice = 0;
		MessageBean message;
		DataInbound ws = (DataInbound) session.getAttribute("ws");
		
		if (numShares > 100000) {
			message = new MessageBean("Can't buy more than 100000 shares!"); 
		}
		else {
			for (int i = 0; i < packs.size(); i++) {
				aux = packs.get(i);
				if (!aux.getOwner().equals(user.getUsername())){ //se o user nao for owner do pack
					if (numShares < aux.getShares()) {
						toBuy = numShares;
					} else {
						toBuy = aux.getShares();
					}
					buyed = BuyService.buy(aux, toBuy, user, rmi);
					done += buyed;
					numShares -= buyed;
					if (buyed!=0) {
						lastPrice = aux.getPrice();
						ws.broadcast("NOT#User " + user.getUsername() + " bought you " + buyed + " shares of the idea " + aux.getTitle() + ".", aux.getOwner());
					}
					if (buyed != toBuy){
						message = new MessageBean("It was not possible to buy all the requested shares. Not enough balance!");
						break;
					}
				}
			}
			ws.broadcast("UPD#" + id + "#" + lastPrice, "ALL");
		}
		
		response.sendRedirect("/IdeaBroker/IdeaServlet?id="+id);
	}

}
