package co.com.fredymosqueralemus.pelucitas.utilidades;

import java.text.SimpleDateFormat;

/**
 * Created by Fredy Mosquera Lemus on 8/02/17.
 */

public class MiSimpleDateFormat extends SimpleDateFormat {
    private static MiSimpleDateFormat miSimpleDateFormatInstance = null;

    private MiSimpleDateFormat(){

    }
    public static MiSimpleDateFormat getInstance(String pattern){
        if(miSimpleDateFormatInstance == null){
            miSimpleDateFormatInstance = new MiSimpleDateFormat();
            miSimpleDateFormatInstance.applyPattern(pattern);
        }
        return miSimpleDateFormatInstance;
    }
}
