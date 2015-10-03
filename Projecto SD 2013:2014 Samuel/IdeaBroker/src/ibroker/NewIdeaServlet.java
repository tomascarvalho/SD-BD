package ibroker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.TopicBean;
import beans.UserBean;

/**
 * Servlet implementation class NewIdeaServlet
 */
@WebServlet("/NewIdeaServlet")
public class NewIdeaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewIdeaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int newIdeaCheck = 1;
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		ArrayList<TopicBean> topics = rmi.getTopic("SELECT * FROM topics");
	
		session.setAttribute("newIdeaCheck", newIdeaCheck);
		session.setAttribute("topics", topics);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/newidea.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		
		String name = request.getParameter("title");
		String description = request.getParameter("desc");
		String [] topics = request.getParameterValues("topics");
		String price = request.getParameter("price");
		UserBean user = (UserBean)session.getAttribute("user");
		int idIdea;
		
		if (topics == null) {
			MessageBean error = new MessageBean("Select at least on topic!");
			session.setAttribute("error", error);
			response.sendRedirect("/IdeaBroker/NewIdeaServlet");
		}
		else {
			System.out.println(name + " " + description + " " + Arrays.toString(topics) + " " + price);
			
			rmi.setData("INSERT INTO ideas (title, description, id_main, creator, num_shares, pos) VALUES (?, ?, 0, ?, 100000, 3)", name, description, user.getUsername());
			//get id da nova ideia
			idIdea = rmi.getInteger("SELECT id AS return_value FROM ideas WHERE title = ? AND creator = ?", name, user.getUsername());
			rmi.setData("INSERT INTO packs (id_user, id_idea, num_av, num_un, price) VALUES (?, ?, 100000, 0, ?)", user.getUsername(), idIdea, Integer.parseInt(price));
			//update relation
			for (int i = 0; i < topics.length; i++) {
				rmi.setData("INSERT INTO relation (id_idea, id_topic) VALUES (?,?)", idIdea, Integer.parseInt(topics[i]));
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/idea.jsp?id="+idIdea); // o id tem que ser o que ficou na BD
			dispatcher.forward(request, response);
		}
	}

}
