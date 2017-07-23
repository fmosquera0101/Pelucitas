package co.com.fredymosqueralemus.pelucitas.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.com.fredymosqueralemus.pelucitas.AdministrarMiNegocioActivity;
import co.com.fredymosqueralemus.pelucitas.InicioActivity;
import co.com.fredymosqueralemus.pelucitas.ListaAgendaXDiaActivity;
import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.reserva.ReservaXUsuario;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificadorReservaAgendaService extends IntentService {


    public NotificadorReservaAgendaService() {
        super("NotificadorReservaAgendaService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = this;

        SharedPreferencesSeguro sharedPreferencesSeguro  = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("reservasXEmpleado").child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ReservaXUsuario reservaXUsuario = dataSnapshot.getValue(ReservaXUsuario.class);
                if(null != reservaXUsuario) {
                    int id = 1;
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                    mBuilder.setSmallIcon(R.drawable.ic_event_busy_black_24dp);
                    Intent resultIntent = new Intent(context, ListaAgendaXDiaActivity.class);
                    //resultIntent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);

                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                    taskStackBuilder.addNextIntent(new Intent(context, ListaAgendaXDiaActivity.class));
                    taskStackBuilder.addNextIntent(resultIntent);

                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setAutoCancel(true);
                    final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    mBuilder.setProgress(0, 0, true);
                    notificationManager.notify(id, mBuilder.build());
                    mBuilder.setContentTitle("Tienes una cita agendada");
                    mBuilder.setContentText(getString(R.string.str_toca_ver_opciones));
                    mBuilder.setProgress(0, 0, false);
                    notificationManager.notify(id, mBuilder.build());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
