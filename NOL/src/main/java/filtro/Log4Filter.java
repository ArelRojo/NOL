package filtro;

import java.io.FileWriter;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

/**
 * Servlet Filter implementation class Log4Filter
 */
public class Log4Filter implements Filter {

	FilterConfig config;
    /**
     * Default constructor. 
     */
    public Log4Filter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	
	Date req_date = new Date();
	String ip_address = req.getRemoteAddr();
	String method = req.getMethod();
	String uri = req.getRequestURI();
	
	String strlog = new String(req_date + " " + ip_address +" "+ method + " " + uri);
	String fileName = config.getInitParameter("logFile");
	
	PrintWriter log = new PrintWriter( new FileWriter(fileName, true));
	
	log.println(strlog);

	chain.doFilter(request, response);
	
	log.close();	 
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.config = fConfig;
	}

}
