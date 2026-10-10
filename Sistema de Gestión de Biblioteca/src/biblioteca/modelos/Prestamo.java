package biblioteca.modelos;
import java.time.LocalDateTime;

public class Prestamo {
    private Socio socio;
    private Libro libro;
    private LocalDateTime fechaRetiro; //Fecha y hora en que se retiró el libro.
    private LocalDateTime fechaDevolucion; //Null hasta que se devuelva (US futura)

    public Prestamo(Socio socio, Libro libro){
        this.socio = socio;
        this.libro = libro;
        this.fechaRetiro = LocalDateTime.now(); 
        this.fechaDevolucion = null;
    }

    public Socio getSocio(){ 
        return socio; 
    }
    public Libro getLibro(){ 
        return libro; 
    }
    public LocalDateTime getFechaRetiro(){ 
        return fechaRetiro; 
    }

    //Permite consultar si el préstamo ya fue devuelto.
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    //Registra la fecha y hora de devolución.
    //Evita sobrescribirla si ya se había registrado.
    public void registrarDevolucion() {
        if (fechaDevolucion == null) {
            fechaDevolucion = LocalDateTime.now();
        }
    }
        
}