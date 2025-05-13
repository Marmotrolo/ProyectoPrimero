package dam.primero.contralodor;

import dam.primero.dao.*;
import dam.primero.modelos.*;

import java.util.Scanner;
import java.util.List;

public class GestionaQueriesBaseDatos {

    public static void main(String[] args) {
        try (
            Scanner sc = new Scanner(System.in)
        ) {
            ParticipanteIndividualDao individualDao = new ParticipanteIndividualDao();
            GrupoDao grupoDao = new GrupoDao();
            ParticipanteGrupoDao participanteGrupoDao = new ParticipanteGrupoDao();

            int opcion = 1 ;
        while (opcion != 0) {
                System.out.println("\n=== Menú de Gestión Deportiva ===");
                System.out.println("1. Insertar participante individual");
                System.out.println("2. Insertar grupo");
                System.out.println("3. Relacionar participante con grupo");
                System.out.println("4. Mostrar participantes individuales");
                System.out.println("5. Mostrar grupos");
                System.out.println("6. Mostrar relaciones participante-grupo");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.print("ID: ");
                        int idP = Integer.parseInt(sc.nextLine());
                        System.out.print("Nombre: ");
                        String nombreP = sc.nextLine();
                        System.out.print("Curso: ");
                        String cursoP = sc.nextLine();
                        individualDao.insertar(new ParticipanteIndividual(idP, nombreP, cursoP));
                        System.out.println("✔ Participante individual insertado.");
                        break;

                    case 2:
                        System.out.print("ID: ");
                        int idG = Integer.parseInt(sc.nextLine());
                        System.out.print("Nombre responsable del grupo: ");
                        String nombreG = sc.nextLine();
                        System.out.print("Curso: ");
                        String cursoG = sc.nextLine();
                        System.out.print("Nombre del grupo: ");
                        String nombreGrupo = sc.nextLine();
                        grupoDao.insertar(new Grupo(idG, nombreG, cursoG, nombreGrupo));
                        System.out.println("✔ Grupo insertado.");
                        break;

                    case 3:
                        System.out.print("ID del participante: ");
                        int idPart = Integer.parseInt(sc.nextLine());
                        System.out.print("ID del grupo: ");
                        int idGrp = Integer.parseInt(sc.nextLine());
                        participanteGrupoDao.insertar(new ParticipanteGrupo(idPart, idGrp));
                        System.out.println("✔ Relación insertada.");
                        break;


                    case 5:
                        System.out.println("=== Grupos ===");
                        List<Grupo> grupos = grupoDao.listarTodos();
                        grupos.forEach(System.out::println);
                        break;

                    case 6:
                        System.out.println("=== Participantes en Grupos ===");
                        List<ParticipanteGrupo> relaciones = participanteGrupoDao.listarTodos();
                        relaciones.forEach(System.out::println);
                        break;

                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }}

           ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
