package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;

/**
 * Created by fredymosqueralemus on 10/07/17.
 */

public class NegocioXAdministrador implements Serializable {
    private String nitNegocio;
    private String fechaInsercion;
    private String fechaModificacion;

    public NegocioXAdministrador(){

    }

    public String getNitNegocio() {
        return nitNegocio;
    }

    public void setNitNegocio(String nitNegocio) {
        this.nitNegocio = nitNegocio;
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
