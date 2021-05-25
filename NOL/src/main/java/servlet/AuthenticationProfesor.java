package servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import modelo.Alumno;
import modelo.Asignatura;
import modelo.DetallesAsignatura;
import modelo.Profesor;


/**
 * Servlet implementation class AuthenticationProfesor
 */
public class AuthenticationProfesor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticationProfesor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		HttpServletRequest req = (HttpServletRequest) request;
		String authHeader = req.getHeader("Authorization");
		request.getSession(true).setAttribute("authHeader", authHeader);
		
		Map<String, String> map = this.parseAuthorizationBasic(authHeader);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String url = RUTA+"/login";
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(
				new StringEntity("{\"dni\":\"" + map.get("user") + "\",\"password\":\"" + map.get("pass") + "\"}"));
		
		try {

			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				if(req.isUserInRole("rolpro")) {

				String key = EntityUtils.toString(httpResponse.getEntity());
				request.getSession().setAttribute("key", key);
				System.out.println(key);

				// Llamar a lista de asignaturas
				url = new StringBuilder(RUTA+"/profesores/").append(map.get("user"))
						.append("/asignaturas?key=").append(key).toString();
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				List<DetallesAsignatura> asignaturas = gson.fromJson(reader, new TypeToken<List<DetallesAsignatura>>() {
				}.getType());

				request.getSession().setAttribute("detallesAsignaturas", asignaturas);
				request.getSession().setAttribute("nombreProfesor", this.getNombreUsuario(map.get("user"), key,httpClient));
				

				System.out.println(asignaturas);

				response.sendRedirect("/NOL/profesores.jsp");}
				else {
					response.sendRedirect("/NOL/NoAutorizado.html");
				}

			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private Map<String, String> parseAuthorizationBasic(String authHeader) {
		String userpassEncoded = authHeader.substring(6);
		String data = StringUtils.newStringUtf8(Base64.decodeBase64(userpassEncoded));
		int sepIdx = data.indexOf(':');
		Map<String, String> map = new HashMap<String, String>();
		if (sepIdx > 0) {
			String username = data.substring(0, sepIdx);
			String password = data.substring(sepIdx + 1);
			map.put("user", username);
			map.put("pass", password);
			return map;
		} else {
			return null;
		}
	}
	
	private String getNombreUsuario(String dni, String key,CloseableHttpClient httpClient) {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA+"/profesores/" + dni + "?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

//				String json = EntityUtils.toString(httpResponse.getEntity());
//				System.out.println(json);

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				Profesor prof = gson.fromJson(reader, new TypeToken<Profesor>() {
				}.getType());

				return prof.getNombre() + " " + prof.getApellidos();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
