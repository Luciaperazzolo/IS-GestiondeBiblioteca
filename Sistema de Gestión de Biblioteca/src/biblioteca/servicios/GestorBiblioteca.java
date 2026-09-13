package biblioteca.servicios;
import java.util.ArrayList;
import java.util.List;
import biblioteca.estrategias.EstrategiaValidacionLibro;
import biblioteca.estrategias.ValidacionEstandar;
import biblioteca.modelos.Libro;
import biblioteca.modelos.Prestamo;
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
    private final List<Observador> observadores; // quienes escuchan nuevos préstamos (Observer)
    private EstrategiaValidacionLibro estrategiaValidacion; // validación actual (Strategy)

    // Constructor privado: nadie fuera de la clase puede hacer "new GestorBiblioteca()"
    private GestorBiblioteca() {
        this.libros = new ArrayList<>();
        this.socios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.estrategiaValidacion = new ValidacionEstandar(); // estrategia por defecto
    }

    // Único punto de acceso a la instancia (se crea la primera vez que se pide)
    public static GestorBiblioteca getInstancia() {
        if (instancia == null) {
            instancia = new GestorBiblioteca();
        }
        return instancia;
    }

    // Permite cambiar la estrategia de validación en tiempo de ejecución (Strategy)
    public void setEstrategiaValidacion(EstrategiaValidacionLibro estrategia) {
        this.estrategiaValidacion = estrategia;
    }

    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    // --- SCRUM-13: Como Bibliotecario, quiero cargar un libro nuevo ---
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

    // --- SCRUM-9: Como Bibliotecario, quiero registrar el préstamo de un libro a un socio ---
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

    public void registrarSocio(Socio socio) {
        socios.add(socio);
    }

    public List<Libro> getLibros(){
        return libros; }

    public List<Socio> getSocios(){ 
        return socios; }

    public List<Prestamo> getPrestamos(){
         return prestamos; }
}