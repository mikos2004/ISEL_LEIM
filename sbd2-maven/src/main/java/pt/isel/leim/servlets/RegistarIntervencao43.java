package pt.isel.leim.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.BD_backend;

@WebServlet("/RegistarIntervencao43")
public class RegistarIntervencao43 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Captura os parâmetros enviados pelo formulário
        String matricula = request.getParameter("matricula");
        String quilometragem = request.getParameter("quilometragem");
        String descricao = request.getParameter("descricao");
        String data = request.getParameter("data");

        Connection conn = null;
        String message = null;

        try {
            // Conectar ao banco de dados
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null) {
                // Inserir os dados na base de dados
                String sql = "INSERT INTO veiculos (matricula, quilometragem, descricao, data) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, matricula);
                stmt.setString(2, quilometragem);
                stmt.setString(3, descricao);
                stmt.setString(4, data);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    message = "Veículo inserido com sucesso!";
                } else {
                    message = "Falha ao inserir o veículo.";
                }

                stmt.close();
            } else {
                message = "Erro ao conectar ao banco de dados.";
            }

        } catch (SQLException e) {
            message = "Erro de SQL: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Define o atributo para a mensagem de retorno
        request.setAttribute("message", message);

        // Redireciona para a JSP
        request.getRequestDispatcher("Funcionario43.jsp").forward(request, response);
    }
}
