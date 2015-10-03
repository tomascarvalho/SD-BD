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
import beans.TopicBean;

/**
 * Servlet implementation class TopicServlet
 */
@WebServlet("/TopicServlet")
public class TopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		HttpSession session = request.getSession(true);
		DBAccess rmi = (DBAccess) session.getAttribute("rmi");
		TopicBean topic = rmi.getTopic("SELECT * FROM topics WHERE id = ?", Integer.parseInt(id)).get(0);
				
		int topicCheck = 1;
		int idIdea;
		int lastPrice = 0;
		// Ir buscar as informaçoes do topico e todas as ideias daquele topico à base de dados
		
		ArrayList<IdeaBean> ideas = rmi.getIdea("SELECT * FROM ideas WHERE id IN (SELECT id_idea FROM relation WHERE id_topic=?)", Integer.parseInt(id));
		IdeaBean aux;
		for (int i = 0; i < ideas.size(); i++) {
			aux = ideas.get(i);
			idIdea = aux.getId();
			lastPrice = rmi.getInteger("SELECT price AS return_value FROM history WHERE id_idea = ? ORDER BY id DESC LIMIT 1", idIdea);
			ideas.get(i).setLastPrice(lastPrice);
		}
		
		session.setAttribute("topicCheck", topicCheck);
		session.setAttribute("topic", topic);
		session.setAttribute("ideas", ideas);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/topic.jsp?id="+id);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
