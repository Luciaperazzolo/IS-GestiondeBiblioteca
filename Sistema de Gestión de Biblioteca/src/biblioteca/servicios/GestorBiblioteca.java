package biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;

import biblioteca.estrategias.EstrategiaValidacionLibro;
import biblioteca.estrategias.ValidacionEstandar;
import biblioteca.modelos.Libro;
import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;
import biblioteca.modelos.Socio;
import biblioteca.observadores.Observador;

// Patrón Singleton: solo puede existir una instancia de GestorBiblioteca en
// toda la aplicación, ya que representa el catálogo y los préstamos "centrales"
// del sistema. El constructor es privado y se accede siempre vía getInstancia().

public class GestorBiblioteca {

    private static GestorBiblioteca instancia;

    private final List<Libro> libros;
    private final List<Socio> socios;
    private final List<Prestamo> prestamos;
    private final List<Reserva> reservas; //Nueva lista del SCRUM 20, almacena las reservas registradas.
    private final List<Observador> observadores; // quienes escuchan nuevos préstamos (Observer)
    private EstrategiaValidacionLibro estrategiaValidacion; // validación actual (Strategy)

    // Constructor privado: nadie fuera de la clase puede hacer "new GestorBiblioteca()"
    private GestorBiblioteca() {
        this.libros = new ArrayList<>();
        this.socios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.observadores = new ArrayList<>();
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

        // Notifica a todos los observadores registrados (Observer)
        for (Observador observador : observadores) {
            observador.actualizar(prestamo);
        }
        return true;
    }

    public boolean devolverLibro(Libro libro) {
        Prestamo prestamoActivo = null;
        for (int i = prestamos.size() - 1; i >= 0; i--) {
            Prestamo prestamo = prestamos.get(i);
            if (prestamo.getLibro().getId() == libro.getId() && prestamo.getFechaDevolucion() == null) {
                prestamoActivo = prestamo;
                break;
            }
        }
        if (prestamoActivo == null) {
            System.out.println("[Error] No existe un préstamo activo para este libro.");
            return false;
        }
        prestamoActivo.registrarDevolucion();
        libro.setDisponible(true);

        System.out.println("[OK] Se devolvió el libro \"" + libro.getTitulo() + "\".");

        for (Reserva reserva : reservas) {
            if (reserva.getLibro().getId() == libro.getId() && !reserva.isAtendida() && !reserva.isNotificada()) {
                reserva.marcarNotificada();
                for (Observador observador : observadores) {
                    observador.notificarReserva(reserva);
                }
                // Solo se avisa a la primera reserva pendiente.
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
}