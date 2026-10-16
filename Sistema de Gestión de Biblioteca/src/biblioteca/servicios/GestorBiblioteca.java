package biblioteca.servicios;

import biblioteca.estrategias.EstrategiaValidacionLibro;
import biblioteca.estrategias.ValidacionEstandar;
import biblioteca.modelos.Libro;
import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;
import biblioteca.modelos.Socio;
import biblioteca.observadores.Observador;
import biblioteca.observadores.NotificadorMail;
import biblioteca.observadores.NotificadorSMS;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// Patrón Singleton: solo puede existir una instancia de GestorBiblioteca en
// toda la aplicación, ya que representa el catálogo y los préstamos "centrales"
// del sistema. El constructor es privado y se accede siempre vía getInstancia().

public class GestorBiblioteca {
    // Se avisa cuando faltan 3 días o menos para devolver el libro.
    public static final int DIAS_ANTICIPACION_AVISO = 3;

    private static GestorBiblioteca instancia;

    private final List<Libro> libros;
    private final List<Socio> socios;
    private final List<Prestamo> prestamos;
    private final List<Reserva> reservas; //Nueva lista del SCRUM 20, almacena las reservas registradas.
    private final List<Observador> observadores; // quienes escuchan nuevos préstamos (Observer)
    private final Set<Prestamo> prestamosAvisados; // evita repetir el aviso en la misma ejecución
    private EstrategiaValidacionLibro estrategiaValidacion; // validación actual (Strategy)

    // Constructor privado: nadie fuera de la clase puede hacer "new GestorBiblioteca()"
    private GestorBiblioteca() {
        this.libros = new ArrayList<>();
        this.socios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.prestamosAvisados = new HashSet<>();
        this.estrategiaValidacion = new ValidacionEstandar(); // estrategia por defecto
    }

    //Único punto de acceso a la instancia (se crea la primera vez que se pide)
    public static GestorBiblioteca getInstancia() {
        if (instancia == null) {
            instancia = new GestorBiblioteca();
        }
        return instancia;
    }

    //Permite cambiar la estrategia de validación en tiempo de ejecución (Strategy)
    public void setEstrategiaValidacion(EstrategiaValidacionLibro estrategia) {
        this.estrategiaValidacion = estrategia;
    }

    //Registra un observador para que pueda recibir notificaciones.
    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    //SCRUM-13: Como Bibliotecario, quiero cargar un libro nuevo ---
    public boolean cargarLibro(Libro libro) {
        StringBuilder mensajeError = new StringBuilder();

        if (!estrategiaValidacion.esValido(libro, mensajeError)) {
            System.out.println("[Error] No se pudo cargar el libro: " + mensajeError);
            return false;
        }

        libros.add(libro);
        System.out.println("[OK] Libro cargado al catálogo: " + libro);
        return true;
    }

    //SCRUM-9: Como Bibliotecario, quiero registrar el préstamo de un libro a un socio ---
    public boolean registrarPrestamo(Socio socio, Libro libro) {
        if (!libro.isDisponible()) {
            System.out.println("[Error] El libro \"" + libro.getTitulo() + "\" no está disponible.");
            return false;
        }
        libro.setDisponible(false);
        Prestamo prestamo = new Prestamo(socio, libro);
        prestamos.add(prestamo);

        // Notificamos a cada canal de forma independiente.
        for (Observador observador : observadores) {
            try {
                observador.actualizar(prestamo);
            } catch (Exception e) {
                System.err.println(
                    "[Error] Falló el canal "
                    + observador.getClass().getSimpleName()
                    + " al notificar el préstamo: "
                    + e.getMessage()
                );

            }
        }
        return true;
    }

    public boolean devolverLibro(Libro libro) {
    Prestamo prestamoActivo = null;

    // Buscamos el préstamo activo más reciente de este libro.
    for (int i = prestamos.size() - 1; i >= 0; i--) {
        Prestamo prestamo = prestamos.get(i);

        if (prestamo.getLibro().getId() == libro.getId()
                && prestamo.getFechaDevolucion() == null) {
            prestamoActivo = prestamo;
            break;
        }
    }

    // Si no hay un préstamo activo, no podemos registrar la devolución.
    if (prestamoActivo == null) {
        System.out.println(
                "[Error] No existe un préstamo activo para este libro.");
        return false;
    }

    // Registramos la devolución y liberamos el libro.
    prestamoActivo.registrarDevolucion();
    libro.setDisponible(true);

    System.out.println(
            "[OK] Se devolvió el libro \"" + libro.getTitulo() + "\".");

    // Buscamos la primera reserva pendiente que todavía no fue notificada.
    for (Reserva reserva : reservas) {
        if (reserva.getLibro().getId() == libro.getId()
                && !reserva.isAtendida()
                && !reserva.isNotificada()) {

            // Intentamos notificar por todos los canales registrados.
            for (Observador observador : observadores) {
                try {
                    observador.notificarReserva(reserva);
                } catch (Exception e) {
                    System.err.println(
                            "[Error] Falló el canal "
                            + observador.getClass().getSimpleName()
                            + " al notificar la reserva: "
                            + e.getMessage());
                }
            }

            // Marcamos la reserva como notificada después de intentar
            // enviar el aviso por todos los canales.
            reserva.marcarNotificada();

            // Solo notificamos a la primera reserva pendiente.
            break;
        }
    }

    return true;
}

    //SCRUM 20: registra una reserva para un libro prestado.
    public boolean registrarReserva(Socio socio, Libro libro) {
        //Solo pueden reservar los socios registrados.
        if (!socios.contains(socio)) {
            System.out.println("[Error] El socio no está registrado.");
            return false;
        }
        //Solo se pueden reservar libros del catálogo.
        if (!libros.contains(libro)) {
            System.out.println("[Error] El libro no está en el catálogo.");
            return false;
        }
        //Si el libro está disponible, no necesita una reserva.
        if (libro.isDisponible()) {
            System.out.println("[Información] El libro \"" + libro.getTitulo() + "\" ya está disponible; no necesita reserva.");
            return false;
        }
        // Evita que el mismo socio registre varias reservas pendientes para el mismo libro.
        for (Reserva reserva : reservas) {
            if (reserva.getSocio().getId() == socio.getId() && reserva.getLibro().getId() == libro.getId() && !reserva.isAtendida()) {
                System.out.println("[Error] El socio ya tiene una reserva pendiente "+ "para este libro.");
                return false;
            }
        }
        //Se crea y almacena la reserva.
        Reserva reserva = new Reserva(socio, libro);
        reservas.add(reserva);
        System.out.println("[OK] " + socio.getNombre()+ " reservó el libro \"" + libro.getTitulo() + "\".");
        return true;
    }

    //Busca una reserva que ya fue notificada, pero cuyo libro todavía no fue retirado por el socio.
    private Reserva buscarReservaParaRetirar(Libro libro) {
        for (Reserva reserva : reservas) {
            if (reserva.getLibro().getId() == libro.getId() && reserva.isNotificada() && !reserva.isAtendida()) {
                return reserva;
            }
        }
        return null;
    }

    public void registrarSocio(Socio socio) {
        socios.add(socio);
    }

    public List<Libro> getLibros(){
        return libros; 
    }

    public List<Socio> getSocios(){ 
        return socios; 
    }

    public List<Prestamo> getPrestamos(){
        return prestamos; 
    }
    //Permite consultar las reservas registradas.
    public List<Reserva> getReservas(){
        return reservas;
    }

    // US: Como Bibliotecario, quiero ver un listado de préstamos vencidos.
    // Solo devuelve préstamos que siguen activos y superaron la fecha límite.
    // Devuelve los préstamos vencidos a la fecha actual.
    public List<Prestamo> listarPrestamosVencidos() {
        return listarPrestamosVencidos(LocalDateTime.now());
    }

    // Devuelve los préstamos vencidos a una fecha específica.
    public List<Prestamo> listarPrestamosVencidos(LocalDateTime fechaConsulta) {
        List<Prestamo> vencidos = new ArrayList<>();
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaVencido(fechaConsulta)) {
                vencidos.add(prestamo);
            }
        }
        return vencidos;
    }

    // Sobrecarga para consultar los vencimientos en una fecha determinada.
    public int avisarPrestamosVencidos() {
        return avisarPrestamosVencidos(LocalDateTime.now());
    }

    public int avisarPrestamosVencidos(LocalDateTime fechaConsulta) {
            Objects.requireNonNull(fechaConsulta, "La fecha de consulta es obligatoria");
            if (observadores.isEmpty()) {
                return 0;
            }
            int avisos = 0;

        for (Prestamo prestamo : prestamos){
            if (prestamo.estaVencido(fechaConsulta)){

                // Intentamos enviar el aviso por todos los canales.
            for (Observador observador : observadores) {
                try {
                    observador.notificarPrestamoVencido(prestamo);
                } catch (Exception e) {
                        System.err.println(
                                "[Error] Falló el canal "
                                + observador.getClass().getSimpleName()
                                + " al notificar el préstamo vencido: "
                                + e.getMessage());
                    }
            }
            avisos++;
            }
        }
        return avisos;
    }

    // Historia de usuario: avisar al socio cuando se acerca el vencimiento.
    // Debe llamarse periódicamente (o al iniciar el sistema): el proyecto
    // es una aplicación de consola y no tiene un servicio en segundo plano.
    public int avisarPrestamosProximosAVencer() {
        return avisarPrestamosProximosAVencer(LocalDateTime.now());
    }

    // Sobrecarga con fecha de referencia, útil para pruebas y demostraciones.
    // Devuelve cuántos préstamos generaron un aviso en esta revisión.
    public int avisarPrestamosProximosAVencer(LocalDateTime fechaConsulta) {
        Objects.requireNonNull(fechaConsulta, "La fecha de consulta es obligatoria");
        if (observadores.isEmpty()) {
            return 0;
        }
        int avisos = 0;

        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaProximoAVencer(
                    fechaConsulta, DIAS_ANTICIPACION_AVISO)
                    && !prestamosAvisados.contains(prestamo)) {

                // Cada canal se procesa de forma independiente.
                for (Observador observador : observadores) {
                    try {
                        observador.notificarProximoVencimiento(prestamo);
                    } catch (Exception e) {
                        System.err.println(
                            "[Error] Falló el canal "
                            + observador.getClass().getSimpleName()
                            + " al notificar el próximo vencimiento: "
                            + e.getMessage());
                        }
                    }
                    // Evitamos repetir el aviso para este préstamo.
                    prestamosAvisados.add(prestamo);
                    avisos++;
                }
        }
        return avisos;
    }

    // NUEVA FUNCIONALIDAD (US): Buscar libros por título o autor
    // Permite al socio buscar coincidencias y verificar su disponibilidad.
    public List<Libro> buscarLibros(String criterio) {
        List<Libro> resultados = new ArrayList<>();
        
        // Validamos que el criterio de búsqueda no esté vacío
        if (criterio == null || criterio.isBlank()) {
            return resultados;
        }

        // Pasamos el criterio a minúsculas para una búsqueda flexible (case-insensitive)
        String filtro = criterio.toLowerCase().trim();

        for (Libro libro : libros) {
            // Comprobamos si el título o el autor contienen el texto buscado
            boolean coincideTitulo = libro.getTitulo().toLowerCase().contains(filtro);
            boolean coincideAutor = libro.getAutor().toLowerCase().contains(filtro);

            if (coincideTitulo || coincideAutor) {
                resultados.add(libro);
            }
        }

        return resultados;
    }
}