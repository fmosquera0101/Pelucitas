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

import co.com.fredymosqueralemus.pelucitas.AdministrarMiPerfilActivity;
import co.com.fredymosqueralemus.pelucitas.InicioActivity;
import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

/**
 * Created by fredymosqueralemus on 5/04/17.
 */

public class TaskCargarImagenMiPerfil extends AsyncTask<Void, Void, Void> {

    private Context context;
    private byte[] dataImage;
    private Usuario usuario;
    private ImageView imageView;
    private StorageReference storageReference;

    public TaskCargarImagenMiPerfil(Context context, byte[] dataImage, Usuario usuario, ImageView imageView) {
        this.context = context;
        this.dataImage = dataImage;
        this.usuario = usuario;
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
        Intent resultIntent = new Intent(context, AdministrarMiPerfilActivity.class);
        resultIntent.putExtra(Constantes.USUARIO_OBJECT, usuario);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(new Intent(context, InicioActivity.class));
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        mBuilder.setProgress(0, 0, true);
        notificationManager.notify(id, mBuilder.build());

        final UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, usuario).putBytes(dataImage);

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
                ImagenModelo imagenModelo = usuario.getImagenModelo();
                Date fechahoy = new Date();
                if (null == imagenModelo) {
                    imagenModelo = new ImagenModelo();
                    imagenModelo.setNombreImagen("usuario" + usuario.getCedulaIdentificacion());
                    imagenModelo.setFechaCreacion(UtilidadesFecha.convertirDateAString(fechahoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
                    imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(fechahoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
                } else {
                    imagenModelo.setFechaUltimaModificacion(UtilidadesFecha.convertirDateAString(fechahoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
                }

                usuario.setImagenModelo(imagenModelo);
                usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(fechahoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReferenceMiNegocio = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(usuario.getUid()));
                databaseReferenceMiNegocio.setValue(usuario);

                mBuilder.setContentTitle("Carga de Pelucitas finalizada");
                mBuilder.setContentText(context.getString(R.string.str_toca_ver_opciones));
                mBuilder.setProgress(0, 0, false);
                notificationManager.notify(id, mBuilder.build());

                try {
                    UtilidadesImagenes.cargarImagenPerfilUsuario(imageView, usuario, context, storageReference);
                } catch (Exception e) {

                }
            }

        });

        return null;

    }
}
