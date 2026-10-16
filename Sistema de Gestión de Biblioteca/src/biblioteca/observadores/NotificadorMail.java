package biblioteca.observadores;

import biblioteca.modelos.Prestamo;
import biblioteca.modelos.Reserva;

//Canal de aviso por mail (simulado por consola). Se suma como un canal más
//Sin modificar GestorBiblioteca ni los otros canales (SCRUM-25).
public class NotificadorMail implements Observador {

    // Permite simular un fallo para probar el SCRUM-26.
    private boolean simularFallo = false;

    // Activa o desactiva el fallo simulado.
    public void setSimularFallo(boolean simularFallo) {
        this.simularFallo = simularFallo;
    }

    //Simula el correo enviado cuando se registra un préstamo.
    @Override
    public void actualizar(Prestamo prestamo) {
        verificarFallo();
        System.out.println("[MAIL a " + prestamo.getSocio().getCorreo()
                + "] Retiraste el libro \"" + prestamo.getLibro().getTitulo()
                + "\". Vence el " + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    //Simula el correo que informa que una reserva está disponible.
    @Override
    public void notificarReserva(Reserva reserva) {
        verificarFallo();
        System.out.println("[MAIL a " + reserva.getSocio().getCorreo()
                + "] Hola " + reserva.getSocio().getNombre()
                + ", el libro \"" + reserva.getLibro().getTitulo()
                + "\" que reservaste ya está disponible para retirar.");
    }

    //Simula el correo de aviso de vencimiento próximo.
    @Override
    public void notificarProximoVencimiento(Prestamo prestamo) {
        verificarFallo();
        System.out.println("[MAIL a " + prestamo.getSocio().getCorreo()
                + "] El préstamo de \"" + prestamo.getLibro().getTitulo()
                + "\" vence el " + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    //Simula el correo de aviso de préstamo vencido.
    @Override
    public void notificarPrestamoVencido(Prestamo prestamo) {
        verificarFallo();
        System.out.println("[MAIL a " + prestamo.getSocio().getCorreo()
                + "] El préstamo de \"" + prestamo.getLibro().getTitulo()
                + "\" está vencido desde el "
                + prestamo.getFechaVencimiento().toLocalDate() + ".");
    }

    // Lanza un error intencional cuando la prueba lo solicita.
    private void verificarFallo() {
        if (simularFallo) {
            throw new RuntimeException("Fallo simulado del servicio de mail");
        }
    }
}