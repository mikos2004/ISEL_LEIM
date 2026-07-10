package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Condutor {
    private static Scanner input = new Scanner(System.in);

    public static void condutorConsola(Connection conn) throws SQLException {
        int option;
        do {
            System.out.println("===== Menu Condutor =====");
            System.out.println("1 - Levantar veículo");
            System.out.println("2 - Entregar Veiculo");
            System.out.println("0 - Voltar Menu Principal");
            System.out.print("Escolha uma opcao: ");

            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next();
            }
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Levantar veículo");
                    System.out.println("Digite o seu número da Carta de Condução:");
                    input.nextLine();
                    String numeroCartaConducao = input.nextLine();
                    BD_backend.verReservasPendentesCondutor(conn, numeroCartaConducao);
                    
                    String query = "SELECT * FROM Aluguer " +
     	                   "WHERE numeroCartaConducao = '" + numeroCartaConducao + "' AND status = 'Pendente';";
                    
                    Integer linha = input.nextInt();
                    input.nextLine();
                    String[] aluguer = BD_backend.showDataLinha(query, conn, linha);
                    
                    String[] lugarLevantamento = BD_backend.levantarVeiculo(conn, aluguer[9]);
                    
                    System.out.println(aluguer[9]);
                    
                    BD_backend.desocuparLugar(conn, lugarLevantamento);
                    BD_backend.desassociarVeiculo(conn, aluguer[9], lugarLevantamento);
                    
                    BD_backend.mudarStatusAluguer(conn, "Em andamento", aluguer[0], aluguer[9]);
                    break;
                case 2:
                    System.out.println("Entregar Veiculo");
                    String matricula = Auxiliar.askMatricula(input);
                    Auxiliar.entregarVeiculo(conn, matricula, input);
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