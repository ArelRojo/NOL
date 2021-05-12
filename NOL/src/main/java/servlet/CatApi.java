package servlet;


import modelo.Cats;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.GsonBuilder;  
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Servlet implementation class Login
 */
public class CatApi<T> extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatApi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		String url = request.getParameter("url");
		
		HttpGet httpGet = new HttpGet(url);
		
		try {
    		HttpResponse httpResponse= httpClient.execute(httpGet);
    		
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
                String srtResult = EntityUtils.toString (httpResponse.getEntity ()); // Obtenga el resultado devuelto
                System.out.println(srtResult);
                
                GsonBuilder builder = new GsonBuilder(); 
                builder.setPrettyPrinting(); 
                
                Gson gson = builder.create(); 
                JsonReader reader = new JsonReader(new StringReader(srtResult));
                List<Cats<T>> catsList = gson.fromJson(reader, new TypeToken<List<Cats<T>>>() {}.getType());
                
                System.out.println(catsList);

          
           String ru = catsList.get(0).getUrl();
             response.sendRedirect(ru);
                		
            }else{
            	System.out.println(httpResponse.getStatusLine().getStatusCode());
            }
    	} catch (Exception e) {
    	    e.printStackTrace();
  
    	}finally {
    	    try {
    	             httpClient.close (); // Liberar recursos
    	    } catch (IOException e) {
    	    	System.out.println(e.getMessage());
    	    }
    	}
	}
		
		
//		URL url = new URL("https://api.thecatapi.com/v1/images/search");
//		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//		con.setRequestMethod("GET");
//
//		int status = con.getResponseCode();
//		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer content = new StringBuffer();
//		while((inputLine = in.readLine()) != null) {
//			content.append(inputLine);
//		}
//		in.close();
//		con.disconnect();
//		System.out.println("Response status: " + status);
//		System.out.println(content.toString());
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	} 
	
	
	
}
