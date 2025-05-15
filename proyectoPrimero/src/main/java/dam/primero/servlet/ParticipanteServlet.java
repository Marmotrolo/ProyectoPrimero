package dam.primero.servlet;

import dam.primero.dao.ParticipanteDaoImpl;
import dam.primero.modelos.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "ParticipanteServlet", urlPatterns = { "/participantes/*" })
public class ParticipanteServlet extends HttpServlet {

	private TemplateEngine templateEngine;
	private ParticipanteDaoImpl participanteDao;

	@Override
	public void init() throws ServletException {
		System.out.println("En init");
		// Configuración de Thymeleaf
		ServletContext servletContext = getServletContext();
		JavaxServletWebApplication application = JavaxServletWebApplication.buildApplication(servletContext);
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		try {
			participanteDao = new ParticipanteDaoImpl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		// Inicialización del DAO
		try {
			participanteDao = new ParticipanteDaoImpl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = getServletContext();
		JavaxServletWebApplication application = JavaxServletWebApplication.buildApplication(servletContext);
		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());
		response.setContentType("text/html;charset=UTF-8");
		// Datos de ejemplo: lista de nombres

		String contextPath = request.getContextPath(); // Ejemplo: /listarUsuarios o null		String pathInfo = request.getPathInfo(); // Ejemplo: /listarUsuarios o null

		String servletPath = request.getServletPath();
	
		System.out.println("En getservletPath: " + servletPath);
		if (servletPath == null || servletPath.trim().isEmpty() || servletPath.trim().equalsIgnoreCase("/index")) {
			// Redirigir a la página de login
			templateEngine.process("index", context, response.getWriter());
		} else {
		
				// Dividimos por segmentos
				String[] partes = servletPath.substring(1).split("/");
				String accion = partes[0]; // ej: "detalleUsuario"
				String parametro1 = partes.length > 1 ? partes[1] : null;

				System.out.println("Get:Servlet invocado. accion: " + accion);

			

				switch (accion) {
				case "index":
					templateEngine.process("index", context, response.getWriter());
					break;
				case "participantes":
					templateEngine.process("Participantes", context, response.getWriter());
					break;
				case "altaParticipantes":
					templateEngine.process("altaparticipantes.individual", context, response.getWriter());
					break;
				case "altaparticipante.grupo":
					templateEngine.process("altaparticipantes.grupo", context, response.getWriter());
					break;
				case "buscaParticipantes":
					templateEngine.process("busquedaparticipante", context, response.getWriter());
					break;
				case "modificaParticipantes":
				    try {
				        List<String> individuales = participanteDao.consultaNombresPorTipo("INDIVIDUAL");
				        List<String> grupos = participanteDao.consultaNombresPorTipo("GRUPO");
				        context.setVariable("individuales", individuales);
				        context.setVariable("grupos", grupos);
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
				    templateEngine.process("modificarparticipantes", context, response.getWriter());
				    break;

				case "nuevoparticipante":
					templateEngine.process("nuevoparticipante", context, response.getWriter());
					break;
				case "listar":
				//	listarParticipantes(request, response, context);
					break;
				default:
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ruta no válida: " + servletPath);
				}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Verificar sesión
		HttpSession session = request.getSession(false);
		ServletContext servletContext = getServletContext();
		JavaxServletWebApplication application = JavaxServletWebApplication.buildApplication(servletContext);
		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());
		response.setContentType("text/html;charset=UTF-8");
		// Datos de ejemplo: lista de nombres

			String servletPath = request.getServletPath();
		

		try {
			String[] partes = servletPath.substring(1).split("/");
			String accion = partes[0]; // ej: "detalleUsuario"

			System.out.println("Post:Servlet invocado. accion: " + accion);


			switch (accion) {
			case "nuevoparticipante":
				boolean exito = guardarParticipanteIndividual(request, response, context);
				if (exito) {
					context.setVariable("mensaje", "Participante individual registrado con éxito");
				} else {
					context.setVariable("error", "Error al registrar participante");
				}
				templateEngine.process("nuevoparticipante", context, response.getWriter());
				break;
			case "guardarGrupo":
			//	guardarGrupo(request, response, context);
				break;
			case "actualizar":
			//	actualizarParticipante(request, response, context);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ruta no válida: " + accion);
			}
		} catch (SQLException e) {
			context.setVariable("error", "Error de base de datos: " + e.getMessage());
			templateEngine.process("error", context, response.getWriter());
		}
	}

	/*private void listarParticipantes(HttpServletRequest request, HttpServletResponse response, WebContext context)
			throws ServletException, IOException, SQLException {
		List<Participante> participantes = participanteDao.buscarPorCurso("");
		context.setVariable("participantes", participantes);
		templateEngine.process("participantes/listado", context, response.getWriter());
	}
*/
	private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response, WebContext context)
			throws ServletException, IOException {
		templateEngine.process("participantes/formulario", context, response.getWriter());
	}

	/*private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response, WebContext context,
			String tipo, String idStr) throws ServletException, IOException, SQLException {

		int id = Integer.parseInt(idStr);
		List<Participante> participantes = participanteDao.buscarPorCurso("");
		Participante participante = null;

		for (Participante p : participantes) {
			if (p.getId() == id && p.getTipo().equals(tipo.toUpperCase())) {
				participante = p;
				break;
			}
		}

		if (participante == null) {
			context.setVariable("error", "Participante no encontrado");
			templateEngine.process("error", context, response.getWriter());
			return;
		}

		if ("individual".equalsIgnoreCase(tipo)) {
			context.setVariable("participante", participante);
			templateEngine.process("participantes/editarIndividual", context, response.getWriter());
		} else {
			context.setVariable("grupo", (Grupo) participante);
			templateEngine.process("participantes/editarGrupo", context, response.getWriter());
		}
	}
*/
	/*private void buscarPorCurso(HttpServletRequest request, HttpServletResponse response, WebContext context)
			throws ServletException, IOException, SQLException {
		String curso = request.getParameter("curso");
	//	List<Participante> participantes = participanteDao.buscarPorCurso(curso);

	//	context.setVariable("participantes", participantes);
		context.setVariable("cursoSeleccionado", curso);
		templateEngine.process("participantes/listado", context, response.getWriter());
	}
*/
	private boolean guardarParticipanteIndividual(HttpServletRequest request, HttpServletResponse response,
			WebContext context) throws ServletException, IOException, SQLException {
		String nombre = request.getParameter("nombre");
		String curso = request.getParameter("curso");
	System.out.println(nombre);
	System.out.println(curso);

		Participante participante = new Participante( nombre, curso, "INDIVIDUAL");
		
		System.out.println(participante);

		boolean exito = participanteDao.insertarIndividual(participante);

		return exito;
	}

	/*private void guardarGrupo(HttpServletRequest request, HttpServletResponse response, WebContext context)
			throws ServletException, IOException, SQLException {
		
		
		String nombreGrupo = request.getParameter("nombreGrupo");
		String curso = request.getParameter("curso");
		String[] miembrosNombres = request.getParameterValues("miembros");

		if (nombreGrupo == null || nombreGrupo.trim().isEmpty() || curso == null || curso.trim().isEmpty()) {
			context.setVariable("error", "Nombre del grupo y curso son obligatorios");
			templateEngine.process("participantes/formulario", context, response.getWriter());
			return;
		}

		if (miembrosNombres == null || miembrosNombres.length < 2) {
			context.setVariable("error", "Un grupo debe tener al menos 2 miembros");
			templateEngine.process("participantes/formulario", context, response.getWriter());
			return;
		}

		Grupo grupo = new Grupo();
		grupo.setNombreGrupo(nombreGrupo);
		grupo.setCurso(curso);

		List<Participante> miembros = new ArrayList<>();
		for (String nombre : miembrosNombres) {
			if (nombre != null && !nombre.trim().isEmpty()) {
				Participante miembro = new Participante();
				miembro.setNombre(nombre);
				miembro.setCurso(curso);
				miembros.add(miembro);
			}
		}

		grupo.setMiembros(miembros);

		try {
		//	boolean exito = participanteDao.insertarGrupo(grupo);
			
			if (exito) {
				context.setVariable("mensaje", "Grupo registrado con éxito con " + miembros.size() + " miembros");
			} else {
				context.setVariable("error", "Error al registrar grupo");
			}
		} catch (SQLException e) {
			context.setVariable("error", "Error al registrar grupo: " + e.getMessage());
		}

		listarParticipantes(request, response, context);
	}*/

	/*private void actualizarParticipante(HttpServletRequest request, HttpServletResponse response, WebContext context)
			throws ServletException, IOException, SQLException {
		String tipo = request.getParameter("tipo");
		int id = Integer.parseInt(request.getParameter("id"));

		if ("individual".equalsIgnoreCase(tipo)) {
			Participante participante = new Participante();
		
			participante.setNombre(request.getParameter("nombre"));
			participante.setCurso(request.getParameter("curso"));

		//	boolean exito = participanteDao.actualizar(participante);
		/*	if (exito) {
				context.setVariable("mensaje", "Participante individual actualizado con éxito");
			} else {
				context.setVariable("error", "Error al actualizar participante");
			}
		} else {
			Grupo grupo = new Grupo();
			grupo.setId(id);
			grupo.setNombreGrupo(request.getParameter("nombreGrupo"));
			grupo.setCurso(request.getParameter("curso"));

			String[] miembrosNombres = request.getParameterValues("miembros");
			List<ParticipanteIndividual> miembros = new ArrayList<>();

			for (String nombre : miembrosNombres) {
				if (nombre != null && !nombre.trim().isEmpty()) {
					ParticipanteIndividual miembro = new ParticipanteIndividual();
					miembro.setNombre(nombre);
					miembro.setCurso(grupo.getCurso());
					miembros.add(miembro);
				}
			}

			grupo.setMiembros(miembros);

			try {
				boolean exito = participanteDao.actualizar(grupo);
				if (exito) {
					context.setVariable("mensaje", "Grupo actualizado con éxito");
				} else {
					context.setVariable("error", "Error al actualizar grupo");
				}
			} catch (SQLException e) {
				context.setVariable("error", "Error al actualizar grupo: " + e.getMessage());
			}
		}

		listarParticipantes(request, response, context);
	}*/
}