package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Log0
 */
public class Log0 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * Constructor de la clase
     */
    public Log0() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		HttpServletRequest myrequest = (HttpServletRequest) request;
		Date fecha = new Date();
		String datos_formulario = request.getParameter("nombre");
		String ip_address = myrequest.getRemoteAddr();
		String metodo = myrequest.getMethod();
		String uri = myrequest.getRequestURI();
	    
	    PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("Nombre introducido: " + datos_formulario);
		out.println("<br>");
		out.println("Fecha: " + fecha);
		out.println("<br>");
		out.println("Dirección IP: " + ip_address);
		out.println("<br>");
		out.println("Acceso: " + metodo);
		out.println("<br>");
		out.println("URI: " + uri);
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * peticiones POST (aquellas realizadas al rellenar un formulario que tenga METHOD=POST)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}
	

}
