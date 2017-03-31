package co.com.fredymosqueralemus.pelucitas.utilidades;

import android.os.Environment;

import java.io.File;

import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

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

    public static File getFileImagenPerfilUsuario(Usuario usuario){
        return new File(getPathImagenUsuarioPerfil(usuario));
    }
    private static String getPathImagenUsuarioPerfil(Usuario usuario){
        StringBuilder strbPathFileImage = new StringBuilder();
        strbPathFileImage.append(Environment.getExternalStorageDirectory());
        strbPathFileImage.append("/co.com.fredymosqueralemus.pelucitas/imagenes/usuario/perfil/");
        strbPathFileImage.append("usuario");
        strbPathFileImage.append(usuario.getCedulaIdentificacion());
        strbPathFileImage.append(".jpg");
        return strbPathFileImage.toString();
    }
}
