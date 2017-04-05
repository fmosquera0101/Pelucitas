package co.com.fredymosqueralemus.pelucitas.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.AdministrarMiNegocioActivity;
import co.com.fredymosqueralemus.pelucitas.AdministrarMiPerfilActivity;
import co.com.fredymosqueralemus.pelucitas.EditarInforamcionMiNegocioActivity;
import co.com.fredymosqueralemus.pelucitas.InicioActivity;
import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class CargarImagenMiNegocioIntentService extends IntentService {

    public CargarImagenMiNegocioIntentService() {
        super("CargarImagenMiNegocioIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            byte[] dataImage = intent.getByteArrayExtra("byteArrayImagenMiNegocio");
            final MiNegocio miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
            StorageReference storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();

            final int id = 1;
            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_notification_24dp);
            mBuilder.setContentTitle("Carga de Pelucitas");
            mBuilder.setContentText("Se esta cargando su imagen");
            Intent resultIntent = new Intent(this, AdministrarMiNegocioActivity.class);
            resultIntent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(new Intent(this, InicioActivity.class));
            taskStackBuilder.addNextIntent(resultIntent);

            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            mBuilder.setProgress(0, 0, true);
            notificationManager.notify(id, mBuilder.build());

            UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio).putBytes(dataImage);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mBuilder.setContentTitle("Carga de Pelucitas fallida");
                    mBuilder.setContentText("Toca para ver las opciones");
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
                        imagenModelo.setFechaCreacion(UtilidadesFecha.convertirDateAString(new Date()));
                        imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                    } else {
                        imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                    }

                    miNegocio.setImagenModelo(imagenModelo);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReferenceMiNegocio = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador()));
                    databaseReferenceMiNegocio.child(miNegocio.getKeyChild()).setValue(miNegocio);
                    mBuilder.setContentTitle("Carga de Pelucitas finalizada");
                    mBuilder.setContentText("Toca para ver las opciones");
                    mBuilder.setProgress(0, 0, false);
                    notificationManager.notify(id, mBuilder.build());
                }

            });

        }
    }

}
