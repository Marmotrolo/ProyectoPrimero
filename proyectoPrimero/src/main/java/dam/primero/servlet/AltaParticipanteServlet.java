import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import dam.primero.dao.ParticipanteDaoImpl;

@WebServlet("/altaParticipante")
public class AltaParticipanteServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private ParticipanteDaoImpl dao = new ParticipanteDaoImpl();

    @Override
    public void init() throws ServletException {
        // Configuración básica de Thymeleaf
        ServletContext servletContext = getServletContext();
        JavaxServletWebApplication app = JavaxServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        
        dao = new ParticipanteDaoImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = dao.crearContexto(request, response);
        templateEngine.process("formularioAlta", context, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = crearContexto(request, response);
        String tipo = request.getParameter("tipo");

        try {
            if ("individual".equals(tipo)) {
                ParticipanteIndividual pi = new ParticipanteIndividual();
                pi.setNombre(request.getParameter("nombre"));
                pi.setCurso(request.getParameter("curso"));
                dao.insertarIndividual(pi);
            } 
            else if ("grupo".equals(tipo)) {
                Grupo grupo = new Grupo();
                grupo.setNombre(request.getParameter("nombre_grupo"));
                grupo.setCurso(request.getParameter("curso"));
                
                List<ParticipanteIndividual> miembros = new ArrayList<>();
                // Lógica para añadir miembros (máx 5)
                dao.insertarGrupo(grupo, miembros);
            }
            
            response.sendRedirect(request.getContextPath() + "/buscarParticipantes");
            
        } catch (Exception e) {
            context.setVariable("error", "Error en alta: " + e.getMessage());
            templateEngine.process("error", context, response.getWriter());
        }
    }
}