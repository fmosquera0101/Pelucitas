package co.com.fredymosqueralemus.pelucitas.modelo.usuario;

import java.io.Serializable;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class DireccionUsuario extends Direccion implements Serializable {
    private String keyUidUsuario;
    private Date fechaInsercion;
    private Date fechaModificacion;

    public String getKeyUidUsuario() {
        return keyUidUsuario;
    }

    public void setKeyUidUsuario(String keyUidUsuario) {
        this.keyUidUsuario = keyUidUsuario;
    }

    @Override
    public Date getFechaInsercion() {
        return fechaInsercion;
    }

    @Override
    public void setFechaInsercion(Date fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    @Override
    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    @Override
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
