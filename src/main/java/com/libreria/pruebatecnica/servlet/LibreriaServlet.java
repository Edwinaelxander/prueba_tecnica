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

@WebServlet("/api/libros/")
public class LibreriaServlet extends HttpServlet {

    private List<Libro> libros = new ArrayList<>();
    private static final String DB_URL = "jdbc:mysql://mysql_databases:3306/prueba";
    private static final String USER = "root";
    private static final String PASS = "";
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT * FROM libros";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                libros.add(new Libro(id, nombre));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new ServletException("Error al conectar con la base de datos", e);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(libros));
        out.close();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);

        // Insertar en la base de datos
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "INSERT INTO libros (nombre) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nuevoLibro.getNombre());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1); // Obtener el  ID generado
                nuevoLibro.setId(idGenerado); // Establecer el ID en el objeto libro
            }
            stmt.close();
            conn.close();

            libros.add(nuevoLibro);
        } catch (SQLException e) {
            throw new ServletException("Error al agregar el libro a la base de datos", e);
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(libros));
        out.close();
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);

        int id = nuevoLibro.getId();
        String nombre = nuevoLibro.getNombre();
       
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        for (Libro libro : libros) {
            if (id==libro.getId()) {

                libro.setNombre(nombre);

                // Actualizar en la base de datos
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
                    throw new ServletException("Error al actualizar el libro en la base de datos", e);
                }

                out.println(gson.toJson(libro));
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        out.close();
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Libro nuevoLibro = gson.fromJson(req.getReader(), Libro.class);
        int id = nuevoLibro.getId();
        // Eliminar el libro de la lista
        boolean removed = libros.removeIf(libro -> id == libro.getId());

        // Eliminar de la base de datos
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "DELETE FROM libros WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new ServletException("Error al eliminar el libro en la base de datos", e);
        }

        resp.setStatus(removed ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_FOUND);
    }
}
