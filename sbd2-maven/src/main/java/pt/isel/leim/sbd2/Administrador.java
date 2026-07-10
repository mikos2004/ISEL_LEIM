package pt.isel.leim.sbd2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Administrador {
	private static Scanner input = new Scanner(System.in);

    public static void adminConsola(Connection conn) throws SQLException, IOException {
        int option;
        do {
            System.out.println("===== Menu Admintrador =====");
            System.out.println("1 - Criar Cliente");
            System.out.println("2 - Atualizar Cliente");
            System.out.println("3 - Criar Condutor");
            System.out.println("4 - Atualizar Condutor");
            System.out.println("5 - Criar Veiculo");
            System.out.println("6 - Atualizar veiculo");
            System.out.println("7 - Arrumar veiculo");
            System.out.println("8 - Exportar dados veiculo");
            System.out.println("9 - Importar dados veiculo");
            System.out.println("0 - Voltar Menu Principal");
            System.out.print("Escolha uma opcao: ");

            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next();
            }
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Criar Cliente");
                    Auxiliar.criarCliente(conn, input);
                    break;
                case 2:
                    System.out.println("Atualizar Cliente");
                    Auxiliar.atualizarCliente(conn, input);
                    break;
                case 3:
                	System.out.println("Criar Condutor");
                    Auxiliar.criarCondutor(conn, input);
                    break;
                case 4:
                	System.out.println("Atualizar Condutor");
                    Auxiliar.atualizarCondutor(conn, input);
                    break;
                case 5:
                	System.out.println("Criar Veiculo");
                    Auxiliar.criarVeiculo(conn, input);
                    break;
                case 6:
                	System.out.println("Atualizar Veiculo");
                    Auxiliar.atualizarVeiculo(conn, input);
                    break;
                case 7:
                	System.out.println("Arrumar Veiculo");
                    String matricula = Auxiliar.askMatricula(input);
                    BD_backend.arrumaVeiculo(conn, matricula);
                    break;
                case 8:
                    System.out.println("Exportar dados veiculo");
                    Auxiliar.exportarDados(conn, input);
                    break;
                case 9:
                    System.out.println("Importar dados veiculo");
                    Auxiliar.importarDados(conn, input);
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
}
