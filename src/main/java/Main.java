import bd.InicializadorBD;
import controlador.EditorialController;
import controlador.NotificacionController;
import controlador.ReservacionController;
import controlador.StandController;
import excepciones.FileyException;
import java.sql.SQLException;
import java.time.LocalDate;
import modelo.Configuracion;
import modelo.Editorial;
import modelo.EstadoStand;
import modelo.Reservacion;
import modelo.Stand;
import vista.ConsolaVista;

/**
 * Punto de entrada. Solo "arma" las piezas MVC: no tiene logica de negocio,
 * no escribe SQL y no imprime directamente (eso lo hace la Vista).
 */
public class Main {

    /** Una operacion de la demo; puede lanzar errores logicos o tecnicos. */
    private interface Operacion {
        void ejecutar() throws SQLException, FileyException;
    }

    public static void main(String[] args) {
        ConsolaVista vista = new ConsolaVista();
        vista.mostrarTitulo("SISTEMA ADMINISTRATIVO FILEY (MVC + JDBC + CRUD)");

        try {
            // 1. Capa de BD: preparar (si es BD en memoria) y probar la conexion
            InicializadorBD.inicializar();
            vista.mostrarConexionExitosa(InicializadorBD.probarConexion());

            // 2. Controladores (uno por entidad)
            StandController standController = new StandController();
            EditorialController editorialController = new EditorialController();
            ReservacionController reservacionController = new ReservacionController();

            LocalDate hoy = LocalDate.now();
            Configuracion config = new Configuracion(
                    500.0,              // costo por metro cuadrado
                    hoy.plusDays(2),    // fecha limite para cubrir el costo
                    0.50,               // porcentaje minimo para pasar de reserva a ocupado
                    hoy.plusDays(5),    // fecha de pronto pago
                    0.10);              // descuento por pronto pago

            // 3. READ: listas iniciales
            vista.mostrarStands(standController.listar());
            vista.mostrarEditoriales(editorialController.listar());
            vista.mostrarReservaciones(reservacionController.listar());

            // 4. CREATE / UPDATE / DELETE + errores logicos
            demostrarCrud(vista, config, standController, editorialController, reservacionController);

            // 5. Listas finales (deben quedar igual que al inicio)
            vista.mostrarTitulo("LISTAS DESPUES DEL CRUD");
            vista.mostrarStands(standController.listar());
            vista.mostrarEditoriales(editorialController.listar());
            vista.mostrarReservaciones(reservacionController.listar());

            // 6. Notificaciones (base del PD2), con datos leidos de la BD
            NotificacionController notificaciones = new NotificacionController(
                    standController, editorialController, reservacionController);
            vista.mostrarAvisos("VENCIMIENTO DE RESERVAS", notificaciones.verificarVencimientoReservas());
            vista.mostrarAvisos("PRONTO PAGO", notificaciones.verificarProntoPago(config));
            vista.mostrarAvisos("FECHA LIMITE DE COSTO", notificaciones.verificarFechaLimiteCosto(config));

        } catch (SQLException e) {
            vista.mostrarError("Fallo la conexion o lectura de la base de datos", e);
        }
    }

    private static void demostrarCrud(ConsolaVista vista, Configuracion config,
                                      StandController stands, EditorialController editoriales,
                                      ReservacionController reservaciones) {
        LocalDate hoy = LocalDate.now();
        vista.mostrarTitulo("DEMOSTRACION DEL CRUD");

        // ---- Editorial ----
        probar(vista, "CREATE editorial E-004", () ->
                editoriales.crear(new Editorial("E-004", "Ediciones del Sureste", "info@sureste.com")));
        probar(vista, "CREATE editorial E-004 otra vez (id duplicado)", () ->
                editoriales.crear(new Editorial("E-004", "Repetida", "x@x.com")));
        probar(vista, "CREATE editorial con correo invalido", () ->
                editoriales.crear(new Editorial("E-005", "Sin correo valido", "correo-malo")));
        probar(vista, "UPDATE editorial E-004 (nuevo correo)", () ->
                editoriales.actualizar(new Editorial("E-004", "Ediciones del Sureste", "ventas@sureste.com")));
        probar(vista, "UPDATE editorial E-999 (no existe)", () ->
                editoriales.actualizar(new Editorial("E-999", "Fantasma", "f@f.com")));

        // ---- Stand ----
        Stand nuevo = new Stand("S-06", "3x3", 9.0, 0, EstadoStand.DISPONIBLE);
        nuevo.setPrecio(stands.calcularPrecio(nuevo, config));
        probar(vista, "CREATE stand S-06", () -> stands.crear(nuevo));
        probar(vista, "CREATE stand con metros negativos", () ->
                stands.crear(new Stand("S-07", "3x3", -4.0, 0, EstadoStand.DISPONIBLE)));
        probar(vista, "UPDATE estado del stand S-04: BLOQUEADO -> DISPONIBLE", () ->
                stands.cambiarEstado("S-04", EstadoStand.DISPONIBLE));
        probar(vista, "UPDATE estado del stand S-04: vuelve a BLOQUEADO", () ->
                stands.cambiarEstado("S-04", EstadoStand.BLOQUEADO));

        // ---- Reservacion (conflictos) ----
        probar(vista, "CREATE reservacion 4: stand S-06 para E-004", () ->
                reservaciones.crear(new Reservacion(4, "S-06", "E-004", hoy, hoy.plusDays(7), 0.0)));
        probar(vista, "CREATE reservacion 5 sobre S-06 (ya reservado: conflicto)", () ->
                reservaciones.crear(new Reservacion(5, "S-06", "E-001", hoy, hoy.plusDays(7), 0.0)));
        probar(vista, "CREATE reservacion 6 sobre S-04 (bloqueado: conflicto)", () ->
                reservaciones.crear(new Reservacion(6, "S-04", "E-001", hoy, hoy.plusDays(7), 0.0)));
        probar(vista, "CREATE reservacion 1 (id duplicado)", () ->
                reservaciones.crear(new Reservacion(1, "S-03", "E-001", hoy, hoy.plusDays(7), 0.0)));
        probar(vista, "UPDATE reservacion 4 (pago del 30%)", () ->
                reservaciones.actualizar(new Reservacion(4, "S-06", "E-004", hoy, hoy.plusDays(7), 0.30)));
        probar(vista, "UPDATE reservacion 99 (no existe)", () ->
                reservaciones.actualizar(new Reservacion(99, "S-06", "E-004", hoy, hoy.plusDays(7), 0.30)));

        // ---- DELETE (se limpia lo creado y se prueban los conflictos) ----
        probar(vista, "DELETE editorial E-004 (tiene reservaciones: conflicto)", () ->
                editoriales.eliminar("E-004"));
        probar(vista, "DELETE stand S-06 (tiene reservaciones: conflicto)", () ->
                stands.eliminar("S-06"));
        probar(vista, "DELETE reservacion 4 (libera el stand)", () -> reservaciones.eliminar(4));
        probar(vista, "DELETE reservacion 4 otra vez (no existe)", () -> reservaciones.eliminar(4));
        probar(vista, "DELETE stand S-06", () -> stands.eliminar("S-06"));
        probar(vista, "DELETE editorial E-004", () -> editoriales.eliminar("E-004"));
        vista.mostrarMensaje("");
    }

    /** Ejecuta una operacion: si funciona dice OK; si falla, la Vista muestra el motivo. */
    private static void probar(ConsolaVista vista, String titulo, Operacion operacion) {
        vista.mostrarMensaje("-> " + titulo);
        try {
            operacion.ejecutar();
            vista.mostrarMensaje("   [OK]");
        } catch (FileyException e) {
            vista.mostrarErrorLogico(e);
        } catch (SQLException e) {
            vista.mostrarError("Error de base de datos", e);
        }
    }
}
