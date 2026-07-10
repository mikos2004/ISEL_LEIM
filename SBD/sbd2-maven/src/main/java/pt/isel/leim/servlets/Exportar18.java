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

@WebServlet("/Exportar18")
public class Exportar18 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar os parâmetros recebidos do formulário
        String matricula = request.getParameter("matricula");
        String filePath = request.getParameter("ficheiro");

        
        // Conexão ao banco de dados
        Connection conn = null;
        
        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null) {
                // Chamar método auxiliar para atualizar o veículo
            	BD_backend.exportJSON(conn, matricula, filePath);

                // Caso tudo ocorra bem
                request.setAttribute("success", "Ficheiro exportado com sucesso!");
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (ClassNotFoundException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao executar atualização: " + e.getMessage());
            e.printStackTrace();
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
        request.getRequestDispatcher("Exportar18.jsp").forward(request, response);
    }
}
