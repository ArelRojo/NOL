package servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Log1
 */
public class Log1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String FILE_NAME = "." + File.separator + "log1";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Log1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpServletRequest myrequest = (HttpServletRequest) request;
		response.setContentType("text/html");
		
		
		Date my_request_date = new Date();
		String datos_formulario = request.getParameter("nombre");
		String ip_address = myrequest.getRemoteAddr();
		String metodo = myrequest.getMethod();
		String uri = myrequest.getRequestURI();
		
		String my_log = new String(my_request_date + " " + datos_formulario + " " + ip_address + " " 
				+ uri + " acceso " + metodo);
		
		PrintWriter log = new PrintWriter( new FileWriter(FILE_NAME, true));
		
		 log.println(my_log);
		    
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("Nombre introducido: " + datos_formulario);
			out.println("<br>");
			out.println("Puede encontrar más información en el log1");
			out.println("</body>");
			out.println("</html>");
			
			log.close();
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}
