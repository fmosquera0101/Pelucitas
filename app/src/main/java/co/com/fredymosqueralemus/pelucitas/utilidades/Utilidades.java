package co.com.fredymosqueralemus.pelucitas.utilidades;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;

/**
 * Created by Fredy Mosquera Lemus on 20/02/17.
 */

public class Utilidades {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean verificarPermisos(final Context context){
        int currentVersionApi = Build.VERSION.SDK_INT;
        if(currentVersionApi >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle(context.getString(R.string.titulo_alertpermisos));
                    builder.setMessage(context.getString(R.string.mensaje_alertpermisos));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    Dialog alert = builder.create();
                    alert.show();
                }else{
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            }else {
                return true;
            }

        }else {
            return true;
        }
    }

    public static String getStrHorario(Horario horario){
        StringBuilder strbHorario = new StringBuilder();
        strbHorario.append(horario.getDiasLaborales());
        strbHorario.append(", ");
        strbHorario.append(horario.getHoraInicial());
        strbHorario.append("-");
        strbHorario.append(horario.getHoraFinal());
        return strbHorario.toString();
    }
    public static String getStrDireccion(Direccion direccion){
        StringBuilder strbDireccion = new StringBuilder();
        strbDireccion.append(direccion.getCarreraCalle());
        strbDireccion.append(", ");
        strbDireccion.append(direccion.getNumero1());
        strbDireccion.append("-");
        strbDireccion.append(direccion.getNumero2());

        return strbDireccion.toString();


    }
}
