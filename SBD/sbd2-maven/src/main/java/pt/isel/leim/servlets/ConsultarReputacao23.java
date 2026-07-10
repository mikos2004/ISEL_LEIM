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

@WebServlet("/ConsultarReputacao23")
public class ConsultarReputacao23 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nif = request.getParameter("nif");
        Connection conn = null;
        String consultaOutput = null;

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && nif != null && !nif.trim().isEmpty()) {
                // Capturar a saída do método `consultarReputacaoDescontos`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;

                try {
                    // Redirecionar a saída padrão para capturar o que é impresso pelo método
                	System.setOut(printStream);
                	Auxiliar.consultarReptucaoDescontosCliente(conn, nif);
                    
                } finally {
                    // Restaurar a saída padrão do sistema
                    System.setOut(originalOut);
                }

                // Converter a saída capturada para uma string
                consultaOutput = outputStream.toString();
                request.setAttribute("consultaOutput", consultaOutput);

            } else {
                request.setAttribute("error", "Erro: NIF inválido ou conexão indisponível.");
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
        request.getRequestDispatcher("Cliente23.jsp").forward(request, response);
    }
}
