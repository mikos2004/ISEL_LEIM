package pt.isel.leim.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RecolherDados21")
public class RecolherDados21 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numeroFiscal = request.getParameter("numeroFiscal");
        String localidade = request.getParameter("localidade");

        // Encaminha NIF e localidade para a próxima página
        request.setAttribute("numeroFiscal", numeroFiscal);
        request.setAttribute("localidade", localidade);
        request.getRequestDispatcher("Cliente21.jsp").forward(request, response);
    }
}