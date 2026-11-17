package bd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * CAPA DE BD: clase dedicada EXCLUSIVAMENTE a la conexion.
 * No tiene consultas SQL ni logica de negocio: solo lee db.properties
 * y entrega conexiones JDBC mediante DriverManager.
 */
public class ConexionBD {
    private static final String ARCHIVO = "/db.properties";
    private static final Properties props = cargarPropiedades();

    private ConexionBD() { }

    private static Properties cargarPropiedades() {
        Properties p = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(ARCHIVO)) {
            if (in == null) {
                throw new IllegalStateException("No se encontro " + ARCHIVO + " en resources");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + ARCHIVO, e);
        }
        return p;
    }

    /** Abre una conexion nueva; quien la pida debe cerrarla (try-with-resources). */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("bd.url"),
                props.getProperty("bd.usuario"),
                props.getProperty("bd.password"));
    }

    /** Lee un valor de db.properties (usado por otras clases de la capa de BD). */
    public static String getPropiedad(String clave, String porDefecto) {
        return props.getProperty(clave, porDefecto);
    }
}
