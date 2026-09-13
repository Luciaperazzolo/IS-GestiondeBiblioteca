package biblioteca.modelos;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String fechaEdicion;
    private String categoria;
    private boolean disponible;

    public Libro(int id, String titulo, String autor, String fechaEdicion, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaEdicion = fechaEdicion;
        this.categoria = categoria;
        this.disponible = true; // al cargarse, siempre arranca disponible
    }

    public int getId(){ 
        return id; }

    public String getTitulo(){ 
        return titulo; }

    public String getAutor(){ 
        return autor; }
    public String getFechaEdicion(){ 
        return fechaEdicion; }

    public String getCategoria(){ 
        return categoria; }

    public boolean isDisponible(){ 
        return disponible; }

    public void setDisponible(boolean disponible){ 
        this.disponible = disponible; }

    @Override
    public String toString() {
        return "Libro{" + titulo + " - " + autor + " (" + categoria + ")}";
    }
}