package dam.primero.contralodor;

import dam.primero.dao.ParticipanteDaoImpl;
import dam.primero.modelos.ParticipanteIndividual;
import dam.primero.modelos.Grupo;
import dam.primero.modelos.Participante;

import java.util.Scanner;
import java.util.List;

public class GestionaQueriesBaseDatos {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ParticipanteDaoImpl participanteDao = new ParticipanteDaoImpl();
            int opcion = -1;

            while (opcion != 0) {
                mostrarMenu();
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        insertarParticipanteIndividual(sc, participanteDao);
                        break;
                    case 2:
                        insertarGrupo(sc, participanteDao);
                        break;
                    case 3:
                        buscarPorCurso(sc, participanteDao);
                        break;
                    case 4:
                        modificarParticipante(sc, participanteDao);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Menú de Gestión Deportiva ===");
        System.out.println("1. Insertar participante individual");
        System.out.println("2. Insertar grupo");
        System.out.println("3. Buscar participantes por curso");
        System.out.println("4. Modificar participante o grupo");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void insertarParticipanteIndividual(Scanner sc, ParticipanteDaoImpl participanteDao) {
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        System.out.print("Curso: ");
        String curso = sc.nextLine();

        ParticipanteIndividual participante = new ParticipanteIndividual();
        participante.setNombre(nombre);
        participante.setCurso(curso);

        try {
            boolean exito = participanteDao.insertarIndividual(participante);
            if (exito) {
                System.out.println("✔ Participante individual insertado.");
            } else {
                System.out.println("✖ Error al insertar participante individual.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void insertarGrupo(Scanner sc, ParticipanteDaoImpl participanteDao) {
        System.out.print("Nombre del grupo: ");
        String nombreGrupo = sc.nextLine();
        System.out.print("Curso: ");
        String curso = sc.nextLine();

        Grupo grupo = new Grupo();
        grupo.setNombreGrupo(nombreGrupo);
        grupo.setCurso(curso);

        // Aquí puedes agregar lógica para ingresar miembros al grupo
        for (int i = 1; i <= 5; i++) {
            System.out.print("Nombre del miembro " + i + ": ");
            String nombreMiembro = sc.nextLine();
            ParticipanteIndividual miembro = new ParticipanteIndividual();
            miembro.setNombre(nombreMiembro);
            miembro.setCurso(curso);
            grupo.getMiembros().add(miembro);
        }

        try {
            boolean exito = participanteDao.insertarGrupo(grupo);
            if (exito) {
                System.out.println("✔ Grupo insertado.");
            } else {
                System.out.println("✖ Error al insertar grupo.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void buscarPorCurso(Scanner sc, ParticipanteDaoImpl participanteDao) {
        System.out.print("Ingrese el curso: ");
        String curso = sc.nextLine();

        try {
            List<Participante> participantes = participanteDao.buscarPorCurso(curso);
            System.out.println("=== Participantes en el curso " + curso + " ===");
            for (Participante p : participantes) {
                System.out.println(p);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar participantes: " + e.getMessage());
        }
    }

    private static void modificarParticipante(Scanner sc, ParticipanteDaoImpl participanteDao) {
        // Aquí deberías implementar la lógica para mostrar todos los participantes y permitir la modificación
        // Por simplicidad, se omite en este ejemplo
        System.out.println("Funcionalidad de modificación no implementada aún.");
    }
}
