package dam.primero.modelos;

public class ParticipanteGrupo {
    private int idParticipante; // Participante individual
    private int idGrupo; // Grupo

    public ParticipanteGrupo(int idParticipante, int idGrupo) {
        this.idParticipante = idParticipante;
        this.idGrupo = idGrupo;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public String toString() {
        return "ParticipanteGrupo [idParticipante=" + idParticipante + ", idGrupo=" + idGrupo + "]";
    }
}
