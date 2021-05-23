<%@ page session="true"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="modelo.DetallesAsignatura"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="Trabajo Nol">
<meta name="author" content="3TI21_g1">
<meta charset="ISO-8859-1">

<!-- Hoja de estilos CSS -->
<link rel="stylesheet" href="css/styles.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
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
				<a class="boton-cerrar-sesion" href="" style="float: right;">Cerrar
					sesión</a>
			</div>
		</div>
		<!-- Fin encabezado -->

		<div class="row" id="separador"></div>

		<!-- Inicio main-->
		<main role="main" class="container" id="main">
			<h4 class="text-center">
				Asignaturas del/la profesor@
				<%=request.getSession().getAttribute("nombreProfesor")%></h4>
			<div class="row">
				<div class="col-4">


					<%
					List<DetallesAsignatura> asignaturas = ((List<DetallesAsignatura>) request.getSession()
							.getAttribute("detallesAsignaturas"));

					String cadena = "";

					if (asignaturas == null) {
						cadena = "<p>No hay asignaturas</p>";
					} else {
						for (int i = 0; i < asignaturas.size(); i++) {
							cadena = "<a href=\"/NOL/AsignaturaAlumnos?acronimo=" + asignaturas.get(i).getAcronimo()
							+ "\" class=\"list-group-item list-group-item-action boton-izquierda\"  id=\"asignatura-" + i + "\">";
							cadena = cadena + asignaturas.get(i).getNombre();
							cadena = cadena + "</a>";
						}
						out.println(cadena);
					}
					%>

				</div>
			</div>


		</main>
		
		<div class="d-flex align-items-end-left flex-column mt-auto p-2">
			<a href="javascript: history.go(-1)">Volver atrás</a>
			</div>
		<!-- Fin main -->

		<footer>
			<div class="row" id="footer">
				<p>&nbsp; La aplicación NOL se trata de un sistema de consulta
					de asignaturas y notas para alumnos y profesores</p>
			</div>

		</footer>
	</div>
	<script>
		$(".boton-cerrar-sesion").click(function(){
			alert("Para cerrar sesión es necesario reiniciar el navegador!");
		});
	</script>  
</body>
</html>