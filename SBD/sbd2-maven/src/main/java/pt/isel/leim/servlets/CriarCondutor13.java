package pt.isel.leim.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.BD_backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/CriarCondutor13")
public class CriarCondutor13 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Receber os dados do formulário
        String numeroCarta = request.getParameter("numeroCarta");
        String nome = request.getParameter("nome");
        String dataInput = request.getParameter("data");


        // Conexão ao banco de dados
        Connection conn = null;

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
            if (conn != null) {
                
            	BD_backend.adicionarCondutor(conn, String.valueOf(numeroCarta), nome, dataFormatada);
                
                // Caso tudo ocorra bem, podemos redirecionar ou informar sucesso
                request.setAttribute("success", "Condutor registrado com sucesso!");
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            request.setAttribute("error", "Driver JDBC não encontrado.");
        } finally {
            // Fechar a conexão
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Erro ao fechar a conexão com a base de dados: " + e.getMessage());
            }
        }

        // Encaminhar para a JSP para exibir o resultado
        request.getRequestDispatcher("CriarCondutor13.jsp").forward(request, response);
    }
}
