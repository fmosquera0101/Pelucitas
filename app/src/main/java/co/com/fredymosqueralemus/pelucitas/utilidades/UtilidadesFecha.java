package co.com.fredymosqueralemus.pelucitas.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

/**
 * Created by fredymosqueralemus on 4/02/17.
 */

public class UtilidadesFecha {

    public static String convertirDateAString(Date dateFecha){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHAYYYMMDD);
        return simpleDateFormat.format(dateFecha);
    }
    public static Date convertirStringADate(String strFecha){
        Date fechaConvertida = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHAYYYMMDD);
            fechaConvertida =  simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaConvertida;
    }
    public static Date formatearDate(Date fecha){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHAYYYMMDD);
        String strFecha =  simpleDateFormat.format(fecha);
        Date fechaConvertida = null;
        try {
            fechaConvertida = simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaConvertida;
    }
}
