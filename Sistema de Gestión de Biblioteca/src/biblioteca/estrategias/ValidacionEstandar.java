package biblioteca.estrategias;
import biblioteca.modelos.Libro;

// Estrategia "estándar": solo valida que el libro tenga título y autor cargados.
public class ValidacionEstandar implements EstrategiaValidacionLibro {

    @Override
    public boolean esValido(Libro libro, StringBuilder mensajeError) {
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            mensajeError.append("El libro debe tener un título. ");
            return false;
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            mensajeError.append("El libro debe tener un autor. ");
            return false;
        }
        return true;
    }
}