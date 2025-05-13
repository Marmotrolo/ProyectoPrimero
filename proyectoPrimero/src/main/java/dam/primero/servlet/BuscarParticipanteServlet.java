package dam.primero.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import dam.primero.dao.ParticipanteDaoImpl;
import dam.primero.modelos.Participante;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/buscarParticipantes")
public class BuscarParticipanteServlet extends HttpServlet {
    
    private TemplateEngine templateEngine;
    private ParticipanteDaoImpl participanteDaoImpl;
    
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
        
        participanteDaoImpl = new ParticipanteDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        // Crear contexto Thymeleaf
        IServletWebExchange webExchange = JavaxServletWebApplication
            .buildApplication(getServletContext())
            .buildExchange(request, response);
        
        WebContext context = new WebContext(webExchange, request.getLocale());
        
        try {
            // 1. Obtener parámetro de búsqueda (curso)
            String curso = request.getParameter("curso");
            
            // 2. Validar que se haya enviado el curso
            if (curso == null || curso.isEmpty()) {
                context.setVariable("error", "Debe seleccionar un curso (1ESO o 2ESO)");
                templateEngine.process("buscarParticipantes", context, response.getWriter());
                return;
            }
            
            // 3. Buscar en la base de datos
            List<Participante> participantes = participanteDaoImpl.buscarPorCurso(curso);
            
            // 4. Pasar resultados a la plantilla
            context.setVariable("participantes", participantes);
            context.setVariable("cursoBuscado", curso);
            
            // 5. Mostrar resultados
            templateEngine.process("resultadosBusqueda", context, response.getWriter());
            
        } catch (Exception e) {
            context.setVariable("error", "Error en la búsqueda: " + e.getMessage());
            templateEngine.process("error", context, response.getWriter());
        }
    }
}