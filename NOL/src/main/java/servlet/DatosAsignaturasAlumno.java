package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import modelo.Alumno;
import modelo.Asignatura;
import modelo.DetallesAsignatura;

/**
 * Servlet implementation class DatosAsignaturasAlumno
 */
public class DatosAsignaturasAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatosAsignaturasAlumno() {
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
		
		String user = this.parseAuthorizationBasic(authHeader);
		
		Map<String, String> alumnos = new HashMap<String,String>();
		alumnos.put("93847525G", "123456"); 
		alumnos.put("12345678W", "123456"); 
		alumnos.put("23456387R", "123456");  
		alumnos.put("34567891F", "123456"); 
		alumnos.put("37264096W", "123456"); 
		
		Map<String,String> usuarioAuth = new HashMap<String,String>();
		
		for(Map.Entry<String, String> entry : alumnos.entrySet()) {
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
				
				String key = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(key);
				
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
				
				List<String> nombreAsignaturas = new ArrayList<String>();
				List<String> acronimos = new ArrayList<String>();
				for(int i = 0; i<asignaturas.size(); i++) {
					String acronimo = asignaturas.get(i).getAsignatura();
					acronimos.add(asignaturas.get(i).getAsignatura());
					nombreAsignaturas.add(this.getNombreAsignatura(acronimo, key, httpClient));
					
				}
				
				String nombre_alumno = this.getNombreUsuario(dni, key, httpClient);
				
				response.setContentType("application/json");
				
				JsonObject json = new JsonObject();
				JsonArray array = new JsonArray();
				JsonObject datos = new JsonObject();
				
				datos.add("asignaturas", JsonParser.parseString(gson.toJson(nombreAsignaturas)));
				datos.add("usuario", JsonParser.parseString(gson.toJson(nombre_alumno)));
				datos.add("acronimos", JsonParser.parseString(gson.toJson(acronimos)));
				
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		return null;
	}

}
