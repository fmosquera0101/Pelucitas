package co.com.fredymosqueralemus.pelucitas.modelo.usuario;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;

/**
 * Esta clase representa el modelo de usuario de la aplicaion
 * Created by fredymosqueralemus on 3/02/17.
 */

public class Usuario {
    private String cedulaIdentificacion;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String perfil;
    private Direccion direccion;

    public String getCedulaIdentificacion() {
        return cedulaIdentificacion;
    }

    public void setCedulaIdentificacion(String cedulaIdentificacion) {
        this.cedulaIdentificacion = cedulaIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
