package co.com.fredymosqueralemus.pelucitas.modelo.usuario;

import java.io.Serializable;

/**
 * Created by Fredy Mosquera Lemus on 9/02/17.
 */

public class PerfilesXUsuario implements Serializable {
    private String keyUsuarioId;
    private String fechaInsercion;
    private String fechaModificacion;
    private String activo;

    public String getKeyUsuarioId() {
        return keyUsuarioId;
    }

    public void setKeyUsuarioId(String keyUsuarioId) {
        this.keyUsuarioId = keyUsuarioId;
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
    public String getActivo() {
        return activo;
    }
    public void setActivo(String activo) {
        this.activo = activo;
    }
}
