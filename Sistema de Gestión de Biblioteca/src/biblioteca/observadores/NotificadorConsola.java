package biblioteca.observadores;
import biblioteca.modelos.Prestamo;

// Implementación concreta del Observer: por ahora simula la notificación
// imprimiendo por consola. El día de mañana esta clase se puede reemplazar
// (o convivir con otras) por un envío real de mail/WhatsApp (epic Notificaciones).

public class NotificadorConsola implements Observador {

    @Override
    public void actualizar(Prestamo prestamo) {
        System.out.println("[Notificación] " + prestamo.getSocio().getNombre()
                + " retiró el libro \"" + prestamo.getLibro().getTitulo() + "\" el "
                + prestamo.getFechaRetiro());
    }
}