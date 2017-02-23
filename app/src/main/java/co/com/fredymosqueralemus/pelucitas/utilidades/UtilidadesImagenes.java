package co.com.fredymosqueralemus.pelucitas.utilidades;

import android.os.Environment;

import java.io.File;

import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;

/**
 * Created by Fredy Mosquera Lemus on 22/02/17.
 */

public class UtilidadesImagenes {

    public static File getFileImagenMiNegocio(MiNegocio miNegocio){
        return new File(getPathImagenMiNegoico(miNegocio));
    }
    private static String getPathImagenMiNegoico(MiNegocio miNegocio){
        StringBuilder strbPathFileImage = new StringBuilder();
        strbPathFileImage.append(Environment.getExternalStorageDirectory());
        strbPathFileImage.append("/Pelucitas/images/");
        strbPathFileImage.append("Minegocio");
        strbPathFileImage.append(miNegocio.getNitNegocio());
        strbPathFileImage.append(".jpg");
        return strbPathFileImage.toString();
    }
}
