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

@WebServlet("/EscolherAluguer31")
public class EscolherAluguer31 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String linha = request.getParameter("aluguer");
        String numeroCartaConducao = request.getParameter("numeroCartaConducao");
        Connection conn = null;
        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && linha != null && !linha.trim().isEmpty()) {
                // Capturar a saída do método `consultarDadosVeiculo`
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                PrintStream originalOut = System.out;

                try {
                    // Redirecionar a saída padrão para capturar o que é impresso pelo método
                    System.setOut(printStream);
                    String query = "SELECT * FROM Aluguer " +
      	                   "WHERE numeroCartaConducao = '" + numeroCartaConducao.trim() + "' AND status = 'Pendente';";
                    String[] aluguer = BD_backend.showDataLinha(query, conn, Integer.parseInt(linha));
                    System.setOut(originalOut);
                    
                    String[] lugarLevantamento = BD_backend.levantarVeiculo(conn, aluguer[9]);
                    
                    //System.out.println(aluguer[9]);
                    
                    BD_backend.desocuparLugar(conn, lugarLevantamento);
                    BD_backend.desassociarVeiculo(conn, aluguer[9], lugarLevantamento);
                    
                    BD_backend.mudarStatusAluguer(conn, "Em andamento", aluguer[0], aluguer[9]);
                    
                } finally {
                    // Restaurar a saída padrão do sistema
                    System.setOut(originalOut);
                }

                request.setAttribute("message", "Veículo levantado com sucesso");

            } else {
                request.setAttribute("error", "Erro: Matrícula inválida ou conexão indisponível.");
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
        request.getRequestDispatcher("Condutor31.jsp").forward(request, response);
    }
}
