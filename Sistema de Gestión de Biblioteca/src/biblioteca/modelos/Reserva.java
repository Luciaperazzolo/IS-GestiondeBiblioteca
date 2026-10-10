package biblioteca.modelos;

import java.time.LocalDateTime;

//Representa la reserva de un libro realizada por un socio.
//Se utiliza en el SCRUM 20 para saber a quién notificar cuando el libro reservado vuelve a estar disponible.
public class Reserva {
    private Socio socio; //Representa a quien reservo el libro.
    private Libro libro; //Representa a que libro esta esperando.
    private LocalDateTime fechaReserva; //Cuando se hizo la reserva.
    private boolean notificada; //Indica si ya se avisó al socio que el libro está disponible.
    private boolean atendida; //Indica si la reserva ya fue completada.

    //Constructor
    public Reserva(Socio socio, Libro libro) {
        this.socio = socio;
        this.libro = libro;
        this.fechaReserva = LocalDateTime.now();
        //Al crear una reserva, todavía no se notificó al socio ni se completó el retiro del libro.
        this.notificada = false; 
        this.atendida = false;
    }

    public Socio getSocio() {
        return socio;
    }
    public Libro getLibro() {
        return libro;
    }
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }
    public boolean isNotificada() {
        return notificada;
    }
    public boolean isAtendida() {
        return atendida;
    }
    //Se llama cuando se informa al socio que puede retirar el libro.
    public void marcarNotificada() {
        this.notificada = true;
    }
    //Se llama cuando el socio retira el libro reservado.
    public void marcarAtendida() {
        this.atendida = true;
    }
}
