package co.com.fredymosqueralemus.pelucitas.utilidades;

import java.text.ParseException;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class UtilidadesFecha {

    public static String convertirDateAString(Date dateFecha, String formatoFecha){
        MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(formatoFecha);
        return simpleDateFormat.format(dateFecha);
    }
    public static Date convertirStringADate(String strFecha, String formatoFecha){
        Date fechaConvertida = null;
        try {
            MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(formatoFecha);
            fechaConvertida =  simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaConvertida;
    }
    public static Date formatearDate(Date fecha, String formatoFecha){
        MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(formatoFecha);
        String strFecha =  simpleDateFormat.format(fecha);
        Date fechaConvertida = null;
        try {
            fechaConvertida = simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaConvertida;
    }

    /**
     * Metodo encargado de validar si una fecha es valida y es del formado tipo ddMMyyyy
     * @param strFecha
     * @return
     * Created by Fredy Mosquera Lemus on 8/02/17.
     */
    public static boolean isFechaddmmyyyy(String strFecha, String formatoFecha){
        Date fechaConvertida = null;
        try {
            MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(formatoFecha);
            fechaConvertida =  simpleDateFormat.parse(strFecha);
            if (null != fechaConvertida){
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return  false;

    }
    public static String agregarCeroMinutosTimePicker(int fecha){
        if(fecha < 10){
            return "0"+String.valueOf(fecha);


        }else{
            return String.valueOf(fecha);
        }
    }
}
