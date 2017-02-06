package co.com.fredymosqueralemus.pelucitas.utilidades;

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
}
