package pt.isel.leim.sbd2;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Auxiliar {
	private static Scanner input = new Scanner(System.in);
	
	public static void printArray(String[] array) {
		// Verifica se o array está vazio ou nulo
	    if (array == null || array.length == 0) {
	        System.out.println("[]");
	        return;
	    }
	    
	    // Imprime o array no formato [elemento1, elemento2, ..., elementoN]
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    
	    for (int i = 0; i < array.length; i++) {
	        sb.append(array[i]);
	        if (i < array.length - 1) {
	            sb.append(", ");
	        }
	    }
	    
	    sb.append("]");
	    System.out.println(sb.toString());
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
    public static int askNumFiscal(Scanner input) {
        int numeroFiscal = 0;
        boolean validInput = false;
        do {
            System.out.print("Digite o seu numero Fiscal: ");
            if (input.hasNextInt()) {
                numeroFiscal = input.nextInt();
                if (validNumFiscal(String.valueOf(numeroFiscal))) {
                    validInput = true;
                } else {
                    System.out.println("Erro! Numero Fiscal Inválido! Tente novamente.");
                }
            } else {
                System.out.println("Erro! Entrada inválida! Por favor, insira apenas números.");
                input.next();
            }
        } while (!validInput);

        return numeroFiscal;
    }
    
    public static long askNumCarta(Scanner input) {
    	long numeroCarta = 0;
        boolean validInput = false;
        do {
            System.out.print("Digite o seu numero da carta de conducao: ");
            if (input.hasNextLong()) {
            	numeroCarta = input.nextLong();
                if (valdiarNumero12Digitos(String.valueOf(numeroCarta))) {
                    validInput = true;
                } else {
                    System.out.println("Erro! Numero da carta Inválido! Tente novamente.");
                }
            } else {
                System.out.println("Erro! Entrada inválida! Por favor, insira apenas números.");
                input.next();
            }
        } while (!validInput);

        return numeroCarta;
    }

    /**
     * 
     * @param numeroFiscal
     * @return
     */
    public static boolean validNumFiscal(String numeroFiscal) {
        return numeroFiscal.length() == 9;
    }
    
    public static boolean valdiarNumero9Digitos(String numeroFiscal) {
        return numeroFiscal.length() == 9;
    }
    
    public static boolean valdiarNumero12Digitos(String numeroFiscal) {
        return numeroFiscal.length() == 12;
    }

    /**
     * 
     * @param input
     * @return
     */
    public static String askMatricula(Scanner input) {
        String matricula = "";
        boolean validInput = false;
        do {
            System.out.print("Digite a matricula do veiculo desejado: ");
            matricula = input.next();
            if (validMatricula(matricula)) {
                validInput = true;
            } else {
                System.out.println("Erro! Matricula Inválida! Tente novamente.");
            }
        } while (!validInput);

        return matricula;
    }

    /**
     * 
     * @param matricula
     * @return
     */
    public static boolean validMatricula(String matricula) {
        String regex = "\\d{2}-[A-Z]{2}-\\d{2}";
        return matricula != null && matricula.matches(regex);
    }
    
    /**
     * 
     * @param input
     * @return
     */
    public static String askData(Scanner input) {
	    int ano, mes, dia;

        // Solicita o ano
        System.out.print("Digite o ano: ");
        while (!input.hasNextInt()) {
            System.out.println("Entrada invalida! Por favor, insira um número valido para o ano.");
            input.next();
        }
        ano = input.nextInt();

        // Solicita o mês
        System.out.print("Digite o mes (1-12): ");
        while (!input.hasNextInt() || (mes = input.nextInt()) < 1 || mes > 12) {
            System.out.println("Entrada invalida! Por favor, insira um numero entre 1 e 12 para o mes.");
            if (!input.hasNextInt()) input.next();
        }

        // Solicita o dia
        System.out.print("Digite o dia (1-31): ");
        while (!input.hasNextInt() || (dia = input.nextInt()) < 1 || dia > 31) {
            System.out.println("Entrada invalida! Por favor, insira um numero entre 1 e 31 para o dia.");
            if (!input.hasNextInt()) input.next();
        }

        // Formata a data no formato "ano-mes-dia"
        String dataFormatada = String.format("%04d-%02d-%02d", ano, mes, dia);
        return dataFormatada;
	}
  
    /**
     * 
     * @param input
     * @return
     */
    public static String askHora(Scanner input) {
        int horas, minutos, segundos;

        // Solicita as horas
        System.out.print("Digite as horas (0-23): ");
        while (!input.hasNextInt() || (horas = input.nextInt()) < 0 || horas > 23) {
            System.out.println("Entrada invalida! Por favor, insira um numero entre 0 e 23 para as horas.");
            if (!input.hasNextInt()) input.next();
        }

        // Solicita os minutos
        System.out.print("Digite os minutos (0-59): ");
        while (!input.hasNextInt() || (minutos = input.nextInt()) < 0 || minutos > 59) {
            System.out.println("Entrada invalida! Por favor, insira um numero entre 0 e 59 para os minutos.");
            if (!input.hasNextInt()) input.next();
        }

        // Solicita os segundos
        System.out.print("Digite os segundos (0-59): ");
        while (!input.hasNextInt() || (segundos = input.nextInt()) < 0 || segundos > 59) {
            System.out.println("Entrada invalida! Por favor, insira um numero entre 0 e 59 para os segundos.");
            if (!input.hasNextInt()) input.next();
        }

        // Formata a hora no formato "HH:MM:SS"
        String horaFormatada = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        return horaFormatada;
    }
    
    /**
     * 
     * @param input
     * @return
     * @throws SQLException 
     */
    public static String[] reservarVeiculo(Connection conn, Scanner input) throws SQLException {
    	String[] camposReserva = new String[7];

        System.out.println("Digite o número fiscal:");
        // Tentar fazer com um getNumFiscal do cliente?
        int numeroFiscal = askNumFiscal(input);
        camposReserva[0] = String.valueOf(numeroFiscal);
        
        input.nextLine(); 
        
        System.out.println("Digite o parque de levantamento:");
        camposReserva[1] = input.nextLine();
        
        List<String> listaTipos = BD_backend.listarTiposVeiculo(conn, camposReserva[1]);
        System.out.println("Digite o tipo de carro (ex: 1):");
        camposReserva[2] = listaTipos.get(Integer.valueOf(input.nextLine())-1);

        System.out.println("Digite a data e hora de início:");
        String dataHoraIncio = askData(input);
        dataHoraIncio = dataHoraIncio + " " + askHora(input);
        camposReserva[3] = dataHoraIncio;

        System.out.println("Digite a data e hora de fim:");
        String dataHoraFim = askData(input);
        dataHoraFim = dataHoraFim + " " + askHora(input);
        camposReserva[4] = dataHoraFim;
        
        
        HashMap<String, String> descontosAtivosCliente = BD_backend.obterDescontosAtivosCliente(conn, String.valueOf(numeroFiscal));
        
        if (descontosAtivosCliente.size()!=0) {
	        int contador = 1; // Contador para a numeração dos itens
	
	        System.out.println("\nDescontos ativos:\n");
		    for (Map.Entry<String, String> entry : descontosAtivosCliente.entrySet()) {
		        String codigo = entry.getKey();
		        String descricao = entry.getValue();
		        System.out.println(contador + " - " + codigo + " -> " + descricao);
		        contador++;
		    }
		    System.out.println(descontosAtivosCliente.size());
		    
		    	System.out.println("Digite o código de desconto (ex: 1): ");
			    
			    int codEscolhido = input.nextInt();
			    
			    // Validar se a escolha é válida
			    if (codEscolhido > 0 && codEscolhido <= descontosAtivosCliente.size()) {
			        // Percorrer o HashMap e encontrar o código correspondente à escolha
			        contador = 1;
			        for (Map.Entry<String, String> entry : descontosAtivosCliente.entrySet()) {
			            if (contador == codEscolhido) {
			                String codigoSelecionado = entry.getKey();
			                System.out.println("Você selecionou o código: " + codigoSelecionado);
			                camposReserva[5] =  codigoSelecionado;
			                BD_backend.apagaDescontoCliente(conn, String.valueOf(numeroFiscal));
			            }
			            contador++;
			        }
			    } else {
			        System.out.println("Escolha inválida. Por favor, selecione um número entre 1 e " + descontosAtivosCliente.size());
			    }
	    }else {
	    	camposReserva[5] = null;
	    }
	    
	    
	    int numDias = calcularDias(camposReserva[3], camposReserva[4]);
	    float custoFinal = calcularCustoFinal(conn, camposReserva[0], camposReserva[2], numDias, camposReserva[5]);
	    
	    camposReserva[6] = String.valueOf(custoFinal);
        
        System.out.println("Info sobre a Reserva: ");
        printArray(camposReserva);
        
        BD_backend.adiconarReserva(conn, camposReserva);
        return camposReserva;
    }
    
    public static float calcularCustoFinal(Connection conn, String numeroFiscal, String categoria, int numDias, String desconto) throws SQLException {
    	float custoFinal = 0.0f;
    	
    	// Obter moeda do cliente
    	String moeda = BD_backend.obterMoedaCliente(conn, numeroFiscal);
    	
    	// Obter cambio da moeda
    	String cambio = BD_backend.obterCambio(conn, moeda);
    	
    	// Obter tarifa da categoria
    	String tarifa = BD_backend.obterTarifaCategoria(conn, categoria);

    	float desc = 0.0f;;
    	
    	if (desconto != null){
    		desc = Float.valueOf(BD_backend.obterValorDesconto(conn, desconto));
    	}
    	
    	custoFinal = Float.parseFloat(cambio) * (float) numDias * Float.parseFloat(tarifa);
    		
    	custoFinal = custoFinal - custoFinal * desc;
    	return custoFinal;
    }

    public static int calcularDias(String dataInicio, String dataFim) {
    	 // Definir o formato esperado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Converter as strings para LocalDateTime
        LocalDateTime inicio = LocalDateTime.parse(dataInicio, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataFim, formatter);

        // Calcular os dias entre as duas datas
        long dias = ChronoUnit.DAYS.between(inicio, fim);

        return (int) dias; // Retornar como int
    }
    
    public static void consultarReptucaoDescontosCliente(Connection conn, String numeroFiscal) throws SQLException {
    	
    	String reputacaoString = BD_backend.obterReputacaoCliente(conn, numeroFiscal);
    	System.out.println("\nReputacao do cliente: " + reputacaoString + "\n");
    	
    	HashMap<String, String> descontos = BD_backend.obterDescontosAtivosCliente(conn, numeroFiscal);
    	System.out.println("Descontos do cliente");
    	
    	int contador = 1; // Para contar os valores
    	for (Map.Entry<String, String> entry : descontos.entrySet()) {
    	    System.out.println(entry.getKey() + " -> " + entry.getValue());
    	    contador++;
    	}
    	
    }
    
    public static String entregarVeiculo(Connection conn, String matricula, Scanner input) throws SQLException {
    	
    	String tipoVeiculo = BD_backend.obterTipoVeiculoPorMatricula(conn, matricula);
    	
    	input.nextLine(); 
    	
    	System.out.println("Digite o parque de levantamento:");
        String localidade = input.nextLine();
        
        String[] lugar = BD_backend.obterLugaresLivresLocalidadeCategoria(conn, localidade, tipoVeiculo);
        Auxiliar.printArray(lugar);
        
        BD_backend.ocuparLugar(conn, lugar);
        BD_backend.associarVeiculo(conn, matricula, lugar);
        
        String entregaString = "Veiculo com a matricula: " + matricula + " guardado no parque em "+ localidade + 
        		"no Piso " + lugar[1] +", Fila "+ lugar[2] + " e Posicao: " + lugar[3];
        
        System.out.println(entregaString);
        
        String[] aluguer = BD_backend.verAluguerAtualCondutor(conn, matricula);
        
        BD_backend.mudarStatusAluguer(conn, "Concluído", aluguer[0], aluguer[9]);
        
        return entregaString;
    }
    
    public static void atribuirVeiculosReservas(Connection conn, Scanner input) throws SQLException {
        System.out.println("Reservas feitas:\n");

        // Exibe todas as reservas
        BD_backend.consultarTodasReservas(conn);

        // Solicitar ao usuário a escolha de uma linha
        System.out.println("\nDigite o número da linha da reserva pretendida (exemplo: 1 para a primeira linha):");
        int escolha = input.nextInt();
        input.nextLine(); // Consumir nova linha

        // Mostrar a linha escolhida
        String query = "SELECT * FROM reserva";
        String[] reserva = BD_backend.showDataLinha(query, conn, escolha);
        
        printArray(reserva);
       
        System.out.println("\nVeiculos "+ reserva[2] +" disponiveis em "+ reserva[3] + ": \n");
        BD_backend.obterVeiculoPorTipo(conn, reserva[2], reserva[3]);
        
        System.out.println("\nDigite o número da linha do veiculo pretendida (exemplo: 1 para a primeira linha):");
        int escolha2 = input.nextInt();
        input.nextLine(); // Consumir nova linha
        
        String query2 = "SELECT * FROM vw_veiculosparque";
        String[] veiculo = BD_backend.showDataLinha(query2, conn, escolha2);
        
        printArray(veiculo);
        
        System.out.println("\nLista de Condutores: ");
        BD_backend.consultarCondutores(conn);
        
        // Solicitar ao usuário a escolha de uma linha
        System.out.println("\nDigite o número da linha do condutor pretendido (exemplo: 1 para a primeira linha):");
        int escolha3 = input.nextInt();
        input.nextLine(); // Consumir nova linha

        // Mostrar a linha escolhida
        String query3 = "SELECT * FROM condutor";
        String[] condutorInfo = BD_backend.showDataLinha(query3, conn, escolha3);
        
        printArray(condutorInfo);
        
        String dataHoraInicio = reserva[4];
        String dataHoraFim = reserva[5];
        String custoFinal = reserva[8];
        String numeroFiscal = reserva[1];
        String numeroCartaConducao = condutorInfo[0];
        String codigo = reserva[7];
        String matricula = veiculo[0];
        
        BD_backend.adicionarAluguer(conn, dataHoraInicio, dataHoraFim, custoFinal, numeroFiscal, numeroCartaConducao, codigo, matricula);
        BD_backend.eliminarReserva(conn, reserva[0]);
    }

    public static String[] obterDadosIntervencao(Scanner scanner) {
        // Criação do array para armazenar os dados
        String[] dados = new String[4];
        scanner.nextLine();
        
        // Perguntar pela matrícula
        System.out.print("Digite a matrícula do veículo: ");
        dados[0] = scanner.nextLine();  // Captura a matrícula

        // Perguntar pela quilometragem
        System.out.print("Digite a quilometragem do veículo: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, insira um número válido para a quilometragem.");
            scanner.next(); // descarta a entrada inválida
        }
        dados[1] = String.valueOf(scanner.nextInt()); // Converte para String para armazenar no array

        // Limpar o buffer do scanner após a leitura de um número
        scanner.nextLine();  // Esse nextLine() limpa o buffer

        // Perguntar pelo tipo de intervenção
        System.out.print("Digite o tipo de intervenção (máximo 50 caracteres): ");
        dados[2] = scanner.nextLine();

        // Garantir que o tipo de intervenção tenha no máximo 50 caracteres
        if (dados[2].length() > 50) {
            dados[2] = dados[2].substring(0, 50);
        }
        
        System.out.println("\nDigite a data da Intervencao:\n");
        
        dados[3] = askData(input);

        return dados;
    }
    
    public static String[][] separarString(String input) {
        if (input == null) {
            return null;
        }
        // Dividir a string usando o delimitador "-"
        String[] reservasStr = input.split("\n");

        String[][] reservasPartes = new String[reservasStr.length][reservasStr[0].split("#").length];
        for (int i = 0; i < reservasStr.length; i++) {
            String[] partes = reservasStr[i].split("#");
            for (int j = 0; j < partes.length; j++) {
                reservasPartes[i][j] = partes[j];
            }
        }

        return reservasPartes;
    }
    
	public static String entregarVeiculoWeb(Connection conn, String matricula, String localidade) throws SQLException {
	
	        String tipoVeiculo = BD_backend.obterTipoVeiculoPorMatricula(conn, matricula);
	
	        String[] lugar = BD_backend.obterLugaresLivresLocalidadeCategoria(conn, localidade, tipoVeiculo);
	        //Auxiliar.printArray(lugar);
	
	        BD_backend.ocuparLugar(conn, lugar);
	        BD_backend.associarVeiculo(conn, matricula, lugar);
	
	        String entregaString = "Veiculo com a matricula: " + matricula + " guardado no parque em "+ localidade + 
	                "no Piso " + lugar[1] +", Fila "+ lugar[2] + " e Posicao: " + lugar[3];
	
	        //System.out.println(entregaString);
	
	        String[] aluguer = BD_backend.verAluguerAtualCondutor(conn, matricula);
	
	        BD_backend.mudarStatusAluguer(conn, "Concluído", aluguer[0], aluguer[9]);
	
	        return entregaString;
	    }

    
    public static String askString(Scanner input) {
    	String texto = "";
        boolean validInput = false;
        do {
            if (input.hasNextLine()) {
            	texto = input.nextLine();
                if (!texto.isEmpty()) {
                    validInput = true;
                } else {
                    System.out.println("Erro! Tente novamente.");
                }
            } else {
                System.out.println("Erro! Entrada inválida.");
                input.next();
            }
        } while (!validInput);

        return texto;
    }
    
    public static String askMorada(Scanner input) {
        String morada = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Digite a morada no formato 'Rua, numero': ");
            morada = input.nextLine().trim();

            // Verificar se a morada está no formato correto: Rua, número
            if (morada.matches("^[^,]+,\\s*\\d+$")) {
                isValid = true;
            } else {
                System.out.println("Formato inválido. Certifique-se de que a morada está no formato 'Rua, numero'.");
            }
        }

        return morada;
    }
    
    public static String askLinguas(Scanner input) {
        String linguasInput = "";
        boolean isValid = false;

        // Regex para validar o formato: uma ou mais línguas separadas por vírgulas
        String regex = "^[^,]+(,\\s*[^,]+)*$";

        while (!isValid) {
            System.out.print("Digite as línguas separadas por vírgulas (Ex: Português, Inglês): ");
            linguasInput = input.nextLine().trim();

            if (linguasInput.matches(regex)) {
                isValid = true;
            } else {
                System.out.println("Formato inválido. Certifique-se de que as línguas estão separadas por vírgulas.");
            }
        }

        // Dividir a entrada em um array de línguas, removendo espaços extras
        String[] linguas = linguasInput.split("\\s*,\\s*");

        // Construir a String no formato desejado
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < linguas.length; i++) {
            sb.append("\"").append(linguas[i]).append("\"");
            if (i < linguas.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }
    
    public static String askMoeda(Scanner input) {
        // Lista de siglas válidas
        List<String> moedasValidas = Arrays.asList("USD", "EUR", "BRL", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR");
        String moeda = "";
        boolean isValid = false;

        System.out.println("Siglas de moedas disponíveis: " + String.join(", ", moedasValidas));

        while (!isValid) {
            System.out.print("Digite a sigla da moeda: ");
            moeda = input.nextLine().trim().toUpperCase();

            // Verificar se a sigla está na lista de moedas válidas
            if (moedasValidas.contains(moeda)) {
                isValid = true;
            } else {
                System.out.println("Sigla inválida. Por favor, escolha uma das siglas disponíveis.");
            }
        }

        return moeda;
    }
    
    public static String askTipoCliente(Scanner input) {
        String tipoCliente = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Escolha o tipo de cliente:");
            System.out.println("1 - Pessoa");
            System.out.println("2 - Empresa");
            System.out.print("Digite o número correspondente: ");

            String escolha = input.nextLine().trim();

            if (escolha.equals("1")) {
                tipoCliente = "Pessoa";
                isValid = true;
            } else if (escolha.equals("2")) {
                tipoCliente = "Empresa";
                isValid = true;
            } else {
                System.out.println("Entrada inválida. Por favor, escolha '1' para Pessoa ou '2' para Empresa.");
            }
        }

        return tipoCliente;
    }
    
    public static String askCapitalSocial(Scanner input) {
        String capitalSocial = "";
        boolean isValid = false;

        // Regex para validar o formato: dígitos seguidos de vírgula e até 2 casas decimais
        String regex = "^\\d+(,\\d{1,2})?$";

        while (!isValid) {
            System.out.print("Digite o valor do capital social no formato 15,2: ");
            capitalSocial = input.nextLine().trim();

            if (capitalSocial.matches(regex)) {
                isValid = true;
            } else {
                System.out.println("Formato inválido. O valor deve estar no formato 15,2 (vírgula como separador).");
            }
        }

        // Substituir vírgula por ponto para validação e cálculos internos, se necessário
        double valor = Double.parseDouble(capitalSocial.replace(",", "."));

        // Garantir que o valor seja formatado corretamente ao retornar
        return String.format("%.2f", valor).replace(",", ".");
    }
    
    public static void criarCliente(Connection conn, Scanner input) throws SQLException {
    	Integer numeroFiscal = askNumFiscal(input);
    	input.nextLine();
    	System.out.println("Digite o seu nome: ");
    	String nome = askString(input);
    	
    	System.out.println("Digite o seu contacto telefónico: ");
    	String contacto = askString(input);
    	if (valdiarNumero9Digitos(contacto))
    	{
    		System.out.println(contacto);
    	}
    	
    	String morada = askMorada(input);
    	System.out.println("Digite o seu distrito: ");
    	String distrito = askString(input);
    	System.out.println("Digite o seu concelho: ");
    	String concelho = askString(input);
    	System.out.println("Digite o seu freguesia: ");
    	String freguesia = askString(input);
    	
    	String preferenciasLinguisticasECulturais = askLinguas(input);
    	
    	String moeda = askMoeda(input);
    	
    	String tipo = askTipoCliente(input);
    	
    	String capitalSocial = null;
    	
    	if (tipo == "Empresa"){
    		capitalSocial = askCapitalSocial(input);
    	}
    	
    	BD_backend.adicionarCliente(conn, String.valueOf(numeroFiscal), nome, contacto, morada, distrito, concelho, freguesia, preferenciasLinguisticasECulturais, moeda, tipo, capitalSocial);
    	BD_backend.adicionarDescontoCliente(conn, String.valueOf(numeroFiscal), "D00001");
    }
    
    public static void criarCondutor(Connection conn, Scanner input) throws SQLException {
    	long numeroCartaConducao = askNumCarta(input);
    	input.nextLine();
    	
    	System.out.println("Digite o seu nome: ");
    	String nome = askString(input);
    	
    	System.out.println("Digite a data da validade da carta de conducao: ");
    	String dataCarta = askData(input);
    	
    	
    	BD_backend.adicionarCondutor(conn, String.valueOf(numeroCartaConducao), nome, dataCarta);
    }
    
    public static void criarVeiculo(Connection conn, Scanner input) throws SQLException {
    	String matricula = askMatricula(input);
    	input.nextLine();
    	
    	System.out.println("Digite a marca: ");
    	String marca = askString(input);
    	
    	System.out.println("Digite o modelo: ");
    	String modelo = askString(input);
    	
    	System.out.println("Digite a cor: ");
    	String cor = askString(input);
    	
    	String tipo = "";
    	do{
            System.out.println("Digite o tipo do carro (Ex: Familiar): ");
            tipo = askString(input);
        }while(!BD_backend.validarTipoVeiculo(conn, tipo));
    	
    	System.out.println("Digite o numero de Lugares: ");
    	String numeroLugares = askString(input);
    	
    	System.out.println("Digite o numero de Portas: ");
    	String numeroPortas = askString(input);
    	
    	System.out.println("Digite a capacida de Carga: ");
    	String capacidadeCarga = askString(input);
    	
    	System.out.println("Digite o tipo de Motor: ");
    	String tipoMotor = askString(input);
    	
    	System.out.println("Digite a potencia: ");
    	String potencia = askString(input);
    	
    	String localidade = "";
    	do{
            System.out.println("Digite a localidade do parque do veiculo: ");
            localidade = askString(input);
        }while(!BD_backend.validarLocalidade(conn, localidade));
    	
    	BD_backend.adicionarVeiculo(conn, matricula, marca, modelo, cor, tipo, numeroLugares, numeroPortas, capacidadeCarga, tipoMotor,potencia, localidade);
    }
    
    public static String askCampoCliente(Scanner input) {
        // Array com os campos disponíveis
        String[] campos = {
            "numeroFiscal",
            "nome",
            "contacto",
            "morada",
            "distrito",
            "concelho",
            "freguesia",
            "preferenciasLinguisticasECulturais",
            "moeda",
            "tipoCliente",
            "capitalSocial"
        };

        // Listar os campos numerados
        System.out.println("Selecione um dos campos do cliente:");
        for (int i = 0; i < campos.length; i++) {
            System.out.println((i + 1) + " - " + campos[i]);
        }

        int escolha = -1;
        boolean isValid = false;

        // Solicitar a escolha do usuário
        while (!isValid) {
            System.out.print("Digite o número correspondente ao campo desejado: ");
            try {
                escolha = Integer.parseInt(input.nextLine().trim());

                // Validar se a escolha está dentro do intervalo
                if (escolha >= 1 && escolha <= campos.length) {
                    isValid = true;
                } else {
                    System.out.println("Número inválido. Escolha um número entre 1 e " + campos.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            }
        }

        // Retornar o campo correspondente
        return campos[escolha - 1];
    }
    
    public static String askCampoCondutor(Scanner input) {
        // Array com os campos disponíveis
        String[] campos = {
            "numeroCartaConducao",
            "nome",
            "validadeCarta"
        };

        // Listar os campos numerados
        System.out.println("Selecione um dos campos do condutor:");
        for (int i = 0; i < campos.length; i++) {
            System.out.println((i + 1) + " - " + campos[i]);
        }

        int escolha = -1;
        boolean isValid = false;

        // Solicitar a escolha do usuário
        while (!isValid) {
            System.out.print("Digite o número correspondente ao campo desejado: ");
            try {
                escolha = Integer.parseInt(input.nextLine().trim());

                // Validar se a escolha está dentro do intervalo
                if (escolha >= 1 && escolha <= campos.length) {
                    isValid = true;
                } else {
                    System.out.println("Número inválido. Escolha um número entre 1 e " + campos.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            }
        }

        // Retornar o campo correspondente
        return campos[escolha - 1];
    }
    
    public static String askCampoVeiculo(Scanner input) {
        // Array com os campos disponíveis
        String[] campos = {
            "matricula",
            "marca",
            "modelo",
            "cor",
            "tipo",
            "numeroLugares",
            "numeroPortas",
            "capacidadeCarga",
            "tipoMotor",
            "potencia",
            "localidade"
        };

        // Listar os campos numerados
        System.out.println("Selecione um dos campos do veiculo:");
        for (int i = 0; i < campos.length; i++) {
            System.out.println((i + 1) + " - " + campos[i]);
        }

        int escolha = -1;
        boolean isValid = false;

        // Solicitar a escolha do usuário
        while (!isValid) {
            System.out.print("Digite o número correspondente ao campo desejado: ");
            try {
                escolha = Integer.parseInt(input.nextLine().trim());

                // Validar se a escolha está dentro do intervalo
                if (escolha >= 1 && escolha <= campos.length) {
                    isValid = true;
                } else {
                    System.out.println("Número inválido. Escolha um número entre 1 e " + campos.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            }
        }

        // Retornar o campo correspondente
        return campos[escolha - 1];
    }
    
    public static String askNovoValorCliente(String campo) {
    	String novoCampo = "";
    	
    	if(campo =="numeroFiscal") {
    		novoCampo = String.valueOf(askNumFiscal(input));
    	}else if (campo == "contacto") {
    		System.out.println("Digite o seu contacto telefónico: ");
    		novoCampo = askString(input);
        	if (valdiarNumero9Digitos(novoCampo))
        	{
        		System.out.println(novoCampo);
        	}
		}else if (campo == "morada") {
			novoCampo = askMorada(input);
		}else if (campo == "moeda") {
			novoCampo = askMoeda(input);
		}else if (campo == "tipoCliente") {
			novoCampo = askTipoCliente(input);
		}else if (campo == "capitalSocial") {
			novoCampo = askCapitalSocial(input);
		}else if (campo == "preferenciasLinguisticasECulturais") {
			novoCampo = askLinguas(input);
		}else {
			System.out.println("Digite o novo valor para o campo "+ campo +": ");
			novoCampo = askString(input);
		}
    	
    	return novoCampo;
    }
    
    public static String askNovoValorCondutor(String campo) {
    	String novoCampo = "";
    	
    	if(campo =="numeroCartaConducao") {
    		novoCampo = String.valueOf(askNumCarta(input));
    	}else if (campo == "validadeCarta") {
    		System.out.println("Digite a validade da Carta: ");
    		novoCampo = askData(input);
		}else {
			System.out.println("Digite o novo valor para o campo "+ campo +": ");
			novoCampo = askString(input);
		}
    	
    	return novoCampo;
    }
    
    public static String askNovoValorVeiculo(Connection conn, String campo) throws SQLException {
    	String novoCampo = "";
    	
    	if(campo =="matricula") {
    		novoCampo = askMatricula(input);
    	}else if (campo == "tipo") {
    		do{
                System.out.println("Digite o tipo do carro (Ex: Familiar): ");
                novoCampo = askString(input);
            }while(!BD_backend.validarTipoVeiculo(conn, novoCampo));
    	}else if (campo == "localidade") {
    		do{
                System.out.println("Digite a localdiade do parque do Veiculo: ");
                novoCampo = askString(input);
            }while(!BD_backend.validarLocalidade(conn, novoCampo));
		}else {
			System.out.println("Digite o novo valor para o campo "+ campo +": ");
			novoCampo = askString(input);
		}
    	
    	return novoCampo;
    }
    
    public static void updateCampoCliente(Connection conn, String numeroFiscal, String campo, String valor) throws SQLException {
    	if (campo == "capitalSocial") {
    		BD_backend.updateEmpresa(conn, numeroFiscal, "capitalSocial", valor);
    	}else if (campo == "numeroFiscal") {
    		BD_backend.updateCliente(conn, numeroFiscal, "numeroFiscal", valor);
    		if(BD_backend.verTipoCliente(conn, numeroFiscal) == "Empresa") {
    			BD_backend.updateEmpresa(conn, numeroFiscal, "numeroFiscal", valor);
    		}else {
    			BD_backend.updatePessoa(conn, numeroFiscal, "numeroFiscal", valor);
    		}
		}else {
			BD_backend.updateCliente(conn, numeroFiscal, campo, valor);
		}
    }
    
    public static void updateCampoCondutor(Connection conn, String numeroFiscal, String campo, String valor) throws SQLException {
		BD_backend.updateCondutor(conn, numeroFiscal, campo, valor);
	}
    
    public static void updateCampoVeiculo(Connection conn, String matricula, String campo, String valor) throws SQLException {
		BD_backend.updateVeiculo(conn, matricula, campo, valor);
	}
    
    public static void atualizarCliente(Connection conn, Scanner input) throws SQLException {
    	String numeroFiscalString = String.valueOf(askNumFiscal(input));
    	
    	String campo = askCampoCliente(input);
    	
    	String valor = askNovoValorCliente(campo);
    	
    	updateCampoCliente(conn, numeroFiscalString, campo, valor);
    }
    
    public static void atualizarCondutor(Connection conn, Scanner input) throws SQLException {
    	String numeroCartaString = String.valueOf(askNumCarta(input));
    	
    	String campo = askCampoCondutor(input);
    	
    	String valor = askNovoValorCondutor(campo);
    	
    	updateCampoCondutor(conn, numeroCartaString, campo, valor);
    }
    
	public static void exportarDados(Connection conn, Scanner input) throws SQLException, IOException {
    	String matricula = askMatricula(input);
    	
    	BD_backend.exportJSON(conn, matricula, null);
    }
    
    public static void importarDados(Connection conn, Scanner input) throws SQLException, IOException {
    	System.out.println("Tenha em atenção que o veiculo deve existir na Base de Dados antes de importar os seus dados ");
    	System.out.println("Digite o filepath do ficheiro a importar: ");
    	String filePath = askString(input);
    	
    	BD_backend.importJSON(conn, filePath);
    }
    
    public static void atualizarVeiculo(Connection conn, Scanner input) throws SQLException {
    	String matricula = askMatricula(input);
    	
    	String campo = askCampoVeiculo(input);
    	
    	String valor = askNovoValorVeiculo(conn, campo);
    	
    	updateCampoVeiculo(conn, matricula, campo, valor);
    }
    
    public static void ranking3MenorLucro(Connection conn) throws SQLException {
        // Query para calcular o ranking
        String query = 
            "SELECT " +
            "    v.marca, " +
            "    SUM(a.custoFinal / c.valorPorDolar) AS lucroEmDolares " +
            "FROM Aluguer a " +
            "JOIN Cliente cl ON a.numeroFiscal = cl.numeroFiscal " +
            "JOIN Cambio c ON cl.moeda = c.codigoMoeda " +
            "JOIN Veiculo v ON a.matricula = v.matricula " +
            "GROUP BY v.marca " +
            "ORDER BY lucroEmDolares ASC " +
            "LIMIT 3;";

        // Executar a query
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        // Exibir o resultado
        //System.out.println("Ranking das 3 marcas de veículos com menor lucro:");
        while (rs.next()) {
            String marca = rs.getString("marca");
            double lucro = rs.getDouble("lucroEmDolares");
            System.out.printf("%s # $%.2f%n", marca, lucro);
        }

        // Fechar recursos
        rs.close();
        stm.close();
    }
    

    public static void ranking5Modelos(Connection conn, String dataInicio, String dataFim) throws SQLException {
        // Query para calcular o ranking
        String query = 
            "SELECT " +
            "    v.marca, " +
            "    v.modelo, " +
            "    AVG(a.avaliacao) AS mediaAvaliacao " +
            "FROM Aluguer a " +
            "JOIN Veiculo v ON a.matricula = v.matricula " +
            "WHERE a.avaliacao IS NOT NULL " +
            "  AND a.dataHoraInicio >= '" + dataInicio + "' " +
            "  AND a.dataHoraInicio <= '" + dataFim + "' " +
            "GROUP BY v.marca, v.modelo " +
            "ORDER BY mediaAvaliacao DESC " +
            "LIMIT 5;";

        // Executar a query
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        // Exibir o resultado
        //System.out.println("Ranking dos 5 modelos de veículos com melhor avaliação:");
        while (rs.next()) {
            String marca = rs.getString("marca");
            String modelo = rs.getString("modelo");
            double mediaAvaliacao = rs.getDouble("mediaAvaliacao");
            // MARCA / MODELO / MEDIA AVALIACAO
            System.out.printf("%s # %s # %.2f%n", marca, modelo, mediaAvaliacao);
        }

        // Fechar recursos
        rs.close();
        stm.close();
    }
	
	public static void ranking10MenorQuilometragem(Connection conn, String dataInicio, String dataFim) throws SQLException {
	    // Query para calcular o ranking
	    String query = 
	        "SELECT " +
	        "    matricula, " +
	        "    SUM(quilometragem) AS quilometragemTotal " +
	        "FROM Intervencao " +
	        "WHERE dataIntervencao >= '" + dataInicio + "' " +
	        "  AND dataIntervencao <= '" + dataFim + "' " +
	        "GROUP BY matricula " +
	        "ORDER BY quilometragemTotal ASC " +
	        "LIMIT 10;";

	    // Executar a query
	    Statement stm = conn.createStatement();
	    ResultSet rs = stm.executeQuery(query);

	    // Exibir o resultado
	    //System.out.println("Ranking dos 10 veículos com menor quilometragem somada:");
	    while (rs.next()) {
	        String matricula = rs.getString("matricula");
	        int quilometragemTotal = rs.getInt("quilometragemTotal");
	        // Matricula / Quilometragem /
	        System.out.printf("%s # %d km%n", matricula, quilometragemTotal);
	    }

	    // Fechar recursos
	    rs.close();
	    stm.close();
	}

	public static void ranking100ClientesPorReputacao(Connection conn, String freguesia) throws SQLException {
	    // Construir a query com ou sem filtro de freguesia
	    String query = "SELECT " +
	                   "nome, " +
	                   "reputacao, " +
	                   "freguesia " +
	                   "FROM vw_clientecomreputacao " +
	                   (freguesia != null && !freguesia.isEmpty() 
	                       ? "WHERE freguesia = '" + freguesia + "' " 
	                       : "") +
	                   "ORDER BY reputacao DESC " +
	                   "LIMIT 100;";

	    // Executar a query
	    Statement stm = conn.createStatement();
	    ResultSet rs = stm.executeQuery(query);

	    // Exibir o resultado
	    //System.out.println("Ranking dos 100 Clientes com maior reputação:");
	    while (rs.next()) {
	        String nome = rs.getString("nome");
	        double reputacao = rs.getDouble("reputacao");
	        String freguesiaCliente = rs.getString("freguesia");
	        // Nome / Reputacao / Freguesia
	        System.out.printf("%s # %.2f # %s%n", nome, reputacao, freguesiaCliente);
	    }

	    // Fechar recursos
	    rs.close();
	    stm.close();
	}
	
	// Método principal
    public static void pesquisarCliente(Connection conn, Scanner input) throws SQLException {
    	// Criar lista de sugestões (numeroFiscal - nome)
        List<String> sugestoes = obterSugestoes(conn);

        // Se a lista estiver vazia, informar e sair
        if (sugestoes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado na base de dados.");
            return;
        }

        // Exibir instruções
        System.out.println("Digite parte do nome do cliente (autocomplete ativo):");

        // Variável para armazenar a entrada do usuário
        String entrada = "";

        while (true) {
            System.out.print("> ");
            
            // Certifique-se de que há entrada disponível
            if (!input.hasNextLine()) {
                System.out.println("Entrada encerrada inesperadamente. Encerrando operação.");
                break;
            }

            entrada = input.nextLine().trim();

            // Filtrar sugestões com base na entrada
            List<String> resultadosFiltrados = filtrarSugestoes(sugestoes, entrada);

            // Se houver apenas uma sugestão, selecionar automaticamente
            if (resultadosFiltrados.size() == 1) {
                System.out.println("Selecionado: " + resultadosFiltrados.get(0));
                String numeroFiscalSelecionado = resultadosFiltrados.get(0).split(" - ")[0];
                BD_backend.exibirDetalhesCliente(conn, numeroFiscalSelecionado);
                break;
            }

            // Exibir sugestões
            if (!resultadosFiltrados.isEmpty()) {
                System.out.println("Sugestões:");
                resultadosFiltrados.forEach(System.out::println);
            } else {
                System.out.println("Nenhuma correspondência encontrada. Tente novamente.");
            }
        }
    }

    // Obter lista de sugestões (numeroFiscal - nome)
    public static List<String> obterSugestoes(Connection conn) throws SQLException {
        List<String> sugestoes = new ArrayList<>();
        String query = "SELECT numeroFiscal, nome FROM vw_clientecomreputacao";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String numeroFiscal = rs.getString("numeroFiscal");
            String nome = rs.getString("nome");
            sugestoes.add(numeroFiscal + " - " + nome);
        }

        rs.close();
        stmt.close();
        return sugestoes;
    }

    // Filtrar sugestões com base na entrada do usuário
    public static List<String> filtrarSugestoes(List<String> sugestoes, String entrada) {
        List<String> resultadosFiltrados = new ArrayList<>();
        for (String sugestao : sugestoes) {
            if (sugestao.toLowerCase().contains(entrada.toLowerCase())) {
                resultadosFiltrados.add(sugestao);
            }
        }
        return resultadosFiltrados;
    }
    
    public static String dataAtual() {
    	LocalDate currentDate = LocalDate.now();
        
        // Converte a data para string no formato yyyy-MM-dd
        String dateString = currentDate.toString();
        
        return dateString;
    }
    
    public static void main(String[] args) {
		//String texto = askString(input);
    	
		//String texto = askString(input);
		//if (valdiarNumero9Digitos(texto))
		//{
		//	System.out.println(texto);
		//}
    	
    	//String morada = askMorada(input);
        //System.out.println("Morada válida: " + morada);
    	
        //String linguas = askLinguas(input);
        //System.out.println(linguas);
    	
    	//String moeda = askMoeda(input);
        //System.out.println("Moeda selecionada: " + moeda);
    	
    	//System.out.println("Cliente: "+ askTipoCliente(input));
    	
    	// Chamar o método e obter o valor do capital social
        //String capitalSocial = askCapitalSocial(input);
    	// Exibir o valor do capital social
        //System.out.println("Capital Social registrado: " + capitalSocial);
    	
    	// Chamar o método e obter o campo selecionado
        //String campoSelecionado = askCampoCliente(input);
        // Exibir o campo selecionado
        //System.out.println("Campo selecionado: " + campoSelecionado);
    	
        //System.out.println(dataAtual());
    	
    	//System.out.println(texto);
	}
}
