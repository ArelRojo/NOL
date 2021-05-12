package servlet;


import modelo.Cats;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Clases de HttpComponents
//El principal punto de entrada de la API HttpClient es la interfaz HttpClient. Su función esencial es ejecutar métodos HTTP. Eso implica
//uno o varios intercambios de solicitud/respuesta HTTP manejados internamente por HttpClient.
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

//Clases Gson
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

		//CloseableHttpClient es una clase abstracta que es la implementación base de HttpClient que tambien implementa java.io.Closeable.
		//Creando un cliente http. Un cliente se cree para poder consumir un servicio publicado en un endpoint (la url)
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//Cogemos la url que le pasamos al formulario de la vista que en html tiene como nombre de parámetro "url"
		String url = request.getParameter("url");
		
		//El método GET se usa para recuperar información del servidor dado usando un URI dado. 
		//Las solicitudes que utilizan GET solo deben recuperar datos y no deben tener ningún otro efecto en los datos.
		HttpGet httpGet = new HttpGet(url);
		
		try {
			//Interfaz httpResponse. Después de recibir e interpretar un mensaje de solicitud el servidor responde con un mensaje de respuesta Http.
			//aqui le decimos al cliente que ejecute el metodo get (la solicitud) para recuperar el objeto de respuesta.
    		HttpResponse httpResponse= httpClient.execute(httpGet);
    		
    		//Comprobamos que el estado que devuelve el servidor sea un 200. 
    		//Es un código de estado que nos dice que la petición que acabamos de hacer ha sido entendida, enviada y recibida.
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
    			
    			//Devolvemos el cuerpo de la respuesta convirtiendo la entidad de respuesta en una cadena (string)
                String srtResult = EntityUtils.toString (httpResponse.getEntity ()); 
                System.out.println(srtResult);
                
                //De forma predeterminada, Gson imprime el JSON en formato compacto. 
                //Significa que no habrá ningún espacio en blanco entre los nombres de campo y su valor,
                //campos de objeto y objetos dentro de matrices en la salida JSON, etc.
                //Para ello habilitamos la función Pretty Printing. Para hacerlo debemos configurar la Gsoninstancia usando el GsonBuilder.
                GsonBuilder builder = new GsonBuilder(); 
                builder.setPrettyPrinting(); 
                
                //Creamos una instancia de Gson con el metodo create.
                Gson gson = builder.create(); 
                
                //La clase JsonReader lee un valor codificado JSON como un flujo de tokens. 
                //Esta secuencia incluye tanto valores literales (cadenas, números, valores booleanos y nulos) como los delimitadores de inicio y fin de objetos y matrices. 
                //Los tokens se atraviesan en orden de profundidad, el mismo orden en que aparecen en el documento JSON. 
                //Dentro de los objetos JSON, los pares de nombre / valor están representados por un solo token.
                //El Json que queremos leer se encuentra en la respuesta que el servidor envia.
                JsonReader reader = new JsonReader(new StringReader(srtResult));
                
                //El siguiente paso es deserializar este JSON que nos han devuelto. Para ello creamos un objeto que representará ese 
                //JSON con sus pares nombre-valor. (Hay que ver la clase java Cats que representa el objeto java al que convertiremos el JSON)
                //Vemos que la clase Cat es de tipo genérico. 
                //Java aún no proporciona una forma de representar tipos genéricos, por lo que esta clase (TypeToken) lo hace.
                // Obliga a los clientes a crear una subclase de esta clase que permite recuperar la información de tipo incluso en tiempo de ejecución.
                
                List<Cats<T>> catsList = gson.fromJson(reader, new TypeToken<List<Cats<T>>>() {}.getType());
                
                System.out.println(catsList);

          //Cogemos el elemento 0 de la lista de Cats (Esta lista siempre tendrá solo un elemento) donde se encuentra el json representado y de ahí sacamos 
          //la variable url que encontramos en ese json (La url a donde tenemos que acceder)
           String ru = catsList.get(0).getUrl();
           //Utilizamos sendRedirect para redirigir la respuesta a otro recurso (url en este caso)
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


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	} 
	
	
	
}
