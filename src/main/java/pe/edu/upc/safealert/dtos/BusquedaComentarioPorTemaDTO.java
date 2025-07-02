package pe.edu.upc.safealert.dtos;

import java.time.LocalDate;

public class BusquedaComentarioPorTemaDTO {
    private String username;
    private String contenido;
    private LocalDate fechaComentario;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(LocalDate fechaComentario) {
        this.fechaComentario = fechaComentario;
    }
}