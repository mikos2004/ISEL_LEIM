package pt.isel.leim.sbd2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_Test {
	
	private static final String URL = "jdbc:mysql://localhost:3306/Northwind"; // NorthWind or another schema
	private static final String USER = "root"; // or userXX
	private static final String PASSWORD = "root";
	/** get MySQL Connection */
	
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
		System.out.println("Structure/data change: " + command);
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
		System.out.println("Data from: " + select + "\n");
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
							System.out.print(" - ");
				}
				// separador de linha
				System.out.println();
		}
		// libertar statement
		stm.close();
	}
	
	/** main method */
	public static void main(String[] args) throws SQLException {
		// open/get connection do BD
		Connection conn = getConnection(URL, USER, PASSWORD);
		System.out.println("Connected to BD: " + URL + " - user: " + USER);
		System.out.println("\n");
		
		// create table T1
		try {
			execute_change("CREATE TABLE T1 (id INT, name VARCHAR(255))", conn);
		} catch (SQLException ex) {
			System.out.println("Erro na inserção de dados: " + ex.getMessage());
		}
		
		System.out.println("\n");
		
		// add data to T1
		try {
			execute_change("INSERT INTO T1 (id, name) VALUES (1,'John')", conn);
			execute_change("INSERT INTO T1 (id, name) VALUES (2,'Anna')", conn);
		} catch (SQLException ex) {
			System.out.println("Erro na inserção de dados: " + ex.getMessage());
		}
		
		System.out.println("\n");
		
		// show data from T1
		showDataFromSelect("SELECT * FROM T1 ORDER BY id", conn);
		
		System.out.println("\n");
		
		try {
			execute_change("DROP TABLE T1", conn);
		} catch (SQLException ex) {
			System.out.println("Erro na eliminação da tabela T1: " + ex.getMessage());
		}
		
		System.out.println("\n");
		
		// close connection
		conn.close();
		
		System.out.println("End.");
	}
}