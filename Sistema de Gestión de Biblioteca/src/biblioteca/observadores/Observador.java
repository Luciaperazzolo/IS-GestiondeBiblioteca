package biblioteca.observadores;
import biblioteca.modelos.Prestamo;

// Patrón Observer: cualquier clase que quiera enterarse de un nuevo préstamo
// implementa esta interfaz y se registra en el sujeto (GestorBiblioteca).

public interface Observador {
    void actualizar(Prestamo prestamo);
}