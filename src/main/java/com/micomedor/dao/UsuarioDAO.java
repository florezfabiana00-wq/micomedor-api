package com.micomedor.dao;

import com.micomedor.modelo.Usuario;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Acceso a datos de la tabla usuarios
public class UsuarioDAO {

    // Convierte la contraseña a hash SHA-256 (no se guarda en texto plano)
    private String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Registra un usuario nuevo; retorna true si se insertó
    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios (usuario, contrasena) VALUES (?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, hash(u.getContrasena()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Falla, por ejemplo, si el usuario ya existe
            e.printStackTrace();
            return false;
        }
    }

    // Valida usuario y contraseña; retorna true si coinciden
    public boolean autenticar(String usuario, String contrasena) {
        String sql = "SELECT id FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, hash(contrasena));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}