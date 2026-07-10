package pt.isel.leim.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.Auxiliar;
import pt.isel.leim.sbd2.BD_backend;


@WebServlet("/MenorLucro52")
public class MenorLucro52 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Connection conn = null;
       
        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null) {
                // Capturar a saída do método `consultarDadosVeiculo`

            	
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                System.setOut(printStream);
                Auxiliar.ranking3MenorLucro(conn);
                System.setOut(System.out);
                
                // Converter a saída capturada para uma string
                String consultaOutput = outputStream.toString();
                request.setAttribute("consultaOutput", consultaOutput);
                
                

            } else {
                request.setAttribute("message", "Erro: conexão indisponível.");
            }

        } catch (Exception e) {
            request.setAttribute("message", "Erro ao consultar banco de dados: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        request.getRequestDispatcher("Gerente52.jsp").forward(request, response);
    }

}
