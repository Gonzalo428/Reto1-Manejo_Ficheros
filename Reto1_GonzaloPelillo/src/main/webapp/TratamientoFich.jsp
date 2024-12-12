<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Acceso a los Datos</title>
<script>
	function toggleInputFields() {
		const operacion = document.querySelector('select[name="operacion"]').value;
		const datosInput = document.getElementById('datosInput');
		if (operacion === 'escritura') {
			datosInput.style.display = 'block';
		} else {
			datosInput.style.display = 'none';
		}
	}
</script>
</head>
<body>
	<form action="ServletFich" method="post">
		<label for="formato">Formato:</label> <select name="formato">
			<option value="CSV">CSV</option>
			<option value="XLS">XLS</option>
		</select> </br> </br> <label for="operacion">Operación:</label> <select name="operacion">
			<option value="lectura">Lectura</option>
			<option value="escritura">Escritura</option>
		</select><input type="text" name="datos" required></br> </br> <label for="ruta">Ruta del fichero:</label> <input type="text"
			name="ruta" required> <input type="submit" value="Enviar">
	</form>
</body>
</html>