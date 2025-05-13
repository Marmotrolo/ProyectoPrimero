package dam.primero.dao;

import dam.primero.modelos.ParticipanteGrupo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteGrupoDao extends JdbcDao {

    public ParticipanteGrupoDao() throws Exception {
        super();
    }

    public void insertar(ParticipanteGrupo pg) throws Exception {
        String sql = "INSERT INTO ParticipanteGrupo (Id_Participante, Id_Grupo) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pg.getIdParticipante());
            stmt.setInt(2, pg.getIdGrupo());
            stmt.executeUpdate();
        }
    }

    public List<ParticipanteGrupo> listarTodos() throws Exception {
        List<ParticipanteGrupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM ParticipanteGrupo";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new ParticipanteGrupo(
                    rs.getInt("Id_Participante"),
                    rs.getInt("Id_Grupo")
                ));
            }
        }

        return lista;
    }
}
