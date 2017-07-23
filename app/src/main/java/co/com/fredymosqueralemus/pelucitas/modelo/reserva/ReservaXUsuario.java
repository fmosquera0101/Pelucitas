package co.com.fredymosqueralemus.pelucitas.modelo.reserva;

import java.io.Serializable;

/**
 * Created by fredymosqueralemus on 23/07/17.
 */

public class ReservaXUsuario implements Serializable {

    private String fechaAgenda;
    private String uidUsuarioReserva;
    private String fechaInsercion;
    private String fechaModificacion;
    private String keyUidHoraAgenda;

    public void ReservaXUsuario(){

    }

    public String getFechaAgenda() {
        return fechaAgenda;
    }

    public void setFechaAgenda(String fechaAgenda) {
        this.fechaAgenda = fechaAgenda;
    }

    public String getUidUsuarioReserva() {
        return uidUsuarioReserva;
    }

    public void setUidUsuarioReserva(String uidUsuarioReserva) {
        this.uidUsuarioReserva = uidUsuarioReserva;
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

    public String getKeyUidHoraAgenda() {
        return keyUidHoraAgenda;
    }

    public void setKeyUidHoraAgenda(String keyUidHoraAgenda) {
        this.keyUidHoraAgenda = keyUidHoraAgenda;
    }
}
