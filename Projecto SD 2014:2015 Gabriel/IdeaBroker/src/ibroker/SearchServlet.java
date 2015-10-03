package ibroker;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.IdeaBean;
import beans.TopicBean;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
		String query = request.getParameter("query");
		System.out.println(query);
		/*se forem retornados topicos como resultado da pesquisa*/
		ArrayList<TopicBean> topics = new ArrayList<TopicBean>();
		/*adicionar os resultados da db a este ^ array list*/
		
		/*se forem retornadas ideias como resultado da pesquisa*/
		ArrayList<IdeaBean> ideas = new ArrayList<IdeaBean>();
		/*adicionar os resultados da db a este ^ array list*/
	}

}
