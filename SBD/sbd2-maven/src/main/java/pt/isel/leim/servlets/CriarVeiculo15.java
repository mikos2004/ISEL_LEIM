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

@WebServlet("/CriarVeiculo15")
public class CriarVeiculo15 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recebendo os dados do formulário
        String matricula = request.getParameter("matricula");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String cor = request.getParameter("cor");
        String tipo = request.getParameter("tipo");
        String numeroLugares = request.getParameter("numeroLugares");
        String numeroPortas = request.getParameter("numeroPortas");
        String capacidadeCarga = request.getParameter("capacidadeCarga");
        String tipoMotor = request.getParameter("tipoMotor");
        String potencia = request.getParameter("potencia");
        String localidade = request.getParameter("localidade");

        // Conexão ao banco de dados
        Connection conn = null;

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null || matricula != null || matricula.isEmpty() ||
                    marca != null || marca.isEmpty() ||
                    modelo != null || modelo.isEmpty() ||
                    cor != null || cor.isEmpty() ||
                    tipo != null || tipo.isEmpty() ||
                    numeroLugares != null || numeroLugares.isEmpty() ||
                    numeroPortas != null || numeroPortas.isEmpty() ||
                    capacidadeCarga != null || capacidadeCarga.isEmpty() ||
                    tipoMotor != null || tipoMotor.isEmpty() ||
                    potencia != null || potencia.isEmpty() ||
                    localidade != null || localidade.isEmpty()) {

                // Inserir veículo no banco de dados
            	BD_backend.adicionarVeiculo(conn, matricula, marca, modelo, cor, tipo, numeroLugares, numeroPortas, capacidadeCarga, tipoMotor,potencia, localidade);

                // Caso tudo ocorra bem, podemos redirecionar ou informar sucesso
                request.setAttribute("success", "Veículo registrado com sucesso!");
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        request.getRequestDispatcher("CriarVeiculo15.jsp").forward(request, response);
    }
}