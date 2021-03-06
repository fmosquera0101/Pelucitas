package co.com.fredymosqueralemus.pelucitas.modelo.agenda;

import java.io.Serializable;

import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

/**
 * Representa el modelo de la agenda del empleado
 * Created by fredymosqueralemus on 28/06/17.
 */

public class AgendaXEmpleado implements Serializable {

    private String fechaAgenda;
    private String horaReserva;
    private String snReservado;
    private String uidUsuarioReserva;
    private String cedulaUsuarioReserva;
    private String uidEmpleado;
    private String fechaInsercion;
    private String fechaModificacion;
    private String reservadoPor;
    private boolean isPuedeVerimagenReservaAgenda;
    private Usuario usuario;


    public String getFechaAgenda() {
        return fechaAgenda;
    }

    public void setFechaAgenda(String fechaAgenda) {
        this.fechaAgenda = fechaAgenda;
    }

    public String getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }

    public String getSnReservado() {
        return snReservado;
    }

    public void setSnReservado(String snReservado) {
        this.snReservado = snReservado;
    }

    public String getUidUsuarioReserva() {
        return uidUsuarioReserva;
    }

    public void setUidUsuarioReserva(String uidUsuarioReserva) {
        this.uidUsuarioReserva = uidUsuarioReserva;
    }

    public String getCedulaUsuarioReserva() {
        return cedulaUsuarioReserva;
    }

    public void setCedulaUsuarioReserva(String cedulaUsuarioReserva) {
        this.cedulaUsuarioReserva = cedulaUsuarioReserva;
    }

    public String getUidEmpleado() {
        return uidEmpleado;
    }

    public void setUidEmpleado(String uidEmpleado) {
        this.uidEmpleado = uidEmpleado;
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

    public String getReservadoPor() {
        return reservadoPor;
    }

    public void setReservadoPor(String reservadoPor) {
        this.reservadoPor = reservadoPor;
    }

    public boolean isPuedeVerimagenReservaAgenda() {
        return isPuedeVerimagenReservaAgenda;
    }

    public void setPuedeVerimagenReservaAgenda(boolean puedeVerimagenReservaAgenda) {
        isPuedeVerimagenReservaAgenda = puedeVerimagenReservaAgenda;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
