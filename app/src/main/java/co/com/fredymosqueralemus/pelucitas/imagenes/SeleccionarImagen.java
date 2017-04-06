package co.com.fredymosqueralemus.pelucitas.imagenes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;

/**
 * Created by fredymosqueralemus on 2/04/17.
 */

public class SeleccionarImagen {

    private Context context;
    private Activity activity;
    private String userChoose;
    public SeleccionarImagen(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public String getUserChoose() {
        return userChoose;
    }

    public void setUserChoose(String userChoose) {
        this.userChoose = userChoose;
    }

    public void seleccionarImagen() {
        final CharSequence[] items = {context.getString(R.string.st_camara), context.getString(R.string.st_galeria), context.getString(R.string.st_cancelar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.st_elegirImagen));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utilidades.verificarPermisos(context);
                if (context.getString(R.string.st_camara).equals(items[which])) {
                    userChoose = context.getString(R.string.st_camara);
                    if (result) {
                        camaraIntent();
                    }
                } else if (context.getString(R.string.st_galeria).equals(items[which])) {
                    userChoose = context.getString(R.string.st_galeria);
                    if (result) {
                        galeriaIntent();
                    }
                } else if (context.getString(R.string.st_cancelar).equals(items[which])) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    public void camaraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, Constantes.REQUEST_CAMERA);
    }

    public void galeriaIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.st_selecImagen)), Constantes.SELECT_FILE);
    }
}
