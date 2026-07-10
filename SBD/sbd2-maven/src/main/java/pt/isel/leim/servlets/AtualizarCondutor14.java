package pt.isel.leim.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.Auxiliar;
import pt.isel.leim.sbd2.BD_backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/AtualizarCondutor14")
public class AtualizarCondutor14 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar os parâmetros recebidos do formulário
        String numeroCarta = request.getParameter("numeroCarta");
        String data = request.getParameter("data");

        // Verificar se os parâmetros estão presentes
        if (numeroCarta == null) {
            request.setAttribute("error", "Dados inválidos ou ausentes.");
            request.getRequestDispatcher("AtualizarCondutor.jsp").forward(request, response);
            return;
        }

        // Conexão ao banco de dados
        Connection conn = null;
        try {
            // Validar e converter a data para o formato correto (yyyy-MM-dd)
            String dataFormatada;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); // Caso o input esteja em dd/MM/yyyy
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(data); // Converter para objeto Date
                dataFormatada = dbFormat.format(date);   // Reformatar para yyyy-MM-dd
            } catch (ParseException e) {
                dataFormatada = data; // Caso a data já esteja no formato yyyy-MM-dd
            }

            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null) {
                // Atualizar o campo do condutor
                Auxiliar.updateCampoCondutor(conn, numeroCarta, "validadeCarta", dataFormatada);                

                // Caso tudo ocorra bem, podemos redirecionar ou informar sucesso
                request.setAttribute("success", "Condutor atualizado com sucesso!");
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (ClassNotFoundException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao executar atualização: " + e.getMessage());
        } finally {
            // Fechar a conexão com o banco de dados
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }

        // Encaminhar para a JSP para exibir o resultado
        request.getRequestDispatcher("AtualizarCondutor14.jsp").forward(request, response);
    }
}