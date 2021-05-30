package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.tomcat.util.json.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import modelo.Alumno;
import modelo.Asignatura;
import modelo.DetallesAsignatura;

/**
 * Servlet implementation class ExpedienteAlumno
 */
public class DatosExpediente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatosExpediente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key ="";
		final String RUTA = getServletConfig().getInitParameter("ruta");
	String authHeader = (String)request.getSession().getAttribute("authHeader");
		String user = this.parseAuthorizationBasic(authHeader);
		Map<String, String> map = new HashMap<String,String>();
		map.put("93847525G", "123456");
		map.put("12345678W", "123456");
		map.put("23456387R", "123456");
		map.put("34567891F", "123456");
		map.put("37264096W", "123456");
		
		Map<String,String> usuarioAuth = new HashMap<String,String>();
		
		for(Map.Entry<String, String> entry : map.entrySet()) {
			if(entry.getKey().equals(user)) {
				usuarioAuth.put(entry.getKey(),entry.getValue());
			}
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = RUTA + "/login";
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		String dni = "";
		
		for(Map.Entry<String, String> entry : usuarioAuth.entrySet()) {
			dni = entry.getKey();
			httpPost.setEntity(new StringEntity("{\"dni\":\"" + entry.getKey() + "\",\"password\":\"" + entry.getValue() + "\"}"));
		}
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				key = EntityUtils.toString(httpResponse.getEntity());
				url = new StringBuilder(RUTA+"/alumnos/").append(dni)
						.append("/asignaturas?key=").append(key).toString();
				
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
				
				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();

				Gson gson = builder.create();
				JsonReader reader = new JsonReader(new StringReader(EntityUtils.toString(httpResponse.getEntity())));

				List<Asignatura> asignaturas = gson.fromJson(reader, new TypeToken<List<Asignatura>>() {
				}.getType());
				
				List<String> acronimos = new ArrayList<String>();
				List<String> nombreAsignatura = new ArrayList<String>();
				List<String> notas = new ArrayList<String>();
				for(int i = 0; i<asignaturas.size(); i++) {
					String acronimo = asignaturas.get(i).getAsignatura();
					String nota = asignaturas.get(i).getNota();
					nombreAsignatura.add(this.getNombreAsignatura(acronimo, key, httpClient));
					acronimos.add(acronimo);
					notas.add(nota);
				}
				
				response.setContentType("application/json");
				
				JsonObject json = new JsonObject();
				JsonArray array = new JsonArray();
				JsonObject datos = new JsonObject();
				
				datos.add("acronimos", JsonParser.parseString(gson.toJson(acronimos)));
				datos.add("nombreAsignatura", JsonParser.parseString(gson.toJson(nombreAsignatura)));
				datos.add("notas", JsonParser.parseString(gson.toJson(notas)));
				List<String> nombreAlumno = new ArrayList<String>();
				nombreAlumno.add(this.getNombreUsuario(dni, key,httpClient));
				datos.add("nombreAlumno", JsonParser.parseString(gson.toJson(nombreAlumno)));
				List<String> dniAlumno = new ArrayList<String>();
				dniAlumno.add(dni);
				datos.add("dni", JsonParser.parseString(gson.toJson(dniAlumno)));
				
				array.add(datos);
				json.add("jsonArray", array);
				
				PrintWriter pw = response.getWriter(); 
		        pw.print(json.toString());
		        pw.close();
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private String getNombreAsignatura(String acronimo, String key, CloseableHttpClient httpClient) {
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

				return asig.getNombre();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
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
