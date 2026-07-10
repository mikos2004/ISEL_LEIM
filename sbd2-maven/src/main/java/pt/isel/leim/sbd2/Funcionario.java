package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Funcionario {
    private static Scanner input = new Scanner(System.in);
    private static String matricula = "";


    public static void funcionarioConsola(Connection conn) throws SQLException {
        int option;

        do {
            // Mostra o menu do funcionário
            System.out.println("===== Menu Funcionario =====");
            System.out.println("1 - Atribuir Veiculos às Reservas");
            System.out.println("2 - Localizar Veiculo");
            System.out.println("3 - Registar intervencao");
            System.out.println("4 - Pesquisa Cliente Autocomplete");
            System.out.println("5 - Avaliar a reputacao de um cliente");
            System.out.println("6 - Identificar o condutor de um veiculo numa determinada data.");
            System.out.println("0 - Voltar Menu Principal");
            System.out.print("Escolha uma opcao: ");

            // Lê a escolha do utilizador
            while (!input.hasNextInt()) {
                System.out.println("Por favor, insira um numero valido!");
                input.next(); // Limpa a entrada inválida
            }
            option = input.nextInt();

            // Executa ações baseadas na escolha
            switch (option) {
                case 1:
                    System.out.println("Atribuir Veiculos às Reservas");
                    Auxiliar.atribuirVeiculosReservas(conn, input);
                    break;
                case 2:
                	System.out.println("Localizar Veiculo");
                    matricula = Auxiliar.askMatricula(input);
                    BD_backend.consultarLocalizarVeiculo(conn, matricula);
                    break;
                case 3:
                    System.out.println("Registar intervencao");
                    String[] dadoIntervencao = Auxiliar.obterDadosIntervencao(input);
                    BD_backend.adicionarIntervencao(conn, dadoIntervencao);
                    break;
                case 4:
                    System.out.println("Pesquisa Cliente Autocomplete");
                    Auxiliar.pesquisarCliente(conn, input);
                    break;
                case 5:
                    System.out.println("Avaliar a reputacao de um cliente");
                    int numFiscal = Auxiliar.askNumFiscal(input);
                    Double reputacao = Double.valueOf(BD_backend.obterReputacaoCliente(conn, String.valueOf(numFiscal)));
                    //System.out.println(reputacao);
                    
                    if (reputacao >= 3.00  || reputacao == 0.00) {
                    	HashMap<String, String> descontosCliente = BD_backend.obterDescontosAtivosCliente(conn, String.valueOf(numFiscal));
                    	
                    	//System.out.println(descontosCliente.size());
                    	
                    	if (descontosCliente.size() >=1) {
                    		System.out.println("O cliente não pode ter mais descontos até usufruir daqueles que dispõe.");
                    	}else {
                    		BD_backend.verDescontosAtivos(conn);
                    		
                    		String query = "SELECT * FROM vw_DescontosAtivos ;";
                             
                            System.out.println("Escolha o desconto a atribuir");
         	                Integer linha = input.nextInt();
         	                input.nextLine();
         	                String[] desconto = BD_backend.showDataLinha(query, conn, linha);
         	                
         	               BD_backend.adicionarDescontoCliente(conn, String.valueOf(numFiscal), desconto[0]);
                    	}
                    }
                    
                    break;
                case 6:
                	System.out.println("Identificar o condutor de um veiculo numa determinada data.");
                    matricula = Auxiliar.askMatricula(input);
                    String data = Auxiliar.askData(input);
                    BD_backend.identificarCondutor_Matricula_Data(conn, matricula, data);
                    break;
                case 0:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opcao invalida! Por favor, escolha entre 0 e 6.");
            }

            System.out.println(); // Espaçamento entre interações
        } while (option != 0);
    }

}