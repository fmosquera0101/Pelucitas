package co.com.fredymosqueralemus.pelucitas.modelo.reserva;

import java.io.ObjectInput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fredymosqueralemus on 23/07/17.
 */

public class NotificacionReservaXUsuario implements Serializable {

    private String fechaAgenda;
    private String uidUsuarioReserva;
    private String fechaInsercion;
    private String fechaModificacion;
    private String keyUidHoraAgenda;
    private long estado;
    private String keyUidEmpleadoDuenoAgenda;

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

    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

    public String getKeyUidEmpleadoDuenoAgenda() {
        return keyUidEmpleadoDuenoAgenda;
    }

    public void setKeyUidEmpleadoDuenoAgenda(String keyUidEmpleadoDuenoAgenda) {
        this.keyUidEmpleadoDuenoAgenda = keyUidEmpleadoDuenoAgenda;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> mapNotificacionesObject = new HashMap<String, Object>();
        mapNotificacionesObject.put("fechaAgenda", fechaAgenda);
        mapNotificacionesObject.put("uidUsuarioReserva", uidUsuarioReserva);
        mapNotificacionesObject.put("fechaInsercion", fechaInsercion);
        mapNotificacionesObject.put("fechaModificacion", fechaModificacion);
        mapNotificacionesObject.put("keyUidHoraAgenda", keyUidHoraAgenda);
        mapNotificacionesObject.put("estado", estado);
        mapNotificacionesObject.put("keyUidEmpleadoDuenoAgenda", keyUidEmpleadoDuenoAgenda);
        return mapNotificacionesObject;
    }
}
