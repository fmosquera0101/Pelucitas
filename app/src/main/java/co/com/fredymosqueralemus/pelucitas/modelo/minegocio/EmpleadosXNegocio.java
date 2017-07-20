package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;

/**
 * Created by fredymosqueralemus on 20/07/17.
 */

public class EmpleadosXNegocio implements Serializable{
    private String uidUsario;
    private String cedulaUsuario;
    private String fechaInsercion;
    private String fechaModificacion;

    public EmpleadosXNegocio(){

    }

    public String getUidUsario() {
        return uidUsario;
    }

    public void setUidUsario(String uidUsario) {
        this.uidUsario = uidUsario;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
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
