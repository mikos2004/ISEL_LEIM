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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ConsultarReservas22")
public class ConsultarReservas22 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nif = request.getParameter("nif");
        Connection conn = null;
        List<String[]> reservasList = new ArrayList<>();

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && nif != null && !nif.trim().isEmpty()) {
                // Capturar a saída do método `consultarReservas`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;
                System.setOut(printStream);
                BD_backend.consultarReservas(conn, nif);
                System.setOut(originalOut);
                String consultaOutput = outputStream.toString();
                request.setAttribute("reservasList", consultaOutput);
                
                ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                PrintStream printStream2 = new PrintStream(outputStream2);
                System.setOut(printStream2);
                BD_backend.consultarAlugueres(conn, nif);
                System.setOut(originalOut);
                String consultaOutput2 = outputStream2.toString();
                request.setAttribute("alugueresList", consultaOutput2);
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
        request.getRequestDispatcher("Cliente22.jsp").forward(request, response);
    }
}
