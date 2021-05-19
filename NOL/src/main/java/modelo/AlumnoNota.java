package modelo;

public class AlumnoNota {

	private String alumno;
	private String nota;
	public String getAlumno() {
		return alumno;
	}
	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public AlumnoNota(String alumno, String nota) {
		super();
		this.alumno = alumno;
		this.nota = nota;
	}
	
	
}
