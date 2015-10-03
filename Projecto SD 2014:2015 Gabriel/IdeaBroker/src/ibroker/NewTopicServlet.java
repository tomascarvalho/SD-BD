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
 * Servlet implementation class NewTopicServlet
 */
@WebServlet("/NewTopicServlet")
public class NewTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewTopicServlet() {
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
		String title = request.getParameter("title");
		HttpSession session = request.getSession(true);
		UserBean user = (UserBean)session.getAttribute("user");
		DBAccess rmi = (DBAccess)session.getAttribute("rmi");
		// Criar um topico na base de dados e reencaminhar para a nova pagina do topico
		
		int count = rmi.getInteger("SELECT COUNT(*) AS return_value FROM topics WHERE title = ?", title);
		
		if (count == 0) {
			rmi.setData("INSERT INTO topics (title, creator, num_ideas) VALUES (?, ?, 0)", title, user.getUsername());
		}
		
		int topicID = rmi.getInteger("SELECT id AS return_value FROM topics WHERE title = ?", title);
		
		response.sendRedirect("/IdeaBroker/TopicServlet?id="+topicID); // id tem que ser o que ficou na base de dados!!!!
	}

}
