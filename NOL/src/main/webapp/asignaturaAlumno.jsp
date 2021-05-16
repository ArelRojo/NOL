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
			<h4 class="text-center">Asignaturas del/la alumn@ Pepe Garcia</h4>
			<div class="col-4">
			<div class="list-group" id="list-tab" role="tablist">

				<%

List<Asignatura> asignaturas = ((List<Asignatura>)request.getSession().getAttribute("asignaturas"));

for(int i = 0; i< asignaturas.size();i++){
String cadena = "<a class=\"list-group-item list-group-item-action\" data-toggle=\"list\" role=\"tab\" aria-controls=\"home\">";
cadena = cadena +asignaturas.get(i).getAsignatura();
cadena = cadena + "</a>";

out.println(cadena);
}

%>
				
			</div>
			<div class="col-8">
				<div class="tab-content" id="nav-tabContent">
					<div class="tab-pane fade show" role="tabpanel"
						aria-labelledby="list-home-list">
						<p>Detalles</p>
					</div>
				</div>
			</div>
		</main>
		<!-- Fin main -->

		<footer>
			<div class="row" id="footer">
				<p>La aplicación NOL se trata de un sistema de consulta de
					asignaturas y notas para alumnos y profesores</p>
			</div>

		</footer>
	</div>
</body>
</html>