package bd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Reservacion;

/** CAPA DE BD: CRUD de la tabla reservacion. Solo SQL; lo unico que lanza es SQLException. */
public class ReservacionDAO {
    private static final String SELECT =
            "SELECT id_reservacion, id_stand, id_editorial, fecha_reservacion, "
          + "vigencia_reserva, porcentaje_pagado FROM reservacion";

    // ---- READ ----
    public List<Reservacion> listarTodos() throws SQLException {
        List<Reservacion> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SELECT + " ORDER BY id_reservacion")) {
            while (rs.next()) {
                lista.add(construir(rs));
            }
        }
        return lista;
    }

    /** Devuelve null si no existe. */
    public Reservacion obtenerPorId(int idReservacion) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT + " WHERE id_reservacion = ?")) {
            ps.setInt(1, idReservacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construir(rs) : null;
            }
        }
    }

    /** Cuantas reservaciones tiene un stand (para validar borrados). */
    public int contarPorStand(String idStand) throws SQLException {
        return contar("SELECT COUNT(*) FROM reservacion WHERE id_stand = ?", idStand);
    }

    /** Cuantas reservaciones tiene una editorial (para validar borrados). */
    public int contarPorEditorial(String idEditorial) throws SQLException {
        return contar("SELECT COUNT(*) FROM reservacion WHERE id_editorial = ?", idEditorial);
    }

    private int contar(String sql, String id) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // ---- CREATE ----
    public void insertar(Reservacion r) throws SQLException {
        String sql = "INSERT INTO reservacion (id_reservacion, id_stand, id_editorial, "
                   + "fecha_reservacion, vigencia_reserva, porcentaje_pagado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getIdReservacion());
            ps.setString(2, r.getIdStand());
            ps.setString(3, r.getIdEditorial());
            ps.setDate(4, Date.valueOf(r.getFechaReservacion()));
            if (r.getVigenciaReserva() == null) {
                ps.setNull(5, Types.DATE);
            } else {
                ps.setDate(5, Date.valueOf(r.getVigenciaReserva()));
            }
            ps.setDouble(6, r.getPorcentajePagado());
            ps.executeUpdate();
        }
    }

    // ---- UPDATE (solo vigencia y porcentaje pagado; stand y editorial no cambian) ----
    public void actualizar(Reservacion r) throws SQLException {
        String sql = "UPDATE reservacion SET vigencia_reserva = ?, porcentaje_pagado = ? "
                   + "WHERE id_reservacion = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (r.getVigenciaReserva() == null) {
                ps.setNull(1, Types.DATE);
            } else {
                ps.setDate(1, Date.valueOf(r.getVigenciaReserva()));
            }
            ps.setDouble(2, r.getPorcentajePagado());
            ps.setInt(3, r.getIdReservacion());
            ps.executeUpdate();
        }
    }

    // ---- DELETE ----
    public void eliminar(int idReservacion) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement("DELETE FROM reservacion WHERE id_reservacion = ?")) {
            ps.setInt(1, idReservacion);
            ps.executeUpdate();
        }
    }

    private Reservacion construir(ResultSet rs) throws SQLException {
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
