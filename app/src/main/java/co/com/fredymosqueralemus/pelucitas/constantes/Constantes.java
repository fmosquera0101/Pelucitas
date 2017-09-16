package co.com.fredymosqueralemus.pelucitas.constantes;

/**
 * Created by Fredy Mosquera Lemus on 4/02/17.
 */

public interface Constantes {

    String URL_STORAGE_FIREBASE = "gs://pelucitas-bb90f.appspot.com";
    String USUARIO_FIREBASE_BD = "usuarios";

    String PELUCITAS_SETTINGS = "pelucitas_settings";
    String IMAGENES_LISTVIEW_INICIO = "imagenes_listview_inicio";

    String PERFILES_X_USUARIO_FIREBASE_BD = "perfilesXUsuairo";
    String PERFIL_ADMINISTRADOR_FIREBASE_BD = "administrador";
    String PERFIL_EMPLEADO_FIREBASE_BD = "empleado";
    String PERFIL_CLIENTE_FIREBASE_BD = "cliente";
    String DIRECCIONES_X_USUARIO_FIREBASE_BD = "direccionesXUsuario";
    String DIRECCIONES_X_NEGOCIO_FIREBASE_BD = "direccionesXNegocio";
    String HORARIOS_X_NEGOCIO_FIREBASE_BD = "horariosXNegocio";
    String AGENDA_X_EMPLEADOS = "agendaxEmpleado";
    String TURNOS_X_CLIENTE = "turnosXCliente";

    String MINEGOCIO_FIREBASE_BD = "negocios";
    String TIPOS_NEGOCIOS_FIREBASE_BD = "tiposnegocios";
    String TIPOS_NEGOCIOS_BARBERIA_FIREBASE_BD = "barberia";
    String TIPOS_NEGOCIOS_PELUQUERIA_FIREBASE_BD = "peluqueria";
    String TIPOS_NEGOCIOS_SALONESDEBELLEZA_FIREBASE_BD = "salondebelleza";
    String MINEGOCIO_X_ADMON_FIREBASE_BD = "negociosXAdministrador";

    String EMPLEADOS_X_NEGOCIO = "empleadosXNegocio";

    String NOTIFICACIONES = "notificaciones";

    String FORMAT_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";

    String FORMAT_DDMMYYYY= "dd/MM/yyyy";
    String FORMAT_YYYYMMDD= "yyyy/MM/dd";

    String PETTERN_VALIDA_CORREO = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    String CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO = "CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO";
    String CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO = "CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO";
    String CALL_FROM_ACTIVITY_EDITARMINEGOCIO = "CALL_FROM_ACTIVITY_EDITARMINEGOCIO";
    String NIT_MINEGOCIO = "NIT_MINEGOCIO";
    String CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL = "CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL";
    String CALL_FROM_ACTIVITY_MISEMPLEADOS = "CALL_FROM_ACTIVITY_MISEMPLEADOS";

    String CALL_FROM_ACTIVITY_HOME = "CALL_FROM_ACTIVITY_HOME";
    String CALL_FROM_LOGINACTIVITY = "CALL_FROM_LOGINACTIVITY";

    //Constantes numericas
    int NUMERO6 = 6;
    int NUMERO10 = 10;

    String KEY_SHAREDPREFERENCE = "qwertyuiopasdfghjklzxcvbnm/";

    String SHARED_PREFERENCES_INFOUSUARIO = "SHARED_PREFERENCES_INFOUSUARIO";
    String SECURE_KEY_SHARED_PREFERENCES = "/mnbvcxzlkjhgfdsapoiuytrewq";



    String CORREO = "CORREO";
    String CONTRASENA = "CONTRASENA";
    String ISLOGGED = "ISLOGGED";
    String USERUID = "USERUID";
    String SI = "SI";
    String NO = "NO";

    String MINEGOCIO_OBJECT = "MINEGOCIOCLASS";
    String CONST_IMAGENES = "imagenes";

    String USUARIO_OBJECT = "USUARIO_OBJECT";
    String DIRECCION_OBJECT = "DIRECCION_OBJECT";
    String APP_FOLDER = "/co.com.fredymosqueralemus.pelucitas/imagenes/usuario/perfil";

    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    String BYTE_ARRAY_IMAGEN_MI_NEGOCIO = "BYTE_ARRAY_IMAGEN_MI_NEGOCIO";

    String SN_READONLY_INFORMACION_USUARIO = "SN_READONLY_INFORMACION_USUARIO";

    String AGENDA_X_NEGOCIO_OBJECT = "AGENDA_X_NEGOCIO_OBJECT";

    String STR_DIA_AGENDA = "STR_DIA_AGENDA";
    String STR_FECHA_AGENDA = "STR_FECHA_AGENDA";

    String  CALL_TO_AGREGAR_AGENDA_XEMPLEADO = "CALL_TO_AGREGAR_AGENDA_XEMPLEADO";
    String  CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY = "CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY" ;

    String TIPO_NEGOCIO_SELECCIONADO = "TIPO_NEGOCIO_SELECCIONADO";

    String CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR = "CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR";

    String CALL_FROM_NOTIFICADORRESERVAAGENDA = "CALL_FROM_NOTIFICADORRESERVAAGENDA";
}
