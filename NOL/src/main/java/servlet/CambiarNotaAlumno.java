package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Servlet implementation class CambiarNotaAlumno
 */
public class CambiarNotaAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarNotaAlumno() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String authHeader = (String) req.getSession().getAttribute("authHeader");

		String user = this.parseAuthorizationBasic(authHeader);

		Map<String, String> profesores = new HashMap<String, String>();
		profesores.put("23456733H", "123456");
		profesores.put("10293756L", "123456");
		profesores.put("06374291A", "123456");
		profesores.put("65748923M", "123456");

		Map<String, String> usuarioAuth = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : profesores.entrySet()) {
			if (entry.getKey().equals(user)) {
				usuarioAuth.put(entry.getKey(), entry.getValue());
			}
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = RUTA + "/login";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		String dni = "";
		for (Map.Entry<String, String> entry : usuarioAuth.entrySet()) {
			dni = entry.getKey();
			httpPost.setEntity(
					new StringEntity("{\"dni\":\"" + entry.getKey() + "\",\"password\":\"" + entry.getValue() + "\"}"));
		}
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String key = EntityUtils.toString(httpResponse.getEntity());
				
				String acronimo = req.getParameter("acronimo");
				String dniAlumno = req.getParameter("dni");
				String nota = req.getParameter("nota");
				
				String url_put = RUTA + "/alumnos/" +dniAlumno+ "/asignaturas/" +acronimo+ "?key=" +key;
				
				HttpPut httpPut = new HttpPut(url_put);

				httpPut.addHeader("Content-Type", "application/json");
				httpPut.setEntity(new StringEntity(nota));
				HttpResponse res = httpClient.execute(httpPut);
				if(res.getStatusLine().getStatusCode() == 200) {
					resp.sendRedirect("/NOL/alumnos.jsp");
				}else {
					resp.sendError(res.getStatusLine().getStatusCode());
				}
				
			}else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	private String parseAuthorizationBasic(String authHeader) {
		String userpassEncoded = authHeader.substring(6);
		String data = StringUtils.newStringUtf8(Base64.decodeBase64(userpassEncoded));
		int sepIdx = data.indexOf(':');
		if (sepIdx > 0) {
			String username = data.substring(0, sepIdx);
			return username;
		} else {
			return "";
		}
	}
}
