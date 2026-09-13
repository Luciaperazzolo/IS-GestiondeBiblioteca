package biblioteca;

import biblioteca.modelos.Libro;
import biblioteca.modelos.Socio;
import biblioteca.observadores.NotificadorConsola;
import biblioteca.servicios.GestorBiblioteca;

public class Main {
    public static void main(String[] args) {
        GestorBiblioteca gestor = GestorBiblioteca.getInstancia();
        gestor.agregarObservador(new NotificadorConsola());

        // SCRUM-13: cargar un libro nuevo al catálogo
        Libro libro = new Libro(1, "Clean Code", "Robert C. Martin", "2008", "Programación");
        gestor.cargarLibro(libro);

        // SCRUM-9: registrar el préstamo de ese libro a un socio
        Socio socio = new Socio(1, "Juan Pérez", "Calle Falsa 123", "1234567", "juan@mail.com");
        gestor.registrarSocio(socio);
        gestor.registrarPrestamo(socio, libro);
    }
}