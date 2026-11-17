package bd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Editorial;
import modelo.EstadoStand;
import modelo.Reservacion;
import modelo.Stand;

/**
 * CAPA DE BD: clase independiente que agrupa TODAS las lecturas (SELECT / Read).
 * No sabe como se conecta: pide la conexion a ConexionBD.
 * Flujo JDBC: Connection -> Statement -> executeQuery -> ResultSet -> objeto.
 */
public class LecturaBD {

    // ---------------- STANDS ----------------
    private static final String SQL_STAND =
            "SELECT id_stand, dimensiones, metros_cuadrados, precio, estado FROM stand";

    public List<Stand> listarStands() throws SQLException {
        List<Stand> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_STAND + " ORDER BY id_stand")) {
            while (rs.next()) {
                lista.add(construirStand(rs));
            }
        }
        return lista;
    }

    public Stand obtenerStandPorId(String idStand) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_STAND + " WHERE id_stand = ?")) {
            ps.setString(1, idStand);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construirStand(rs) : null;
            }
        }
    }

    // ---------------- EDITORIALES ----------------
    private static final String SQL_EDITORIAL =
            "SELECT id_editorial, nombre, correo_contacto FROM editorial";

    public List<Editorial> listarEditoriales() throws SQLException {
        List<Editorial> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_EDITORIAL + " ORDER BY id_editorial")) {
            while (rs.next()) {
                lista.add(construirEditorial(rs));
            }
        }
        return lista;
    }

    public Editorial obtenerEditorialPorId(String idEditorial) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_EDITORIAL + " WHERE id_editorial = ?")) {
            ps.setString(1, idEditorial);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construirEditorial(rs) : null;
            }
        }
    }

    // ---------------- RESERVACIONES ----------------
    private static final String SQL_RESERVACION =
            "SELECT id_reservacion, id_stand, id_editorial, fecha_reservacion, "
          + "vigencia_reserva, porcentaje_pagado FROM reservacion";

    public List<Reservacion> listarReservaciones() throws SQLException {
        List<Reservacion> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_RESERVACION + " ORDER BY id_reservacion")) {
            while (rs.next()) {
                lista.add(construirReservacion(rs));
            }
        }
        return lista;
    }

    // ---------------- Reconstruccion de objetos (mapeo objeto-relacional) ----------------
    private Stand construirStand(ResultSet rs) throws SQLException {
        return new Stand(
                rs.getString("id_stand"),
                rs.getString("dimensiones"),
                rs.getDouble("metros_cuadrados"),
                rs.getDouble("precio"),
                EstadoStand.valueOf(rs.getString("estado")));
    }

    private Editorial construirEditorial(ResultSet rs) throws SQLException {
        return new Editorial(
                rs.getString("id_editorial"),
                rs.getString("nombre"),
                rs.getString("correo_contacto"));
    }

    private Reservacion construirReservacion(ResultSet rs) throws SQLException {
        return new Reservacion(
                rs.getInt("id_reservacion"),
                rs.getString("id_stand"),
                rs.getString("id_editorial"),
                aLocalDate(rs.getDate("fecha_reservacion")),
                aLocalDate(rs.getDate("vigencia_reserva")),   // puede ser NULL
                rs.getDouble("porcentaje_pagado"));
    }

    private LocalDate aLocalDate(Date fecha) {
        return fecha == null ? null : fecha.toLocalDate();
    }
}
