package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;

//Simula el envío de avisos por SMS.
public class NotificadorSMS implements Observador {

    //Simula el SMS enviado cuando se registra un préstamo.
    @Override
    public void actualizar(Prestamo prestamo) {
        System.out.println("[SMS a " + prestamo.getSocio().getTelefono()
                + "] Retiraste \"" + prestamo.getLibro().getTitulo()
                + "\". Vence el " + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    //Simula el SMS que informa que una reserva está disponible.
    @Override
    public void notificarReserva(Reserva reserva) {
        System.out.println("[SMS a " + reserva.getSocio().getTelefono()
                + "] El libro \"" + reserva.getLibro().getTitulo()
                + "\" que reservaste ya está disponible para retirar.");
    }

    //Simula el SMS de aviso de vencimiento próximo.
    @Override
    public void notificarProximoVencimiento(Prestamo prestamo) {
        System.out.println("[SMS a " + prestamo.getSocio().getTelefono()
                + "] Recordá que \"" + prestamo.getLibro().getTitulo()
                + "\" vence el " + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    //Simula el SMS de aviso de préstamo vencido.
    @Override
    public void notificarPrestamoVencido(Prestamo prestamo) {
        System.out.println("[SMS a " + prestamo.getSocio().getTelefono()
                + "] El préstamo de \"" + prestamo.getLibro().getTitulo()
                + "\" está vencido.");
    }
}    