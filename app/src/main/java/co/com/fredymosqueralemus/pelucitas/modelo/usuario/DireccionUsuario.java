package co.com.fredymosqueralemus.pelucitas.modelo.usuario;

import java.io.Serializable;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;

/**
 * Esta clase representa el modelo de las direcciones ingresadas por el usuario
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class DireccionUsuario extends Direccion implements Serializable {
    private String keyUidUsuario;
    private String fechaInsercion;
    private String fechaModificacion;

    public String getKeyUidUsuario() {
        return keyUidUsuario;
    }

    public void setKeyUidUsuario(String keyUidUsuario) {
        this.keyUidUsuario = keyUidUsuario;
    }

    @Override
    public String getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(String fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    @Override
    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
