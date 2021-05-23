<%@ page session="true"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="modelo.AlumnoNota"%>
<%@ page import="modelo.Alumno"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="modelo.Asignatura"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="Trabajo Nol">
<meta name="author" content="3TI21_g1">
<meta charset="ISO-8859-1">

<!-- Hoja de estilos CSS -->
<link rel="stylesheet" href="css/styles.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- Integración bootstrap -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js"
	type="text/javascript">
	
</script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css">

<!-- Integración iconos Font Awesome -->
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
<title>Asignaturas</title>
</head>


<body id="body">

	<!-- Contenedor principal página -->
	<div class="container">

		<!-- Inicio encabezado -->
		<div class="row align-items-start" id="encabezado">
			<div class="col-sm-4" id="img-wrap">
				<a href="https://www.upv.es/"> <img src="img/logo3.png" alt="">
				</a>
			</div>

			<div class="col-sm-6">
				<h2 class="text-center" id="bienvenida">
					<b>Notas OnLine.</b>
				</h2>

			</div>
			<div class="d-flex align-items-end flex-column mt-auto p-2">
				<a class="boton-cerrar-sesion" href="" style="float: right;">Cerrar sesi�n</a>
			</div>
		</div>
		<!-- Fin encabezado -->

		<div class="row" id="separador"></div>

		<!-- Inicio main-->
		<main role="main" class="container" id="main">
			<h4 class="text-center"> Alumnos de la asignatura</h4>
			<div class="row">
			
			<%List<AlumnoNota> alumno = (List<AlumnoNota>)request.getSession().getAttribute("alumnonota"); 
			 List<Alumno> detallealumno= (List<Alumno>)request.getSession().getAttribute("detalledeAlumno"); 
			 Map<String,List<Asignatura>> asignaturasDeAlumno = (Map<String,List<Asignatura>>) request.getSession().getAttribute("mapAsignaturas");
			 %>
			<div class="col-4">
		
			<div class="list-group" id="list-tab" role="tablist">
	<%
	String cadena = "";
	if(alumno == null){
	cadena = "<p>No hay alumnos</p>";
	}else{
	for(int i = 0; i< alumno.size();i++){
		cadena = "<a class=\"list-group-item list-group-item-action boton-izquierda\" data-toggle=\"list\" role=\"tab\" aria-controls=\"home\" id=\"alumno-"+i+"\">";
		cadena = cadena + alumno.get(i).getAlumno();
		cadena = cadena + "</a>";
	}
		out.println(cadena);
		} %>
				</div>
			</div>
			 
			 <%
			 	String div = ""; 
			 if(alumno == null){
					div = "<div>";
					}else{
					for(int i = 0; i< alumno.size();i++){
						div = "<div class=\"d-none panel-derecha col-8\" id=\"alumno-"+i+"-detalle\">";
						
					}
						out.println(div);
						} 
			 
						String dni = "";
						String nombreAlumno = "";
						if(detallealumno == null){
						nombreAlumno = "<h4>No hay detalles de alumno<h4>";
						}else{
						for(int i = 0; i< detallealumno.size();i++){
							
							nombreAlumno = "<h4>";
							nombreAlumno  = nombreAlumno + detallealumno.get(i).getNombre() + " " + detallealumno.get(i).getApellidos()+ "( " +  detallealumno.get(i).getDni() + " )";
							nombreAlumno = nombreAlumno +"</h4>";
							dni = detallealumno.get(i).getDni();
						}
							out.println(nombreAlumno);
						}
						
						
						%>
				<img class="imagen" src="img/user.png" alt="An user" id="img">
				<%
				
					String asignaturas_matricula = "";
					String asignatura ="";
					List<Asignatura> asig = new ArrayList<Asignatura>();
				if (asignaturasDeAlumno == null){
					asignaturas_matricula = "";
				}else{
					int i = 0;
					
					for(Map.Entry entry : asignaturasDeAlumno.entrySet()){
						if(!dni.equals("")){
						if(entry.getKey().equals(dni)){
							
							asignaturas_matricula = "<h6> Matriculad@ en:";
							asig = (List<Asignatura>)entry.getValue();
							if(asig != null){
							for(Asignatura a : asig){
							asignatura = a.getAsignatura();
							asignaturas_matricula = asignaturas_matricula + " " + asignatura;
							}
							asignaturas_matricula = asignaturas_matricula + "</h6>";
							}
						}
						
						}
						i++;
					}
					out.println(asignaturas_matricula);
				}
				
				%>
				<!-- <p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
				Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
				Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
				Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum </p> -->
			</div>
			
		</main>
		<div class="d-flex align-items-end-left flex-column mt-auto p-2">
			<a href="javascript: history.go(-1)">Volver atr�s</a>
			</div>
			</div>
			
		<!-- Fin main -->

		<footer>
			<div class="row" id="footer">
				<p> &nbsp; La aplicación NOL se trata de un sistema de consulta de
					asignaturas y notas para alumnos y profesores</p>
			</div>


		</footer>
	
	<%
	out.println("</div>");
	%>
	
	
	<script>
	$(".boton-izquierda").click(function(){
		
		$(".panel-derecha").addClass("d-none")
		
		console.log(this.id);
		var cadena = "#"+this.id +"-detalle";
		$(cadena).removeClass("d-none");
		
		
	});

	
	$(".boton-cerrar-sesion").click(function(){
		alert("Para cerrar sesi�n es necesario reiniciar el navegador!");
	});

	</script>
</body>
</html>