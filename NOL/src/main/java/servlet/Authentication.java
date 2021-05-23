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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Servlet implementation class Authentication
 */
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Authentication() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		String authHeader = req.getHeader("Authorization");
		

		Map<String, String> map = this.parseAuthorizationBasic(authHeader);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String url = "http://localhost:9090/CentroEducativo/login";

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(
				new StringEntity("{\"dni\":\"" + map.get("user") + "\",\"password\":\"" + map.get("pass") + "\"}"));

		// "dni": "23456733H",
		// "password": "123456"

		try {

			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
				if(req.isUserInRole("rolalu")) {

				String key = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(key);

				// Llamar a lista de asignaturas
				url = new StringBuilder("http://localhost:9090/CentroEducativo/alumnos/").append(map.get("user"))
						.append("/asignaturas?key=").append(key).toString();
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				List<Asignatura> asignaturas = gson.fromJson(reader, new TypeToken<List<Asignatura>>() {
				}.getType());
				
				List<String> acronimos = new ArrayList();
				for(int i = 0; i<asignaturas.size(); i++) {
					String acronimo = asignaturas.get(i).getAsignatura();
					acronimos.add(this.getNombreAsignatura(acronimo, key, httpClient));
					
				}
				request.getSession().setAttribute("acronimos", acronimos);
				request.getSession().setAttribute("asignaturas", asignaturas);
				request.getSession().setAttribute("nombreAlumno", this.getNombreUsuario(map.get("user"), key,httpClient));
				

				System.out.println(asignaturas);

				response.sendRedirect("/NOL/asignaturaAlumno.jsp");
				}
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		String url = "http://localhost:9090/CentroEducativo/alumnos/" + dni + "?key=" + key;
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

				Alumno alumno = gson.fromJson(reader, new TypeToken<Alumno>() {
				}.getType());

				return alumno.getNombre() + " " + alumno.getApellidos();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String getNombreAsignatura(String acronimo, String key, CloseableHttpClient httpClient) {
		String url = "http://localhost:9090/CentroEducativo/asignaturas/" + acronimo + "?key=" + key;
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

				DetallesAsignatura asig = gson.fromJson(reader, new TypeToken<DetallesAsignatura>() {
				}.getType());

				return asig.getNombre();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
