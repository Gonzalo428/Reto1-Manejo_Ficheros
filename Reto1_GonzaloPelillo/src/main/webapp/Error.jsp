<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ERROR</title>
</head>
<body>
	<%
	String mensaje = request.getParameter("mensaje");
	%>
	<h1 style="color: red;" align="center">Tipo de Error</h1>
	<p><%=mensaje%></p>
</body>
</html>