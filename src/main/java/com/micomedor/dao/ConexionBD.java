package com.micomedor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Administra la conexión a la base de datos
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/micomedor";
    private static final String USER = "root";
    private static final String PASS = "admin1234";

    // Devuelve una conexión activa a la BD
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}