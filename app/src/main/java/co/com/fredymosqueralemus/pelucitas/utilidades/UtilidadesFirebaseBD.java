package co.com.fredymosqueralemus.pelucitas.utilidades;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class UtilidadesFirebaseBD {

    public static String getUrlInserccionUsuario(String usuarioUid){
        StringBuilder strbNodoInserUsuario = new StringBuilder();
        strbNodoInserUsuario.append(Constantes.USUARIO_FIREBASE_BD);
        strbNodoInserUsuario.append("/");
        strbNodoInserUsuario.append(usuarioUid);

        return strbNodoInserUsuario.toString();
    }

    public static String getUrlInserccionDireccionesXUsuario(String usuarioUid){
        StringBuilder strbNodoInserccionesDireccionXUsuario = new StringBuilder();
        strbNodoInserccionesDireccionXUsuario.append(Constantes.DIRECCIONES_X_USUARIO_FIREBASE_BD);
        strbNodoInserccionesDireccionXUsuario.append("/");
        strbNodoInserccionesDireccionXUsuario.append(usuarioUid);

        return strbNodoInserccionesDireccionXUsuario.toString();
    }
}
