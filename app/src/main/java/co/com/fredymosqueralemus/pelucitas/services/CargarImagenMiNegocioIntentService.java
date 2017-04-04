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

import co.com.fredymosqueralemus.pelucitas.EditarInforamcionMiNegocioActivity;
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
            UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio).putBytes(dataImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(context, "No se pudo cargar la imagen",
                    //        Toast.LENGTH_SHORT).show();

                    Log.d("Imagen", "Imagen cargada Fallo");
                    mostrarNotificacion("Imagen cargada Fallo", miNegocio);

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

                    mostrarNotificacion("Imagen cargada correctamente", miNegocio);
                    //Toast.makeText(context, "Imagen cargada correctamente",
                    //       Toast.LENGTH_SHORT).show();
                }

            });

        }
    }

    private void mostrarNotificacion(String mensaje, MiNegocio miNegocio) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification_24dp)
                        .setContentTitle("My notification")
                        .setContentText("Imagen cargada correctamente");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, EditarInforamcionMiNegocioActivity.class);
        resultIntent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(EditarInforamcionMiNegocioActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId = 1;
        mBuilder.setProgress(0, 0, true);
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
