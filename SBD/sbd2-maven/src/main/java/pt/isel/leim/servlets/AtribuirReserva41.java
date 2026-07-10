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

@WebServlet("/AtribuirReserva41")
public class AtribuirReserva41 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String matricula = request.getParameter("veiculo");
        String numeroCartaConducao = request.getParameter("condutor");
        Connection conn = null;
        
 
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
                String consultaOutput = outputStream.toString();
                String[] reserva = consultaOutput.split("#");
                
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dataHoraInicio = LocalDateTime.parse(reserva[4].trim(), inputFormatter).format(outputFormatter);
                String dataHoraFim = LocalDateTime.parse(reserva[5].trim(), inputFormatter).format(outputFormatter);
                String custoFinal = reserva[8].trim();
                String numeroFiscal = reserva[1].trim();
                String codigo = reserva[7].trim();

            	BD_backend.adicionarAluguer(conn, dataHoraInicio, dataHoraFim, custoFinal, numeroFiscal, numeroCartaConducao, codigo, matricula);
                BD_backend.eliminarReserva(conn, reserva[0]);

                request.setAttribute("message","Reserva Atribuida com sucesso");
           
                

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

        request.getRequestDispatcher("Funcionario41.jsp").forward(request, response);
 
    }

}