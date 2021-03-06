package co.com.fredymosqueralemus.pelucitas.modelo.usuario;

import java.io.Serializable;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;

/**
 * Esta clase representa el modelo de usuario de la aplicacion
 * Created by fredymosqueralemus on 3/02/17.
 */

public class Usuario implements Serializable {
    private String uid;
    private String cedulaIdentificacion;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String perfil;
    private Direccion direccion;
    private String fechaNacimiento;
    private String email;

    private String fechaInsercion;
    private String fechaModificacion;

    private ImagenModelo imagenModelo;

    private PerfilesXUsuario perfilEmpleado;
    private PerfilesXUsuario perfilAdministrador;

    private String nitNegocioEmpleador;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ImagenModelo getImagenModelo() {
        return imagenModelo;
    }

    public void setImagenModelo(ImagenModelo imagenModelo) {
        this.imagenModelo = imagenModelo;
    }

    public PerfilesXUsuario getPerfilEmpleado() {
        return perfilEmpleado;
    }

    public void setPerfilEmpleado(PerfilesXUsuario perfilEmpleado) {
        this.perfilEmpleado = perfilEmpleado;
    }

    public PerfilesXUsuario getPerfilAdministrador() {
        return perfilAdministrador;
    }

    public void setPerfilAdministrador(PerfilesXUsuario perfilAdministrador) {
        this.perfilAdministrador = perfilAdministrador;
    }

    public String getNitNegocioEmpleador() {
        return nitNegocioEmpleador;
    }

    public void setNitNegocioEmpleador(String nitNegocioEmpleador) {
        this.nitNegocioEmpleador = nitNegocioEmpleador;
    }
}
