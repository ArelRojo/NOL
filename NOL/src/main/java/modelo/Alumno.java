package modelo;

public class Alumno {
	
	private String apellidos;
	private String dni;
	private String nombre;
	private String password;
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Alumno(String apellidos, String dni, String nombre, String password) {
		super();
		this.apellidos = apellidos;
		this.dni = dni;
		this.nombre = nombre;
		this.password = password;
	}
	
	

}
