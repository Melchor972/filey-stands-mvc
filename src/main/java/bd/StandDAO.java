package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.EstadoStand;
import modelo.Stand;

/**
 * CAPA DE BD: CRUD de la tabla stand. Solo ejecuta SQL; NO valida reglas de negocio
 * (eso lo hace el controlador). Lo unico que lanza es SQLException.
 */
public class StandDAO {
    private static final String SELECT =
            "SELECT id_stand, dimensiones, metros_cuadrados, precio, estado FROM stand";

    // ---- READ ----
    public List<Stand> listarTodos() throws SQLException {
        List<Stand> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SELECT + " ORDER BY id_stand")) {
            while (rs.next()) {
                lista.add(construir(rs));
            }
        }
        return lista;
    }

    /** Devuelve null si no existe. */
    public Stand obtenerPorId(String idStand) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT + " WHERE id_stand = ?")) {
            ps.setString(1, idStand);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construir(rs) : null;
            }
        }
    }

    // ---- CREATE ----
    public void insertar(Stand s) throws SQLException {
        String sql = "INSERT INTO stand (id_stand, dimensiones, metros_cuadrados, precio, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getIdStand());
            ps.setString(2, s.getDimensiones());
            ps.setDouble(3, s.getMetrosCuadrados());
            ps.setDouble(4, s.getPrecio());
            ps.setString(5, s.getEstado().name());
            ps.executeUpdate();
        }
    }

    // ---- UPDATE ----
    public void actualizar(Stand s) throws SQLException {
        String sql = "UPDATE stand SET dimensiones = ?, metros_cuadrados = ?, precio = ?, estado = ? "
                   + "WHERE id_stand = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getDimensiones());
            ps.setDouble(2, s.getMetrosCuadrados());
            ps.setDouble(3, s.getPrecio());
            ps.setString(4, s.getEstado().name());
            ps.setString(5, s.getIdStand());
            ps.executeUpdate();
        }
    }

    // ---- DELETE ----
    public void eliminar(String idStand) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement("DELETE FROM stand WHERE id_stand = ?")) {
            ps.setString(1, idStand);
            ps.executeUpdate();
        }
    }

    private Stand construir(ResultSet rs) throws SQLException {
        return new Stand(
                rs.getString("id_stand"),
                rs.getString("dimensiones"),
                rs.getDouble("metros_cuadrados"),
                rs.getDouble("precio"),
                EstadoStand.valueOf(rs.getString("estado")));
    }
}
