package pt.isel.leim.servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import pt.isel.leim.sbd2.BD_backend;

@WebServlet("/AvaliarReputacao45")
public class AvaliarReputacao45 extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Inicializar variáveis
        String numeroFiscal = request.getParameter("numFiscal");
        String codigoDesconto = request.getParameter("desconto");
        String message = null;
        Connection conn = null;
        String descontosDisponiveis = null;

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());

            if (conn != null && numeroFiscal != null && !numeroFiscal.trim().isEmpty()) {
                // Verificar se o cliente deseja atribuir um desconto
                if (codigoDesconto != null && !codigoDesconto.trim().isEmpty()) {
                    try {
                        // Atribuir desconto ao cliente
                        BD_backend.adicionarDescontoCliente(conn, numeroFiscal, codigoDesconto);
                        message = "Desconto atribuído com sucesso!";
                    } catch (Exception e) {
                        message = "Erro ao atribuir desconto: " + e.getMessage();
                    }
                } else {
                    // Caso não seja um pedido de atribuir desconto, avaliar a reputação
                    double reputacao = Double.valueOf(BD_backend.obterReputacaoCliente(conn, numeroFiscal));
                    request.setAttribute("reputacao", reputacao);

                    // Avaliar reputação e obter descontos
                    if (reputacao >= 3.00 || reputacao == 0.00) {
                        HashMap<String, String> descontosCliente = BD_backend.obterDescontosAtivosCliente(conn, numeroFiscal);

                        if (descontosCliente.size() >= 1) {
                            message = "O cliente não pode ter mais descontos até usufruir daqueles que dispõe.";
                        } else {
                            // Capturar a saída do método `verDescontosAtivos`
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            PrintStream printStream = new PrintStream(outputStream);
                            PrintStream originalOut = System.out;

                            try {
                                // Redirecionar a saída padrão para capturar o que é impresso pelo método
                                System.setOut(printStream);
                                BD_backend.verDescontosAtivos(conn);
                            } finally {
                                // Restaurar a saída padrão do sistema
                                System.setOut(originalOut);
                            }

                            // Converter a saída capturada para uma string
                            descontosDisponiveis = outputStream.toString();
                            System.out.println(descontosDisponiveis);
                            request.setAttribute("descontosAtivos", descontosDisponiveis);
                        }
                    } else {
                        message = "O cliente não tem reputação suficiente para receber descontos.";
                    }
                }
            } else {
                message = "Erro: Número fiscal inválido ou conexão indisponível.";
            }

        } catch (Exception e) {
            message = "Erro ao consultar banco de dados: " + e.getMessage();
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
        request.setAttribute("message", message);
        request.getRequestDispatcher("Funcionario45.jsp").forward(request, response);
    }
}
