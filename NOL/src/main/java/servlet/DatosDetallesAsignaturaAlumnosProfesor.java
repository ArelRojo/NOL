package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import modelo.Alumno;
import modelo.AlumnoNota;
import modelo.DetallesAsignatura;

/**
 * Servlet implementation class DatosDetallesAsignaturaAlumnosProfesor
 */
public class DatosDetallesAsignaturaAlumnosProfesor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DatosDetallesAsignaturaAlumnosProfesor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String authHeader = (String) request.getSession().getAttribute("authHeader");

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

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();

				String acronimo = request.getParameter("acronimo");
				String dniAlumno = request.getParameter("dni");
				
				List<AlumnoNota> alumnos = this.getAlumnosAsignatura(acronimo, key, httpClient);
				String nota ="";
				for(AlumnoNota alumno : alumnos) {
					if(alumno.getAlumno().equals(dniAlumno)) {
						nota = alumno.getNota();
					}
				}
				
				DetallesAsignatura asignatura = this.getAsignatura(acronimo, key, httpClient);
				String nombre_alumno = this.getNombreUsuario(dniAlumno, key, httpClient);
				
				response.setContentType("application/json");
				
				JsonObject json = new JsonObject();
				JsonArray array = new JsonArray();
				JsonObject datos = new JsonObject();
				
				datos.add("nombreAlumno",JsonParser.parseString(gson.toJson(nombre_alumno)));
				datos.add("dniAlumno", JsonParser.parseString(gson.toJson(dniAlumno)));
				datos.add("acronimo", JsonParser.parseString(gson.toJson(acronimo)));
				datos.add("nota",JsonParser.parseString(gson.toJson(nota)));
				datos.add("curso",JsonParser.parseString(gson.toJson(asignatura.getCurso())));
				datos.add("cuatrimestre",JsonParser.parseString(gson.toJson(asignatura.getCuatrimestre())));
				datos.add("creditos",JsonParser.parseString(gson.toJson(asignatura.getCreditos())));
				
				array.add(datos);
				json.add("jsonArray", array);
				
				
				PrintWriter pw = response.getWriter(); 
		        pw.print(json.toString());
		        pw.close();
				
			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
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
	
	private List<AlumnoNota> getAlumnosAsignatura(String acronimo, String key, CloseableHttpClient httpClient){
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA + "/asignaturas/" + acronimo + "/alumnos?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				List<AlumnoNota> alumnos = gson.fromJson(reader, new TypeToken<List<AlumnoNota>>() {
				}.getType());
				
				return alumnos;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private DetallesAsignatura getAsignatura(String acronimo, String key, CloseableHttpClient httpClient) {
		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA + "/asignaturas/" + acronimo + "?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				DetallesAsignatura asig = gson.fromJson(reader, new TypeToken<DetallesAsignatura>() {
				}.getType());

				return asig;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String getNombreUsuario(String dni, String key,CloseableHttpClient httpClient) {

		final String RUTA = getServletConfig().getInitParameter("ruta");
		String url = RUTA+ "/alumnos/" + dni + "?key=" + key;
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

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

}
