package dam.primero.dao;

import dam.primero.modelos.*;
import java.sql.*;
import java.util.*;

public class ParticipanteDaoImpl  {

private 	JdbcDao jdbcdao;
	// Insertar participante individual
    public boolean insertarIndividual(ParticipanteIndividual individual) throws SQLException {
        Connection conn = null;
        try {
            conn = jdbcdao.getConnection();
            conn.setAutoCommit(false);

            // 1. Insertar en tabla Participantes
            String sqlParticipante = "INSERT INTO Participantes (Nombre, Curso, Tipo) VALUES (?, ?, 'INDIVIDUAL')";
            try (PreparedStatement stmt = conn.prepareStatement(sqlParticipante, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, individual.getNombre());
                stmt.setString(2, individual.getCurso());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Error al insertar participante, ninguna fila afectada.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        individual.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Error al obtener ID generado.");
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
            	conn.rollback();
         
            }
            throw e;
        } finally {
            if (conn != null) { 
            	conn.setAutoCommit(true);
            }
        }
    }

    public boolean insertarGrupo(Grupo grupo, List<ParticipanteIndividual> miembros) throws SQLException {
        if (miembros.size() > 5) {
            throw new SQLException("Un grupo no puede tener más de 5 miembros");
        }

        Connection conn = null;
        try {
            conn = jdbcdao.getConnection();
            conn.setAutoCommit(false);

            // 1. Insertar el grupo en Participantes
            String sqlGrupo = "INSERT INTO Participantes (Nombre, Curso, Tipo) VALUES (?, ?, 'GRUPO')";
            int grupoId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlGrupo, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, grupo.getNombre());
                stmt.setString(2, grupo.getCurso());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Error al insertar grupo, ninguna fila afectada.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        grupoId = generatedKeys.getInt(1);
                        grupo.setId(grupoId);
                    } else {
                        throw new SQLException("Error al obtener ID generado para el grupo.");
                    }
                }
            }

            // 2. Insertar en tabla Grupos
            String sqlDetalleGrupo = "INSERT INTO Grupos (Id_participante, Nombre_grupo) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalleGrupo)) {
                stmt.setInt(1, grupoId);
                stmt.setString(2, grupo.getNombreGrupo());
                stmt.executeUpdate();
            }

            // 3. Insertar miembros
            String sqlMiembro = "INSERT INTO MiembrosGrupo (Id_grupo, Id_participante) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMiembro)) {
                for (ParticipanteIndividual miembro : miembros) {
                    stmt.setInt(1, grupoId);
                    stmt.setInt(2, miembro.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    // Buscar por curso
    public List<Participante> buscarPorCurso(String curso) throws SQLException {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT Id_participante, Nombre, Curso, Tipo FROM Participantes WHERE Curso = ?";
        
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, curso);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("Tipo");
                    Participante p;
                    
                    if ("INDIVIDUAL".equals(tipo)) {
                        p = new ParticipanteIndividual(
                            rs.getInt("Id_participante"),
                            rs.getString("Nombre"),
                            rs.getString("Curso")
                        );
                    } else {
                        p = new Grupo(
                            rs.getInt("Id_participante"),
                            rs.getString("Nombre"),
                            rs.getString("Curso"),
                            obtenerNombreGrupo(rs.getInt("Id_participante"))
                        );
                    }
                    participantes.add(p);
                }
            }
        }
        return participantes;
    }

    // Obtener nombre del grupo
    private String obtenerNombreGrupo(int idGrupo) throws SQLException {
        String sql = "SELECT Nombre_grupo FROM Grupos WHERE Id_participante = ?";
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrupo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Nombre_grupo");
                }
            }
        }
        return null;
    }

    // Actualizar participante
    public boolean actualizarParticipante(Participante participante) throws SQLException {
        String sql = "UPDATE Participantes SET Nombre = ?, Curso = ? WHERE Id_participante = ?";
        
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, participante.getNombre());
            stmt.setString(2, participante.getCurso());
            stmt.setInt(3, participante.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    // Obtener todos los participantes
    public List<Participante> obtenerTodos() throws SQLException {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT Id_participante, Nombre, Curso, Tipo FROM Participantes";
        
        try (Connection conn = jdbcdao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String tipo = rs.getString("Tipo");
                Participante p;
                
                if ("INDIVIDUAL".equals(tipo)) {
                    p = new ParticipanteIndividual(
                        rs.getInt("Id_participante"),
                        rs.getString("Nombre"),
                        rs.getString("Curso")
                    );
                } else {
                    p = new Grupo(
                        rs.getInt("Id_participante"),
                        rs.getString("Nombre"),
                        rs.getString("Curso"),
                        obtenerNombreGrupo(rs.getInt("Id_participante"))
                    );
                }
                participantes.add(p);
            }
        }
        return participantes;
    }
}