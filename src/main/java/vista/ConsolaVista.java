package vista;

import excepciones.FileyException;
import java.util.List;
import modelo.Editorial;
import modelo.Reservacion;
import modelo.Stand;

/** VISTA: unica clase que imprime en consola. No consulta la BD ni decide reglas. */
public class ConsolaVista {

    public void mostrarTitulo(String titulo) {
        System.out.println("=== " + titulo + " ===");
    }

    public void mostrarConexionExitosa(String descripcion) {
        System.out.println("[OK] Conexion a la BD exitosa: " + descripcion);
        System.out.println();
    }

    public void mostrarError(String mensaje, Exception e) {
        System.err.println("[ERROR] " + mensaje + ": " + e.getMessage());
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /** Errores logicos / de validacion (paquete excepciones). */
    public void mostrarErrorLogico(FileyException e) {
        System.out.println("   [RECHAZADO] " + e.getMessage());
    }

    public void mostrarStands(List<Stand> stands) {
        System.out.println("--- STANDS (" + stands.size() + ") ---");
        System.out.printf("%-6s %-12s %8s %12s  %s%n", "ID", "DIMENSIONES", "M2", "PRECIO", "ESTADO");
        for (Stand s : stands) {
            System.out.printf("%-6s %-12s %8.1f %12.2f  %s%n", s.getIdStand(), s.getDimensiones(),
                    s.getMetrosCuadrados(), s.getPrecio(), s.getEstado());
        }
        System.out.println();
    }

    public void mostrarEditoriales(List<Editorial> editoriales) {
        System.out.println("--- EDITORIALES (" + editoriales.size() + ") ---");
        System.out.printf("%-7s %-22s %s%n", "ID", "NOMBRE", "CORREO");
        for (Editorial e : editoriales) {
            System.out.printf("%-7s %-22s %s%n", e.getIdEditorial(), e.getNombre(), e.getCorreoContacto());
        }
        System.out.println();
    }

    public void mostrarReservaciones(List<Reservacion> reservaciones) {
        System.out.println("--- RESERVACIONES (" + reservaciones.size() + ") ---");
        System.out.printf("%-4s %-6s %-9s %-12s %-12s %s%n",
                "ID", "STAND", "EDITORIAL", "FECHA", "VIGENCIA", "PAGADO");
        for (Reservacion r : reservaciones) {
            String vigencia = r.getVigenciaReserva() == null ? "-" : r.getVigenciaReserva().toString();
            System.out.printf("%-4d %-6s %-9s %-12s %-12s %.0f%%%n", r.getIdReservacion(), r.getIdStand(),
                    r.getIdEditorial(), r.getFechaReservacion(), vigencia, r.getPorcentajePagado() * 100);
        }
        System.out.println();
    }

    public void mostrarAvisos(String titulo, List<String> avisos) {
        System.out.println("--- " + titulo + " ---");
        if (avisos.isEmpty()) {
            System.out.println("(sin avisos)");
        }
        for (String aviso : avisos) {
            System.out.println(aviso);
        }
        System.out.println();
    }
}
