package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;

// Patrón Observer: cualquier clase que quiera enterarse de un nuevo préstamo
// implementa esta interfaz y se registra en el sujeto (GestorBiblioteca).

public interface Observador {
    void actualizar(Prestamo prestamo);

    //Nuevo método para el SCRUM 20.
    //Permite notificar que un libro reservado está disponible.
    //Se declara default para no obligar a todas las clases que implementan Observador a definir este método.
    //Por defecto, el observador no hace nada con las reservas.
    default void notificarReserva(Reserva reserva) {
    }
}