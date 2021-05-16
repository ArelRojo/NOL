package modelo;

public class DetallesAsignatura {

	private String acronimo;
	private String creditos;
	private String cuatrimestre;
	private String curso;
	private String nombre;
	public String getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	public String getCreditos() {
		return creditos;
	}
	public void setCreditos(String creditos) {
		this.creditos = creditos;
	}
	public String getCuatrimestre() {
		return cuatrimestre;
	}
	public void setCuatrimestre(String cuatrimestre) {
		this.cuatrimestre = cuatrimestre;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DetallesAsignatura(String acronimo, String creditos, String cuatrimestre, String curso, String nombre) {
		super();
		this.acronimo = acronimo;
		this.creditos = creditos;
		this.cuatrimestre = cuatrimestre;
		this.curso = curso;
		this.nombre = nombre;
	}
	
	
	
}
