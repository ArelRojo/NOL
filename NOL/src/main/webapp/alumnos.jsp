<%@ page session="true"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="modelo.AlumnoNota"%>
<%@ page import="modelo.Alumno"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="Trabajo Nol">
<meta name="author" content="3TI21_g1">
<meta charset="ISO-8859-1">

<!-- Hoja de estilos CSS -->
<link rel="stylesheet" href="css/styles.css">
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
		</div>
		<!-- Fin encabezado -->

		<div class="row" id="separador"></div>

		<!-- Inicio main-->
		<main role="main" class="container">
			<h4 class="text-center"> Alumnos de la asignatura</h4>
			<div class="row">
			
			<%List<AlumnoNota> alumno = (List<AlumnoNota>)request.getSession().getAttribute("alumnonota"); %>
			<% List<Alumno> detallealumno= (List<Alumno>)request.getSession().getAttribute("detalledeAlumno"); %>
			<div class="col-4">
				<div class="list-group" id="list-tab" role="tablist">
				
	<%for(int i = 0; i< alumno.size();i++){
		String cadena = "<a class=\"list-group-item list-group-item-action boton-izquierda\" data-toggle=\"list\" role=\"tab\" aria-controls=\"home\" id=\"alumno-"+i+"\">";
		cadena = cadena + alumno.get(i).getAlumno();
		cadena = cadena + "</a>";

		out.println(cadena);
		} %>
				</div>
			</div>
			
			<div class="col-8">
			
						<%
						for(int i = 0; i< detallealumno.size();i++){
							
							String nombreAlumno = "<h4 id=\"alumno-"+i+"-detalle\" class=\"d-none panel-derecha\" >";
							nombreAlumno  = nombreAlumno + detallealumno.get(i).getNombre() + " " + detallealumno.get(i).getApellidos()+ "( " +  detallealumno.get(i).getDni() + " )";
							nombreAlumno = nombreAlumno +"</h4>";
							out.println(nombreAlumno);
						}
						%>
				<img class="imagen" src="img/user.png" alt="An user" id="img">
				<h6> Matriculad@ en: DCU, DEW </h6>
				<p> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum </p>
			</div>
			</div>
		</main>
		<!-- Fin main -->

		<footer>
			<div class="row" id="footer">
				<p> &nbsp; La aplicación NOL se trata de un sistema de consulta de
					asignaturas y notas para alumnos y profesores</p>
			</div>


		</footer>
	</div>
	
	<script>
	$(".boton-izquierda").click(function(){
		
		$(".panel-derecha").addClass("d-none")
		
		console.log(this.id);
		var cadena = "#"+this.id +"-detalle";
		$(cadena).removeClass("d-none");
		
		var cadena = "#"+this.id +"-nota";
		$(cadena).removeClass("d-none");
		
	});
	
	</script>
</body>
</html>