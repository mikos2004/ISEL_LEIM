package tps.tp4;

import java.util.Scanner;

/**
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 22/23
 */

public class CmdSistema {

    /**
     * Prints para os diferentes menus
     */

    public static void AppOpts() {
        System.out.println("[1] -> Login Adminstrador");
        System.out.println("[2] -> Login Médico");
        System.out.println("[3] -> Login Enfermeiro");
        System.out.println("[4] -> Login Doente");
        System.out.println("----------------------------------------");
    }

    public static void AdminOpts() {
        System.out.println("----------------------------------------\n");
        System.out.println("**** MENU ADMINSTRADOR ****\n");
        System.out.println("[1] -> Registar Adminstrador");
        System.out.println("[2] -> Apagar Adminstrador");
        System.out.println("[3] -> Listar Adminstrador");
        System.out.println("[4] -> Registar Médico");
        System.out.println("[5] -> Apagar Médico");
        System.out.println("[6] -> Listar Médicos");
        System.out.println("[7] -> Registar Enfermeiro");
        System.out.println("[8] -> Apagar Enfermeiro");
        System.out.println("[9] -> Listar Enfermeiro");
        System.out.println("[10] -> Registar Doentes");
        System.out.println("[11] -> Apagar Doentes");
        System.out.println("[12] -> Listar Doentes");
        System.out.println("[13] -> Listar Consultas");
        System.out.println("[14] -> Marcar Consultas");
        System.out.println("[15] -> Apagar Consultas");
        System.out.println("[0] -> Logout");
        System.out.println("----------------------------------------");
    }

    public static void MedOpts() {
        System.out.println("----------------------------------------\n");
        System.out.println("**** MENU MÉDICO ****\n");
        System.out.println("[1] -> Listar Médicos");
        System.out.println("[2] -> Listar Doentes");
        System.out.println("[3] -> Listar Enfermeiros");
        System.out.println("[4] -> Listar Consultas");
        System.out.println("[5] -> Marcar Consultas");
        System.out.println("[6] -> Apagar Consultas");
        System.out.println("[0] -> Logout");
        System.out.println("----------------------------------------");
    }

    public static void EnfOpts() {
        System.out.println("----------------------------------------\n");
        System.out.println("**** MENU ENFERMEIRO ****\n");
        System.out.println("[1] -> Listar Enfermeiros");
        System.out.println("[2] -> Listar Doentes");
        System.out.println("[3] -> Listar Médicos");
        System.out.println("[0] -> Logout");
        System.out.println("----------------------------------------");
    }

    public static void DntOpts() {
        System.out.println("----------------------------------------\n");
        System.out.println("**** MENU DOENTE ****\n");
        System.out.println("[1] -> Listar Médicos");
        System.out.println("[2] -> Listar Consultas");
        System.out.println("[0] -> Logout");
        System.out.println("----------------------------------------");
    }

    /**
     * Método criado para otimizar tempo
     * Cria e retorna um Scanner para a leitura de dados no sistema.
     *
     * @return um Scanner para a leitura de dados no sistema
     */
    public static Scanner scanner() {
        return new Scanner(System.in);
    }

    /**
     * Método para ler as opções escolhidas nos menus
     * @return número da opção escolhida
     */
    public static int lerOpts() {
        System.out.print("Opção desejada: ");
        return scanner().nextInt();
    }
}
