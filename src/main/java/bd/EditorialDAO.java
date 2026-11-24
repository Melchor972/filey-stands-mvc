package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Editorial;

/** CAPA DE BD: CRUD de la tabla editorial. Solo SQL; lo unico que lanza es SQLException. */
public class EditorialDAO {
    private static final String SELECT =
            "SELECT id_editorial, nombre, correo_contacto FROM editorial";

    // ---- READ ----
    public List<Editorial> listarTodos() throws SQLException {
        List<Editorial> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SELECT + " ORDER BY id_editorial")) {
            while (rs.next()) {
                lista.add(construir(rs));
            }
        }
        return lista;
    }

    /** Devuelve null si no existe. */
    public Editorial obtenerPorId(String idEditorial) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT + " WHERE id_editorial = ?")) {
            ps.setString(1, idEditorial);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construir(rs) : null;
            }
        }
    }

    // ---- CREATE ----
    public void insertar(Editorial e) throws SQLException {
        String sql = "INSERT INTO editorial (id_editorial, nombre, correo_contacto) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getIdEditorial());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getCorreoContacto());
            ps.executeUpdate();
        }
    }

    // ---- UPDATE ----
    public void actualizar(Editorial e) throws SQLException {
        String sql = "UPDATE editorial SET nombre = ?, correo_contacto = ? WHERE id_editorial = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreoContacto());
            ps.setString(3, e.getIdEditorial());
            ps.executeUpdate();
        }
    }

    // ---- DELETE ----
    public void eliminar(String idEditorial) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement("DELETE FROM editorial WHERE id_editorial = ?")) {
            ps.setString(1, idEditorial);
            ps.executeUpdate();
        }
    }

    private Editorial construir(ResultSet rs) throws SQLException {
        return new Editorial(
                rs.getString("id_editorial"),
                rs.getString("nombre"),
                rs.getString("correo_contacto"));
    }
}
