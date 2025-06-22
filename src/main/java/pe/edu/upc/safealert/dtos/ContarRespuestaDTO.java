package pe.edu.upc.safealert.dtos;
public class ContarRespuestaDTO {
    private String username;
    private String titulo;
    private int contarrespuesta;
    private String contenido;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getContarrespuesta() {
        return contarrespuesta;
    }

    public void setContarrespuesta(int contarrespuesta) {
        this.contarrespuesta = contarrespuesta;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
