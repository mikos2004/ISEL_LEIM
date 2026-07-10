package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BD_backend {
	private static final String SERVERNAME = "localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://"+SERVERNAME+"/SBD_A50746_A50766";
	/** get MySQL Connection */
	
	public BD_backend() {
		
	}
	
	public static Connection getConnection(String url, String user, String password) {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("Erro ao conectar com a base de dados: " + e.getMessage());
			return null;
		}
	}
	
	/** executa um comando SQL de alteração de dados: insert, update, delete, ... */
	private static int execute_change(String command, Connection conn) throws SQLException {
		// PRINT
		//System.out.println("Structure/data change: " + command);
		
		// criar um statement para execução directa
		Statement stm = conn.createStatement();
		// executar o comando
		int retVal = stm.executeUpdate(command);
		// libertar statement
		stm.close();
		return retVal;
	}
	
	/** mostra no output o conteúdo das linhas devolvidas pelo select recebido */
	private static void showDataFromSelect(String select, Connection conn) throws SQLException {
		// PRINT
		//System.out.println("Data from: " + select + "\n");
		
		// criar um comando para execução directa
		Statement stm = conn.createStatement();
		// executar o comando
		stm.executeQuery(select);
		// processar resultado
		ResultSet rs = stm.getResultSet();
		// obter o número de linhas devolvidas
		int ncols = rs.getMetaData().getColumnCount();
		// para cada linha - mostrar o seu conteúdo
		while (rs.next()) {
				// obter e mostrar os atributos – índice a começar em 1
				for (int i = 1; i <= ncols; ++i) {
						// obter como Object, pois não se sabe o tipo da coluna
						Object val = rs.getObject(i);
						// mostrar valor do atributo
						System.out.print(val);
						// separador de atributo
						if (i < ncols)
							System.out.print(" # ");
				}
				// separador de linha
				System.out.println();
		}
		// libertar statement
		stm.close();
	}
	
	public static void consultarReservas(Connection conn, String numFiscal) throws SQLException {
		//System.out.println("=== RESERVAS ===");
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM reserva "
	    		+ "WHERE numeroFiscal = '" + numFiscal + "'";
	    showDataFromSelect(query, conn);
	    
	    
	}
	
	public static void consultarAlugueres(Connection conn, String numFiscal) throws SQLException {
		//System.out.println("\n=== ALUGUERES ===");
		String query = ""
	    		+ "SELECT * "
	    		+ "FROM aluguer "
	    		+ "WHERE numeroFiscal = '" + numFiscal + "'";
	    showDataFromSelect(query, conn);
	}   
	
	public static void consultarTodasReservas(Connection conn) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM reserva ";
	    showDataFromSelect(query, conn);
	}
	
	public static void consultarReserva(Connection conn, int id) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM reserva "
	    		+ "WHERE reservaID = " + id;
	    showDataFromSelect(query, conn);
	}
	
	public static String[] showDataLinha(String query, Connection conn, int linhaEscolhida) throws SQLException {
	    Statement stm = conn.createStatement();
	    ResultSet rs = stm.executeQuery(query);

	    // Processar o resultado até a linha desejada
	    int currentRow = 0;
	    String[] resultArray = null;

	    while (rs.next()) {
	        currentRow++;
	        if (currentRow == linhaEscolhida) {
	            // Obter o número de colunas
	            int ncols = rs.getMetaData().getColumnCount();
	            resultArray = new String[ncols];

	            // Extrair os valores para o array
	            for (int i = 1; i <= ncols; ++i) {
	                Object val = rs.getObject(i);
	                resultArray[i - 1] = (val != null) ? val.toString() : null;
	            }
	            break;
	        }
	    }

	    if (resultArray == null) {
	        System.out.println("Linha " + linhaEscolhida + " não encontrada. Verifique o número da linha.");
	    }

	    // Fechar recursos
	    rs.close();
	    stm.close();

	    return resultArray;
	}

	

	/** Método para consultar a moeda preferida dos clientes */
	/*private static void consultarMoedasClientes(Connection conn) throws SQLException {
	    String query = "SELECT numeroFiscal, nome, moeda FROM Cliente";
	    showDataFromSelect(query, conn);
	}*/
	
	public static void consultarLocalizarVeiculo(Connection conn, String matricula) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_LocalizacaoVeiculos " 
	    		+ "WHERE matricula = '" + matricula + "'";
	    showDataFromSelect(query, conn);
	}
	
	public static void identificarCondutor_Matricula_Data(Connection conn, String matricula, String data) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_condutorporaluguer " 
	    		+ "WHERE matricula = '" + matricula + "' "
	    		+ "AND '" + data+ "'"
	    		+ "BETWEEN dataInicio  AND dataFim";
	    showDataFromSelect(query, conn);
	}
	
	public static String[] levantarVeiculo(Connection conn, String matricula) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_LevantarVeiculos " 
	    		+ "WHERE matricula = '" + matricula + "'";
	    showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        
        String[] levantamento = new String[4];
        
        if (rs.next()) {
        	levantamento[0] = rs.getString("localidade");
        	levantamento[1] = rs.getString("piso");
        	levantamento[2] = rs.getString("fila");
        	levantamento[3] = rs.getString("posicao");
        } else {
            System.out.println("Nenhum lugar encontrado.");
        }

        // Fechar recursos
        rs.close();
        stm.close();
        
        //System.out.println("\nVeiculo Levantado: ");
        //Auxiliar.printArray(levantamento);
        return levantamento;
	}
	
	public static List<String> listarTiposVeiculo(Connection conn, String localidade) throws SQLException {
	    String query = ""
	            + "SELECT "
	            + "    nomeCategoria "
	            + "FROM  Parque_Tipo " 
	            + "WHERE localidade = '" + localidade + "'";

	    // Criar um comando para execução direta
	    Statement stm = conn.createStatement();
	    // Executar o comando
	    ResultSet rs = stm.executeQuery(query);

	    // Lista para armazenar os resultados
	    List<String> resultados = new ArrayList<>();

	    // Processar resultado
	    while (rs.next()) {
	        // Adicionar o valor da coluna "nomeCategoria" na lista
	        resultados.add(rs.getString("nomeCategoria"));
	    }

	    // Libertar resources
	    rs.close();
	    stm.close();
	    
	    for(int i=0; i!=resultados.size(); i++) {
	    	System.out.println(i+1 + " - " + resultados.get(i));
	    }

	    // Converter a lista para array e retornar
	    return resultados;
	}
	
	public static void verDescontosAtivos(Connection conn) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_DescontosAtivos;";
	    showDataFromSelect(query, conn);
	}
	
	public static void verDescontosAtivosCliente(Connection conn, String numeroFiscal) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_DescontosAtivosCliente "
	    		+ "WHERE numeroFiscal = '"+ numeroFiscal +"';";
	    showDataFromSelect(query, conn);
	    
	}
	
	public static HashMap<String, String> obterDescontosAtivosCliente(Connection conn, String numeroFiscal) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_DescontosAtivosCliente "
	    		+ "WHERE numeroFiscal = '"+ numeroFiscal +"';";
	    
	    //showDataFromSelect(query, conn);
	    
	    // Criar um comando para execução direta
	    Statement stm = conn.createStatement();
	    // Executar o comando
	    ResultSet rs = stm.executeQuery(query);

	    // Criar um HashMap para armazenar os descontos
	    HashMap<String, String> descontos = new HashMap<>();

	    // Processar resultado
	    while (rs.next()) {
	        String codigo = rs.getString("codigo");
	        String descricao = rs.getString("descricao");
	        descontos.put(codigo, descricao);
	    }
	    
	    // Libertar resources
	    rs.close();
	    stm.close();
	    
	    
	    // Converter a lista para array e retornar
	    return descontos;
	}
	
	public static String obterCambio(Connection conn, String codigoMoeda) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "valorPorDolar FROM Cambio "
	    		+ "WHERE codigoMoeda = '"+ codigoMoeda +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String cambio = null;
        if (rs.next()) {
            cambio = rs.getString("valorPorDolar");
        } else {
            System.out.println("Nenhuma moeda encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return cambio;
	}
	
	public static String obterMoedaCliente(Connection conn, String numeroFiscal) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "moeda FROM Cliente "
	    		+ "WHERE numeroFiscal = '"+ numeroFiscal +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String moeda = null;
        if (rs.next()) {
            moeda = rs.getString("moeda");
        } else {
            System.out.println("Nenhum cliente encontrado com o número fiscal: " + numeroFiscal);
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return moeda;
	}

	public static String obterTarifaCategoria(Connection conn, String cateogria) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "tarifa FROM TipoVeiculo "
	    		+ "WHERE nomeCategoria = '"+ cateogria +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String tarifa = null;
        if (rs.next()) {
        	tarifa = rs.getString("tarifa");
        } else {
            System.out.println("Nenhuma tarifa encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return tarifa;
	}
	
	public static String obterValorDesconto(Connection conn, String descontoCodigo) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "valor FROM Desconto "
	    		+ "WHERE codigo = '"+ descontoCodigo +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String desconto = null;
        if (rs.next()) {
        	desconto = rs.getString("valor");
        } else {
            System.out.println("Nenhum desconto encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return desconto;
	}
	
	public static String obterReputacaoCliente(Connection conn, String numeroFiscal) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "reputacao FROM vw_ClienteComReputacao "
	    		+ "WHERE numeroFiscal = '"+ numeroFiscal +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String reputacao = null;
        if (rs.next()) {
        	reputacao = rs.getString("reputacao");
        } else {
            System.out.println("Nenhum desconto encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return reputacao;
	}
	
	public static String obterTipoVeiculoPorMatricula(Connection conn, String matricula) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "tipoVeiculo FROM vw_veiculosparque "
	    		+ "WHERE veiculoMatricula = '"+ matricula +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String categoria = null;
        if (rs.next()) {
        	categoria = rs.getString("tipoVeiculo");
        } else {
            System.out.println("Nenhum desconto encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return categoria;
	}
	
	public static String obterVeiculoPorTipo(Connection conn, String tipo, String localidade) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_veiculosparque "
	    		+ "WHERE tipoVeiculo = '"+ tipo +"' AND parqueLocalidade = '"+localidade+"';";
	    showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String categoria = null;
        if (rs.next()) {
        	categoria = rs.getString("tipoVeiculo");
        } else {
            System.out.println("Nenhum desconto encontrada.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return categoria;
	}
	
	public static String[] obterLugaresLivresLocalidadeCategoria(Connection conn, String localidade, String categoria) throws SQLException {
		String query = ""
	    		+ "SELECT * "
	    		+ "FROM lugarparque "
	    		+ "WHERE localidade = '"+ localidade +"' AND tipoVeiculoAdmitido = '"+ categoria +"' AND estado = 'livre'";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);
        
        String[] lugar = new String[4];
        lugar[0] = localidade;
        if (rs.next()) {
        	lugar[1] = rs.getString("piso");
        	lugar[2] = rs.getString("fila");
        	lugar[3] = rs.getString("posicao");
        } else {
            System.out.println("Nenhum lugar encontrado.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return lugar;
	}
	
	public static void ocuparLugar(Connection conn, String[] lugar) throws SQLException {
        // Montar o comando SQL para atualizar o estado
        String command = "UPDATE LugarParque " +
                         "SET estado = 'ocupado' " +
                         "WHERE localidade = '" + lugar[0] + "' " +
                         "AND piso = " + lugar[1] + " " +
                         "AND fila = '" + lugar[2] + "' " +
                         "AND posicao = " + lugar[3] + ";";

        // Executar a alteração
        int rowsAffected = execute_change(command, conn);

        // Verificar se o comando foi bem-sucedido
        if (rowsAffected > 0) {
            System.out.println("Lugar atualizado para 'ocupado' com sucesso.");
        } else {
            System.out.println("Nenhum lugar foi encontrado com os dados fornecidos.");
        }
    }
	
	public static void associarVeiculo(Connection conn, String matricula, String[] lugar) throws SQLException {
	    // Montar o comando SQL para inserir a associação
	    String command = "INSERT INTO Veiculo_LugaresParque (matricula, localidade, piso, fila, posicao) " +
	                     "VALUES ('" + matricula + "', '" + lugar[0] + "', " + lugar[1] + ", '" + lugar[2] + "', '" + lugar[3] + "');";

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Veículo associado ao lugar com sucesso.");
	    } else {
	        System.out.println("Falha ao associar o veículo ao lugar.");
	    }
	}

	
	public static void desocuparLugar(Connection conn, String[] lugar) throws SQLException {
        // Montar o comando SQL para atualizar o estado
        String command = "UPDATE LugarParque " +
                         "SET estado = 'livre' " +
                         "WHERE localidade = '" + lugar[0] + "' " +
                         "AND piso = " + lugar[1] + " " +
                         "AND fila = '" + lugar[2] + "' " +
                         "AND posicao = " + lugar[3] + ";";

        // Executar a alteração
        int rowsAffected = execute_change(command, conn);

        // Verificar se o comando foi bem-sucedido
        /*if (rowsAffected > 0) {
            System.out.println("Lugar atualizado para 'livre' com sucesso.");
        } else {
            System.out.println("Nenhum lugar foi encontrado com os dados fornecidos.");
        }*/
    }
	
	public static void desassociarVeiculo(Connection conn, String matricula, String[] lugar) throws SQLException {
	    // Montar o comando SQL para deletar a associação
	    String command = "DELETE FROM Veiculo_LugaresParque " +
	                     "WHERE matricula = '" + matricula + "' " +
	                     "AND localidade = '" + lugar[0] + "' " +
	                     "AND piso = " + lugar[1] + " " +
	                     "AND fila = '" + lugar[2] + "' " +
	                     "AND posicao = " + lugar[3] + ";";

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    /*if (rowsAffected > 0) {
	        System.out.println("Veículo desassociado do lugar com sucesso.");
	    } else {
	        System.out.println("Nenhuma associação foi encontrada para os dados fornecidos.");
	    }*/
	}

	public static void adiconarReserva(Connection conn, String[] reserva) throws SQLException {
		
		// Tratar valor de código de desconto
	    String codigoDesconto = reserva[5] == null || reserva[5].equalsIgnoreCase("null") ? "NULL" : "'" + reserva[5] + "'";

	    // Montar comando SQL corretamente com as aspas e separadores
	    String command = "INSERT INTO Reserva (numeroFiscal, parqueLevantamento, tipoCarro, dataHoraInicio, dataHoraFim, codigoDesconto, custoFinal) " +
	                     "VALUES ('" +
	                     reserva[0] + "', '" +
	                     reserva[1] + "', '" +
	                     reserva[2] + "', '" +
	                     reserva[3] + "', '" +
	                     reserva[4] + "', " +
	                     codigoDesconto + ", " +
	                     reserva[6] + ");";

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Reserva criada com sucesso.");
	    } else {
	        System.out.println("Falha ao criar a reserva.");
	    }
	}
	
	public static void consultarCondutores(Connection conn) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM condutor; ";
	    showDataFromSelect(query, conn);
	}
	
	public static void adicionarAluguer(Connection conn, String dataHoraInicio, String dataHoraFim, String custoFinal, String numeroFiscal, String numeroCartaConducao, String codigo, String matricula) throws SQLException {
		// Montar o comando SQL para inserir a associação
		String codigoDesconto = codigo == null || codigo.equalsIgnoreCase("null") ? "NULL" : "'" + codigo + "'";
		
	    String command = "INSERT INTO Aluguer (dataHoraInicio, dataHoraFim, duracao, custoFinal, avaliacao, comentario, numeroFiscal, numeroCartaConducao, codigo, matricula) " +
	                     "VALUES ('" + 
	                     dataHoraInicio + "', '" + 
	                     dataHoraFim + "', " + 
	                     "TIMESTAMPDIFF(SECOND, '"+dataHoraInicio+"', '"+dataHoraFim+"') / 3600, '" + 
	                     custoFinal + "', null, null, '" +
	                     numeroFiscal + "', '" +
	                     numeroCartaConducao + "', " + 
	                     codigoDesconto + ", '" + 
	                     matricula + "');";
	    
	    System.out.println(command);

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Aluguer criado com sucesso.");
	    } else {
	        System.out.println("Falha ao criar aluguer.");
	    }
	}
	
	public static void eliminarReserva(Connection conn, String reservaID) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String command = "DELETE FROM Reserva " +
	                     "WHERE reservaID = '" + reservaID + "';";

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Reserva eliminada com sucesso.");
	    } else {
	        System.out.println("Nenhuma associação foi encontrada para os dados fornecidos.");
	    }
	}
	
	public static void verReservasPendentesCondutor(Connection conn, String numeroCartaConducao) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String query = "SELECT * FROM Aluguer " +
	                   "WHERE numeroCartaConducao = '" + numeroCartaConducao + "' AND status = 'Pendente';";

	    showDataFromSelect(query, conn);
	}
	
	public static void mudarStatusAluguer(Connection conn, String status, String dataHoraInicio, String matricula) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String command = "UPDATE Aluguer "
	    		 	   + "SET status = '"+ status + "' "
	                   + "WHERE dataHoraInicio = '" + dataHoraInicio + "' AND matricula = '"+ matricula +"';";

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    /*if (rowsAffected > 0) {
	        System.out.println("Status do aluguer mudado com sucesso.");
	    } else {
	        System.out.println("Nenhum aluguer foi encontrado para os dados fornecidos.");
	    }*/
	}
	
	public static String[] verAluguerAtualCondutor(Connection conn, String matricula) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String query = "SELECT * FROM Aluguer " +
	                   "WHERE matricula = '" + matricula + "' AND status = 'Em andamento';";

	    return showDataLinha(query, conn, 1);
	}
	
	public static void atualizarComentarioAvaliacao(Connection conn, String dataHoraInicio, String matricula, String comentario, Double avaliacao) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String command = "UPDATE Aluguer "
	    		 	   + "SET avaliacao = " + avaliacao + ", comentario ='" + comentario + "' "
	                   + "WHERE dataHoraInicio = '" + dataHoraInicio + "' AND matricula = '"+ matricula +"';";

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    /*
	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Avaliacao e Comentario atualizados com sucesso.");
	    } else {
	        System.out.println("Nenhum aluguer foi encontrado para os dados fornecidos.");
	    }*/
	}
	
	public static void alugueresPorAvaliar(Connection conn, String numeroFiscal) throws SQLException {
		// Montar o comando SQL para deletar a associação
	    String query = "SELECT * FROM Aluguer " +
	                   "WHERE numeroFiscal = '" + numeroFiscal + "' AND avaliacao IS null AND comentario IS null AND status = 'Concluído';";

	    showDataFromSelect(query, conn);
	}
	
	public static void adicionarIntervencao(Connection conn, String[] dadosIntervencao) throws SQLException {
	
	    String command = "INSERT INTO Intervencao (quilometragem, tipo, matricula, dataIntervencao) " +
	                     "VALUES (" + 
	                     dadosIntervencao[1] + ", '" + 
	                     dadosIntervencao[2] + "', '" +
	                     dadosIntervencao[0] + "', '" +
	                     dadosIntervencao[3] + "');";
	    
	    System.out.println(command);

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Intervencao criada com sucesso.");
	    } else {
	        System.out.println("Falha ao criar intervencao.");
	    }
	}
	
	public static boolean adicionarIntervencaoWeb(Connection conn, String[] dadosIntervencao) throws SQLException {

        String command = "INSERT INTO Intervencao (quilometragem, tipo, matricula, dataIntervencao) " +
                         "VALUES (" + 
                         dadosIntervencao[1] + ", '" + 
                         dadosIntervencao[2] + "', '" +
                         dadosIntervencao[0] + "', '" +
                         dadosIntervencao[3] + "');";

        System.out.println(command);

        // Executar o comando
        int rowsAffected = execute_change(command, conn);

        // Verificar se o comando foi bem-sucedido
        return rowsAffected>0;
    }
	
	public static void adicionarDescontoCliente(Connection conn, String numFiscal, String desconto) throws SQLException {
		String command = "INSERT INTO Cliente_Desconto (numeroFiscal, codigo) " +
                "VALUES (" + 
                numFiscal + ", '" +
                desconto + "');";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Desconto atribuido com sucesso.");
		} else {
		   System.out.println("Falha ao atribuir d.");
		}
	}
	
	public static void apagaDescontoCliente(Connection conn, String numeroFiscal) throws SQLException {
	    // Monta o comando SQL para deletar a entrada pelo número fiscal
	    String command = "DELETE FROM Cliente_Desconto WHERE numeroFiscal = " + numeroFiscal + ";";

	    // Exibe o comando para depuração
	    System.out.println(command);

	    // Executa o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verifica se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Desconto removido com sucesso.");
	    } else {
	        System.out.println("Falha ao remover desconto ou desconto inexistente.");
	    }
	}
	
	public static void verHistorico(Connection conn, String matricula) throws SQLException {
	    String query = ""
	    		+ "SELECT * "
	    		+ "FROM vw_historyCar "
	    		+ "WHERE matricula = '"+ matricula +"';";
	    showDataFromSelect(query, conn);
	    
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
	
	public static void adicionarCliente(Connection conn, String numeroFiscal, String nome, String contacto, String morada, String distrito, String concelho, String freguesia, String preferenciasLinguisticasECulturais, String moeda, String tipo, String capitalSocial) throws SQLException {
		String command = "INSERT INTO Cliente (numeroFiscal, nome, contacto, morada, distrito, concelho, freguesia, preferenciasLinguisticasECulturais, moeda) " +
                "VALUES ('" + 
                numeroFiscal + "', '" +
                nome + "', '" +
                contacto + "', '" +
                morada + "', '" +
                distrito + "', '" +
                concelho + "', '" +
                freguesia + "', '" +
                preferenciasLinguisticasECulturais + "', '" +
                moeda + "');";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Cliente criado com sucesso.");
		} else {
		   System.out.println("Falha ao criar cliente.");
		}
		
		
		if (tipo.equals("Empresa")) {
			adicionarEmpresa(conn, numeroFiscal, capitalSocial);
		}else {
			System.out.println("pes");
			adicionarPessoa(conn, numeroFiscal);
		}
	}
	
	public static void adicionarCondutor(Connection conn, String numeroCartaConducao, String nome, String validadeCarta) throws SQLException {
		String command = "INSERT INTO Condutor (numeroCartaConducao, nome, validadeCarta, reputacao) " +
                "VALUES ('" + 
                numeroCartaConducao + "', '" +
                nome + "', '" +
                validadeCarta + "', NULL);";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Condutor criado com sucesso.");
		} else {
		   System.out.println("Falha ao criar condutor.");
		}
	}
	
	public static void adicionarVeiculo(Connection conn, String matricula, String marca, String modelo, String cor, String tipo, String numeroLugares, String numeroPortas, String capacidadeCarga,String tipoMotor, String potencia, String localidade) throws SQLException {
		String command = "INSERT INTO Veiculo (matricula, marca, modelo, fotos, cor, tipo, numeroLugares, numeroPortas, capacidadeCarga, tipoMotor, potencia, localidade) " +
                "VALUES ('" + 
                matricula + "', '" +
                marca + "', '" +
                modelo + "', NULL,'" +
                cor + "', '" +
                tipo + "', " +
                numeroLugares + ", " +
                numeroPortas + ", " +
                capacidadeCarga + ", '" +
                tipoMotor + "', '" +
                potencia + "', '" +
                localidade + "');";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Veiculo criado com sucesso.");
		} else {
		   System.out.println("Falha ao criar veiculo.");
		}
	}
	
	public static void adicionarPessoa(Connection conn, String numeroFiscal) throws SQLException {
		String command = "INSERT INTO Pessoa (numeroFiscal) " +
                "VALUES ('" + 
                numeroFiscal + "');";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Pessoa criada com sucesso.");
		} else {
		   System.out.println("Falha ao criar pessoa.");
		}
	}
	
	public static void adicionarEmpresa(Connection conn, String numeroFiscal, String capitalSocial) throws SQLException {
		String command = "INSERT INTO Empresa (numeroFiscal, capitalSocial) " +
				"VALUES ('" + 
                numeroFiscal + "', " +
                capitalSocial + ");";

		System.out.println(command);
		
		// Executar o comando
		int rowsAffected = execute_change(command, conn);
		
		// Verificar se o comando foi bem-sucedido
		if (rowsAffected > 0) {
		   System.out.println("Empresa criada com sucesso.");
		} else {
		   System.out.println("Falha ao criar empresa.");
		}
	}

	
	public static String verTipoCliente(Connection conn, String numeroFiscal) throws SQLException {
	    String query = ""
	    		+ "SELECT "
	    		+ "tipoCliente FROM vw_clientes "
	    		+ "WHERE numeroFiscal = '"+ numeroFiscal +"';";
	    //showDataFromSelect(query, conn);
	    
	    // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Obter o valor da moeda
        String categoria = null;
        if (rs.next()) {
        	categoria = rs.getString("tipoCliente");
        } else {
            System.out.println("Nenhum cliente encontrado.");
        }

        // Fechar recursos
        rs.close();
        stm.close();

        return categoria;
	}
	
	public static boolean validarTipoVeiculo(Connection conn, String tipo) throws SQLException {
        String query = ""
                + "SELECT nomeCategoria FROM TipoVeiculo "
                + "WHERE nomeCategoria = '" + tipo + "';";

        // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Verificar se existe algum resultado
        boolean exists = rs.next();

        // Fechar recursos
        rs.close();
        stm.close();

        return exists;
    }
	
	public static boolean validarLocalidade(Connection conn, String tipo) throws SQLException {
        String query = ""
                + "SELECT localidade FROM Parque "
                + "WHERE localidade = '" + tipo + "';";

        // Criar o Statement
        Statement stm = conn.createStatement();

        // Executar a query
        ResultSet rs = stm.executeQuery(query);

        // Verificar se existe algum resultado
        boolean exists = rs.next();

        // Fechar recursos
        rs.close();
        stm.close();

        return exists;
    }
	
	public static void updateEmpresa(Connection conn, String numeroFiscal, String campo, String valor) throws SQLException {
		// Montar o comando SQL para deletar a associação
		
		String command = "";
		if (campo != "capitalSocial")
		{
			command = "UPDATE Empresa "
	    		 	   + "SET "+ campo + " = '" + valor
	                   + "' WHERE numeroFiscal = '" + numeroFiscal +"';";
		}else {
			command = "UPDATE Empresa "
	    		 	   + "SET "+ campo + " = " + valor
	                   + " WHERE numeroFiscal = '" + numeroFiscal +"';";
		}

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println(campo + " da Empresa atualizado com sucesso.");
	    } else {
	        System.out.println("Nenhuma Empresa foi encontrada para os dados fornecidos.");
	    }
	}
	
	public static void updateCliente(Connection conn, String numeroFiscal, String campo, String valor) throws SQLException {
		// Montar o comando SQL para deletar a associação
		
		String command = "UPDATE Cliente "
	    		 	   + "SET "+ campo + " = '" + valor
	                   + "' WHERE numeroFiscal = '" + numeroFiscal +"';";

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println(campo + " do Cliente atualizado com sucesso.");
	    } else {
	        System.out.println("Nenhuma Cliente foi encontrado para os dados fornecidos.");
	    }
	}
	
	public static void updateCondutor(Connection conn, String numeroCarta, String campo, String valor) throws SQLException {
		// Montar o comando SQL para deletar a associação
		
		String command = "UPDATE Condutor "
	    		 	   + "SET "+ campo + " = '" + valor
	                   + "' WHERE numeroCartaConducao = '" + numeroCarta +"';";

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println(campo + " do Condutor atualizado com sucesso.");
	    } else {
	        System.out.println("Nenhum Condutor foi encontrado para os dados fornecidos.");
	    }
	}
	
	public static void updateVeiculo(Connection conn, String matricula, String campo, String valor) throws SQLException {
		// Montar o comando SQL para deletar a associação
		// numeroLugares, numeroPortas, capacidadeCarga potencia
		String command = "";
		if (campo == "numeroLugares" || campo == "numeroPortas" || campo == "capacidadeCarga" || campo == "potencia") {
			command = "UPDATE Veiculo "
	    		 	   + "SET "+ campo + " = " + valor
	                   + " WHERE matricula = '" + matricula +"';";
		}else {
			command = "UPDATE Veiculo "
	    		 	   + "SET "+ campo + " = '" + valor
	                   + "' WHERE matricula = '" + matricula +"';";
		}
		
		

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println(campo + " do Veiculo atualizado com sucesso.");
	    } else {
	        System.out.println("Nenhum Veiculo foi encontrado para os dados fornecidos.");
	    }
	}
	
	public static void updatePessoa(Connection conn, String numeroFiscal, String campo, String valor) throws SQLException {
		// Montar o comando SQL para deletar a associação
		
		String command = "UPDATE Pessoa "
	    		 	   + "SET "+ campo + " = '" + valor
	                   + "' WHERE numeroFiscal = '" + numeroFiscal +"';";

	    System.out.println(command);
	    
	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println(campo + " da Pessoa atualizado com sucesso.");
	    } else {
	        System.out.println("Nenhuma Pessoa foi encontrada para os dados fornecidos.");
	    }
	}
	
	public static void exportJSON(Connection conn, String matricula, String path) throws SQLException, IOException {
        // Consulta SQL
        String query = "SELECT * FROM vw_fullHistoryCar WHERE matricula = '" + matricula + "';";

        // Criar Statement e executar a consulta
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Criar JSON Array para armazenar os registros
        JSONArray jsonArray = new JSONArray();

        // Iterar pelos resultados e construir os objetos JSON
        while (rs.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("matricula", rs.getString("matricula"));
            jsonObject.put("evento", rs.getString("evento"));
            jsonObject.put("data", rs.getDate("data"));
            jsonObject.put("avaliacao", rs.getObject("avaliacao")); // Suporta valores nulos
            jsonObject.put("quilometragem", rs.getObject("quilometragem"));
            jsonObject.put("tipo", rs.getString("tipo"));
            jsonObject.put("dataHoraInicio", rs.getObject("dataHoraInicio"));
            jsonObject.put("duracao", rs.getObject("duracao"));
            jsonObject.put("custoFinal", rs.getObject("custoFinal"));
            jsonObject.put("comentario", rs.getString("comentario"));
            jsonObject.put("numeroFiscal", rs.getString("numeroFiscal"));
            jsonObject.put("numeroCartaConducao", rs.getString("numeroCartaConducao"));
            jsonObject.put("codigo", rs.getString("codigo"));
            jsonObject.put("status", rs.getString("status"));

            jsonArray.put(jsonObject);
        }

        // Fechar recursos
        rs.close();
        stmt.close();

        // Gerar nome do arquivo com base na matrícula e data/hora
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = matricula + "-" + timestamp + ".json";

        // Criar o caminho completo do arquivo
        String fullPath = path + "/" + fileName;

        // Escrever o JSON em um arquivo
        try (FileWriter file = new FileWriter(fullPath)) {
            file.write(jsonArray.toString(4)); // Formata o JSON com indentação de 4 espaços
        }

        System.out.println("Arquivo JSON exportado com sucesso: " + fullPath);
    }
	
	public static void importJSON(Connection conn, String filePath) throws IOException, SQLException {
        // Ler o arquivo JSON
        FileReader fileReader = new FileReader(filePath);
        JSONTokener tokener = new JSONTokener(fileReader);
        JSONArray jsonArray = new JSONArray(tokener);

        // Iterar pelos objetos JSON no array
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            // Determinar se é Intervenção ou Aluguer
            String evento = jsonObject.getString("evento");

            if ("Intervenção".equalsIgnoreCase(evento)) {
            	adicionarIntervencaoJSON(conn, jsonObject);
            } else if ("Aluguer".equalsIgnoreCase(evento)) {
            	adicionarAluguerJSON(conn, jsonObject);
            }
        }

        System.out.println("Importação do JSON concluída com sucesso.");
    }
	
	public static void adicionarAluguerJSON(Connection conn, JSONObject aluguerJson) throws SQLException {
	    // Obter valores do JSON
	    String dataHoraInicio = aluguerJson.getString("dataHoraInicio");
	    String dataHoraFim = aluguerJson.getString("data");
	    double custoFinal = aluguerJson.getDouble("custoFinal");
	    String numeroFiscal = aluguerJson.getString("numeroFiscal");
	    String numeroCartaConducao = aluguerJson.getString("numeroCartaConducao");
	    String codigo = aluguerJson.optString("codigo", null);
	    String matricula = aluguerJson.getString("matricula");
	    String status = aluguerJson.optString("status", null);
	    String avaliacao = aluguerJson.optString("avaliacao", null);
	    String comentario = aluguerJson.optString("comentario", null);

	    // Tratar o valor de código
	    String codigoDesconto = (codigo == null || codigo.equalsIgnoreCase("null")) ? "NULL" : "'" + codigo + "'";

	    // Montar o comando SQL
	    String command = "INSERT INTO Aluguer (dataHoraInicio, dataHoraFim, duracao, custoFinal, avaliacao, comentario, numeroFiscal, numeroCartaConducao, codigo, matricula, status) " +
	                     "VALUES ('" + 
	                     dataHoraInicio + "', '" + 
	                     dataHoraFim + "', " +
	                     "TIMESTAMPDIFF(SECOND, '"+dataHoraInicio+"', '"+dataHoraFim+"') / 3600, " + 
	                     custoFinal + ", '"+avaliacao+"', '"+comentario+"', '" +
	                     numeroFiscal + "', '" +
	                     numeroCartaConducao + "', " + 
	                     codigoDesconto + ", '" + 
	                     matricula + "', '" + 
	                     status + "');";

	    System.out.println(command);

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Aluguer criado com sucesso.");
	    } else {
	        System.out.println("Falha ao criar aluguer.");
	    }
	}
	
	public static void adicionarIntervencaoJSON(Connection conn, JSONObject intervencaoJson) throws SQLException {
	    // Obter valores do JSON
	    int quilometragem = intervencaoJson.getInt("quilometragem");
	    String tipo = intervencaoJson.getString("tipo");
	    String matricula = intervencaoJson.getString("matricula");
	    String dataIntervencao = intervencaoJson.getString("data");

	    // Montar o comando SQL
	    String command = "INSERT INTO Intervencao (quilometragem, tipo, matricula, dataIntervencao) " +
	                     "VALUES (" + 
	                     quilometragem + ", '" + 
	                     tipo + "', '" +
	                     matricula + "', '" +
	                     dataIntervencao + "');";

	    System.out.println(command);

	    // Executar o comando
	    int rowsAffected = execute_change(command, conn);

	    // Verificar se o comando foi bem-sucedido
	    if (rowsAffected > 0) {
	        System.out.println("Intervenção criada com sucesso.");
	    } else {
	        System.out.println("Falha ao criar intervenção.");
	    }
	}

    // Exibir detalhes do cliente selecionado
	public static void exibirDetalhesCliente(Connection conn, String numeroFiscal) throws SQLException {
	    // Consulta SQL utilizando concatenação de strings
	    String query = "SELECT * " +
	                   "FROM vw_clientecomreputacao " +
	                   "WHERE numeroFiscal = '" + numeroFiscal + "';";

	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(query);

	    if (rs.next()) {
	        System.out.println("Detalhes do Cliente:");
	        System.out.println("Número Fiscal: " + rs.getString("numeroFiscal"));
	        System.out.println("Nome: " + rs.getString("nome"));
	        System.out.println("Reputação: " + rs.getDouble("reputacao"));
	        System.out.println("Freguesia: " + rs.getString("freguesia"));
	        // Adicione outras colunas conforme necessário
	    } else {
	        System.out.println("Cliente não encontrado.");
	    }

	    rs.close();
	    stmt.close();
	}
    

	
	public static void arrumaVeiculo(Connection conn, String matricula) throws SQLException {
	    // Consulta SQL para obter localidade e categoria do veículo
	    String query = "SELECT localidade, tipo AS categoria FROM Veiculo WHERE matricula = '" + matricula + "';";
	    
	    try {
	        // Executar a consulta
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	
	        // Verificar se o veículo foi encontrado
	        if (rs.next()) {
	            String localidade = rs.getString("localidade");
	            String categoria = rs.getString("categoria");
	
	            // Obter os lugares livres para a localidade e categoria
	            String[] lugar = obterLugaresLivresLocalidadeCategoria(conn, localidade, categoria);
	            if (lugar != null && lugar.length > 0) {
	                // Ocupar o lugar
	                ocuparLugar(conn, lugar);
	                System.out.println("Veículo com matrícula " + matricula + " foi arrumado no lugar: " + String.join(", ", lugar));
	                
	                associarVeiculo(conn, matricula,lugar);
	            } else {
	                System.out.println("Não há lugares livres disponíveis para a localidade '" + localidade + "' e categoria '" + categoria + "'.");
	            }
	        } else {
	            System.out.println("Nenhum veículo encontrado com a matrícula " + matricula + ".");
	        }
	
	        // Fechar recursos
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        System.out.println("Erro ao arrumar o veículo: " + e.getMessage());
	        throw e;
	    }
	}
	
	public static void main(String[] args) throws SQLException, IOException {
	    // Abrir conexão com a base de dados
	    Connection conn = getConnection(URL, USER, PASSWORD);
	    if (conn == null) {
	        System.out.println("Conexão falhou.");
	        return;
	    }
	    System.out.println("Conectado à base de dados: " + URL);
	    
	    
	    ////////////////
	    // TESTES
	    ///////////////
	    
	    
	    // == Consultar reservas 
	    //consultarReservas(conn, "987654321");
	    
	    // == Listar Tipos num parqu
	    //List<String> lt = listarTiposVeiculo(conn, "Lisboa");
	    //System.out.println(lt.get(0));
	    
	    // == Ver Descontos Ativos
	    //verDescontosAtivos(conn);
	    
	    // == Obter Descontos Ativos
	    //System.out.println(obterDescontosAtivosCliente(conn, "123456789"));
	    
	    // == Obter Cambio
	    //obterCambio(conn, "JPY");
	    
	    // == Obter Moeda Cliente
	    //obterMoedaCliente(conn, "123987654");
	    
	    // == Obter Tarifa
	    //obterTarifaCategoria(conn, "Familiar");
	    
	    // == Obter Desconto Valor
	    //System.out.println(obterValorDesconto(conn, "D00002"));
	    
	    // == Obter Reputacao
	    //System.out.println(obterReputacaoCliente(conn, "123456789"));
	    
	    // == Obter Categoria pela matricula
	    //System.out.println(obterTipoVeiculoPorMatricula(conn, "11-AA-11"));
	    
	    // == Obter Lugares Livres
	    //Auxiliar.printArray(obterLugaresLivresLocalidadeCategoria(conn, "Lisboa", "Familiar"));
	    
	    // == Desocupar Lugares
	    //String[] lugarStrings = {"Lisboa", "1", "A", "1"};
	    //desocuparLugar(conn, lugarStrings);
	    
	    // == Consultar todas as reservas
	    //consultarTodasReservas(conn);
	    //obterVeiculoPorTipo(conn, "Familiar", "Lisboa");
	    
	    // == Consultar Condutores
	    //onsultarCondutores(conn);
	    
	    // == Ver Reservas Pendentes de um Condutor
	    //verReservasPendentesCondutor(conn, "123456789012");
	    
	    // == Ver alugueres por avaliar
	    //alugueresPorAvaliar(conn, "123456789");
	    
	    // == Ver tipoCliente
	    //System.out.println(verTipoCliente(conn, "123456789"));
	    
	    // == Ver existencia de tipo de veiculo
	    /*String tipoVeiculo = "Familiar";
		if (validarTipoVeiculo(conn, tipoVeiculo)) {
            System.out.println("O tipo de veículo '" + tipoVeiculo + "' é válido.");
        } else {
            System.out.println("O tipo de veículo '" + tipoVeiculo + "' não foi encontrado.");
        }*/
	    
	    
	    // == Exportar JSON 
	    //exportJSON(conn, "11-AA-11");
        
	    // == Import JSON - criar carro antes
	    //importJSON(conn, "98-AA-89-2023-12-29_18-04-39.json");
	    
	    // Fechar conexão
	    
	    conn.close();
	    System.out.println("Conexão encerrada.");
	}

	public static String getServername() {
		return SERVERNAME;
	}

	public static String getUser() {
		return USER;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	public static String getUrl() {
		return URL;
	}
	
	

}
