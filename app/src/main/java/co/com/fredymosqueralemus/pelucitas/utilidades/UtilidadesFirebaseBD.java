package co.com.fredymosqueralemus.pelucitas.utilidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.settings.TiposDeNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public class UtilidadesFirebaseBD {

    public static String getUrlInserccionEmpleadosXNegocios(String nitNegocio){
        StringBuilder strbNodoInserEmpl = new StringBuilder();
        strbNodoInserEmpl.append(Constantes.EMPLEADOS_X_NEGOCIO);
        strbNodoInserEmpl.append("/");
        strbNodoInserEmpl.append(nitNegocio);

        return strbNodoInserEmpl.toString();
    }
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
        strbNodoInserPerfilAdministrador.append(Constantes.PERFILES_X_USUARIO_FIREBASE_BD);
        strbNodoInserPerfilAdministrador.append("/");
        strbNodoInserPerfilAdministrador.append(strPerfil);
        strbNodoInserPerfilAdministrador.append("/");
        strbNodoInserPerfilAdministrador.append(usuarioUid);
        strbNodoInserPerfilAdministrador.append("/");

        return strbNodoInserPerfilAdministrador.toString();

    }
    public static String getUrlInsercionMiNegocio(String nitNegocio){
        StringBuilder strbNodoInsercionMiNegocio = new StringBuilder();
        strbNodoInsercionMiNegocio.append(Constantes.MINEGOCIO_FIREBASE_BD);
        strbNodoInsercionMiNegocio.append("/");
        strbNodoInsercionMiNegocio.append(nitNegocio);
        strbNodoInsercionMiNegocio.append("/");
        return strbNodoInsercionMiNegocio.toString();
    }

    public static String getUrlInsercionMiNegocioXAdmon(String uidAdministrador){
        StringBuilder strbNodoInsercionMiNegocio = new StringBuilder();
        strbNodoInsercionMiNegocio.append(Constantes.MINEGOCIO_X_ADMON_FIREBASE_BD);
        strbNodoInsercionMiNegocio.append("/");
        strbNodoInsercionMiNegocio.append(uidAdministrador);
        strbNodoInsercionMiNegocio.append("/");
        return strbNodoInsercionMiNegocio.toString();
    }

    public static String getUrlInsercionTiposNegocio(String tipoNegocio, String nitNegocio){
        StringBuilder strbNodoInserccionTiposNegocios = new StringBuilder();
        strbNodoInserccionTiposNegocios.append(Constantes.TIPOS_NEGOCIOS_FIREBASE_BD);
        strbNodoInserccionTiposNegocios.append("/");
        strbNodoInserccionTiposNegocios.append(tipoNegocio);
        strbNodoInserccionTiposNegocios.append("/");
        strbNodoInserccionTiposNegocios.append(nitNegocio);
        strbNodoInserccionTiposNegocios.append("/");
        return strbNodoInserccionTiposNegocios.toString();

    }
    public static String getUrlInsercionHorariosXNegocios(String nitNegocio){
        StringBuilder strbHorariosXnegocio = new StringBuilder();
        strbHorariosXnegocio.append(Constantes.HORARIOS_X_NEGOCIO_FIREBASE_BD);
        strbHorariosXnegocio.append("/");
        strbHorariosXnegocio.append(nitNegocio);
        strbHorariosXnegocio.append("/");
        return strbHorariosXnegocio.toString();
    }
    public static String getUrlInserccionEmpleadosXNegocio(String nitNegocio){
        StringBuilder strbEmpXNegocios = new StringBuilder();
        strbEmpXNegocios.append(Constantes.EMPLEADOS_X_NEGOCIO);
        strbEmpXNegocios.append("/");
        strbEmpXNegocios.append(nitNegocio);
        strbEmpXNegocios.append("/");
        return  strbEmpXNegocios.toString();
    }
    public static String getUrlInsercionAgendaXEmpleado(AgendaXEmpleado agendaXEmpleado){
        StringBuilder strbAgendaXEmpleado = new StringBuilder();
        strbAgendaXEmpleado.append(Constantes.AGENDA_X_EMPLEADOS);
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXEmpleado.getUidEmpleado());
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXEmpleado.getFechaAgenda());
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXEmpleado.getHoraReserva());

        return  strbAgendaXEmpleado.toString();
    }
    public static String getUrlInsercionAgendaXCliente(AgendaXEmpleado agendaXCliente){
        StringBuilder strbAgendaXEmpleado = new StringBuilder();
        strbAgendaXEmpleado.append(Constantes.TURNOS_X_CLIENTE);
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXCliente.getUidEmpleado());
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXCliente.getFechaAgenda());
        strbAgendaXEmpleado.append("/");
        strbAgendaXEmpleado.append(agendaXCliente.getHoraReserva());

        return  strbAgendaXEmpleado.toString();
    }

    public static String getUrlInserccionNofiticaciones(String userUID){
        StringBuilder strbNotificaciones = new StringBuilder();
        strbNotificaciones.append(Constantes.NOTIFICACIONES);
        strbNotificaciones.append("/");
        strbNotificaciones.append(userUID);

        return strbNotificaciones.toString();
    }
    public static StorageReference getFirebaseStorageFromUrl(){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        return firebaseStorage.getReferenceFromUrl(Constantes.URL_STORAGE_FIREBASE);
    }
    public static StorageReference getReferenceImagenMiNegocio(StorageReference storageReference, MiNegocio miNegocio){
        return storageReference.child(Constantes.CONST_IMAGENES).child("negocios").child(miNegocio.getNitNegocio()).child("Minegocio" + miNegocio.getNitNegocio());
    }
    public static StorageReference getReferenceImagenMiPerfil(StorageReference storageReference, Usuario usuario){
        return storageReference.child(Constantes.CONST_IMAGENES).child(Constantes.USUARIO_FIREBASE_BD).
                child(usuario.getCedulaIdentificacion()).child("MiPerfil" + usuario.getCedulaIdentificacion());
    }
    public static void insertarAgendaXEmpleadoFirebaseBD(AgendaXEmpleado agendaXEmpleado){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
        databaseReference.setValue(agendaXEmpleado);
    }
    public static StorageReference getReferenceImagenTiposNegocios(StorageReference storageReference, TiposDeNegocio tiposDeNegocio){
        return storageReference.child(Constantes.CONST_IMAGENES).child(Constantes.PELUCITAS_SETTINGS).child(Constantes.IMAGENES_LISTVIEW_INICIO)
                .child(tiposDeNegocio.getImagenModelo().getNombreImagen()+".jpg");
    }

    public static void insertarAgendaXClienteFirebaseBD(AgendaXEmpleado agendaXCliente){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getUrlInsercionAgendaXCliente(agendaXCliente));
        databaseReference.setValue(agendaXCliente);
    }
}
