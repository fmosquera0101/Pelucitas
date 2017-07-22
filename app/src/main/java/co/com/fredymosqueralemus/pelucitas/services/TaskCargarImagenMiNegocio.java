package co.com.fredymosqueralemus.pelucitas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.AdministrarMiNegocioActivity;
import co.com.fredymosqueralemus.pelucitas.InicioActivity;
import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

/**
 * Created by fredymosqueralemus on 5/04/17.
 */

public class TaskCargarImagenMiNegocio extends AsyncTask<Void, Void, Void> {

    private Context context;
    private byte[] dataImage;
    private MiNegocio miNegocio;
    private ImageView imageView;
    private StorageReference storageReference;

    public TaskCargarImagenMiNegocio(Context context, byte[] dataImage, MiNegocio miNegocio, ImageView imageView) {
        this.context = context;
        this.dataImage = dataImage;
        this.miNegocio = miNegocio;
        this.imageView = imageView;
    }

    @Override
    protected Void doInBackground(Void... params) {

        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();

        final int id = 1;
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_notification_24dp);
        mBuilder.setContentTitle("Carga de Pelucitas");
        mBuilder.setContentText("Se esta cargando su imagen");
        Intent resultIntent = new Intent(context, AdministrarMiNegocioActivity.class);
        resultIntent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(new Intent(context, InicioActivity.class));
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        mBuilder.setProgress(0, 0, true);
        notificationManager.notify(id, mBuilder.build());

        UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio).putBytes(dataImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mBuilder.setContentTitle("Carga de Pelucitas fallida");
                mBuilder.setContentText(context.getString(R.string.str_toca_ver_opciones));
                mBuilder.setProgress(0, 0, false);
                notificationManager.notify(id, mBuilder.build());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImagenModelo imagenModelo = miNegocio.getImagenModelo();
                if (null == imagenModelo) {
                    imagenModelo = new ImagenModelo();
                    imagenModelo.setNombreImagen("MiImagenPerfil" + miNegocio.getNitNegocio());
                    imagenModelo.setFechaCreacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                    imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                } else {
                    imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                }

                miNegocio.setImagenModelo(imagenModelo);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReferenceMiNegocio = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getNitNegocio()));
                databaseReferenceMiNegocio.setValue(miNegocio);

                mBuilder.setContentTitle("Carga de Pelucitas finalizada");
                mBuilder.setContentText(context.getString(R.string.str_toca_ver_opciones));
                mBuilder.setProgress(0, 0, false);
                notificationManager.notify(id, mBuilder.build());

                try {
                    UtilidadesImagenes.cargarImagenMiNegocioNoCircular(imageView, miNegocio, context, storageReference);
                } catch (Exception e) {

                }
            }

        });

        return null;

    }
}
