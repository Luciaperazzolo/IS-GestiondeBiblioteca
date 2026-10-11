package biblioteca;

import biblioteca.modelos.Libro;
import biblioteca.modelos.Socio;
import biblioteca.observadores.NotificadorConsola;
import biblioteca.servicios.GestorBiblioteca;

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
}
