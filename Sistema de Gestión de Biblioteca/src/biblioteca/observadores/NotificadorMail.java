package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;

// Canal de aviso por mail (simulado por consola). Se suma como un canal más
// sin modificar GestorBiblioteca ni los otros canales (SCRUM-25).
public class NotificadorMail implements Observador {

    @Override
    public void notificarPrestamo(Prestamo prestamo) {
        System.out.println("[Mail a " + prestamo.getSocio().getCorreo() + "] Retiraste el libro \""
                + prestamo.getLibro().getTitulo() + "\". Vence el "
                + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    @Override
    public void notificarReserva(Reserva reserva) {
        System.out.println("[Mail a " + reserva.getSocio().getCorreo() + "] Hola "
                + reserva.getSocio().getNombre() + ", el libro \""
                + reserva.getLibro().getTitulo() + "\" que reservaste ya está disponible para retirar.");
    }
}