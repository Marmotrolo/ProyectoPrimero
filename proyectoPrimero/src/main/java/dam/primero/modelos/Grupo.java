// Grupo.java
package dam.primero.modelos;

import java.util.List;

public class Grupo extends Participante {
    private String nombreGrupo;
    private List<ParticipanteIndividual> miembros;

    public Grupo() {
       this.tipo="GRUPO";
    }

    // Constructor, getters y setters
    public Grupo(int id, String nombre, String curso, String nombreGrupo) {
        super(id, nombre, curso, nombreGrupo); 	
        this.tipo = "GRUPO";
        this.nombreGrupo = nombreGrupo;
    }
}


	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public List<ParticipanteIndividual> getMiembros() {
		return miembros;
	}

	public void setMiembros(List<ParticipanteIndividual> miembros) {
		this.miembros = miembros;
	}

}