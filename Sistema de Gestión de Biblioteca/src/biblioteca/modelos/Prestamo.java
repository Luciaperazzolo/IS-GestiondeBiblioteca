package biblioteca.modelos;
import java.time.LocalDateTime;

public class Prestamo {
    private Socio socio;
    private Libro libro;
    private LocalDateTime fechaRetiro;
    private LocalDateTime fechaDevolucion; // null hasta que se devuelva (US futura)

    public Prestamo(Socio socio, Libro libro){
        this.socio = socio;
        this.libro = libro;
        this.fechaRetiro = LocalDateTime.now();
        this.fechaDevolucion = null;
    }

    public Socio getSocio(){ 
        return socio; }

    public Libro getLibro(){ 
        return libro; }

    public LocalDateTime getFechaRetiro(){ 
        return fechaRetiro; }
        
}