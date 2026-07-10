package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import javax.lang.model.element.NestingKind;

public class Gerente {
	private static Scanner input = new Scanner(System.in);

    public static void gerenteConsola(Connection conn) throws SQLException {
        int option;
        do {
            System.out.println("===== Menu Gerente =====");
            System.out.println("1 - Apresentar Historico");
            System.out.println("2 - Ranking 3 Marcas Menor Lucro");
            System.out.println("3 - Ranking 5 Modelos Melhor Avaliados");
            System.out.println("4 - Ranking 10 Veiculos Menos Km");
            System.out.println("5 - Ranking 100 Clientes (por Freguesia)");
            System.out.println("0 - Voltar Menu Principal");
            System.out.print("Escolha uma opcao: ");

            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next();
            }
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Apresentar Historico");
                    String matricula = Auxiliar.askMatricula(input);
                    BD_backend.verHistorico(conn, matricula);
                    break;
                case 2:
                    System.out.println("Ranking 3 Marcas Menor Lucro");
                    Auxiliar.ranking3MenorLucro(conn);
                    break;
                    //ranking5Modelos
                case 3:
                    System.out.println("Ranking 5 Modelos Melhor Avaliados");
                    System.out.println("Digite a primeira data do intervalo:");
                    String data1 = Auxiliar.askData(input);
                    System.out.println("Digite a segunda data do intervalo:");
                    String data2 = Auxiliar.askData(input);
                    Auxiliar.ranking5Modelos(conn, data1, data2);
                    break;
                case 4:
                    System.out.println("Ranking 10 Veiculos Menos Km");
                    System.out.println("Digite a primeira data do intervalo:");
                    data1 = Auxiliar.askData(input);
                    System.out.println("Digite a segunda data do intervalo:");
                    data2 = Auxiliar.askData(input);
                    Auxiliar.ranking10MenorQuilometragem(conn, data1, data2);
                    break;
                case 5:
                    System.out.println("Ranking 100 Clientes (por Freguesia)");
                    String freguesia = null;
                    System.out.println("Digite (Caso queira a freguesia por que vai querer filtrar):");
                    input.nextLine();
                    freguesia = input.nextLine();
                    Auxiliar.ranking100ClientesPorReputacao(conn, freguesia);
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