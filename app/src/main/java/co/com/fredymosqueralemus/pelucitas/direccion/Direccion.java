package co.com.fredymosqueralemus.pelucitas.direccion;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */

public class Direccion implements Serializable {
    private String pais;
    private String departamento;
    private String ciudad;
    private String carreraCalle;
    private String numero1;
    private String numero2;
    private String barrio;
    private String datosAdicionales;

    private String fechaInsercion;
    private String fechaModificacion;

    private String keyUidUsuario;
    private String nitIdentificacionNegocio;

    public void Direccion(){

    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCarreraCalle() {
        return carreraCalle;
    }

    public void setCarreraCalle(String carreraCalle) {
        this.carreraCalle = carreraCalle;
    }

    public String getNumero1() {
        return numero1;
    }

    public void setNumero1(String numero1) {
        this.numero1 = numero1;
    }

    public String getNumero2() {
        return numero2;
    }

    public void setNumero2(String numero2) {
        this.numero2 = numero2;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDatosAdicionales() {
        return datosAdicionales;
    }

    public void setDatosAdicionales(String datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
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

    public String getKeyUidUsuario() {
        return keyUidUsuario;
    }

    public void setKeyUidUsuario(String keyUidUsuario) {
        this.keyUidUsuario = keyUidUsuario;
    }

    public String getNitIdentificacionNegocio() {
        return nitIdentificacionNegocio;
    }

    public void setNitIdentificacionNegocio(String nitIdentificacionNegocio) {
        this.nitIdentificacionNegocio = nitIdentificacionNegocio;
    }
}
