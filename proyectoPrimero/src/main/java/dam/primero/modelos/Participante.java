// Participante.java
package dam.primero.modelos;

public class Participante {
    private int id;
    private String nombre;
    private String curso;
    protected String tipo; 
    public Participante() {
    	super();
    }

    public Participante(int id, String nombre, String curso, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.curso = curso;
        this.tipo = tipo;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

    
    
}
