package biblioteca.estrategias;
import biblioteca.modelos.Libro;

// Estrategia "estricta": además de lo básico, exige categoría y fecha de edición.
// Al ser intercambiable con ValidacionEstandar (misma interfaz), GestorBiblioteca
// puede usar una u otra sin modificar su código (esto es lo que aporta Strategy).

public class ValidacionEstricta implements EstrategiaValidacionLibro {

    @Override
    public boolean esValido(Libro libro, StringBuilder mensajeError) {
        boolean valido = true;

        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            mensajeError.append("El libro debe tener un título. ");
            valido = false;
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            mensajeError.append("El libro debe tener un autor. ");
            valido = false;
        }
        if (libro.getCategoria() == null || libro.getCategoria().isBlank()) {
            mensajeError.append("El libro debe tener una categoría. ");
            valido = false;
        }
        if (libro.getFechaEdicion() == null || libro.getFechaEdicion().isBlank()) {
            mensajeError.append("El libro debe tener fecha de edición. ");
            valido = false;
        }
        return valido;
    }
}