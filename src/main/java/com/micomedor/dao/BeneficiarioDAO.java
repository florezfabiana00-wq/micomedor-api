package com.micomedor.dao;

import com.micomedor.modelo.Beneficiario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de Beneficiario.
 * Se encarga de todas las operaciones con la tabla "beneficiario":
 * insertar, listar, buscar por id, actualizar y eliminar.
 *
 * Tabla esperada (MySQL):
 *
 * CREATE TABLE beneficiario (
 *     id INT AUTO_INCREMENT PRIMARY KEY,
 *     nombre VARCHAR(100) NOT NULL,
 *     documento VARCHAR(20) NOT NULL UNIQUE,
 *     telefono VARCHAR(20),
 *     edad INT NOT NULL
 * );
 */
public class BeneficiarioDAO {

    /**
     * Inserta un nuevo beneficiario en la base de datos.
     *
     * @param b beneficiario con los datos a guardar
     * @return true si se guardó correctamente, false si ocurrió un error
     */
    public boolean guardar(Beneficiario b) {
        String sql = "INSERT INTO beneficiarios (nombre, documento, telefono, edad) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.getNombre());
            ps.setString(2, b.getDocumento());
            ps.setString(3, b.getTelefono());
            ps.setInt(4, b.getEdad());

            int filas = ps.executeUpdate();

            // Recupera el id que generó la base de datos y lo asigna al objeto
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        b.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar beneficiario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Devuelve la lista de todos los beneficiarios registrados.
     *
     * @return lista de beneficiarios (vacía si no hay ninguno o si hay error)
     */
    public List<Beneficiario> listarTodos() {
        List<Beneficiario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, documento, telefono, edad FROM beneficiarios ORDER BY id";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(construirBeneficiario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar beneficiarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un beneficiario por su id.
     *
     * @param id identificador del beneficiario
     * @return el beneficiario, o null si no existe
     */
    public Beneficiario buscarPorId(int id) {
        String sql = "SELECT id, nombre, documento, telefono, edad FROM beneficiarios WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirBeneficiario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar beneficiario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza los datos de un beneficiario existente.
     *
     * @param b beneficiario con el id y los nuevos datos
     * @return true si se actualizó, false si no existe o hubo error
     */
    public boolean actualizar(Beneficiario b) {
        String sql = "UPDATE beneficiarios SET nombre = ?, documento = ?, telefono = ?, edad = ? WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, b.getNombre());
            ps.setString(2, b.getDocumento());
            ps.setString(3, b.getTelefono());
            ps.setInt(4, b.getEdad());
            ps.setInt(5, b.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar beneficiario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un beneficiario por su id.
     *
     * @param id identificador del beneficiario
     * @return true si se eliminó, false si no existe o hubo error
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM beneficiarios WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar beneficiario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convierte la fila actual de un ResultSet en un objeto Beneficiario.
     */
    private Beneficiario construirBeneficiario(ResultSet rs) throws SQLException {
        Beneficiario b = new Beneficiario();
        b.setId(rs.getInt("id"));
        b.setNombre(rs.getString("nombre"));
        b.setDocumento(rs.getString("documento"));
        b.setTelefono(rs.getString("telefono"));
        b.setEdad(rs.getInt("edad"));
        return b;
    }
}
