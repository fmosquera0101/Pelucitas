package co.com.fredymosqueralemus.pelucitas.modelo.agenda;

import java.io.Serializable;

/**
 * Created by fredymosqueralemus on 14/09/17.
 */

public class TurnosXCliente implements Serializable{
    private String nombreNegocio;
    private String nombreEmpleado;
    private String uidEmpleado;
    private String horaTurno;
    private String direccionNegocio;
    private String telefonoNegocio;

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getUidEmpleado() {
        return uidEmpleado;
    }

    public void setUidEmpleado(String uidEmpleado) {
        this.uidEmpleado = uidEmpleado;
    }

    public String getHoraTurno() {
        return horaTurno;
    }

    public void setHoraTurno(String horaTurno) {
        this.horaTurno = horaTurno;
    }

    public String getDireccionNegocio() {
        return direccionNegocio;
    }

    public void setDireccionNegocio(String direccionNegocio) {
        this.direccionNegocio = direccionNegocio;
    }

    public String getTelefonoNegocio() {
        return telefonoNegocio;
    }

    public void setTelefonoNegocio(String telefonoNegocio) {
        this.telefonoNegocio = telefonoNegocio;
    }
}
