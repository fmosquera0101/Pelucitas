package co.com.fredymosqueralemus.pelucitas.imagenes;

import java.io.Serializable;

/**
 * Created by fredymosqueralemus on 3/04/17.
 */

public class ImagenModelo implements Serializable {

    private String nombreImagen;
    private String fechaCreacion;
    private String fechaUltimaModificacion;

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }
}
