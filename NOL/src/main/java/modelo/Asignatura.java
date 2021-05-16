package modelo;

public class Asignatura {

	private String asignatura;
	private String nota;	
	
	public Asignatura(String asignatura, String nota) {
		super();
		this.asignatura = asignatura;
		this.nota = nota;
	}
	public String getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(String asignatura) {
		this.asignatura = asignatura;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	
	
}
