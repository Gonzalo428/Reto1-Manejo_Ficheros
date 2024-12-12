<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tratamiento de Ficheros</title>
</head>
<body>
	<h1>Tratamiento de Ficheros</h1>
	<form>
		<%
		List<String[]> filas = (List<String[]>) request.getAttribute("filas");
		%>
		<table border="1">
			<tr>
				<%
				for (String[] fila : filas) {
				%>
			
			<tr>
				<%
				for (String campo : fila) {
				%>
				<td><%=campo%></td>
				<%
				}
				%>
			</tr>
			<%
			}
			%>
			</tr>
		</table>
	</form>
</body>
</html>