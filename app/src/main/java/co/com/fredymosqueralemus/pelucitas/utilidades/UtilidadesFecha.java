package co.com.fredymosqueralemus.pelucitas.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class UtilidadesFecha {

    public static String convertirDateAString(Date dateFecha){
        MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(Constantes.FORMATO_FECHADDMMYYYY);
        return simpleDateFormat.format(dateFecha);
    }
    public static Date convertirStringADate(String strFecha){
        Date fechaConvertida = null;
        try {
            MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(Constantes.FORMATO_FECHADDMMYYYY);
            fechaConvertida =  simpleDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaConvertida;
    }
    public static Date formatearDate(Date fecha){
        MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(Constantes.FORMATO_FECHADDMMYYYY);
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
    public static boolean isFechaddmmyyyy(String strFecha){
        Date fechaConvertida = null;
        try {
            MiSimpleDateFormat simpleDateFormat = MiSimpleDateFormat.getInstance(Constantes.FORMATO_FECHADDMMYYYY);
            fechaConvertida =  simpleDateFormat.parse(strFecha);
            if (null != fechaConvertida){
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return  false;

    }
}
