package ejercicio11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Ejercicio11 {
	


	    public static void main(String[] args) {
	    	Scanner scanner = new Scanner(System.in);
	    	System.out.print("Menu de opciones \n1.Insertar alumno\n2.Mostrar alumno\n3.Escribir en fichero");
            int opcion = scanner.nextInt();
	    	
            switch (opcion) {
			
	        case 1:
				insertarAlumno();
				break;
				
	        case 2:
	        	mostrarAlumnos();
				break;
				
	        case 3:
	        	guardarAlumnosEnFichero("alumnos.txt");
				break;
				
	        case 4:
	        	leerAlumnosDeFicheroYGuardarEnBD("alumnos.txt");
				break;
				
				

			default:
				break;
			}
	    }
	
	
	
	
	public static void insertarAlumno() {
		try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos15", "root", "manager")) {
                
                Scanner scanner = new Scanner(System.in);
                
                // Solicitar y leer datos del usuario
                System.out.print("Introduce el NIA: ");
                int nia = scanner.nextInt();
                scanner.nextLine(); // Consumir nueva línea

                System.out.print("Introduce el Nombre: ");
                String nombre = scanner.nextLine();

                System.out.print("Introduce los Apellidos: ");
                String apellidos = scanner.nextLine();

                System.out.print("Introduce el Género: ");
                String genero = scanner.nextLine();

                System.out.print("Introduce la Fecha de nacimiento (YYYY-MM-DD): ");
                String fechaNacimientoStr = scanner.nextLine();
                Date fechaNacimiento = Date.valueOf(fechaNacimientoStr); // Convertir a java.sql.Date

                System.out.print("Introduce el Ciclo: ");
                String ciclo = scanner.nextLine();

                System.out.print("Introduce el Curso: ");
                String curso = scanner.nextLine();

                System.out.print("Introduce el Grupo: ");
                String grupo = scanner.nextLine();
                
                // Crear la sentencia SQL de inserción
                String insertSql = "INSERT INTO alumnos15 (NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = conexion.prepareStatement(insertSql);

                // Asignar valores a los parámetros
                preparedStatement.setInt(1, nia);
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, apellidos);
                preparedStatement.setString(4, genero);
                preparedStatement.setDate(5, fechaNacimiento);
                preparedStatement.setString(6, ciclo);
                preparedStatement.setString(7, curso);
                preparedStatement.setString(8, grupo);

                // Ejecutar la inserción
                int filasInsertadas = preparedStatement.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Alumno insertado exitosamente.");
                }

                // Cerrar recursos
                preparedStatement.close();
                scanner.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
	}
	
	
	public static void mostrarAlumnos() {
        String sql = "SELECT * FROM alumnos15";
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos15", "root", "manager");
        	Statement sentencia = conexion.createStatement();
             ResultSet resul = sentencia.executeQuery(sql)) {

            System.out.println("Lista de alumnos:");
            while (resul.next()) {
                int nia = resul.getInt("NIA");
                String nombre = resul.getString("Nombre");
                String apellidos = resul.getString("Apellidos");
                String genero = resul.getString("Genero");
                Date fechaNacimiento = resul.getDate("FechaNacimiento");
                String ciclo = resul.getString("Ciclo");
                String curso = resul.getString("Curso");
                String grupo = resul.getString("Grupo");

                System.out.printf("NIA: %d, Nombre: %s, Apellidos: %s, Género: %s, Fecha de nacimiento: %s, Ciclo: %s, Curso: %s, Grupo: %s%n",
                        nia, nombre, apellidos, genero, fechaNacimiento, ciclo, curso, grupo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	
	public static void guardarAlumnosEnFichero(String nombreFichero) {
        String sql = "SELECT * FROM alumnos15";
        try (
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos15", "root", "manager");
            Statement sentencia = conexion.createStatement();
            ResultSet resul = sentencia.executeQuery(sql);
            BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))
        ) {
            while (resul.next()) {
                int nia = resul.getInt("NIA");
                String nombre = resul.getString("Nombre");
                String apellidos = resul.getString("Apellidos");
                String genero = resul.getString("Genero");
                Date fechaNacimiento = resul.getDate("FechaNacimiento");
                String ciclo = resul.getString("Ciclo");
                String curso = resul.getString("Curso");
                String grupo = resul.getString("Grupo");

                // Escribir cada registro en el archivo de texto
                bw.write(String.format("NIA: %d, Nombre: %s, Apellidos: %s, Género: %s, Fecha de nacimiento: %s, Ciclo: %s, Curso: %s, Grupo: %s%n",
                        nia, nombre, apellidos, genero, fechaNacimiento, ciclo, curso, grupo));
            }
            System.out.println("Los alumnos han sido guardados en el archivo " + nombreFichero);

        } catch (SQLException e) {
            System.out.println("Error al realizar la consulta SQL.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de texto.");
            e.printStackTrace();
        }
    }
	
	
	
	public static void leerAlumnosDeFicheroYGuardarEnBD(String nombreFichero) {
        
        try (
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos15", "root", "manager");
            BufferedReader reader = new BufferedReader(new FileReader(nombreFichero))
        ) {
            String linea;
            String insertSql = "INSERT INTO alumnos15 (NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, Grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(insertSql);
            
            while ((linea = reader.readLine()) != null) {
                // Suponiendo que los datos están en formato específico
                String[] datos = linea.split(", ");
                
                // Extraer y parsear los valores desde el texto
                int nia = Integer.parseInt(datos[0].split(": ")[1]);
                String nombre = datos[1].split(": ")[1];
                String apellidos = datos[2].split(": ")[1];
                String genero = datos[3].split(": ")[1];
                Date fechaNacimiento = Date.valueOf(datos[4].split(": ")[1]);
                String ciclo = datos[5].split(": ")[1];
                String curso = datos[6].split(": ")[1];
                String grupo = datos[7].split(": ")[1];

                // Asignar valores a la consulta SQL
                preparedStatement.setInt(1, nia);
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, apellidos);
                preparedStatement.setString(4, genero);
                preparedStatement.setDate(5, fechaNacimiento);
                preparedStatement.setString(6, ciclo);
                preparedStatement.setString(7, curso);
                preparedStatement.setString(8, grupo);
                
                // Ejecutar la inserción
                preparedStatement.executeUpdate();
            }
            
            System.out.println("Todos los alumnos han sido guardados en la base de datos.");

        } catch (SQLException e) {
            System.out.println("Error al realizar la conexión o insertar datos en la base de datos.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de texto.");
            e.printStackTrace();
        }
    }
	}



