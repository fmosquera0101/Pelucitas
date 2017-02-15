package co.com.fredymosqueralemus.pelucitas.horario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class Horario implements Serializable {
    private String diasLaborales;
    private String horaInicial;
    private String horaFinal;

    private String fechaInsercion;
    private String fechaModificacion;

    public Horario(){

    }

    public String getDiasLaborales() {
        return diasLaborales;
    }

    public void setDiasLaborales(String diasLaborales) {
        this.diasLaborales = diasLaborales;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
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
}
