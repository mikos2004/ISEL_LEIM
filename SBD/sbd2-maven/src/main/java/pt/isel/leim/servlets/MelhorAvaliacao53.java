package pt.isel.leim.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.BD_backend;
import pt.isel.leim.sbd2.Auxiliar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/MelhorAvaliacao53")
public class MelhorAvaliacao53 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dataInicialInput = request.getParameter("dataInicial");
        String dataFinalInput = request.getParameter("dataFinal");
        Connection conn = null;
        String consultaOutput = null;

        try {
            // Validar e converter as datas para o formato correto (yyyy-MM-dd)
            String dataInicialFormatada;
            String dataFinalFormatada;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); // Caso o input esteja em dd/MM/yyyy
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                Date dataInicial = inputFormat.parse(dataInicialInput); // Converter data inicial para objeto Date
                Date dataFinal = inputFormat.parse(dataFinalInput);     // Converter data final para objeto Date
                
                dataInicialFormatada = dbFormat.format(dataInicial);   // Reformatar data inicial para yyyy-MM-dd
                dataFinalFormatada = dbFormat.format(dataFinal);       // Reformatar data final para yyyy-MM-dd
            } catch (ParseException e) {
                // Caso as datas já estejam no formato yyyy-MM-dd
                dataInicialFormatada = dataInicialInput;
                dataFinalFormatada = dataFinalInput;
            }

            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && dataInicialFormatada != null && dataFinalFormatada != null) {
                // Capturar a saída do método `ranking5Modelos`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;

                try {
                    // Redirecionar a saída padrão para capturar o que é impresso pelo método
                    System.setOut(printStream);
                    Auxiliar.ranking5Modelos(conn, dataInicialFormatada, dataFinalFormatada);
                } finally {
                    // Restaurar a saída padrão do sistema
                    System.setOut(originalOut);
                }

                // Converter a saída capturada para uma string
                consultaOutput = outputStream.toString();
                request.setAttribute("consultaOutput", consultaOutput);

            } else {
                request.setAttribute("error", "Erro: Datas inválidas ou conexão indisponível.");
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
        request.getRequestDispatcher("Gerente53.jsp").forward(request, response);
    }
}
