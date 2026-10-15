package biblioteca.modelos;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prestamo {
    // Plazo establecido para la devolución. Puede modificarse si cambia la regla de la biblioteca.
    public static final int DIAS_PRESTAMO = 14;

    private Socio socio;
    private Libro libro;
    private LocalDateTime fechaRetiro; // Fecha y hora en que se retiró el libro.
    private LocalDateTime fechaVencimiento; // Fecha límite para devolver el libro.
    private LocalDateTime fechaDevolucion; // Null hasta que se devuelva.

    public Prestamo(Socio socio, Libro libro) {
        this(socio, libro, LocalDateTime.now());
    }

    // También permite crear préstamos con fecha histórica (útil para registrar y probar vencimientos).
    public Prestamo(Socio socio, Libro libro, LocalDateTime fechaRetiro) {
        this.socio = socio;
        this.libro = libro;
        this.fechaRetiro = Objects.requireNonNull(fechaRetiro, "La fecha de retiro es obligatoria");
        this.fechaVencimiento = fechaRetiro.plusDays(DIAS_PRESTAMO);
        this.fechaDevolucion = null;
    }

    public Socio getSocio() {
        return socio;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDateTime getFechaRetiro() {
        return fechaRetiro;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    // Permite consultar si el préstamo ya fue devuelto.
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    // Un préstamo está vencido si todavía no fue devuelto y ya pasó su fecha límite.
    public boolean estaVencido() {
        return estaVencido(LocalDateTime.now());
    }

    // La fecha de consulta como parámetro permite hacer pruebas sin cambiar el reloj del sistema.
    public boolean estaVencido(LocalDateTime fechaConsulta) {
        return fechaDevolucion == null && fechaConsulta.isAfter(fechaVencimiento);
    }

    // Un préstamo está próximo a vencer si está activo y faltan como máximo
    // los días indicados, sin haber superado todavía la fecha límite.
    public boolean estaProximoAVencer(LocalDateTime fechaConsulta, int diasAnticipacion) {
        Objects.requireNonNull(fechaConsulta, "La fecha de consulta es obligatoria");
        if (diasAnticipacion < 0) {
            throw new IllegalArgumentException("Los días de anticipación no pueden ser negativos");
        }
        return fechaDevolucion == null
                && !fechaConsulta.isAfter(fechaVencimiento)
                && !fechaConsulta.plusDays(diasAnticipacion).isBefore(fechaVencimiento);
    }

    // Registra la fecha y hora de devolución una sola vez.
    public void registrarDevolucion() {
        if (fechaDevolucion == null) {
            fechaDevolucion = LocalDateTime.now();
        }
    }
}
