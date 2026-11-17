import bd.InicializadorBD;
import controlador.EditorialController;
import controlador.NotificacionController;
import controlador.ReservacionController;
import controlador.StandController;
import java.sql.SQLException;
import java.time.LocalDate;
import modelo.Configuracion;
import vista.ConsolaVista;

/**
 * Punto de entrada. Solo "arma" las piezas MVC: no tiene logica de negocio,
 * no escribe SQL y no imprime directamente (eso lo hace la Vista).
 */
public class Main {
    public static void main(String[] args) {
        ConsolaVista vista = new ConsolaVista();
        vista.mostrarTitulo("SISTEMA ADMINISTRATIVO FILEY (MVC + JDBC)");

        try {
            // 1. Capa de BD: preparar (si es BD en memoria) y probar la conexion
            InicializadorBD.inicializar();
            vista.mostrarConexionExitosa(InicializadorBD.probarConexion());

            // 2. Controladores
            StandController standController = new StandController();
            EditorialController editorialController = new EditorialController();
            ReservacionController reservacionController = new ReservacionController();

            // 3. Lectura de las tres tablas -> Vista
            vista.mostrarStands(standController.listar());
            vista.mostrarEditoriales(editorialController.listar());
            vista.mostrarReservaciones(reservacionController.listar());

            // 4. Notificaciones (base del PD2), ahora con datos leidos de la BD
            LocalDate hoy = LocalDate.now();
            Configuracion config = new Configuracion(
                    500.0,              // costo por metro cuadrado
                    hoy.plusDays(2),    // fecha limite para cubrir el costo
                    0.50,               // porcentaje minimo para pasar de reserva a ocupado
                    hoy.plusDays(5),    // fecha de pronto pago
                    0.10);              // descuento por pronto pago

            NotificacionController notificaciones = new NotificacionController(
                    standController, editorialController, reservacionController);
            vista.mostrarAvisos("VENCIMIENTO DE RESERVAS", notificaciones.verificarVencimientoReservas());
            vista.mostrarAvisos("PRONTO PAGO", notificaciones.verificarProntoPago(config));
            vista.mostrarAvisos("FECHA LIMITE DE COSTO", notificaciones.verificarFechaLimiteCosto(config));

        } catch (SQLException e) {
            vista.mostrarError("Fallo la conexion o lectura de la base de datos", e);
        }
    }
}
