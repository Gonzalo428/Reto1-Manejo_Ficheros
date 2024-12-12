package com.gf.Reto1.Controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletFich() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String formato = request.getParameter("formato");
		String operacion = request.getParameter("operacion");

		if ("CSV".equalsIgnoreCase(formato)) {
			if ("lectura".equalsIgnoreCase(operacion)) {
				leerCSV(request, response);
			} else if ("escritura".equalsIgnoreCase(operacion)) {
				escribirCSV(request, response);
			} else {
				mostrarError(response, "Operación no válida.");
			}
		} else if ("XLS".equalsIgnoreCase(formato)) {
			if ("lectura".equalsIgnoreCase(operacion)) {
				leerXLS(request, response);
			} else if ("escritura".equalsIgnoreCase(operacion)) {
				escribirXLS(request, response);
			} else {
				mostrarError(response, "Operación no válida.");
			}
		} else {
			mostrarError(response, "Formato no soportado.");
		}
	}

	private void leerCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String rutaFichero = request.getParameter("ruta");
		try (CSVReader reader = new CSVReader(new FileReader(rutaFichero))) {
			List<String[]> filas = reader.readAll();
			request.setAttribute("filas", filas);
			request.getRequestDispatcher("AccesoDatos.jsp").forward(request, response);
		} catch (Exception e) {
			mostrarError(response, "Error en la lectura del CSV: " + e.getMessage());
		}
	}

	private void escribirCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String rutaFichero = request.getParameter("ruta");
		String[] datos = request.getParameterValues("datos");

		if (rutaFichero == null || rutaFichero.isEmpty()) {
			mostrarError(response, "La ruta del fichero no puede estar vacía.");
			return;
		}
		if (datos == null || datos.length == 0) {
			mostrarError(response, "Los datos no pueden estar vacíos.");
			return;
		}
		try (CSVWriter writer = new CSVWriter(new FileWriter(rutaFichero, true))) {
			writer.writeNext(datos);
			if (!response.isCommitted()) {
				// Redirige a un servlet que maneje la lectura del CSV
				String redirectURL = "ServletFich?operacion=lectura&formato=CSV&ruta=" + rutaFichero;
				response.sendRedirect(redirectURL);
			} else {
				mostrarError(response, "La respuesta ya ha sido comprometida y no se puede redirigir.");
			}
		} catch (IOException e) {
			mostrarError(response, "Error en la escritura del CSV: " + e.getMessage());
		} catch (Exception e) {
			mostrarError(response, "Error inesperado: " + e.getMessage());
		}
	}

	private void leerXLS(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String rutaFichero = request.getParameter("ruta");
		List<String[]> filas = new ArrayList<>();

		try (FileInputStream inp = new FileInputStream(rutaFichero); Workbook workbook = new XSSFWorkbook(inp)) {
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				String[] fila = new String[row.getPhysicalNumberOfCells()];
				for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
					Cell cell = row.getCell(i);
					fila[i] = cell.toString();
				}
				filas.add(fila);
			}
			request.setAttribute("filas", filas);
			request.getRequestDispatcher("AccesoDatos.jsp").forward(request, response);
		} catch (Exception e) {
			mostrarError(response, "Error en la lectura del XLS: " + e.getMessage());
		}
	}

	private void escribirXLS(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String rutaFichero = request.getParameter("ruta");
		String[] datos = request.getParameterValues("datos");

		Workbook workbook;
		Sheet sheet;

		// Verifica si el archivo ya existe
		File file = new File(rutaFichero);
		if (file.exists()) {
			try (FileInputStream inp = new FileInputStream(rutaFichero)) {
				workbook = new XSSFWorkbook(inp);
				sheet = workbook.getSheetAt(0); // Obtén la primera hoja existente
			}
		} else {
			workbook = new XSSFWorkbook(); // Crea un nuevo libro si no existe
			sheet = workbook.createSheet("Datos");
		}

		// Agrega una nueva fila al final
		Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		for (int i = 0; i < datos.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(datos[i]);
		}

		// Escribe los cambios en el archivo
		try (FileOutputStream fileOut = new FileOutputStream(rutaFichero)) {
			workbook.write(fileOut);
		} catch (Exception e) {
			mostrarError(response, "Error en la escritura del XLS: " + e.getMessage());
			return;
		} finally {
			workbook.close(); // Asegúrate de cerrar el libro
		}

		// Redirigir a la página para leer y mostrar el archivo
		response.sendRedirect(rutaFichero);
	}

	private void mostrarError(HttpServletResponse response, String mensaje) throws IOException {
		response.sendRedirect("Error.jsp?mensaje=" + mensaje);
	}

}
