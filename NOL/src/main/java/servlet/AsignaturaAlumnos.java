package servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import modelo.Alumno;
import modelo.AlumnoNota;
import modelo.Asignatura;
import modelo.Profesor;

/**
 * Servlet implementation class AsignaturaAlumnos
 */
public class AsignaturaAlumnos extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AsignaturaAlumnos() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key ="";
		final String RUTA = getServletConfig().getInitParameter("ruta");

		String authHeader = (String)request.getSession().getAttribute("authHeader");
		

		Map<String, String> map = this.parseAuthorizationBasic(authHeader);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String url = RUTA + "/login";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(
				new StringEntity("{\"dni\":\"" + map.get("user") + "\",\"password\":\"" + map.get("pass") + "\"}"));
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				key = EntityUtils.toString(httpResponse.getEntity());
			
			// TODO Auto-generated method stub

			String acronimo = (String) request.getParameter("acronimo");
			url = RUTA + "/asignaturas/" + acronimo + "/alumnos?key=" + key;

			HttpGet httpGet = new HttpGet(url);

			httpResponse = httpClient.execute(httpGet);
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();

			Gson gson = builder.create();
			JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));
			List<AlumnoNota> listMap = new ArrayList<AlumnoNota>();
			listMap= gson.fromJson(reader, new TypeToken<List<AlumnoNota>>() {
			}.getType());
			
			List<Alumno> alumnos = new ArrayList<Alumno>();
			
			for(AlumnoNota alumnoNota : listMap) {
				alumnos.add(this.getAlumno(alumnoNota.getAlumno(), key, httpClient));
			}
			
			Map<String,List<Asignatura>> asignaturasDeAlumno = new HashMap<String, List<Asignatura>>();
			
			for(Alumno alumno : alumnos) {
				asignaturasDeAlumno.put(alumno.getDni(), this.getAsignaturaDeAlumno(alumno.getDni(), key, httpClient));
			}
			
			
			request.getSession().setAttribute("mapAsignaturas", asignaturasDeAlumno);
			request.getSession().setAttribute("detalledeAlumno", alumnos);
			
			request.getSession().setAttribute("acronimo", acronimo);
			request.getSession().setAttribute("key", key);
			for(AlumnoNota alumnoNota : listMap) {
				alumnoNota.setAlumno( getNombreUsuario(alumnoNota.getAlumno(),key,httpClient));
			}
			
			request.getSession().setAttribute("alumnonota", listMap);
			response.setHeader("Allow", "OPTIONS, GET, HEAD, POST, PUT, DELETE");
		response.sendRedirect("/NOL/alumnos.jsp");
	
			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String key = "";
		String pathInfo = req.getPathInfo();
		if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
		String[] partes = pathInfo.split("/");
		String nota = partes[0];
		String dni = partes[1];
		String acronimo = partes[2];
 		
		HttpServletRequest request = (HttpServletRequest) req;
		String authHeader = (String)req.getSession().getAttribute("authHeader");
		
		Map<String, String> map = this.parseAuthorizationBasic(authHeader);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String url = RUTA + "/login";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(
				new StringEntity("{\"dni\":\"" + map.get("user") + "\",\"password\":\"" + map.get("pass") + "\"}"));
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				key = EntityUtils.toString(httpResponse.getEntity());
				
				String url_put = RUTA + "/alumnos/" +dni+ "/asignaturas/" +acronimo+ "?key=" +key;
				
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
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA + "/alumnos/" + dni + "?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				Alumno alumno= gson.fromJson(reader, new TypeToken<Alumno>() {
				}.getType());

				return alumno.getNombre() + " " + alumno.getApellidos();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private Alumno getAlumno(String dni, String key,CloseableHttpClient httpClient) {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA + "/alumnos/" + dni + "?key=" + key;
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

				Alumno alumno= gson.fromJson(reader, new TypeToken<Alumno>() {
				}.getType());

				return alumno;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private List<Asignatura> getAsignaturaDeAlumno(String dni, String key, CloseableHttpClient httpClient) {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url= RUTA + "/alumnos/" + dni + "/asignaturas" + "?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				List<Asignatura> asignaturas= gson.fromJson(reader, new TypeToken<List<Asignatura>>() {
				}.getType());

				return asignaturas;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
		
	}

}
