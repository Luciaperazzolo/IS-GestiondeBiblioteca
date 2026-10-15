package biblioteca;

import biblioteca.modelos.Libro;
import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Socio;
import biblioteca.observadores.NotificadorConsola;
import biblioteca.servicios.GestorBiblioteca;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Obtenemos el gestor central de la biblioteca.
        GestorBiblioteca gestor = GestorBiblioteca.getInstancia();
        //Registramos el observador que muestra las notificaciones.
        gestor.agregarObservador(new NotificadorConsola());

        System.out.println("==================================");
        System.out.println(" SISTEMA DE GESTION DE BIBLIOTECA");
        System.out.println("==================================");

        // ==================================
        // REGISTRAR SOCIOS
        // ==================================

        System.out.println("\n--- Registro de socios ---");
        Socio juan = new Socio(1, "Juan Perez", "Calle Falsa 123", "1234567", "juan@mail.com");
        Socio lucia = new Socio(2, "Lucia Gomez", "Calle Central 456", "7654321", "lucia@mail.com");

        gestor.registrarSocio(juan);
        gestor.registrarSocio(lucia);

        System.out.println("Socio registrado: " + juan.getNombre());
        System.out.println("Socio registrado: " + lucia.getNombre());

        // ==================================
        // CARGAR LIBROS
        // ==================================

        System.out.println("\n--- Carga de libros ---");
        Libro libro1 = new Libro(1, "Clean Code", "Robert C. Martin", "2008", "Programacion");
        Libro libro2 = new Libro(2, "El Principito", "Antoine de Saint-Exupery", "1943", "Literatura");
        Libro libro3 = new Libro(3, "Cien anos de soledad", "Gabriel Garcia Marquez", "1967", "Novela");

        gestor.cargarLibro(libro1);
        gestor.cargarLibro(libro2);
        gestor.cargarLibro(libro3);

        // ==================================
        // REGISTRAR UN PRESTAMO
        // ==================================

        System.out.println("\n--- Prestamo de libros ---");
        System.out.println("Juan intenta retirar: " + libro1.getTitulo());
        gestor.registrarPrestamo(juan, libro1);

        // ==================================
        // RESERVAR UN LIBRO PRESTADO
        // ==================================

        System.out.println("\n--- Reserva de libros ---");

        System.out.println("Lucia intenta reservar: " + libro1.getTitulo());

        gestor.registrarReserva(lucia, libro1);

        // ==================================
        // DEVOLVER EL LIBRO Y NOTIFICAR
        // ==================================

        System.out.println("\n--- Devolucion de libros ---");
        System.out.println("Juan devuelve: " + libro1.getTitulo());
        gestor.devolverLibro(libro1);

        // ==================================
        // CONSULTAR DISPONIBILIDAD
        // ==================================

        System.out.println("\n--- Consulta de disponibilidad ---");
        System.out.println(libro1.getTitulo() + ": " + (libro1.isDisponible() ? "Disponible" : "No disponible"));
        System.out.println(libro2.getTitulo() + ": " + (libro2.isDisponible() ? "Disponible" : "No disponible"));

        // ==================================
        // RETIRAR EL LIBRO RESERVADO
        // ==================================

        System.out.println("\n--- Retiro de una reserva ---");
        System.out.println("Lucia intenta retirar: " + libro1.getTitulo());
        gestor.registrarPrestamo(lucia, libro1);

        // ==================================
        // BUSCAR LIBROS POR TÍTULO O AUTOR
        // ==================================

        System.out.println("\n--- Búsqueda de libros por título o autor ---");
        String terminoBusqueda = "Martin"; // Puede ser parte del título o del autor
        System.out.println("Buscando: \"" + terminoBusqueda + "\"");

        java.util.List<Libro> resultadosBusqueda = gestor.buscarLibros(terminoBusqueda);

        if (resultadosBusqueda.isEmpty()) {
            System.out.println("No se encontraron libros que coincidan con la búsqueda.");
        } else {
            for (Libro libroEncontrado : resultadosBusqueda) {
                // Verificamos el estado de disponibilidad actual
                String estadoDisponibilidad = libroEncontrado.isDisponible() ? "Disponible" : "No disponible";
                System.out.println("-> Encontrado: " + libroEncontrado.getTitulo() 
                    + " (Autor: " + libroEncontrado.getAutor() + ") [" + estadoDisponibilidad + "]");
            }
        }

        // ==================================
        // CONSULTAR PRÉSTAMOS VENCIDOS
        // ==================================

        System.out.println("\n--- Prestamos vencidos al dia de hoy ---");
        mostrarPrestamosVencidos(gestor.listarPrestamosVencidos());

        // Demostración: los préstamos se acaban de registrar, por eso aún no están vencidos.
        // Consultamos una fecha futura para poder ver el resultado sin alterar los datos reales.
        System.out.println("\n--- Demostracion: consulta dentro de 15 dias ---");
        mostrarPrestamosVencidos(gestor.listarPrestamosVencidos(LocalDateTime.now().plusDays(15)));

        // ==================================
        // AVISOS DE PRÓXIMO VENCIMIENTO
        // ==================================

        System.out.println("\n--- Avisos de vencimiento al día de hoy ---");
        int avisosHoy = gestor.avisarPrestamosProximosAVencer();
        if (avisosHoy == 0) {
            System.out.println("No hay préstamos próximos a vencer para avisar.");
        }

        // La devolución se realiza 14 días después del retiro. Como Lucía
        // acaba de llevarse el libro, simulamos una revisión dentro de 12 días.
        // Quedarán aproximadamente 2 días para el vencimiento y aparecerá el aviso.
        System.out.println("\n--- Demostración: avisos dentro de 12 días ---");
        int avisosSimulados = gestor.avisarPrestamosProximosAVencer(LocalDateTime.now().plusDays(12));
        System.out.println("Avisos generados: " + avisosSimulados);

        // ==================================
        // MOSTRAR RESUMEN DEL SISTEMA
        // ==================================

        System.out.println("\n--- Resumen de la biblioteca ---");
        System.out.println("Cantidad de socios: " + gestor.getSocios().size());
        System.out.println("Cantidad de libros: " + gestor.getLibros().size());
        System.out.println("Cantidad de prestamos registrados: " + gestor.getPrestamos().size());
        System.out.println("Cantidad de reservas registradas: " + gestor.getReservas().size());

        System.out.println("\n==================================");
        System.out.println(" FIN DE LA EJECUCION");
        System.out.println("==================================");


    }

    // Muestra la información necesaria para hacer el seguimiento de las devoluciones pendientes.
    private static void mostrarPrestamosVencidos(List<Prestamo> vencidos) {
        if (vencidos.isEmpty()) {
            System.out.println("No hay prestamos vencidos.");
            return;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Prestamo prestamo : vencidos) {
            System.out.println("Libro: " + prestamo.getLibro().getTitulo());
            System.out.println("Socio: " + prestamo.getSocio().getNombre());
            System.out.println("Contacto: " + prestamo.getSocio().getCorreo()
                    + " | " + prestamo.getSocio().getTelefono());
            System.out.println("Fecha de retiro: " + prestamo.getFechaRetiro().format(formato));
            System.out.println("Fecha de vencimiento: " + prestamo.getFechaVencimiento().format(formato));
            System.out.println("----------------------------------");
        }
    }
}
