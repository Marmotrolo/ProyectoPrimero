package dam.primero.dao;

import dam.primero.modelos.ParticipanteIndividual;
import java.sql.*;
import java.util.*;

public class ParticipanteIndividualDao extends JdbcDao {

    public ParticipanteIndividualDao() throws Exception {
        super();
    }

    public void insertar(ParticipanteIndividual p) throws Exception {
        Connection conn = getConnection();

        String sql1 = "INSERT INTO Participantes (Id_participante, Nombre, Curso) VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO ParticipanteIndividual (Id_participante) VALUES (?)";

        PreparedStatement st1 = null;
        PreparedStatement st2 = null;

        try {
            conn.setAutoCommit(false);

            st1 = conn.prepareStatement(sql1);
            st1.setInt(1, p.getId());
            st1.setString(2, p.getNombre());
            st1.setString(3, p.getCurso());
            st1.executeUpdate();

            st2 = conn.prepareStatement(sql2);
            st2.setInt(1, p.getId());
            st2.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            if (st1 != null) st1.close();
            if (st2 != null) st2.close();
            conn.close();
        }
    }



}
