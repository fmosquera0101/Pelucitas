package co.com.fredymosqueralemus.pelucitas.modelo.settings;

import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;

/**
 * Created by fredymosqueralemus on 6/07/17.
 */

public class TiposDeNegocio {
    private String nombreTipoNegocio;
    private int idImagen;
    private String fechaInsercion;
    private String fechaModificacion;
    private ImagenModelo imagenModelo;

    public String getNombreTipoNegocio() {
        return nombreTipoNegocio;
    }

    public void setNombreTipoNegocio(String nombreTipoNegocio) {
        this.nombreTipoNegocio = nombreTipoNegocio;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(String fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ImagenModelo getImagenModelo() {
        return imagenModelo;
    }

    public void setImagenModelo(ImagenModelo imagenModelo) {
        this.imagenModelo = imagenModelo;
    }
}
