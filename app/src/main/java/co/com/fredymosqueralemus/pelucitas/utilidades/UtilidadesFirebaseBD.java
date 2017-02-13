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
    public static String getUrlInserccionDireccionesXNegocio(String nitNegocio){
        StringBuilder strbNodoInserccionesDireccionXUsuario = new StringBuilder();
        strbNodoInserccionesDireccionXUsuario.append(Constantes.DIRECCIONES_X_NEGOCIO_FIREBASE_BD);
        strbNodoInserccionesDireccionXUsuario.append("/");
        strbNodoInserccionesDireccionXUsuario.append(nitNegocio);

        return strbNodoInserccionesDireccionXUsuario.toString();
    }
    public static String getUrlInsercionPerfilCliente(String usuarioUid){
        return getUrlInsertPerfiles(Constantes.PERFIL_CLIENTE_FIREBASE_BD, usuarioUid);
    }
    public static String getUrlInserccionPerfilEmpleado(String usuarioUid){
        return getUrlInsertPerfiles(Constantes.PERFIL_EMPLEADO_FIREBASE_BD, usuarioUid);
    }
    public static String getUrlInserccionPerfilAdministrador(String usuarioUid){

        return getUrlInsertPerfiles(Constantes.PERFIL_ADMINISTRADOR_FIREBASE_BD, usuarioUid);
    }
    private static String getUrlInsertPerfiles(String strPerfil, String usuarioUid){
        StringBuilder strbNodoInserPerfilAdministrador = new StringBuilder();
        strbNodoInserPerfilAdministrador.append(Constantes.PERFILESX_USUARIO_FIREBASE_BD);
        strbNodoInserPerfilAdministrador.append("/");
        strbNodoInserPerfilAdministrador.append(strPerfil);
        strbNodoInserPerfilAdministrador.append("/");
        strbNodoInserPerfilAdministrador.append(usuarioUid);
        strbNodoInserPerfilAdministrador.append("/");

        return strbNodoInserPerfilAdministrador.toString();

    }
    public static String getUrlInsercionMiNegocio(String nitMiNegocio){
        StringBuilder strbNodoInsercionMiNegocio = new StringBuilder();
        strbNodoInsercionMiNegocio.append(Constantes.MINEGOCIO_FIREBASE_BD);
        strbNodoInsercionMiNegocio.append("/");
        strbNodoInsercionMiNegocio.append(nitMiNegocio);
        return strbNodoInsercionMiNegocio.toString();
    }
    public static String getUrlInsercionTiposNegocio(String tipoNegocio){
        StringBuilder strbNodoInserccionTiposNegocios = new StringBuilder();
        strbNodoInserccionTiposNegocios.append(Constantes.TIPOS_NEGOCIOS_FIREBASE_BD);
        strbNodoInserccionTiposNegocios.append("/");
        strbNodoInserccionTiposNegocios.append(tipoNegocio);
        return strbNodoInserccionTiposNegocios.toString();

    }
}
