package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Cliente {
    private static Scanner input = new Scanner(System.in);

    public static void clienteConsola(Connection conn) throws SQLException {
        int option;
        do {
            System.out.println("===== Menu Cliente =====");
            System.out.println("1 - Reservar veiculo");
            System.out.println("2 - Consultar estado reservas");
            System.out.println("3 - Consultar Reputacao e Descontos");
            System.out.println("4 - Avaliar Aluguer");
            System.out.println("0 - Voltar Menu Principal");
            System.out.print("Escolha uma opcao: ");

            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next();
            }
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Reservar veiculo");
                    Auxiliar.reservarVeiculo(conn, input);
                    break;
                case 2:
                    System.out.println("Consultar estado reservas");
                    int numFiscal = Auxiliar.askNumFiscal(input);
                    BD_backend.consultarReservas(conn, String.valueOf(numFiscal));
                    BD_backend.consultarAlugueres(conn, String.valueOf(numFiscal));
                    break;
                case 3:
                    System.out.println("Consultar Reputacao e Descontos");
                    numFiscal = Auxiliar.askNumFiscal(input);
                    Auxiliar.consultarReptucaoDescontosCliente(conn, String.valueOf(numFiscal));
                    break;
                case 4:
                    System.out.println("Avaliar Alugueres");
                    numFiscal = Auxiliar.askNumFiscal(input);
                    BD_backend.alugueresPorAvaliar(conn, String.valueOf(numFiscal));
                    
                    String query = "SELECT * FROM Aluguer " +
     	                   "WHERE numeroFiscal = '" + numFiscal + "' AND avaliacao IS null AND comentario IS null AND status = 'Concluído';";
                    
                    System.out.println("Escolha o aluguer a avaliar");
	                Integer linha = input.nextInt();
	                input.nextLine();
	                String[] aluguer = BD_backend.showDataLinha(query, conn, linha);
	                
	                System.out.println("Escreva um comentário:\n");
	                String comentario = input.nextLine();
	                System.out.println("Dê a sua nota (0,00 a 5,00):\n");
	                Double avaliacao = input.nextDouble();
	                System.out.println(avaliacao);
	                
	                BD_backend.atualizarComentarioAvaliacao(conn, aluguer[0], aluguer[9],comentario, avaliacao);
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
