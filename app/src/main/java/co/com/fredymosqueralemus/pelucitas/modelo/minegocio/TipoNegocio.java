package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;

/**
 * Esta clase representa los tipos de negocios (peluqueria, barberia, salon de belleza)
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class TipoNegocio implements Serializable{
    private String tipoNegocio;
    private String nitNegocio;
    private int idImagen;
    private String fechaInsercion;
    private String fechaModificacion;


    public TipoNegocio(){}

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public String getNitNegocio() {
        return nitNegocio;
    }

    public void setNitNegocio(String nitNegocio) {
        this.nitNegocio = nitNegocio;
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
}
