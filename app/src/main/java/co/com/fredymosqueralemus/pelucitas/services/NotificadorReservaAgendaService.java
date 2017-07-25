package co.com.fredymosqueralemus.pelucitas.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.com.fredymosqueralemus.pelucitas.InicioActivity;
import co.com.fredymosqueralemus.pelucitas.ListaAgendaXDiaActivity;
import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.reserva.NotificacionReservaXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
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
public class NotificadorReservaAgendaService extends Service {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public NotificadorReservaAgendaService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        notificarReservaAgenda();

    }

    protected void notificarReservaAgenda() {
        if (null != firebaseAuth.getCurrentUser()) {
            firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionNofiticaciones(firebaseAuth.getCurrentUser().getUid())).
                    orderByChild("estado").equalTo(0).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    if (null != dataSnapshot) {
                        final String keyNotificacionActual = dataSnapshot.getKey();
                        final NotificacionReservaXUsuario notificacionReservaXUsuario = dataSnapshot.getValue(NotificacionReservaXUsuario.class);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(notificacionReservaXUsuario.getUidUsuarioReserva()));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Usuario usuarioReserva = dataSnapshot.getValue(Usuario.class);
                                int id = 1;
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                                mBuilder.setSmallIcon(R.drawable.ic_event_busy_black_24dp);
                                Intent resultIntent = new Intent(context, ListaAgendaXDiaActivity.class);
                                Usuario empleadoDuenoAgenda = new Usuario();
                                empleadoDuenoAgenda.setUid(notificacionReservaXUsuario.getKeyUidEmpleadoDuenoAgenda());
                                resultIntent.putExtra(Constantes.USUARIO_OBJECT, empleadoDuenoAgenda);
                                resultIntent.putExtra(Constantes.STR_FECHA_AGENDA, notificacionReservaXUsuario.getFechaAgenda());
                                resultIntent.putExtra(Constantes.CALL_FROM_NOTIFICADORRESERVAAGENDA, NotificadorReservaAgendaService.class.getName());



                                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                                taskStackBuilder.addNextIntent(new Intent(context, InicioActivity.class));
                                taskStackBuilder.addNextIntent(resultIntent);

                                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setAutoCancel(true);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                mBuilder.setProgress(0, 0, true);
                                notificationManager.notify(id, mBuilder.build());
                                mBuilder.setContentTitle("Tienes una cita agendada de: "+usuarioReserva.getNombre()+" "+usuarioReserva.getApellidos());
                                mBuilder.setContentText("Para el dia: "+notificacionReservaXUsuario.getFechaAgenda()+" "+notificacionReservaXUsuario.getKeyUidHoraAgenda());
                                mBuilder.setProgress(0, 0, false);
                                notificationManager.notify(id, mBuilder.build());
                                actualizarEstadoNotifiacion(keyNotificacionActual);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

    private void actualizarEstadoNotifiacion(String keyNotificacion) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionNofiticaciones(firebaseAuth.getCurrentUser().getUid()));
        databaseReference.child(keyNotificacion).child("estado").setValue(1);
    }

}
