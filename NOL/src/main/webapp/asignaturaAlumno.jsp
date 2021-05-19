<%@ page session="true"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
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
		</div>
		<!-- Fin encabezado -->

		<div class="row" id="separador"></div>

		<!-- Inicio main-->
		<main role="main" class="container" id="main">
			<h4 class="text-center">Asignaturas del/la alumn@ <%=request.getSession().getAttribute("nombreAlumno")  %></h4>
			<div class="row">
			<div class="col-4">
			<div class="list-group" id="list-tab" role="tablist">


				<%
				
List<Asignatura> asignaturas = ((List<Asignatura>)request.getSession().getAttribute("asignaturas"));
List<String> acronimos= ((List<String>)request.getSession().getAttribute("acronimos"));

for(int i = 0; i< asignaturas.size();i++){
String cadena = "<a class=\"list-group-item list-group-item-action boton-izquierda\" data-toggle=\"list\" role=\"tab\" aria-controls=\"home\" id=\"asignatura-"+i+"\">";
cadena = cadena + asignaturas.get(i).getAsignatura();
cadena = cadena + "</a>";

out.println(cadena);
}

%>
				
			</div>
			</div>
			<div class="col-8">
				<div  id="nav-tabContent">
					<div>
						<%
						for(int i = 0; i< asignaturas.size();i++){
							
							String nombreAsignatura = "<p id=\"asignatura-"+i+"-detalle\" class=\"d-none panel-derecha\" >";
							nombreAsignatura  = nombreAsignatura + acronimos.get(i);
							nombreAsignatura = nombreAsignatura +"</p>";
							out.println(nombreAsignatura);
							String nota = "<p id=\"asignatura-"+i+"-nota\" class=\"d-none panel-derecha\" >";
							nota = nota+ asignaturas.get(i).getNota();
							nota = nota +"</p>";
							out.println(nota);
						}
						%>
					</div>
				</div>
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