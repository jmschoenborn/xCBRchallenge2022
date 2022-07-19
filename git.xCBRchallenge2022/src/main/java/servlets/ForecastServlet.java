package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.CSVmanager;

import java.io.IOException;
import java.util.ArrayList;
import data.Weather;

public class ForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//String date = request.getParameter("date");
		//ArrayList<Weather> content = new ArrayList<Weather>();

		//content = CSVmanager.readCSV("weatherdata.csv");
		CSVmanager.printCSV("D:/weatherdata.csv");

		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
