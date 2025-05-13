package dam.primero.dao;

import dam.primero.modelos.Grupo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDao extends JdbcDao {

    public GrupoDao() throws Exception {
        super();
    }

    public void insertar(Grupo g) throws Exception {
        String sql1 = "INSERT INTO Participantes (Id_participante, Nombre, Curso) VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO Grupo (Id_participante, Nombre_Grupo) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(sql1);
                 PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

                stmt1.setInt(1, g.getId());
                stmt1.setString(2, g.getNombre());
                stmt1.setString(3, g.getCurso());
                stmt1.executeUpdate();

                stmt2.setInt(1, g.getId());
                stmt2.setString(2, g.getNombreGrupo());
                stmt2.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Grupo> listarTodos() throws Exception {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT p.Id_participante, p.Nombre, p.Curso, g.Nombre_Grupo " +
                     "FROM Participantes p INNER JOIN Grupo g ON p.Id_participante = g.Id_participante";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Grupo(
                    rs.getInt("Id_participante"),
                    rs.getString("Nombre"),
                    rs.getString("Curso"),
                    rs.getString("Nombre_Grupo")
                ));
            }
        }

        return lista;
    }

    public Grupo buscarPorId(int id) throws Exception {
        String sql = "SELECT p.Id_participante, p.Nombre, p.Curso, g.Nombre_Grupo " +
                     "FROM Participantes p INNER JOIN Grupo g ON p.Id_participante = g.Id_participante " +
                     "WHERE p.Id_participante = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Grupo(
                    rs.getInt("Id_participante"),
                    rs.getString("Nombre"),
                    rs.getString("Curso"),
                    rs.getString("Nombre_Grupo")
                );
            }
        }

        return null;
    }
}
