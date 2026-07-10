package pt.isel.leim.sbd2;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import javax.lang.model.element.NestingKind;

public class MainMenuApp {
	
	/*private static final String SERVERNAME = "localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://"+SERVERNAME+"/SBD_A50746_A50766";*/
    private static Connection conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());
    private static Scanner input = new Scanner(System.in);

    public static void menuConsola() throws SQLException, IOException {
        int option;
        do {
            System.out.println("===== Menu Principal =====");
            System.out.println("1 - Administrador");
            System.out.println("2 - Cliente");
            System.out.println("3 - Condutor");
            System.out.println("4 - Funcionario");
            System.out.println("5 - Gerente");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opcao: ");

            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next();
            }
            option = input.nextInt();

            switch (option) {
                case 1:
                	Administrador.adminConsola(conn);
                    break;
                case 2:
                    Cliente.clienteConsola(conn);
                    break;
                case 3:
                    Condutor.condutorConsola(conn);
                    break;
                case 4:
                    Funcionario.funcionarioConsola(conn);
                    break;
                case 5:
                	Gerente.gerenteConsola(conn);;
                    break;
                case 0:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opcao invalida! Por favor, escolha entre 0 e 5.");
            }

            System.out.println();
        } while (option != 0);
    }

    public static void main(String[] args) throws SQLException, IOException {
        menuConsola();
    }
}