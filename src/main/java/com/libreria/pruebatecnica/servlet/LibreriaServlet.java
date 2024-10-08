package com.libreria.pruebatecnica.servlet;

import com.libreria.pruebatecnica.model.Libro;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;

// Define el servlet y su URL de acceso
@WebServlet("/api/libros/")
public class LibreriaServlet extends HttpServlet {

    // Lista para almacenar los libros
    private List<Libro> libros = new ArrayList<>();
    // Configuración de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://mysql_databases:3306/prueba";
    private static final String USER = "root";
    private static final String PASS = "";
    // Instancia de Gson para convertir objetos a JSON
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        try {
            // Carga del driver de MySQL
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Conexión a la base de datos
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // Consulta para obtener todos los libros
            String query = "SELECT * FROM libros";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Almacena los libros en la lista
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                libros.add(new Libro(id, nombre));
            }
            // Cierre de recursos
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Manejo de excepciones al conectar a la base de datos
            throw new ServletException("Error al conectar con la base de datos", e);
        }
    }

    // Método para manejar solicitudes GET
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        // Convierte la lista de libros a JSON y la envía como respuesta
        out.println(gson.toJson(libros));
        out.close();
    }

    // Método para manejar solicitudes POST
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Convierte el cuerpo de la solicitud JSON a un objeto Libro
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);

        // Inserta el nuevo libro en la base de datos
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "INSERT INTO libros (nombre) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nuevoLibro.getNombre());
            stmt.executeUpdate();
            // Obtiene el ID generado automáticamente
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1); // Obtener el ID generado
                nuevoLibro.setId(idGenerado); // Establecer el ID en el objeto libro
            }
            stmt.close();
            conn.close();

            // Añade el nuevo libro a la lista en memoria
            libros.add(nuevoLibro);
        } catch (SQLException e) {
            // Manejo de excepciones al agregar el libro a la base de datos
            throw new ServletException("Error al agregar el libro a la base de datos", e);
        }

        // Establece el estado de la respuesta a 201 (Creado)
        resp.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(libros)); // Envía la lista de libros actualizada como respuesta
        out.close();
    }

    // Método para manejar solicitudes PUT
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Convierte el cuerpo de la solicitud JSON a un objeto Libro
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);

        int id = nuevoLibro.getId();
        String nombre = nuevoLibro.getNombre();
       
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Busca el libro por ID en la lista
        for (Libro libro : libros) {
            if (id == libro.getId()) {
                // Actualiza el nombre del libro
                libro.setNombre(nombre);

                // Actualiza en la base de datos
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String query = "UPDATE libros SET nombre = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, libro.getNombre());
                    stmt.setLong(2, libro.getId());
                    stmt.executeUpdate();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    // Manejo de excepciones al actualizar el libro en la base de datos
                    throw new ServletException("Error al actualizar el libro en la base de datos", e);
                }

                out.println(gson.toJson(libro)); // Envía el libro actualizado como respuesta
                return;
            }
        }

        // Si no se encuentra el libro, establece el estado de la respuesta a 404 (No encontrado)
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        out.close();
    }

    // Método para manejar solicitudes DELETE
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Convierte el cuerpo de la solicitud JSON a un objeto Libro
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);
        int id = nuevoLibro.getId();
        // Elimina el libro de la lista en memoria
        boolean removed = libros.removeIf(libro -> id == libro.getId());

        // Elimina de la base de datos
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "DELETE FROM libros WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Manejo de excepciones al eliminar el libro en la base de datos
            throw new ServletException("Error al eliminar el libro en la base de datos", e);
        }

        // Establece el estado de la respuesta a 204 (Sin contenido) o 404 (No encontrado)
        resp.setStatus(removed ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_FOUND);
    }
}
