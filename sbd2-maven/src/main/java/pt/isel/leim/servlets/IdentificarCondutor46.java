package pt.isel.leim.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.BD_backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/IdentificarCondutor46")
public class IdentificarCondutor46 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matricula = request.getParameter("matricula");
        String dataInput = request.getParameter("data");
        Connection conn = null;
        String consultaOutput = null;

        try {
            // Validar e converter a data para o formato correto (yyyy-MM-dd)
            String dataFormatada;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); // Caso o input esteja em dd/MM/yyyy
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(dataInput); // Converter para objeto Date
                dataFormatada = dbFormat.format(date);   // Reformatar para yyyy-MM-dd
            } catch (ParseException e) {
                dataFormatada = dataInput; // Caso a data já esteja no formato yyyy-MM-dd
            }

            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && matricula != null && !matricula.trim().isEmpty() && dataFormatada != null) {
                // Capturar a saída do método `consultarCondutor`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;

                try {
                    // Redirecionar a saída padrão para capturar o que é impresso pelo método
                    System.setOut(printStream);
                    BD_backend.identificarCondutor_Matricula_Data(conn, matricula, dataFormatada);
                } finally {
                    // Restaurar a saída padrão do sistema
                    System.setOut(originalOut);
                }

                // Converter a saída capturada para uma string
                consultaOutput = outputStream.toString();
                request.setAttribute("consultaOutput", consultaOutput);

            } else {
                request.setAttribute("error", "Erro: Matrícula ou data inválida, ou conexão indisponível.");
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
        request.getRequestDispatcher("Funcionario46.jsp").forward(request, response);
    }
}
