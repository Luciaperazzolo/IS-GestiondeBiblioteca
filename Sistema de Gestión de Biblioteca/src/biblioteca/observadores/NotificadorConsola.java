package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;
import java.time.format.DateTimeFormatter;

// Implementación concreta del Observer: por ahora simula la notificación
// imprimiendo por consola. El día de mañana esta clase se puede reemplazar
// (o convivir con otras) por un envío real de mail/WhatsApp (epic Notificaciones).

public class NotificadorConsola implements Observador {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    //Informa que se registró un préstamo.
    @Override
    public void actualizar(Prestamo prestamo) {
        System.out.println("[CONSOLA] " + prestamo.getSocio().getNombre()
                + " retiró el libro \"" + prestamo.getLibro().getTitulo()
                + "\" el " + prestamo.getFechaRetiro().format(FORMATO));
    }

    //Informa que el libro reservado está disponible.
    @Override
    public void notificarReserva(Reserva reserva) {
        System.out.println("[CONSOLA] Hola " + reserva.getSocio().getNombre()
                + ", el libro \"" + reserva.getLibro().getTitulo()
                + "\" que reservaste ya está disponible para retirar.");
    }

    //Informa que se aproxima la fecha de devolución.
    @Override
    public void notificarProximoVencimiento(Prestamo prestamo) {
        System.out.println("[CONSOLA] Hola " + prestamo.getSocio().getNombre()
                + ", el préstamo del libro \"" + prestamo.getLibro().getTitulo()
                + "\" vence el " + prestamo.getFechaVencimiento().format(FORMATO)
                + ". Por favor, devolvé el libro a tiempo.");
    }

    //Informa que el préstamo ya está vencido.
    @Override
    public void notificarPrestamoVencido(Prestamo prestamo) {
        System.out.println("[CONSOLA] AVISO: el préstamo del libro \""
                + prestamo.getLibro().getTitulo() + "\" está vencido desde el "
                + prestamo.getFechaVencimiento().format(FORMATO)
                + ". Socio: " + prestamo.getSocio().getNombre());
    }
}