package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;
import java.time.format.DateTimeFormatter;

// Implementación concreta del Observer: por ahora simula la notificación
// imprimiendo por consola. El día de mañana esta clase se puede reemplazar
// (o convivir con otras) por un envío real de mail/WhatsApp (epic Notificaciones).

public class NotificadorConsola implements Observador {

    @Override
    public void notificarPrestamo(Prestamo prestamo) {
    System.out.println("[Notificación] " + prestamo.getSocio().getNombre()
            + " retiró el libro \"" + prestamo.getLibro().getTitulo() + "\" el "
            + prestamo.getFechaRetiro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    //SCRUM-20: avisa al socio que reservó el libro cuando este vuelve a estar disponible para retirar.
    @Override
    public void notificarReserva(Reserva reserva) {
        System.out.println(
            "[Notificación] Hola "
            + reserva.getSocio().getNombre()
            + ", el libro \""
            + reserva.getLibro().getTitulo()
            + "\" que reservaste ya está disponible para retirar.");
    }
}