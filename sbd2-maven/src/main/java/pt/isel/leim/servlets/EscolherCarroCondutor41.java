package pt.isel.leim.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.BD_backend;

@WebServlet("/EscolherCarroCondutor41")
public class EscolherCarroCondutor41 extends HttpServlet{
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("reserva");
        Connection conn = null;
        String consultaOutput = null;

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null) {
                // Capturar a saída do método `consultarDadosVeiculo`
            	PrintStream originalOut = System.out;
            	
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                System.setOut(printStream);
                BD_backend.consultarReserva(conn, Integer.parseInt(id));
                System.setOut(originalOut);
                
                // Converter a saída capturada para uma string
                consultaOutput = outputStream.toString();
                String[] reserva = consultaOutput.split("#");
                
                ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                PrintStream printStream2 = new PrintStream(outputStream2);
                System.setOut(printStream2);
                BD_backend.obterVeiculoPorTipo(conn, reserva[2].trim(), reserva[3].trim());
                System.setOut(originalOut);
                consultaOutput = outputStream2.toString();
                request.setAttribute("carros", consultaOutput);
                
                ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
                PrintStream printStream3 = new PrintStream(outputStream3);
                System.setOut(printStream3);
                BD_backend.consultarCondutores(conn);
                System.setOut(originalOut);
                consultaOutput = outputStream3.toString();
                request.setAttribute("condutores", consultaOutput);
                
                

            } else {
                request.setAttribute("error", "Erro: conexão indisponível.");
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
        request.getRequestDispatcher("Funcionario41.jsp").forward(request, response);
    }

}
