# Sistema Administrativo Filey — MVC + Base de Datos (JDBC)
# PD4
Con base en las instrucciones de la sesión de hoy 12/Nov utilizar como base el ejercicio del PD2 de Stands

Enviar el URL donde se encuentra esta actividad que incluya:
Documentación de lo implementado
Código
Uso de patrón MVC + BD
Diagrama de clases
Diagrama de Entidad-Relación (Stands, Editorial y Reservación.)
Implementar JDBC para hacer lecturas de las Entidades Stands, Editorial y Reservación.
El main() debe mostrar que es posible la conexión a la BD y presentar las listas de cada tabla de Stands, Editorial y Reservación.
Video que explique el código para la lectura de las tablas Stands, Editorial y Reservación presentando el detalle de las capas. Uso del patrón MCV + capa de BDVideo de máximo 5 minutos. Incluir el URL en el repo (asegurar que hay acceso)

# Video
https://alumnosuady-my.sharepoint.com/:v:/g/personal/a25216423_alumnos_uady_mx/IQCOsIPAClISRrVww036qEZfAVaMcUEPHM1gJoUP79foZKw?e=fs6VL2

# Requisitos

1. Requisitos Funcionales (RF)

RF-01. Gestión de Conexión a Base de Datos:** El sistema debe establecer y validar una conexión activa con el manejador de base de datos relacional utilizando la especificación estándar de JDBC y parámetros de configuración externalizados (URL, usuario y contraseña en `db.properties`).

RF-02. Lectura de Entidades de Stands:** El sistema debe consultar la tabla correspondiente a los **Stands** mediante sentencias SQL `SELECT`, recuperando los registros almacenados y reconstruyendo cada fila en instancias de la clase `Stand` con sus respectivos atributos (identificador, nombre, dimensiones y estado).

RF-03. Lectura de Entidades de Editoriales:** El sistema debe consultar la tabla de **Editorial** en la base de datos para extraer el catálogo completo de editoriales registradas y mapear cada registro a objetos de tipo `Editorial` en memoria.

RF-04. Lectura de Entidades de Reservaciones:** El sistema debe ejecutar consultas de lectura sobre la tabla **Reservación** para obtener el historial y estado de las asignaciones vigentes, reconstruyendo los objetos `Reservacion` y vinculando sus referencias con los stands y editoriales correspondientes.

RF-05. Procesamiento y Lógica de Negocio en Controladores:** El sistema debe delegar toda la lógica operativa y validaciones a las clases del paquete `controlador` (`StandController`, `EditorialController`, `ReservacionController` y `NotificacionController`), manteniendo las clases del modelo libres de reglas de negocio.

RF-06. Gestión de Notificaciones a Editoriales:** El sistema debe contar con un mecanismo en el controlador correspondiente (`NotificacionController`) capaz de procesar y emitir mensajes o avisos hacia las editoriales registradas (como notificaciones de estado o vencimiento de reservaciones).

RF-07. Inicialización y Carga de Esquema SQL:** El sistema debe disponer de scripts SQL estructurados (`schema.sql` y `datos.sql`) y un inicializador (`InicializadorBD`) que permitan generar las tablas relacionales y poblar los registros iniciales necesarios para las pruebas de lectura.

RF-08. Visualización en Consola desde el Método `main()`:** Al ejecutar la clase principal `Main.java`, el sistema debe verificar en consola que la conexión a la base de datos fue exitosa y desplegar de manera ordenada y legible las listas completas de los registros recuperados de las tres tablas: Stands, Editorial y Reservación.

2. Requisitos No Funcionales (RNF)

RNF-01. Arquitectura Modelo-Vista-Controlador (MVC) + Capa de Datos:** El código fuente debe mantener una estricta separación de responsabilidades dividida en cuatro paquetes independientes: `modelo` (entidades puras de datos), `vista` (interfaz de salida en consola), `controlador` (lógica de negocio) y `bd` (persistencia y patrón DAO).

RNF-02. Desacoplamiento mediante Patrón DAO (Data Access Object):** El acceso a los datos debe estar encapsulado en clases DAO específicas (`StandDAO`, `EditorialDAO`, `ReservacionDAO`) para aislar las sentencias SQL y el manejo de `Statement` y `ResultSet` del resto de la aplicación.

RNF-03. Estándar de Conectividad JDBC:** La comunicación con la base de datos debe implementarse exclusivamente mediante la API estándar de Java Database Connectivity (JDBC), garantizando portabilidad entre distintos motores relacionales con mínimos cambios de configuración.

RNF-04. Seguridad y Externalización de Credenciales:** Las credenciales de acceso a la base de datos (usuario y contraseña) no deben estar escritas en código duro (*hardcoded*) dentro de las clases Java, sino almacenadas en un archivo de propiedades (`db.properties`) para facilitar su mantenimiento y protección.

RNF-05. Integridad de Recursos de Base de Datos:** El sistema debe garantizar el cierre adecuado de los objetos de conexión, sentencias (`Statement`) y conjuntos de resultados (`ResultSet`) tras finalizar cada consulta para evitar fugas de memoria o saturación de conexiones activas.

RNF-06. Mantenibilidad y Documentación Técnica:** El proyecto debe contar con documentación clara en el repositorio que incluya el Diagrama de Clases y el Diagrama de Entidad-Relación actualizados, reflejando fielmente la estructura de las entidades y sus relaciones.

RNF-07. Compatibilidad de Entorno y Gestión de Dependencias:** El proyecto debe estar estructurado bajo el estándar de Apache Maven (`pom.xml`) para gestionar automáticamente las dependencias del controlador JDBC y permitir su compilación y ejecución limpia en Visual Studio Code.

RNF-08. Tipos de Datos Estandarizados en Base de Datos:** El diseño relacional de las tablas debe utilizar tipos de datos primitivos y estándar (`INT`, `FLOAT`, `VARCHAR`) que aseguren una conversión directa y sin pérdida de precisión entre los registros SQL y los atributos de los objetos en Java.

# Diagrama-Clases 
<img width="2100" height="1533" alt="diagrama_clases" src="https://github.com/user-attachments/assets/1f32c12d-609d-4577-bd44-d1485ac2a9c0" />

# Diagrama Relacion-Entidad
<img width="861" height="426" alt="diagrama_er" src="https://github.com/user-attachments/assets/4f95d2fa-23cf-46ae-90d1-90aa95e8c3bc" />


