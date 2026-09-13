package biblioteca.estrategias;
import biblioteca.modelos.Libro;

// Patrón Strategy: distintas formas de validar un libro antes de cargarlo al catálogo (SCRUM-13).
// Permite intercambiar la validación sin modificar GestorBiblioteca.

public interface EstrategiaValidacionLibro {
    boolean esValido(Libro libro, StringBuilder mensajeError);
}