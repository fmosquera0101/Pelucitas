package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;

import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;

/**
 * Esta clase representa el negocio, entidad o local ( peluqueria, barberia, salon de belleza)
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */

public class MiNegocio implements Serializable {



    private String nitNegocio;
    private String nombreNegocio;
    private String telefonoNegocio;
    private TipoNegocio tipoNegocio;
    private Horario horarioNegocio;
    private Direccion direccion;
    private String fechaInsercion;
    private String fechaModificacion;
    private String uidAdministrador;
    private String keyChild;
    private ImagenModelo imagenModelo;

    private String descripcionNegocio;

    public MiNegocio(){

    }

    public String getNitNegocio() {
        return nitNegocio;
    }

    public void setNitNegocio(String nitNegocio) {
        this.nitNegocio = nitNegocio;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getTelefonoNegocio() {
        return telefonoNegocio;
    }

    public void setTelefonoNegocio(String telefonoNegocio) {
        this.telefonoNegocio = telefonoNegocio;
    }

    public TipoNegocio getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(TipoNegocio tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public Horario getHorarioNegocio() {
        return horarioNegocio;
    }

    public void setHorarioNegocio(Horario horarioNegocio) {
        this.horarioNegocio = horarioNegocio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
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

    public String getUidAdministrador() {
        return uidAdministrador;
    }

    public void setUidAdministrador(String uidAdministrador) {
        this.uidAdministrador = uidAdministrador;
    }

    public String getKeyChild() {
        return keyChild;
    }

    public void setKeyChild(String keyChild) {
        this.keyChild = keyChild;
    }

    public ImagenModelo getImagenModelo() {
        return imagenModelo;
    }

    public void setImagenModelo(ImagenModelo imagenModelo) {
        this.imagenModelo = imagenModelo;
    }

    public String getDescripcionNegocio() {
        return descripcionNegocio;
    }

    public void setDescripcionNegocio(String descripcionNegocio) {
        this.descripcionNegocio = descripcionNegocio;
    }
}
