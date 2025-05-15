package dam.primero.modelos;

public class ParticipanteIndividual extends Participante {
    
    public ParticipanteIndividual() {
        super();
        this.tipo = "INDIVIDUAL";
    }

	public ParticipanteIndividual(int id, String nombre, String curso) {
		super(nombre, curso, "INDIVUAL");
	}
    
    
}