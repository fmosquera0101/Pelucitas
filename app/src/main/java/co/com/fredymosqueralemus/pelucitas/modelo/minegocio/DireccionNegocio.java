package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class DireccionNegocio extends Direccion implements Serializable {

    private String nitIdentificacionNegocio;
    private String fechaInsercion;
    private String fechaModificacion;

    public String getNitIdentificacionNegocio() {
        return nitIdentificacionNegocio;
    }

    public void setNitIdentificacionNegocio(String nitIdentificacionNegocio) {
        this.nitIdentificacionNegocio = nitIdentificacionNegocio;
    }

    @Override
    public String getFechaInsercion() {
        return fechaInsercion;
    }

    @Override
    public void setFechaInsercion(String fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    @Override
    public String getFechaModificacion() {
        return fechaModificacion;
    }

    @Override
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
