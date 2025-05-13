package dam.primero.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dam.primero.dao.ParticipanteDaoImpl;

@WebServlet("/modificarParticipante")
public class ModificarParticipanteServlet extends HttpServlet {
    private ParticipanteDaoImpl participanteDao = new ParticipanteDaoImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            List<Participante> participantes = participanteDao.obtenerTodos();
            request.setAttribute("participantes", participantes);
            request.getRequestDispatcher("seleccionModificar.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String tipo = request.getParameter("tipo");
        
        try {
            if ("individual".equals(tipo)) {
                ParticipanteIndividual individual = new ParticipanteIndividual();
                individual.setId(id);
                individual.setNombre(request.getParameter("nombre"));
                individual.setCurso(request.getParameter("curso"));
                
                if (participanteDao.actualizarParticipante(individual)) {
                    response.sendRedirect("exito.jsp?tipo=modificado");
                }
            } else if ("grupo".equals(tipo)) {
                Grupo grupo = new Grupo();
                grupo.setId(id);
                grupo.setNombre(request.getParameter("nombre_grupo"));
                grupo.setCurso(request.getParameter("curso"));
                
                if (participanteDao.actualizarParticipante(grupo)) {
                    response.sendRedirect("exito.jsp?tipo=modificado");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}