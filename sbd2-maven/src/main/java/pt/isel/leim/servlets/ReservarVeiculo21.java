package pt.isel.leim.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd2.Auxiliar;
import pt.isel.leim.sbd2.BD_backend;

@WebServlet("/ReservarVeiculo21")
public class ReservarVeiculo21 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numeroFiscal = request.getParameter("numeroFiscal");
        String localidade = request.getParameter("localidade");
        String dataInicio = request.getParameter("dataInicio");
        String dataFim = request.getParameter("dataFim");
        String categoria = request.getParameter("categoria");
        String desconto = request.getParameter("desconto");

        List<String> tiposVeiculo = null;
        HashMap<String, String> descontosAtivosCliente = null;
        String categorias = "";
        String descontos = "";
        Connection conn = null;

        try {
            // Conectar ao banco de dados
        	
			Class.forName("com.mysql.jdbc.Driver");
            conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());
            if (conn != null) {
                // Obter lista de tipos de veículos disponíveis no local
                if (localidade != null && !localidade.isEmpty()) {
                    tiposVeiculo = BD_backend.listarTiposVeiculo(conn, localidade);
                    for (String tipo : tiposVeiculo) {
                    	categorias+=tipo + " "; 
                    }   
                }

                // Obter descontos disponíveis para o cliente
                if (numeroFiscal != null && !numeroFiscal.isEmpty()) {
                    descontosAtivosCliente = BD_backend.obterDescontosAtivosCliente(conn, numeroFiscal);
                    for (String key : descontosAtivosCliente.keySet()) {
                    	descontos+=key + " ";
            	    }
                }
                
                
                
                if (dataInicio != null && dataFim != null && categoria != null) {
                	
                	String dataInicioFormatada;
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
					DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

					// Parse the input date and format it into the desired output
					LocalDateTime dateTime = LocalDateTime.parse(dataInicio, inputFormatter);
					dataInicioFormatada = dateTime.format(outputFormatter);
                    
					String dataFimFormatada;

					// Parse the input date and format it into the desired output
					dateTime = LocalDateTime.parse(dataFim, inputFormatter);
					dataFimFormatada = dateTime.format(outputFormatter);
                	
                	float custoFinal = Auxiliar.calcularCustoFinal(conn, numeroFiscal, categoria, Auxiliar.calcularDias(dataInicioFormatada, dataFimFormatada), desconto);
                	String[] reserva = {numeroFiscal, localidade, categoria, dataInicioFormatada, dataFimFormatada, desconto, Float.toString(custoFinal)};
                	BD_backend.adiconarReserva(conn, reserva);
                	request.setAttribute("custo", Float.toString(custoFinal));
                }
            } else {
                request.setAttribute("error", "Erro ao conectar à base de dados.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Erro ao acessar o banco de dados: " + e.getMessage());
        }

        // Passar dados para a JSP
        request.setAttribute("numeroFiscal", numeroFiscal);
        request.setAttribute("localidade", localidade);
        request.setAttribute("tiposVeiculo", categorias);
        request.setAttribute("descontosAtivosCliente", descontos);

        // Encaminhar para a JSP
        request.getRequestDispatcher("Cliente21.jsp").forward(request, response);
    }
}

