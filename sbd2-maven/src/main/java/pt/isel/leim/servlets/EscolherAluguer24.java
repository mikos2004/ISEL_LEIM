package pt.isel.leim.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.Auxiliar;
import pt.isel.leim.sbd2.BD_backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/EscolherAluguer24")
public class EscolherAluguer24 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String nif = request.getParameter("nif");
        String linha = request.getParameter("aluguer");
        String comentario = request.getParameter("comentario");
        String avaliacao = request.getParameter("avaliacao");
        Connection conn = null;
        String consultaOutput = null;
        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && comentario != null && !comentario.trim().isEmpty() && avaliacao != null && !avaliacao.trim().isEmpty()) {
                // Capturar a saída do método `consultarDadosVeiculo`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;

            	System.setOut(printStream);
            	BD_backend.alugueresPorAvaliar(conn, String.valueOf(nif));
            	System.setOut(originalOut);
            	String alugueres = outputStream.toString();
            	String[][] alugueresParts = Auxiliar.separarString(alugueres);
            	
            	String dataInicioFormatada;
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				// Parse the input date and format it into the desired output
				LocalDateTime dateTime = LocalDateTime.parse(alugueresParts[Integer.parseInt(linha)-1][0].trim(), inputFormatter);
				dataInicioFormatada = dateTime.format(outputFormatter);
            	
            
                BD_backend.atualizarComentarioAvaliacao(conn, dataInicioFormatada, alugueresParts[Integer.parseInt(linha)-1][9].trim(), comentario, Double.parseDouble(avaliacao));


                // Converter a saída capturada para uma string
                consultaOutput = outputStream.toString();
                request.setAttribute("consultaOutput", consultaOutput);
                request.setAttribute("message", "Comentário e avaliação atualizados.");

            } else {
                request.setAttribute("error", "Erro: Comentario inválida ou conexão indisponível.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Erro ao consultar banco de dados: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Encaminhar para a JSP
        request.getRequestDispatcher("Cliente24.jsp").forward(request, response);
    }
}
