package dam.primero.dao;

import dam.primero.modelos.Participante;
import dam.primero.modelos.ParticipanteIndividual;
import dam.primero.contralodor.GestionaQueriesBaseDatos;
import dam.primero.modelos.Grupo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDaoImpl {
    private JdbcDao jdbcdao;

    public ParticipanteDaoImpl() throws Exception {
        jdbcdao = new JdbcDao(); // Inicializa la conexión a la base de datos
    }

    // 1. Insertar participante individual
    public boolean insertarIndividual(Participante p) throws SQLException {
		System.out.println(p);

        String sql = "INSERT INTO participantes (nombre, curso, tipo) VALUES (?, ?, 'INDIVIDUAL')";
        
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getCurso());
            System.out.println(p.getNombre());
            System.out.println(p.getCurso());
            System.out.println(sql);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al insertar participante individual");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
            return true;
        }
    }

    // 2. Insertar grupo con 5 miembros
    public boolean insertarGrupo(Grupo grupo) throws SQLException {
        if (grupo.getMiembros().size() != 5) {
            throw new SQLException("Debe haber exactamente 5 miembros en el grupo");
        }
        
        Connection conn = null;
        try {
            conn = jdbcdao.getConnection();
            conn.setAutoCommit(false);
            
            // Insertar el grupo
            String sqlGrupo = "INSERT INTO Participantes (nombre_grupo, curso, tipo) VALUES (?, ?, 'GRUPO')";
            int grupoId;
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlGrupo, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, grupo.getNombreGrupo());
                stmt.setString(2, grupo.getCurso());
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        grupoId = rs.getInt(1);
                        grupo.setId(grupoId);
                    } else {
                        throw new SQLException("Error al obtener ID del grupo");
                    }
                }
            }
            
            // Insertar miembros
            String sqlMiembro = "INSERT INTO Participantes (nombre_completo, curso, tipo) VALUES (?, ?, 'INDIVIDUAL')";
            for (ParticipanteIndividual miembro : grupo.getMiembros()) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlMiembro, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, miembro.getNombre());
                    stmt.setString(2, grupo.getCurso());
                    stmt.executeUpdate();
                    
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            miembro.setId(rs.getInt(1));
                        }
                    }
                }
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
 // 3. Buscar participantes por curso
    public List<Participante> buscarPorCurso(String curso) throws SQLException {
        List<Participante> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Participantes WHERE curso = ?";

        // Establecer conexión y preparar la consulta
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establecer el parámetro de la consulta
            stmt.setString(1, curso);
            
            // Ejecutar la consulta y procesar los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Verificar el tipo de participante
                    String tipo = rs.getString("tipo");
                    if ("INDIVIDUAL".equals(tipo)) {
                        // Crear y agregar un participante individual
                        Participante participante = new Participante(
                            rs.getString("nombre_completo"),
                            curso // Asignar el curso directamente
                        );
                        resultados.add(participante);
                    } else {
                        // Crear y agregar un grupo
                        Grupo grupo = new Grupo();
                        grupo.setId(rs.getInt("id"));
                        grupo.setNombreGrupo(rs.getString("nombre_grupo"));
                        grupo.setCurso(curso); // Asignar el curso directamente
                        grupo.setMiembros(obtenerMiembrosGrupo(grupo.getId()));
                        resultados.add(grupo);
                    }
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones
            System.err.println("Error al buscar participantes por curso: " + e.getMessage());
            throw e; // Re-lanzar la excepción para manejo externo
        }
        return resultados;
    }

    private List<ParticipanteIndividual> obtenerMiembrosGrupo(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	// 4. Modificar participante o grupo
  /*  public boolean actualizar(Participante p) throws SQLException {
        if (p instanceof ParticipanteIndividual) {
            String sql = "UPDATE Participantes SET nombre_completo = ?, curso = ? WHERE id = ? AND tipo = 'INDIVIDUAL'";
            try (Connection conn = jdbcdao.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, ((ParticipanteIndividual) p).getNombre());
                stmt.setString(2, p.getCurso());
                stmt.setInt(3, p.getId());
                
                return stmt.executeUpdate() > 0;
            }
        } else if (p instanceof Grupo) {
            Grupo grupo = (Grupo) p;
            if (grupo.getMiembros().size() != 5) {
                throw new SQLException("El grupo debe tener exactamente 5 miembros");
            }
            
            Connection conn = null;
            try {
                conn = jdbcdao.getConnection();
                conn.setAutoCommit(false);
                
                // Actualizar datos del grupo
                String sqlGrupo = "UPDATE Participantes SET nombre_grupo = ?, curso = ? WHERE id = ? AND tipo = 'GRUPO'";
                try (PreparedStatement stmt = conn.prepareStatement(sqlGrupo)) {
                    stmt.setString(1, grupo.getNombreGrupo());
                    stmt.setString(2, grupo.getCurso());
                    stmt.setInt(3, grupo.getId());
                    stmt.executeUpdate();
                }
                
                // Actualizar miembros
                String sqlMiembro = "UPDATE Participantes SET nombre_completo = ?, curso = ? WHERE id = ? AND tipo = 'INDIVIDUAL'";
                for (ParticipanteIndividual miembro : grupo.getMiembros()) {
                    try (PreparedStatement stmt = conn.prepareStatement(sqlMiembro)) {
                        stmt.setString(1, miembro.getNombre());
                        stmt.setString(2, grupo.getCurso());
                        stmt.setInt(3, miembro.getId());
                        stmt.executeUpdate();
                    }
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
        return false;
    }*/
    
    public List<String> consultaNombresPorTipo(String tipo) throws SQLException {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM participantes WHERE tipo = ?";
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nombres.add(rs.getString("nombre"));
                }
            }
        }
        return nombres;
    }

	
    
    
    // Método auxiliar para obtener miembros de un grupo
  /*  private List<ParticipanteIndividual> obtenerMiembrosGrupo(int grupoId) throws SQLException {
        List<ParticipanteIndividual> miembros = new ArrayList<>();
        String sql = "SELECT p.* FROM Participantes p JOIN Grupo_Miembros gm ON p.id = gm.miembro_id WHERE gm.grupo_id = ?";
        
        try (Connection conn = jdbcdao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, grupoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    miembros.add(new ParticipanteIndividual(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("curso")
                    ));
                }
            }
        }
        return miembros;
    }*/
}