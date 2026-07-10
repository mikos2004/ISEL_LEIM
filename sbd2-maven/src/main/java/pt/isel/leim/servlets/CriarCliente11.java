package pt.isel.leim.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.Auxiliar;
import pt.isel.leim.sbd2.BD_backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CriarCliente11")
public class CriarCliente11 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recebendo os dados do formulário
        String numeroFiscal = request.getParameter("numeroFiscal");
        String nome = request.getParameter("nome");
        String contacto = request.getParameter("contacto");
        String morada = request.getParameter("morada");
        String distrito = request.getParameter("distrito");
        String concelho = request.getParameter("concelho");
        String freguesia = request.getParameter("freguesia");
        String linguas = request.getParameter("linguas");
        String moeda = request.getParameter("moeda");
        String tipoCliente = request.getParameter("tipoCliente");
        String capitalSocial = request.getParameter("capitalSocial");

     

        // Conexão ao banco de dados
        Connection conn = null;
        
        try {
            // Conectar ao banco de dados
        	Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());
            if (conn != null) {
                // Inserir cliente no banco de dados
                BD_backend.adicionarCliente(conn, numeroFiscal, nome, contacto, morada, distrito, concelho, freguesia, linguas, moeda, tipoCliente.trim(), capitalSocial);
                
                // Adicionar o desconto ao cliente
                BD_backend.adicionarDescontoCliente(conn, numeroFiscal, "D00001");

                // Caso tudo ocorra bem, podemos redirecionar ou informar sucesso
                request.setAttribute("success", "Cliente registrado com sucesso!");
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
        request.getRequestDispatcher("CriarCliente11.jsp").forward(request, response);
    }
}